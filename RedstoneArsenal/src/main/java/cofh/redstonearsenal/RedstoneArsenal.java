package cofh.redstonearsenal;

import cofh.core.item.ItemBlockCoFH;
import cofh.core.item.ItemCoFH;
import cofh.core.util.RegistrationHelper;
import cofh.lib.capabilities.CapabilityEnchantable;
import cofh.redstonearsenal.entity.projectile.EntityArrowFlux;
import cofh.redstonearsenal.init.*;
import cofh.redstonearsenal.proxy.ProxyCommon;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static cofh.lib.util.Constants.*;
import static cofh.redstonearsenal.init.ConfigRSA.disableAllRecipes;

// TODO: DEACTIVATED
// @Mod (modid = RedstoneArsenal.MOD_ID, name = RedstoneArsenal.MOD_NAME, version = RedstoneArsenal.VERSION, dependencies = RedstoneArsenal.DEPENDENCIES, certificateFingerprint = "8a6abf2cb9e141b866580d369ba6548732eff25f")
public class RedstoneArsenal {

	public static final String MOD_ID = ID_REDSTONE_ARSENAL;
	public static final String MOD_NAME = "Redstone Arsenal";
	public static final String VERSION = "3.0.0";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";after:" + ID_THERMAL_SERIES;

	@Instance (MOD_ID)
	public static RedstoneArsenal instance;

	@SidedProxy (clientSide = "cofh.redstonearsenal.proxy.ProxyClient", serverSide = "cofh.redstonearsenal.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static Configuration config;
	public static Configuration configClient;
	public static RegistrationHelper registrar;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		File configDir = event.getModConfigurationDirectory();

		config = new Configuration(new File(configDir, "/cofh/" + MOD_ID + "/config.cfg"), VERSION, true);
		config.load();

		configClient = new Configuration(new File(configDir, "/cofh/" + MOD_ID + "/config_client.cfg"), VERSION, true);
		configClient.load();

		ConfigRSA.configCommon();
		ConfigRSA.configClient();

		registerHandlers();

		proxy.preInit(event);
	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {

		proxy.initialize(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {

		config.save();
		configClient.save();

		log.info(MOD_NAME + ": Load Complete.");
	}
	// endregion

	// region REGISTRATION
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {

		BlocksRSA.registerBlocks();

		proxy.registerBlockModels();
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {

		ItemsRSA.registerItems();
		EquipmentRSA.registerItems();

		proxy.registerItemModels();
	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {

		EntityArrowFlux.initialize(0);
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		if (disableAllRecipes) {
			return;
		}
		RecipesRSA.registerRecipes();
		EquipmentRSA.registerRecipes();
	}
	// endregion

	// region HELPERS
	private static void registerHandlers() {

		MinecraftForge.EVENT_BUS.register(instance);
		registrar = new RegistrationHelper(MOD_ID, CreativeTabsRSA.tabCommon, CreativeTabsRSA.tabItems);
		CapabilityEnchantable.register();
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
