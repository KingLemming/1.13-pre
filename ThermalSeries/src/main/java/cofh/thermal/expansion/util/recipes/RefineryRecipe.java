package cofh.thermal.expansion.util.recipes;

import cofh.thermal.core.util.recipes.AbstractRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE;

public class RefineryRecipe extends AbstractRecipe {

	public RefineryRecipe(int energy, FluidStack inputFluid, List<FluidStack> outputFluids, ItemStack outputItem, float chance) {

		super(energy);

		this.inputFluids.add(inputFluid);
		this.outputFluids.addAll(outputFluids);
		this.outputItems.add(outputItem);
		this.outputItemChances.add(chance);
	}

	public RefineryRecipe(int energy, FluidStack inputFluid, List<FluidStack> outputFluids) {

		super(energy);

		this.inputFluids.add(inputFluid);
		this.outputFluids.addAll(outputFluids);
	}

	public RefineryRecipe(int energy, FluidStack inputFluid, List<FluidStack> outputFluids, ItemStack outputItem) {

		this(energy, inputFluid, outputFluids, outputItem, BASE_CHANCE);
	}

	// region SINGLE FLUIDS
	public RefineryRecipe(int energy, FluidStack inputFluid, FluidStack outputFluid) {

		this(energy, inputFluid, Collections.singletonList(outputFluid));
	}

	public RefineryRecipe(int energy, FluidStack inputFluid, FluidStack outputFluid, ItemStack outputItem) {

		this(energy, inputFluid, Collections.singletonList(outputFluid), outputItem, BASE_CHANCE);
	}

	public RefineryRecipe(int energy, FluidStack inputFluid, FluidStack outputFluid, ItemStack outputItem, float chance) {

		this(energy, inputFluid, Collections.singletonList(outputFluid), outputItem, chance);
	}
	// endregion
}
