package cofh.thermal.core.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static cofh.thermal.core.ThermalSeries.registrar;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);

		EventHandlerClient.register();
	}

	@Override
	public void initialize(FMLInitializationEvent event) {

		super.initialize(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		super.postInit(event);
	}
	// endregion

	// region HELPERS
	@Override
	public void registerBlockModels() {

		registrar.registerBlockModels();
	}

	@Override
	public void registerItemModels() {

		registrar.registerItemModels();
		registrar.registerFluidModels();
	}
	// endregion
}
