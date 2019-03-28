package cofh.thermal.expansion.util.managers.machine;

import cofh.thermal.core.util.managers.SimpleItemRecipeManager;

import static cofh.lib.util.Constants.PREFIX_DUST;
import static cofh.thermal.core.ThermalSeries.config;

public class CentrifugeRecipeManager extends SimpleItemRecipeManager {

	private static final CentrifugeRecipeManager INSTANCE = new CentrifugeRecipeManager();
	protected static final int DEFAULT_ENERGY = 8000;

	public static CentrifugeRecipeManager instance() {

		return INSTANCE;
	}

	private CentrifugeRecipeManager() {

		super(DEFAULT_ENERGY, 4, 1);

		defaultValidator.addPrefix(PREFIX_DUST);
	}

	// region IManager
	@Override
	public void config() {

		String category = "Machines.Centrifuge";
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
