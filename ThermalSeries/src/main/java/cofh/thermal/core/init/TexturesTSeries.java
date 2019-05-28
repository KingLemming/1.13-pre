package cofh.thermal.core.init;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.thermal.core.init.ConfigTSeries.enableColorBlindTextures;
import static cofh.thermal.core.init.FluidsTSeries.*;

public class TexturesTSeries {

	private TexturesTSeries() {

	}

	// region REGISTRATION
	public static void registerTextures(TextureMap map) {

		textureMap = map;

		// region SIDE CONFIG
		CONFIG_BLUE = registerConfig("blue");
		CONFIG_ORANGE = registerConfig("orange");
		CONFIG_OMNI = registerConfig("omni");
		CONFIG_OPEN = registerConfig("open", false);
		CONFIG = new TextureAtlasSprite[] { null, CONFIG_BLUE, CONFIG_ORANGE, CONFIG_OMNI, CONFIG_OPEN };
		// endregion

		// region FLUIDS
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
		// endregion
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

	private static TextureAtlasSprite registerConfig(String config) {

		return registerConfig(config, true);
	}

	private static TextureAtlasSprite registerConfig(String config, boolean hasCb) {

		if (enableColorBlindTextures && hasCb) {
			config += "_cb";
		}
		return textureMap.registerSprite(new ResourceLocation(ID_THERMAL_SERIES, "blocks/config/config_" + config));
	}

	private static void registerFluidTextures(String fluidName) {

		textureMap.registerSprite(new ResourceLocation(ID_THERMAL_SERIES, "blocks/fluids/" + fluidName + "_still"));
		textureMap.registerSprite(new ResourceLocation(ID_THERMAL_SERIES, "blocks/fluids/" + fluidName + "_flow"));
	}

	// endregion

	// region REFERENCES
	public static TextureAtlasSprite[] CONFIG;
	public static TextureAtlasSprite CONFIG_BLUE;
	public static TextureAtlasSprite CONFIG_ORANGE;
	public static TextureAtlasSprite CONFIG_OMNI;
	public static TextureAtlasSprite CONFIG_OPEN;
	// endregion
}
