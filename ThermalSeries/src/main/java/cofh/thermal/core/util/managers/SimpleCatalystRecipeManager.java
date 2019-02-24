package cofh.thermal.core.util.managers;

import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.lib.util.comparison.OreValidator;
import cofh.thermal.core.util.recipes.IRecipeCatalyst;
import cofh.thermal.core.util.recipes.SimpleCatalyst;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;

/**
 * Basic recipe manager - single item key'd + catalyst support.
 */
public abstract class SimpleCatalystRecipeManager extends SimpleItemRecipeManager {

	protected Map<ComparableItemStackValidated, IRecipeCatalyst> catalystMap = new Object2ObjectOpenHashMap<>();
	protected OreValidator catalystValidator = new OreValidator();

	protected SimpleCatalystRecipeManager(int defaultEnergy, int maxOutputItems, int maxOutputFluids) {

		super(defaultEnergy, maxOutputItems, maxOutputFluids);
	}

	// region CATALYSTS
	public IRecipeCatalyst getCatalyst(ItemStack input) {

		ComparableItemStackValidated query = convertInput(input);
		IRecipeCatalyst catalyst = catalystMap.get(query);
		if (catalyst == null) {
			query.metadata = OreDictionary.WILDCARD_VALUE;
			catalyst = catalystMap.get(query);
		}
		return catalyst;
	}

	public IRecipeCatalyst addCatalyst(ItemStack input, float primaryMod, float secondaryMod, float energyMod, float minChance, float useChance) {

		SimpleCatalyst catalyst = new SimpleCatalyst(primaryMod, secondaryMod, energyMod, minChance, useChance);
		catalystMap.put(convertInput(input), catalyst);
		return catalyst;
	}

	public boolean validCatalyst(ItemStack input) {

		return getCatalyst(input) != null;
	}

	public IRecipeCatalyst removeCatalyst(ItemStack input) {

		return catalystMap.remove(convertInput(input));
	}

	public ComparableItemStackValidated catalystInput(ItemStack input) {

		return new ComparableItemStackValidated(input, catalystValidator);
	}
	// endregion

	// region IManager
	@Override
	public void refresh() {

		super.refresh();

		Map<ComparableItemStackValidated, IRecipeCatalyst> tempCatalysts = new Object2ObjectOpenHashMap<>(catalystMap.size());
		catalystMap.forEach((key, value) -> tempCatalysts.put(catalystInput(key.toItemStack()), value));
		catalystMap.clear();
		catalystMap.putAll(tempCatalysts);
	}
	// endregion
}
