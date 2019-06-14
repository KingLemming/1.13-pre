package cofh.thermal.expansion.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.thermal.core.util.recipes.AbstractRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChillerRecipe extends AbstractRecipe {

	public ChillerRecipe(int energy, FluidStack inputFluid, ItemStack template, ItemStack outputItem) {

		super(energy);

		this.inputItems.add(template);
		this.inputFluids.add(inputFluid);
		this.outputItems.add(outputItem);
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

}
