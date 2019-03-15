package cofh.thermal.core.init;

import cofh.thermal.core.util.managers.LexiconManager;

import static cofh.thermal.core.ThermalSeries.config;

public class ConfigTSeries {

	private ConfigTSeries() {

	}

	public static void configCommon() {

		String category;
		String comment;

		// region MODPACK OPTIONS
		category = "~MODPACK OPTIONS~";
		comment = "If TRUE, then ALL RECIPES from the Thermal Series will be DISABLED. Everything will still be registered - this is a convenience option for pack makers who are completely overhauling recipes.";
		disableAllRecipes = config.getBoolean("Disable ALL Recipes", category, false, comment);

		comment = "If TRUE, then ALL HOSTILE MOB SPAWNS from the Thermal Series will be DISABLED. Everything will still be registered (including spawn eggs) - this is a convenience option for pack makers who do not want ANY mobs to naturally spawn.";
		disableAllHostileMobSpawns = config.getBoolean("Disable ALL Hostile Mob Spawns", category, false, comment);

		comment = "Adjust this value to change the render update delay for many Thermal Series tiles. You should really only change this if you know what you're doing. This is a server-wide setting.";
		tileUpdateDelay = config.getInt("Tile Update Delay", category, tileUpdateDelay, 10, 1600, comment);
		// endregion

		// region CRAFTING
		category = "Crafting";

		comment = "If TRUE, non-Basic Gears will require an additional Iron Ingot to craft";
		enableAlternateGears = config.getBoolean("Enable Iron-Core Gears", category, enableAlternateGears, comment);

		comment = "If TRUE, Basic (Wood and Stone) Gears will be craftable.";
		enableBasicGears = config.getBoolean("Enable Basic Gears", category, enableBasicGears, comment);

		//		comment = "If TRUE, Steel will have simple recipes and can be smelted in a Furnace.";
		//		enableBasicSteel = config.getBoolean("Enable Basic Steel", category, enableBasicSteel, comment);

		comment = "If TRUE, Horse Armors will be craftable.";
		enableHorseArmorCrafting = config.getBoolean("Enable Horse Armor Crafting", category, enableHorseArmorCrafting, comment);

		comment = "If TRUE, Saddles will be craftable.";
		enableSaddleCrafting = config.getBoolean("Enable Saddle Crafting", category, enableSaddleCrafting, comment);
		// endregion

		// region FUEL
		category = "Fuels";

		comment = "Burn Time (in ticks) for Coal Coke in a Furnace.";
		fuelCoalCoke = config.getInt("Coal Coke", category, fuelCoalCoke, 800, 32000, comment);
		// endregion

		LexiconManager.config();
	}

	public static void configClient() {

	}

	// region MODPACK OPTIONS
	public static boolean disableAllRecipes = false;
	public static boolean disableAllHostileMobSpawns = false;
	public static int tileUpdateDelay = 160;
	// endregion

	// region THERMAL OPTIONS
	public static boolean enableBioFuelFluids = true;
	public static boolean enableFossilFuelFluids = true;
	public static boolean enableTreeFluids = true;
	// endregion

	// region CRAFTING
	public static boolean enableAlternateGears = false;
	public static boolean enableBasicGears = false;
	// public static boolean enableBasicSteel = false;
	public static boolean enableHorseArmorCrafting = true;
	public static boolean enableSaddleCrafting = true;
	// endregion

	// region FUEL
	public static int fuelCoal = 1600;
	public static int fuelCoalCoke = 3200;
	// endregion

	// region CONSTANTS
	public static final int PACKET_LEXICON_STUDY = 8;
	public static final int PACKET_LEXICON_TRANSMUTE = 9;
	// endregion
}
