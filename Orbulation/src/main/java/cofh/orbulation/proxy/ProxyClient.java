package cofh.orbulation.proxy;

import cofh.orbulation.entity.projectile.EntityFlorb;
import cofh.orbulation.entity.projectile.EntityMorb;
import cofh.orbulation.render.entity.RenderEntityFlorb;
import cofh.orbulation.render.entity.RenderEntityMorb;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static cofh.lib.util.Constants.GEN_JSON_FILES;
import static cofh.orbulation.Orbulation.*;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);

		RenderingRegistry.registerEntityRenderingHandler(EntityFlorb.class, RenderEntityFlorb::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMorb.class, RenderEntityMorb::new);
	}

	@Override
	public void initialize(FMLInitializationEvent event) {

		super.initialize(event);

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemMorb::colorMultiplier, itemMorb);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemMorbReusable::colorMultiplier, itemMorbReusable);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		super.postInit(event);
	}
	// endregion

	// region HELPERS
	@Override
	public void registerItemModels() {

		if (GEN_JSON_FILES) {
			itemFlorb.generateModelFiles();
			itemFlorbMagmatic.generateModelFiles();

			itemMorb.generateModelFiles();
			itemMorbReusable.generateModelFiles();
		}
		itemFlorb.registerModel();
		itemFlorbMagmatic.registerModel();

		itemMorb.registerModel();
		itemMorbReusable.registerModel();
	}
	// endregion
}
