package cofh.thermal.core.util.managers;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.fluid.SimpleFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.core.util.recipes.SimpleFluidFuel;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Basic fuel manager - single fluid key'd.
 */
public abstract class SimpleFluidFuelManager extends AbstractManager implements IFuelManager {

	public static int MIN_ENERGY = 10000;
	public static int MAX_ENERGY = 200000000;

	protected Map<Integer, IDynamoFuel> defaultMap = new Object2ObjectOpenHashMap<>();

	protected SimpleFluidFuelManager(int defaultEnergy) {

		super(defaultEnergy);
	}

	public boolean validFuel(FluidStack stack) {

		return validFuel(Collections.emptyList(), Collections.singletonList(new SimpleFluidStackHolder(stack)));
	}

	public IDynamoFuel removeFuel(FluidStack stack) {

		if (stack == null) {
			return null;
		}
		return defaultMap.remove(stack.getFluid().getName());
	}

	public IDynamoFuel addFuel(FluidStack input) {

		return addFuel(defaultEnergy, input);
	}

	public IDynamoFuel addFuel(int energy, FluidStack input) {

		if (input == null || !FluidRegistry.isFluidRegistered(input.getFluid()) || energy < MIN_ENERGY || energy > MAX_ENERGY || defaultMap.containsKey(FluidHelper.fluidHashcode(input))) {
			return null;
		}
		SimpleFluidFuel fuel = new SimpleFluidFuel(input, energy);
		defaultMap.put(FluidHelper.fluidHashcode(input), fuel);
		return fuel;
	}

	// region IFuelManager
	@Override
	public boolean validFuel(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return getFuel(inputSlots, inputTanks) != null;
	}

	@Override
	public IDynamoFuel getFuel(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputTanks.isEmpty() || inputTanks.get(0).isEmpty()) {
			return null;
		}
		return defaultMap.get(FluidHelper.fluidHashcode(inputTanks.get(0).getFluidStack()));
	}

	@Override
	public List<IDynamoFuel> getFuelList() {

		return new ArrayList<>(defaultMap.values());
	}
	// endregion

	// region IManager
	@Override
	public void refresh() {

	}
	// endregion
}
