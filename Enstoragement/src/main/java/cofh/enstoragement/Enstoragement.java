package cofh.enstoragement;

import cofh.core.item.ItemCoFH;
import cofh.core.util.RegistrationHelper;
import cofh.enstoragement.gui.GuiHandler;
import cofh.enstoragement.init.ItemsEnstor;
import cofh.enstoragement.proxy.ProxyCommon;
import cofh.lib.capabilities.CapabilityEnchantable;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static cofh.lib.util.Constants.ID_COFH_CORE;
import static cofh.lib.util.Constants.ID_ENSTORAGEMENT;

@Mod (modid = Enstoragement.MOD_ID, name = Enstoragement.MOD_NAME, version = Enstoragement.VERSION, dependencies = Enstoragement.DEPENDENCIES, certificateFingerprint = "8a6abf2cb9e141b866580d369ba6548732eff25f")
public class Enstoragement {

	public static final String MOD_ID = ID_ENSTORAGEMENT;
	public static final String MOD_NAME = "Enstoragement";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE;

	@Instance (MOD_ID)
	public static Enstoragement instance;

	@SidedProxy (clientSide = "cofh.enstoragement.proxy.ProxyClient", serverSide = "cofh.enstoragement.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static Configuration config;
	public static RegistrationHelper registrar;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		File configDir = event.getModConfigurationDirectory();

		config = new Configuration(new File(configDir, "/cofh/" + MOD_ID + "/config.cfg"), VERSION, true);
		config.load();

		config();
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
	}
	// endregion

	// region REGISTRATION
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {

		proxy.registerBlockModels();
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {

		ItemsEnstor.registerItems();

		proxy.registerItemModels();
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		if (disableAllRecipes) {
			return;
		}
	}
	// endregion

	// region HELPERS
	public static boolean disableAllRecipes = false;

	private static void config() {

		String category = "~MODPACK OPTIONS~";
		String comment = "If TRUE, then ALL RECIPES from this mod will be DISABLED. Everything will still be registered - this is a convenience option for pack makers who are completely overhauling recipes.";
		disableAllRecipes = config.getBoolean("Disable ALL Recipes", category, false, comment);
	}

	private static void registerHandlers() {

		MinecraftForge.EVENT_BUS.register(instance);
		registrar = new RegistrationHelper(MOD_ID, CreativeTabs.DECORATIONS, CreativeTabs.TOOLS);
		CapabilityEnchantable.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, GuiHandler.INSTANCE);
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
