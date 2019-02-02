package cofh.thermal.core.util.managers;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.inventory.SimpleItemStackHolder;
import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Basic recipe manager - single item input.
 */
public abstract class BasicRecipeManager extends AbstractManager implements IRecipeManager {

	protected Map<ComparableItemStackValidated, IMachineRecipe> defaultMap = new Object2ObjectOpenHashMap<>();
	protected Map<ComparableItemStackValidated, IMachineRecipe> customMap = new Object2ObjectOpenHashMap<>();

	protected BasicRecipeManager(int defaultEnergy) {

		super(defaultEnergy);
	}

	public boolean validRecipe(ItemStack stack) {

		return validRecipe(Collections.singletonList(new SimpleItemStackHolder(stack)), Collections.emptyList());
	}

	public IMachineRecipe removeRecipe(ItemStack stack) {

		return hasCustomOreID(stack) ? customMap.remove(customInput(stack)) : defaultMap.remove(defaultInput(stack));
	}

	// region IRecipeManager
	@Override
	public boolean validRecipe(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return getRecipe(inputSlots, inputTanks) != null;
	}

	@Override
	public IMachineRecipe getRecipe(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputSlots.isEmpty() || inputSlots.get(0).isEmpty()) {
			return null;
		}
		ItemStack inputStack = inputSlots.get(0).getItemStack();
		// If a custom OreID was found:
		if (hasCustomOreID(inputStack)) {
			ComparableItemStackValidated query = customInput(inputStack);
			IMachineRecipe recipe = customMap.get(query);
			if (recipe == null) {
				query.metadata = OreDictionary.WILDCARD_VALUE;
				recipe = customMap.get(query);
			}
			return recipe;
		}
		// Otherwise, default:
		ComparableItemStackValidated query = defaultInput(inputStack);
		IMachineRecipe recipe = defaultMap.get(query);
		if (recipe == null) {
			query.metadata = OreDictionary.WILDCARD_VALUE;
			recipe = defaultMap.get(query);
		}
		return recipe;
	}

	@Override
	public List<IMachineRecipe> getRecipeList() {

		return new ArrayList<>(defaultMap.values());
	}
	// endregion

	// region IManager
	@Override
	public void refresh() {

		Map<ComparableItemStackValidated, IMachineRecipe> tempRecipes = new Object2ObjectOpenHashMap<>(defaultMap.size());
		defaultMap.forEach((key, value) -> tempRecipes.put(defaultInput(value.getInputItems().get(0)), value));
		defaultMap.clear();
		defaultMap.putAll(tempRecipes);

		Map<ComparableItemStackValidated, IMachineRecipe> tempCustom = new Object2ObjectOpenHashMap<>(customMap.size());
		customMap.forEach((key, value) -> tempCustom.put(customInput(value.getInputItems().get(0)), value));
		customMap.clear();
		customMap.putAll(tempCustom);
	}
	// endregion
}
