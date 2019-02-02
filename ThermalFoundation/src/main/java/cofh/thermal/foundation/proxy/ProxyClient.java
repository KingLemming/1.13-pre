package cofh.thermal.foundation.proxy;

import cofh.core.render.RenderEntityAsIcon;
import cofh.thermal.foundation.entity.monster.EntityBasalz;
import cofh.thermal.foundation.entity.monster.EntityBlitz;
import cofh.thermal.foundation.entity.monster.EntityBlizz;
import cofh.thermal.foundation.entity.projectile.EntityBasalzBolt;
import cofh.thermal.foundation.entity.projectile.EntityBlitzBolt;
import cofh.thermal.foundation.entity.projectile.EntityBlizzBolt;
import cofh.thermal.foundation.render.entity.RenderEntityBasalz;
import cofh.thermal.foundation.render.entity.RenderEntityBlitz;
import cofh.thermal.foundation.render.entity.RenderEntityBlizz;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

	@Override
	public void registerEntityModels() {

		RenderingRegistry.registerEntityRenderingHandler(EntityBlizz.class, RenderEntityBlizz::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlitz.class, RenderEntityBlitz::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBasalz.class, RenderEntityBasalz::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityBlizzBolt.class, manager -> new RenderEntityAsIcon(manager).setIcon("cofh.thermal:items/dusts/dust_blizz"));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlitzBolt.class, manager -> new RenderEntityAsIcon(manager).setIcon("cofh.thermal:items/dusts/dust_blitz"));
		RenderingRegistry.registerEntityRenderingHandler(EntityBasalzBolt.class, manager -> new RenderEntityAsIcon(manager).setIcon("cofh.thermal:items/dusts/dust_basalz"));
	}

}
