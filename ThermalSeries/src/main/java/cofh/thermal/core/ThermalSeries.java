package cofh.thermal.core;

import cofh.core.item.ItemBlockCoFH;
import cofh.core.item.ItemCoFH;
import cofh.core.network.PacketHandler;
import cofh.core.util.RegistrationHelper;
import cofh.lib.capabilities.CapabilityEnchantable;
import cofh.lib.util.IModule;
import cofh.thermal.core.gui.GuiHandler;
import cofh.thermal.core.init.*;
import cofh.thermal.core.network.PacketLexiconStudy;
import cofh.thermal.core.network.PacketLexiconTransmute;
import cofh.thermal.core.proxy.ProxyCommon;
import cofh.thermal.core.util.managers.LexiconManager;
import cofh.thermal.core.util.parsers.ConstantParser;
import cofh.thermal.core.util.parsers.OreDictParser;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

import static cofh.lib.util.Constants.ID_COFH_CORE;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.thermal.core.init.ConfigTSeries.*;

@Mod (modid = ThermalSeries.MOD_ID, name = ThermalSeries.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalSeries.DEPENDENCIES)
public class ThermalSeries {

	public static final String MOD_ID = ID_THERMAL_SERIES;
	public static final String MOD_NAME = "Thermal Series";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE;

	@Instance (MOD_ID)
	public static ThermalSeries instance;

	@SidedProxy (clientSide = "cofh.thermal.core.proxy.ProxyClient", serverSide = "cofh.thermal.core.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static Configuration config;
	public static Configuration configClient;
	public static RegistrationHelper registrar;
	public static PacketHandler packetHandler;

	private static ArrayList<IModule> modules = new ArrayList<>();

	public static void addModule(IModule module) {

		if (modules.contains(module)) {
			return;
		}
		modules.add(module);
	}

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		File configDir = event.getModConfigurationDirectory();

		config = new Configuration(new File(configDir, "/cofh/" + MOD_ID + "/config.cfg"), VERSION, true);
		config.load();

		configClient = new Configuration(new File(configDir, "/cofh/" + MOD_ID + "/config_client.cfg"), VERSION, true);
		configClient.load();

		ConfigTSeries.configCommon();
		ConfigTSeries.configClient();

		registerHandlers();

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		BlocksTSeries.initialize();

		proxy.initialize(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		ParsersTSeries.initializeParsers();
		RecipesTSeries.initializeManagers();

		proxy.postInit(event);
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {

		ParsersTSeries.postProcess();
		LexiconManager.initialize();

		config.save();
		configClient.save();
	}
	// endregion

	@EventHandler
	public void handleIdMappingEvent(FMLModIdMappingEvent event) {

		RecipesTSeries.refreshManagers();
	}

	// region REGISTRATION
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {

		BlocksTSeries.registerBlocks();

		for (IModule module : modules) {
			module.registerBlocks(event);
		}
		// TODO: Temporary - remove in 1.13.
		registerFluids();

		proxy.registerBlockModels();
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {

		ItemsTSeries.registerItems();

		for (IModule module : modules) {
			module.registerItems(event);
		}
		proxy.registerItemModels();
	}

	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> event) {

		for (IModule module : modules) {
			module.registerBiomes(event);
		}
	}

	@SubscribeEvent
	public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {

		for (IModule module : modules) {
			module.registerEnchantments(event);
		}
	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {

		for (IModule module : modules) {
			module.registerEntities(event);
		}
	}

	// TODO: Placeholder for 1.13.
	public void registerFluids() {

		FluidsTSeries.registerFluids();

		for (IModule module : modules) {
			module.registerFluids();
		}
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {

		for (IModule module : modules) {
			module.registerPotions(event);
		}
	}

	@SubscribeEvent
	public void registerPotionTypes(RegistryEvent.Register<PotionType> event) {

		for (IModule module : modules) {
			module.registerPotionTypes(event);
		}
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		if (disableAllRecipes) {
			return;
		}
		RecipesTSeries.registerRecipes();

		ParsersTSeries.registerParser("constants", ConstantParser.instance());
		ParsersTSeries.registerParser("oredict", OreDictParser.instance());

		for (IModule module : modules) {
			module.registerRecipes(event);
		}
	}

	@SubscribeEvent
	public void registerSoundEvent(RegistryEvent.Register<SoundEvent> event) {

		SoundsTSeries.registerSoundEvents();

		for (IModule module : modules) {
			module.registerSoundEvents(event);
		}
	}

	@SubscribeEvent
	public void registerVillagerProfessions(RegistryEvent.Register<VillagerProfession> event) {

		for (IModule module : modules) {
			module.registerVillagerProfessions(event);
		}
	}
	// endregion

	// region HELPERS
	private static void registerHandlers() {

		MinecraftForge.EVENT_BUS.register(instance);
		registrar = new RegistrationHelper(MOD_ID, CreativeTabsTSeries.tabBlocks, CreativeTabsTSeries.tabItems);
		CapabilityEnchantable.register();

		packetHandler = new PacketHandler(ID_THERMAL_SERIES);
		packetHandler.registerPacket(PACKET_LEXICON_STUDY, PacketLexiconStudy::new);
		packetHandler.registerPacket(PACKET_LEXICON_TRANSMUTE, PacketLexiconTransmute::new);

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, GuiHandler.INSTANCE);
	}

	public static ItemStack registerBlock(String blockName, ItemBlockCoFH itemBlock, CreativeTabs tab) {

		return registrar.registerBlock(blockName, itemBlock, tab);
	}

	public static ItemStack registerBlock(String blockName, Block block) {

		return registrar.registerBlock(blockName, new ItemBlockCoFH(block));
	}

	public static ItemStack registerBlock(String blockName, ItemBlockCoFH itemBlock) {

		return registrar.registerBlock(blockName, itemBlock);
	}

	public static ItemStack registerBlock(String blockName, String oreName, ItemBlockCoFH itemBlock, CreativeTabs tab) {

		return registrar.registerBlock(blockName, oreName, itemBlock, tab);
	}

	public static ItemStack registerBlock(String blockName, String oreName, ItemBlockCoFH itemBlock) {

		return registrar.registerBlock(blockName, oreName, itemBlock);
	}

	public static ItemStack registerItem(String itemName, ItemCoFH item, CreativeTabs tab) {

		return registrar.registerItem(itemName, item, tab);
	}

	public static ItemStack registerItem(String itemName, ItemCoFH item) {

		return registrar.registerItem(itemName, item);
	}

	public static ItemStack registerItem(String itemName, String oreName, ItemCoFH item, CreativeTabs tab) {

		return registrar.registerItem(itemName, oreName, item, tab);
	}

	public static ItemStack registerItem(String itemName, String oreName, ItemCoFH item) {

		return registrar.registerItem(itemName, oreName, item);
	}
	// endregion
}
