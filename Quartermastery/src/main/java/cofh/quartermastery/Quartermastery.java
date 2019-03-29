package cofh.quartermastery;

import cofh.quartermastery.init.EquipmentArmor;
import cofh.quartermastery.init.EquipmentHorseArmor;
import cofh.quartermastery.init.EquipmentTools;
import cofh.quartermastery.proxy.ProxyCommon;
import net.minecraft.item.Item;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static cofh.lib.util.Constants.ID_COFH_CORE;
import static cofh.lib.util.Constants.ID_QUARTERMASTERY;

// TODO: DEACTIVATED
// @Mod (modid = Quartermastery.MOD_ID, name = Quartermastery.MOD_NAME, version = Quartermastery.VERSION, dependencies = Quartermastery.DEPENDENCIES)
public class Quartermastery {

	public static final String MOD_ID = ID_QUARTERMASTERY;
	public static final String MOD_NAME = "Quartermastery";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE;

	@Instance (MOD_ID)
	public static Quartermastery instance;

	@SidedProxy (clientSide = "cofh.quartermastery.proxy.ProxyClient", serverSide = "cofh.quartermastery.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static Configuration config;

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

		log.info(MOD_NAME + ": Load Complete.");
	}
	// endregion

	// region REGISTRATION
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {

		EquipmentTools.registerItems();
		EquipmentArmor.registerItems();
		EquipmentHorseArmor.registerItems();

		proxy.registerItemModels();
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		if (disableAllRecipes) {
			return;
		}
		EquipmentTools.registerRecipes();
		EquipmentArmor.registerRecipes();
		EquipmentHorseArmor.registerRecipes();
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
	}
	// endregion
}
