package cofh.thermal.expansion.util.managers.machine;

import cofh.thermal.core.util.managers.SimpleItemRecipeManager;

import static cofh.thermal.core.ThermalSeries.config;

public class InsolatorRecipeManager extends SimpleItemRecipeManager {

	private static final InsolatorRecipeManager INSTANCE = new InsolatorRecipeManager();
	protected static final int DEFAULT_ENERGY = 20000;
	protected static boolean defaultSeedRecipes = true;

	public static InsolatorRecipeManager instance() {

		return INSTANCE;
	}

	private InsolatorRecipeManager() {

		super(DEFAULT_ENERGY, 4, 0);

		defaultValidator.addPrefix("seed");
	}

	// region IRecipeManager
	@Override
	public void config() {

		String category = "Machines.Insolator";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		comment = "If TRUE, default Seed processing recipes will be created based on registered items.";
		defaultSeedRecipes = config.getBoolean("Default Seed Recipes", category, defaultSeedRecipes, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}
	// endregion

	// region HELPERS

	// endregion
}
