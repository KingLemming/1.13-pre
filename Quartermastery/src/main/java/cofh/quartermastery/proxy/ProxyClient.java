package cofh.quartermastery.proxy;

import cofh.lib.event.EventHandlerClientLib;
import cofh.quartermastery.init.EquipmentArmor;
import cofh.quartermastery.init.EquipmentHorseArmor;
import cofh.quartermastery.init.EquipmentTools;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);

		EventHandlerClientLib.register();
	}

	@Override
	public void initialize(FMLInitializationEvent event) {

		super.initialize(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		super.postInit(event);

		EventHandlerClientLib.registerReload();
	}
	// endregion

	// region HELPERS
	@Override
	public void registerItemModels() {

		EquipmentTools.registerModels();
		EquipmentArmor.registerModels();
		EquipmentHorseArmor.registerModels();
	}
	// endregion
}
