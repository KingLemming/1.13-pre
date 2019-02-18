package cofh.thermal.expansion.util.managers.machine;

import cofh.thermal.core.util.managers.SimpleItemRecipeManager;
import net.minecraftforge.oredict.OreDictionary;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.config;

public class SmelterRecipeManager extends SimpleItemRecipeManager {

	private static final SmelterRecipeManager INSTANCE = new SmelterRecipeManager();
	protected static final int DEFAULT_ENERGY = 4000;
	protected static float oreMultiplier = 2;
	protected static boolean defaultOreRecipes = true;

	public static SmelterRecipeManager instance() {

		return INSTANCE;
	}

	private SmelterRecipeManager() {

		super(DEFAULT_ENERGY, 4, 0);

		defaultValidator.addPrefix(PREFIX_ORE);
		defaultValidator.addPrefix(PREFIX_INGOT);
		defaultValidator.addPrefix(PREFIX_NUGGET);
		defaultValidator.addPrefix(PREFIX_DUST);
		defaultValidator.addPrefix(PREFIX_GEAR);
		defaultValidator.addPrefix(PREFIX_PLATE);
	}

	// region HELPERS
	private void addDefaultRecipes(String oreType) {

		// TODO: Fix
		//		if (oreType == null || oreType.isEmpty()) {
		//			return;
		//		}
		//		String suffix = titleCase(oreType);
		//
		//		String oreName = PREFIX_ORE + suffix;
		//		String gemName = PREFIX_GEM + suffix;
		//		String dustName = PREFIX_DUST + suffix;
		//		String ingotName = PREFIX_INGOT + suffix;
		//
		//		String oreNetherName = "oreNether" + suffix;
		//		String oreEndName = "oreEnd" + suffix;
		//
		//		ItemStack ore = getPreferredOre(oreName);
		//		ItemStack gem = getPreferredOre(gemName);
		//		ItemStack dust = getPreferredOre(dustName);
		//		ItemStack ingot = getPreferredOre(ingotName);
		//
		//		ItemStack oreNether = getPreferredOre(oreNetherName);
		//		ItemStack oreEnd = getPreferredOre(oreEndName);
		//
		//		if (!ingot.isEmpty() && !gem.isEmpty()) {
		//			return; // This is a confusing Ore and makes no sense. Don't touch.
		//		}
		//		int chance1 = (int) (BASE_CHANCE * oreMultiplier);
		//		int chance2 = chance1 * 2;
		//
		//		if (!gem.isEmpty()) {
		//			addRecipe(ore, cloneStack(gem, 1), chance1);
		//			addRecipe(oreNether, cloneStack(gem, 1), chance2);
		//			addRecipe(oreEnd, cloneStack(gem, 1), chance2);
		//			addRecipe(defaultEnergy / 2, gem, cloneStack(dust, 1), -BASE_CHANCE);
		//		} else {
		//			addRecipe(ore, cloneStack(dust, 1), chance1);
		//			addRecipe(oreNether, cloneStack(dust, 1), chance2);
		//			addRecipe(oreEnd, cloneStack(dust, 1), chance2);
		//		}
	}
	// endregion

	// region IRecipeManager
	@Override
	public void config() {

		String category = "Machines.Smelter";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		comment = "Adjust this value to change the default Ore -> Ingot Multiplier for this machine.";
		oreMultiplier = config.getFloat("Ore -> Ingot Multiplier", category, oreMultiplier, 1.0F, 8.0F, comment);

		comment = "If TRUE, default Ore processing recipes will be created based on registered items.";
		defaultOreRecipes = config.getBoolean("Default Ore Recipes", category, defaultOreRecipes, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}

	@Override
	public void initialize() {

		if (defaultOreRecipes) {
			for (String oreName : OreDictionary.getOreNames()) {
				if (oreName.startsWith(PREFIX_ORE)) {
					addDefaultRecipes(oreName.substring(3));
				} else if (oreName.startsWith(PREFIX_DUST)) {
					addDefaultRecipes(oreName.substring(4));
				}
			}
		}
	}
	// endregion
}
