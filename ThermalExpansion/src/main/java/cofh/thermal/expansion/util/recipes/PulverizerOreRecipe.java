package cofh.thermal.expansion.util.recipes;

import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.lib.util.comparison.OreValidator;
import cofh.thermal.core.util.recipes.IRecipeCatalyst;
import cofh.thermal.core.util.recipes.SimpleItemCatalystRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class PulverizerOreRecipe extends SimpleItemCatalystRecipe {

	protected static Map<ComparableItemStackValidated, IRecipeCatalyst> catalystMap = new Object2ObjectOpenHashMap<>();
	protected static OreValidator validator = new OreValidator();

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

		ComparableItemStackValidated query = validateInput(input);
		IRecipeCatalyst catalyst = catalystMap.get(query);
		if (catalyst == null) {
			query.metadata = OreDictionary.WILDCARD_VALUE;
			catalyst = catalystMap.get(query);
		}
		return catalyst;
	}

	public ComparableItemStackValidated validateInput(ItemStack stack) {

		return new ComparableItemStackValidated(stack, validator);
	}
	// endregion
}
