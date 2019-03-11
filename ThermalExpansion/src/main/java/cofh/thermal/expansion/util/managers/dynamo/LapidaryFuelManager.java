package cofh.thermal.expansion.util.managers.dynamo;

import cofh.thermal.core.util.managers.SimpleItemFuelManager;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.ThermalSeries.config;

public class LapidaryFuelManager extends SimpleItemFuelManager {

	private static final LapidaryFuelManager INSTANCE = new LapidaryFuelManager();
	protected static int DEFAULT_ENERGY = 16000;

	public static LapidaryFuelManager instance() {

		return INSTANCE;
	}

	private LapidaryFuelManager() {

		super(DEFAULT_ENERGY);
	}

	// region IManager
	@Override
	public void config() {

		String category = "Dynamos.Lapidary";
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
