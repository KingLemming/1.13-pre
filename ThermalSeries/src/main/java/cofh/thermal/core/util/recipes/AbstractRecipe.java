package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecipe implements IMachineRecipe {

	protected final List<ItemStack> inputItems = new ArrayList<>();
	protected final List<FluidStack> inputFluids = new ArrayList<>();
	protected final List<ItemStack> outputItems = new ArrayList<>();
	protected final List<FluidStack> outputFluids = new ArrayList<>();
	protected final List<Float> outputItemChances = new ArrayList<>();
	protected final int energy;

	public AbstractRecipe(int energy) {

		this.energy = energy;
	}

	// region IMachineRecipe
	@Override
	public List<ItemStack> getInputItems() {

		return inputItems;
	}

	@Override
	public List<FluidStack> getInputFluids() {

		return inputFluids;
	}

	@Override
	public List<ItemStack> getOutputItems(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return outputItems;
	}

	@Override
	public List<FluidStack> getOutputFluids(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return outputFluids;
	}

	/**
	 * Okay so there's a bit of trickery happening here - internally "unmodifiable" chance is stored as a negative. Saves some memory and is kinda clever.
	 * This shouldn't ever cause problems because you're relying on this method call and not hacking around in the recipe, right? ;)
	 */
	@Override
	public List<Float> getOutputItemChances(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		ArrayList<Float> modifiedChances = new ArrayList<>(outputItemChances);
		for (int i = 0; i < modifiedChances.size(); i++) {
			float chance = modifiedChances.get(i);
			if (chance < 0) {
				modifiedChances.set(i, chance * -1);
			} else {
				modifiedChances.set(i, chance);
			}
		}
		return modifiedChances;
	}

	@Override
	public int getEnergy(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return energy;
	}
	// endregion
}
