package cofh.ensorcellment;

import cofh.ensorcellment.enchantment.EnchantmentsEnsorc;
import cofh.ensorcellment.proxy.ProxyCommon;
import cofh.lib.capabilities.CapabilityEnchantable;
import cofh.lib.util.ConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static cofh.lib.util.Constants.ID_COFH_CORE;
import static cofh.lib.util.Constants.ID_ENSORCELLMENT;

@Mod (modid = Ensorcellment.MOD_ID, name = Ensorcellment.MOD_NAME, version = Ensorcellment.VERSION, dependencies = Ensorcellment.DEPENDENCIES, certificateFingerprint = "8a6abf2cb9e141b866580d369ba6548732eff25f")
public class Ensorcellment {

	public static final String MOD_ID = ID_ENSORCELLMENT;
	public static final String MOD_NAME = "Ensorcellment";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE;

	@Instance (MOD_ID)
	public static Ensorcellment instance;

	@SidedProxy (clientSide = "cofh.ensorcellment.proxy.ProxyClient", serverSide = "cofh.ensorcellment.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static ConfigHandler config;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		File configDir = event.getModConfigurationDirectory();

		config = new ConfigHandler(new Configuration(new File(configDir, "/cofh/" + MOD_ID + "/config.cfg"), VERSION, true));
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
	public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {

		EnchantmentsEnsorc.registerEnchantments(event);
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {

	}
	// endregion

	// region HELPERS
	public static boolean preventFarmlandTrampling = true;
	public static boolean hardDisable = false;

	public static void config() {

		String category = "Enchantment.FeatherFalling";
		String comment = "If TRUE, Feather Falling will prevent Farmland from being trampled.";

		preventFarmlandTrampling = config.get("Prevent Farmland Trampling", category, true, comment);

		category = "~MODPACK OPTIONS~";
		comment = "If TRUE, then disabled Enchantments WILL NOT BE REGISTERED. Only do this if you fully understand the consequences. World backup is recommended.";

		hardDisable = config.get("Disable Prevents Registration", category, true, comment);
	}

	private static void registerHandlers() {

		MinecraftForge.EVENT_BUS.register(instance);
		CapabilityEnchantable.register();
	}
	// endregion
}
