package cofh.thermal.expansion.util.managers.dynamo;

import cofh.thermal.core.util.managers.SimpleItemFuelManager;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.core.util.recipes.SimpleItemFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import static cofh.lib.util.Constants.RF_PER_FURNACE_UNIT;
import static cofh.thermal.core.ThermalSeries.config;

public class SteamFuelManager extends SimpleItemFuelManager {

	private static final SteamFuelManager INSTANCE = new SteamFuelManager();
	public static int DEFAULT_ENERGY = 16000;

	public static SteamFuelManager instance() {

		return INSTANCE;
	}

	private SteamFuelManager() {

		super(DEFAULT_ENERGY);
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

	// region HELPERS
	public static int getEnergyFurnaceFuel(ItemStack stack) {

		if (stack.isEmpty()) {
			return 0;
		}
		if (stack.getItem().hasContainerItem(stack)) {
			return 0;
		}
		int energy = TileEntityFurnace.getItemBurnTime(stack) * RF_PER_FURNACE_UNIT;
		return energy >= MIN_ENERGY ? energy : 0;
	}
	// endregion

	// region IManager
	@Override
	public void config() {

		String category = "Dynamo.Steam";
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
