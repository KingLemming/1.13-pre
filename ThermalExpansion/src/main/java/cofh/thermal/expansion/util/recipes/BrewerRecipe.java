package cofh.thermal.expansion.util.recipes;

import cofh.thermal.core.util.recipes.AbstractRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BrewerRecipe extends AbstractRecipe {

	public BrewerRecipe(ItemStack inputItem, FluidStack inputFluid, FluidStack outputFluid, int energy) {

		super(energy);

		inputItems.add(inputItem);
		inputFluids.add(inputFluid);
		outputFluids.add(outputFluid);
	}

}
