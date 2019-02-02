package cofh.thermal.expansion.util.managers.machine;

import cofh.thermal.core.util.managers.SimpleItemRecipeManager;

import static cofh.thermal.core.ThermalSeries.config;

public class CrucibleRecipeManager extends SimpleItemRecipeManager {

	private static final CrucibleRecipeManager INSTANCE = new CrucibleRecipeManager();
	protected static final int DEFAULT_ENERGY = 40000;

	public static CrucibleRecipeManager instance() {

		return INSTANCE;
	}

	private CrucibleRecipeManager() {

		super(DEFAULT_ENERGY, 0, 1);

		// TODO: Validators
	}

	// region IRecipeManager
	@Override
	public void config() {

		String category = "Machines.Crucible";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}
	// endregion
}
