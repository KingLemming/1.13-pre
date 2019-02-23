package cofh.thermal.core.util.managers;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.inventory.SimpleItemStackHolder;
import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.core.util.recipes.SimpleItemFuel;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Basic fuel manager - single item key'd.
 */
public abstract class SimpleItemFuelManager extends AbstractManager implements IFuelManager {

	public static int MIN_ENERGY = 3000;
	public static int MAX_ENERGY = 200000000;

	protected Map<ComparableItemStackValidated, IDynamoFuel> defaultMap = new Object2ObjectOpenHashMap<>();
	protected Map<ComparableItemStackValidated, IDynamoFuel> customMap = new Object2ObjectOpenHashMap<>();

	protected SimpleItemFuelManager(int defaultEnergy) {

		super(defaultEnergy);
	}

	public boolean validFuel(ItemStack stack) {

		return validFuel(Collections.singletonList(new SimpleItemStackHolder(stack)), Collections.emptyList());
	}

	public IDynamoFuel removeFuel(ItemStack stack) {

		return hasCustomOreID(stack) ? customMap.remove(customInput(stack)) : defaultMap.remove(defaultInput(stack));
	}

	public IDynamoFuel addFuel(ItemStack input) {

		return addFuel(defaultEnergy, input);
	}

	public IDynamoFuel addFuel(int energy, ItemStack input) {

		if (input.isEmpty() || energy < MIN_ENERGY || energy > MAX_ENERGY || validFuel(input)) {
			return null;
		}
		energy = (energy * scaleFactor) / 100;

		SimpleItemFuel fuel = new SimpleItemFuel(input, energy);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), fuel);
		} else {
			defaultMap.put(defaultInput(input), fuel);
		}
		return fuel;
	}

	// region IFuelManager
	@Override
	public boolean validFuel(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return getFuel(inputSlots, inputTanks) != null;
	}

	@Override
	public IDynamoFuel getFuel(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputSlots.isEmpty() || inputSlots.get(0).isEmpty()) {
			return null;
		}
		ItemStack inputStack = inputSlots.get(0).getItemStack();
		// If a custom OreID was found:
		if (hasCustomOreID(inputStack)) {
			ComparableItemStackValidated query = customInput(inputStack);
			IDynamoFuel fuel = customMap.get(query);
			if (fuel == null) {
				query.metadata = OreDictionary.WILDCARD_VALUE;
				fuel = customMap.get(query);
			}
			return fuel;
		}
		// Otherwise, default:
		ComparableItemStackValidated query = defaultInput(inputStack);
		IDynamoFuel fuel = defaultMap.get(query);
		if (fuel == null) {
			query.metadata = OreDictionary.WILDCARD_VALUE;
			fuel = defaultMap.get(query);
		}
		return fuel;
	}

	@Override
	public List<IDynamoFuel> getFuelList() {

		return new ArrayList<>(defaultMap.values());
	}
	// endregion

	// region IManager
	@Override
	public void refresh() {

		Map<ComparableItemStackValidated, IDynamoFuel> tempRecipes = new Object2ObjectOpenHashMap<>(defaultMap.size());
		defaultMap.forEach((key, value) -> tempRecipes.put(defaultInput(key.toItemStack()), value));
		defaultMap.clear();
		defaultMap.putAll(tempRecipes);

		Map<ComparableItemStackValidated, IDynamoFuel> tempCustom = new Object2ObjectOpenHashMap<>(customMap.size());
		customMap.forEach((key, value) -> tempCustom.put(customInput(key.toItemStack()), value));
		customMap.clear();
		customMap.putAll(tempCustom);
	}
	// endregion
}
