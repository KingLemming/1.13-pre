package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractFuel implements IDynamoFuel {

	protected final List<ItemStack> inputItems = new ArrayList<>();
	protected final List<FluidStack> inputFluids = new ArrayList<>();
	protected final int energy;

	public AbstractFuel(int energy) {

		this.energy = energy;
	}

	// region IDynamoFuel
	@Override
	public List<ItemStack> getInputItems() {

		return inputItems;
	}

	@Override
	public List<FluidStack> getInputFluids() {

		return inputFluids;
	}

	@Override
	public List<Integer> getInputItemCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputItems.isEmpty()) {
			return Collections.emptyList();
		}
		ArrayList<Integer> ret = new ArrayList<>();
		for (ItemStack input : inputItems) {
			ret.add(input.getCount());
		}
		return ret;
	}

	@Override
	public List<Integer> getInputFluidCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputFluids.isEmpty()) {
			return Collections.emptyList();
		}
		ArrayList<Integer> ret = new ArrayList<>();
		for (FluidStack input : inputFluids) {
			ret.add(input.amount);
		}
		return ret;
	}

	@Override
	public int getEnergy(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return energy;
	}
	// endregion
}
