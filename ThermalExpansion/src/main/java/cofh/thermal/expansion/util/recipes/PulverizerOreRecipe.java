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
	public PulverizerOreRecipe(ItemStack input, ItemStack output, float chance, int energy) {

		super(input, output, chance, energy);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public PulverizerOreRecipe(ItemStack input, List<ItemStack> output, @Nullable List<Float> chance, int energy) {

		super(input, output, chance, energy);
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
