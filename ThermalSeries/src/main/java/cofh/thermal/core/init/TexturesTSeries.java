package cofh.thermal.core.init;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.thermal.core.init.FluidsTSeries.*;

public class TexturesTSeries {

	private TexturesTSeries() {

	}

	// region REGISTRATION
	public static void registerTextures(TextureMap map) {

		textureMap = map;

		registerFluidTextures(fluidSteam);
		registerFluidTextures(fluidExperience);

		registerFluidTextures(fluidMilk);
		registerFluidTextures(fluidBeetrootSoup);
		registerFluidTextures(fluidMushroomStew);

		registerFluidTextures(fluidPotion);

		registerFluidTextures(fluidRedstone);
		registerFluidTextures(fluidGlowstone);
		registerFluidTextures(fluidEnder);

		// BIOFUEL FLUIDS
		registerFluidTextures(fluidSeedOil);
		registerFluidTextures(fluidBiocrude);
		registerFluidTextures(fluidBiofuel);

		// FOSSIL FUEL FLUIDS
		registerFluidTextures(fluidCreosote);
		registerFluidTextures(fluidCoal);
		registerFluidTextures(fluidCrudeOil);
		registerFluidTextures(fluidRefinedOil);
		registerFluidTextures(fluidRefinedFuel);

		// TREE FLUIDS
		registerFluidTextures(fluidSap);
		registerFluidTextures(fluidSyrup);
		registerFluidTextures(fluidResin);
		registerFluidTextures(fluidTreeOil);
	}
	// endregion

	// region HELPERS
	private static TextureMap textureMap;

	private static void registerFluidTextures(Fluid fluid) {

		if (fluid == null) {
			return;
		}
		registerFluidTextures(fluid.getName());
	}

	private static void registerFluidTextures(String fluidName) {

		textureMap.registerSprite(new ResourceLocation(ID_THERMAL_SERIES, "blocks/fluids/" + fluidName + "_still"));
		textureMap.registerSprite(new ResourceLocation(ID_THERMAL_SERIES, "blocks/fluids/" + fluidName + "_flow"));
	}
	// endregion
}
