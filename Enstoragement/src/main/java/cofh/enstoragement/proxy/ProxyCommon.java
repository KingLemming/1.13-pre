package cofh.enstoragement.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyCommon {

	// region INITIALIZATION
	public void preInit(FMLPreInitializationEvent event) {

		EventHandler.register();
	}

	public void initialize(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
	// endregion

	// region HELPERS
	public void registerBlockModels() {

	}

	public void registerItemModels() {

	}
	// endregion
}
