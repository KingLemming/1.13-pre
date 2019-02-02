package cofh.thermal.core.util.managers;

import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.core.util.recipes.SimpleItemRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE;

/**
 * Simple recipe manager - single item input allowed - single / multi item outputs.
 */
public abstract class SimpleItemRecipeManager extends BasicRecipeManager implements IRecipeManager {

	protected int maxOutputItems;
	protected int maxOutputFluids;

	protected SimpleItemRecipeManager(int defaultEnergy, int maxOutputItems, int maxOutputFluids) {

		super(defaultEnergy);
		this.maxOutputItems = maxOutputItems;
		this.maxOutputFluids = maxOutputFluids;
	}

	// region SINGLE ITEM OUTPUT
	public IMachineRecipe addRecipe(int energy, ItemStack input, ItemStack output, float chance) {

		if (maxOutputItems <= 0 || input.isEmpty() || output.isEmpty() || energy <= 0 || validRecipe(input)) {
			return null;
		}
		energy = (energy * scaleFactor) / 100;

		SimpleItemRecipe recipe = new SimpleItemRecipe(input, output, chance, energy);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}

	public IMachineRecipe addRecipe(int energy, ItemStack input, ItemStack output) {

		return addRecipe(energy, input, output, BASE_CHANCE);
	}

	public IMachineRecipe addRecipe(ItemStack input, ItemStack output, float chance) {

		return addRecipe(defaultEnergy, input, output, chance);
	}

	public IMachineRecipe addRecipe(ItemStack input, ItemStack output) {

		return addRecipe(defaultEnergy, input, output, BASE_CHANCE);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public IMachineRecipe addRecipe(int energy, ItemStack input, List<ItemStack> output, List<Float> chance) {

		if (input.isEmpty() || output.isEmpty() || output.size() > maxOutputItems || energy <= 0 || validRecipe(input)) {
			return null;
		}
		for (ItemStack stack : output) {
			if (stack.isEmpty()) {
				return null;
			}
		}
		energy = (energy * scaleFactor) / 100;

		SimpleItemRecipe recipe = new SimpleItemRecipe(input, output, chance, energy);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}

	public IMachineRecipe addRecipe(int energy, ItemStack input, List<ItemStack> output) {

		return addRecipe(energy, input, output, null);
	}

	public IMachineRecipe addRecipe(ItemStack input, List<ItemStack> output) {

		return addRecipe(defaultEnergy, input, output, null);
	}

	public IMachineRecipe addRecipe(ItemStack input, List<ItemStack> output, List<Float> chance) {

		return addRecipe(defaultEnergy, input, output, chance);
	}
	// endregion

	// region SINGLE FLUID OUTPUT
	public IMachineRecipe addRecipe(int energy, ItemStack input, FluidStack output) {

		if (maxOutputFluids <= 0 || input.isEmpty() || output == null || energy <= 0 || validRecipe(input)) {
			return null;
		}
		energy = (energy * scaleFactor) / 100;

		SimpleItemRecipe recipe = new SimpleItemRecipe(input, output, energy);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}
	// endregion
}
