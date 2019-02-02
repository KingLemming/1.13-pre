package cofh.core.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidCoFH extends Fluid {

	public FluidCoFH(String fluidName, String modName) {

		super(fluidName, new ResourceLocation(modName, "blocks/fluids/" + fluidName + "_still"), new ResourceLocation(modName, "blocks/fluids/" + fluidName + "_flow"));
	}

}
