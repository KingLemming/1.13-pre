package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class SimpleFluidFuel extends AbstractFuel {

	public SimpleFluidFuel(FluidStack input, int energy) {

		super(energy);
		this.inputFluids.add(input);
	}

	// region OPTIMIZATION
	@Override
	public List<Integer> getInputFluidCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return Collections.singletonList(this.inputFluids.get(0).amount);
	}
	// endregion
}
