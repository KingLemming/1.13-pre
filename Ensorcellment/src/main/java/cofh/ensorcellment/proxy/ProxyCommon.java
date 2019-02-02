package cofh.ensorcellment.proxy;

import cofh.lib.event.EventHandlerArchery;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyCommon {

	// region INITIALIZATION
	public void preInit(FMLPreInitializationEvent event) {

		EventHandler.register();
		EventHandlerArchery.register();
	}

	public void initialize(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
	// endregion
}
