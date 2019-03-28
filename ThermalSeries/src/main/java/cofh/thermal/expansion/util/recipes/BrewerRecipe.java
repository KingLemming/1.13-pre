package cofh.thermal.expansion.util.recipes;

import cofh.thermal.core.util.recipes.AbstractRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BrewerRecipe extends AbstractRecipe {

	public BrewerRecipe(int energy, ItemStack inputItem, FluidStack inputFluid, FluidStack outputFluid) {

		super(energy);

		this.inputItems.add(inputItem);
		this.inputFluids.add(inputFluid);
		this.outputFluids.add(outputFluid);
	}

}
