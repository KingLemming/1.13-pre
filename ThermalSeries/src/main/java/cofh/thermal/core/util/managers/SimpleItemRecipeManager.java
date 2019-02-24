package cofh.thermal.core.util.managers;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.inventory.SimpleItemStackHolder;
import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.core.util.recipes.SimpleItemRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cofh.lib.util.Constants.BASE_CHANCE_LOCKED;

/**
 * Basic recipe manager - single item key'd.
 */
public abstract class SimpleItemRecipeManager extends AbstractManager implements IRecipeManager {

	protected Map<ComparableItemStackValidated, IMachineRecipe> defaultMap = new Object2ObjectOpenHashMap<>();
	protected Map<ComparableItemStackValidated, IMachineRecipe> customMap = new Object2ObjectOpenHashMap<>();

	protected int maxOutputItems;
	protected int maxOutputFluids;

	protected SimpleItemRecipeManager(int defaultEnergy, int maxOutputItems, int maxOutputFluids) {

		super(defaultEnergy);
		this.maxOutputItems = maxOutputItems;
		this.maxOutputFluids = maxOutputFluids;
	}

	public boolean validRecipe(ItemStack input) {

		return validRecipe(Collections.singletonList(new SimpleItemStackHolder(input)), Collections.emptyList());
	}

	public IMachineRecipe removeRecipe(ItemStack input) {

		return hasCustomOreID(input) ? customMap.remove(customInput(input)) : defaultMap.remove(defaultInput(input));
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

		return addRecipe(energy, input, output, BASE_CHANCE_LOCKED);
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

	// region MULTIPLE ITEM + SINGLE FLUID OUTPUT
	public IMachineRecipe addRecipe(int energy, ItemStack input, List<ItemStack> output, List<Float> chance, List<FluidStack> outputFluids) {

		if (input.isEmpty() || output.isEmpty() && outputFluids.isEmpty() || output.size() > maxOutputItems || outputFluids.size() > maxOutputFluids || energy <= 0 || validRecipe(input)) {
			return null;
		}
		for (ItemStack stack : output) {
			if (stack.isEmpty()) {
				return null;
			}
		}
		for (FluidStack stack : outputFluids) {
			if (stack == null) {
				return null;
			}
		}
		energy = (energy * scaleFactor) / 100;

		SimpleItemRecipe recipe = new SimpleItemRecipe(input, output, chance, outputFluids, energy);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}
	// endregion

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
		defaultMap.forEach((key, value) -> tempRecipes.put(defaultInput(key.toItemStack()), value));
		defaultMap.clear();
		defaultMap.putAll(tempRecipes);

		Map<ComparableItemStackValidated, IMachineRecipe> tempCustom = new Object2ObjectOpenHashMap<>(customMap.size());
		customMap.forEach((key, value) -> tempCustom.put(customInput(key.toItemStack()), value));
		customMap.clear();
		customMap.putAll(tempCustom);
	}
	// endregion
}
