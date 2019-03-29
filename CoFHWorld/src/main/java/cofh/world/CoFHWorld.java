package cofh.world;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod (modid = CoFHWorld.MOD_ID, name = CoFHWorld.MOD_NAME, version = CoFHWorld.VERSION, dependencies = CoFHWorld.DEPENDENCIES)
public class CoFHWorld {

	public static final String MOD_ID = "cofh_world";
	public static final String MOD_NAME = "CoFH World";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:forge@[14.0.0.0,15.0.0.0);";

	@Instance (MOD_ID)
	public static CoFHWorld instance;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static Configuration config;

	public CoFHWorld() {

		super();

		// WorldHandler.register();
	}

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// WorldProps.configDir = event.getModConfigurationDirectory();

		// config = new Configuration(new File(WorldProps.configDir, "/cofh/world/config.cfg"), VERSION, true);
		// config.load();

		// WorldProps.preInit();

		// WorldHandler.initialize();
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {

		// WorldHandler.reloadConfig(false);
		// config.save();

		log.info(MOD_NAME + ": Load Complete.");
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {

		// event.registerServerCommand(new CommandCoFHWorld());
	}
	// endregion
}
