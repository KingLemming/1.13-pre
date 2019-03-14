package cofh.thermal.expansion.util.recipes;

import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.thermal.core.util.recipes.IRecipeCatalyst;
import cofh.thermal.core.util.recipes.SimpleItemCatalystRecipe;
import cofh.thermal.expansion.util.managers.machine.PulverizerRecipeManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class PulverizerOreRecipe extends SimpleItemCatalystRecipe {

	// region SINGLE ITEM OUTPUT
	public PulverizerOreRecipe(int energy, ItemStack input, ItemStack output, float chance) {

		super(energy, input, output, chance);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public PulverizerOreRecipe(int energy, ItemStack input, List<ItemStack> output, @Nullable List<Float> chance) {

		super(energy, input, output, chance);
	}
	// endregion

	// HELPERS
	public IRecipeCatalyst getCatalyst(ItemStack input) {

		return PulverizerRecipeManager.instance().getCatalyst(input);
	}

	public ComparableItemStackValidated catalystInput(ItemStack input) {

		return PulverizerRecipeManager.instance().catalystInput(input);
	}
	// endregion
}
