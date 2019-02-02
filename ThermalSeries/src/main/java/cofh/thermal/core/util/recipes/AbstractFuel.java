package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
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
	public int getEnergy(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return energy;
	}
	// endregion
}
