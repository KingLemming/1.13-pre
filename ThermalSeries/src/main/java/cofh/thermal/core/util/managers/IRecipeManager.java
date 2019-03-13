package cofh.thermal.core.util.managers;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.thermal.core.util.recipes.IMachineRecipe;

import java.util.List;

public interface IRecipeManager extends IManager {

	default boolean validRecipe(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return getRecipe(inputSlots, inputTanks) != null;
	}

	IMachineRecipe getRecipe(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	List<IMachineRecipe> getRecipeList();

}
