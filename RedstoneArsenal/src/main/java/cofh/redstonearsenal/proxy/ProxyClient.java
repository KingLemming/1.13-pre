package cofh.redstonearsenal.proxy;

import cofh.lib.render.RenderArrowCoFH;
import cofh.redstonearsenal.entity.projectile.EntityArrowFlux;
import cofh.redstonearsenal.init.EquipmentRSA;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static cofh.lib.util.Constants.ID_REDSTONE_ARSENAL;
import static cofh.redstonearsenal.RedstoneArsenal.registrar;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);

		registerEntityModels();
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
	public void registerEntityModels() {

		RenderingRegistry.registerEntityRenderingHandler(EntityArrowFlux.class, manager -> new RenderArrowCoFH(manager, new ResourceLocation(ID_REDSTONE_ARSENAL, "textures/entity/arrow_flux.png")));
	}

	@Override
	public void registerItemModels() {

		registrar.registerItemModels();
		registrar.registerFluidModels();

		EquipmentRSA.registerModels();
	}
	// endregion
}
