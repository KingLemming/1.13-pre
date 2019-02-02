package cofh.thermal.expansion.util.managers.dynamo;

import cofh.thermal.core.util.managers.SimpleFluidFuelManager;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.core.util.recipes.SimpleFluidFuel;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static cofh.thermal.core.ThermalSeries.config;

public class MagmaticFuelManager extends SimpleFluidFuelManager {

	private static final MagmaticFuelManager INSTANCE = new MagmaticFuelManager();
	public static int DEFAULT_ENERGY = 100000;

	public static MagmaticFuelManager instance() {

		return INSTANCE;
	}

	private MagmaticFuelManager() {

		super(DEFAULT_ENERGY);
	}

	public IDynamoFuel addFuel(FluidStack input) {

		if (input == null || input.getFluid() == null) {
			return null;
		}
		return addFuel(defaultEnergy, input.getFluid().getName());
	}

	public IDynamoFuel addFuel(int energy, String fluidName) {

		if (!FluidRegistry.isFluidRegistered(fluidName) || energy < MIN_ENERGY || energy > MAX_ENERGY || defaultMap.containsKey(fluidName)) {
			return null;
		}
		SimpleFluidFuel fuel = new SimpleFluidFuel(new FluidStack(FluidRegistry.getFluid(fluidName), Fluid.BUCKET_VOLUME), energy);
		defaultMap.put(fluidName, fuel);
		return fuel;
	}

	// region IManager
	@Override
	public void config() {

		String category = "Dynamo.Magmatic";
		String comment;

		comment = "Adjust this value to change the default energy value for this dynamo's fuels.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy production of all of this dynamo's fuels. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}
	// endregion
}
