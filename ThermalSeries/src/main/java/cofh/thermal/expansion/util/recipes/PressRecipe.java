package cofh.thermal.expansion.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.thermal.core.util.recipes.AbstractRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PressRecipe extends AbstractRecipe {

	public PressRecipe(int energy, ItemStack inputItem, ItemStack template, ItemStack outputItem) {

		super(energy);

		this.inputItems.add(template);
		this.inputItems.add(inputItem);
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
