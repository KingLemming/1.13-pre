package cofh.thermal.expansion.util.managers.machine;

import cofh.thermal.core.util.managers.SimpleCatalystRecipeManager;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.util.recipes.PulverizerOreRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.thermal.core.ThermalSeries.config;
import static cofh.thermal.core.init.ParsersTSeries.getPreferredOre;

public class PulverizerRecipeManager extends SimpleCatalystRecipeManager {

	private static final PulverizerRecipeManager INSTANCE = new PulverizerRecipeManager();
	protected static final int DEFAULT_ENERGY = 4000;
	protected static float oreMultiplier = 2.0F;
	protected static boolean defaultOreRecipes = true;
	protected static boolean defaultIngotRecipes = true;

	public static PulverizerRecipeManager instance() {

		return INSTANCE;
	}

	private PulverizerRecipeManager() {

		super(DEFAULT_ENERGY, 4, 0);

		defaultValidator.addPrefix(PREFIX_ORE);
		defaultValidator.addPrefix(PREFIX_INGOT);
		defaultValidator.addPrefix(PREFIX_NUGGET);
		defaultValidator.addPrefix(PREFIX_LOG);
		defaultValidator.addPrefix(PREFIX_PLANK);

		defaultValidator.addExact("sand");
		defaultValidator.addExact("treeSapling");
		defaultValidator.addExact("treeLeaves");
	}

	public float getOreMultiplier() {

		return oreMultiplier;
	}

	// region ORE RECIPES
	public IMachineRecipe addOreRecipe(int energy, ItemStack input, ItemStack output, float chance) {

		if (maxOutputItems <= 0 || input.isEmpty() || output.isEmpty() || energy <= 0 || validRecipe(input)) {
			return null;
		}
		energy = (energy * getDefaultScale()) / 100;

		PulverizerOreRecipe recipe = new PulverizerOreRecipe(energy, input, output, chance);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}

	public IMachineRecipe addOreRecipe(int energy, ItemStack input, List<ItemStack> output, List<Float> chance) {

		if (input.isEmpty() || output.isEmpty() || output.size() > maxOutputItems || energy <= 0 || validRecipe(input)) {
			return null;
		}
		for (ItemStack stack : output) {
			if (stack.isEmpty()) {
				return null;
			}
		}
		energy = (energy * getDefaultScale()) / 100;

		PulverizerOreRecipe recipe = new PulverizerOreRecipe(energy, input, output, chance);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}
	// endregion

	// region HELPERS
	private void addDefaultOreRecipes(String oreSuffix) {

		if (oreSuffix == null || oreSuffix.isEmpty()) {
			return;
		}
		oreSuffix = titleCase(oreSuffix);

		String oreName = PREFIX_ORE + oreSuffix;
		String gemName = PREFIX_GEM + oreSuffix;
		String dustName = PREFIX_DUST + oreSuffix;
		String ingotName = PREFIX_INGOT + oreSuffix;

		String oreNetherName = "oreNether" + oreSuffix;
		String oreEndName = "oreEnd" + oreSuffix;

		ItemStack ore = getPreferredOre(oreName);
		ItemStack gem = getPreferredOre(gemName);
		ItemStack dust = getPreferredOre(dustName);
		ItemStack ingot = getPreferredOre(ingotName);

		ItemStack oreNether = getPreferredOre(oreNetherName);
		ItemStack oreEnd = getPreferredOre(oreEndName);

		if (!ingot.isEmpty() && !gem.isEmpty()) {
			return; // This is a confusing Ore and makes no sense. Don't touch.
		}
		int energy = defaultEnergy;
		float chance1 = oreMultiplier;
		float chance2 = chance1 * 2;

		if (!gem.isEmpty()) {
			addOreRecipe(energy, ore, cloneStack(gem, 1), chance1);
			addOreRecipe(energy * 2, oreNether, cloneStack(gem, 1), chance2);
			addOreRecipe(energy * 2, oreEnd, cloneStack(gem, 1), chance2);
			addRecipe(energy / 2, gem, cloneStack(dust, 1));
		} else {
			addOreRecipe(energy, ore, cloneStack(dust, 1), chance1);
			addOreRecipe(energy * 2, oreNether, cloneStack(dust, 1), chance2);
			addOreRecipe(energy * 2, oreEnd, cloneStack(dust, 1), chance2);
			if (defaultIngotRecipes) {
				addRecipe(energy / 2, ingot, cloneStack(dust, 1));
			}
		}
	}
	// endregion

	// region IManager
	@Override
	public void config() {

		String category = "Machines.Pulverizer";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		comment = "Adjust this value to change the default Ore -> Dust Multiplier for this machine.";
		oreMultiplier = config.getFloat("Ore -> Dust Multiplier", category, oreMultiplier, 1.0F, 8.0F, comment);

		comment = "If TRUE, default Ore processing recipes will be created based on registered Ores and Items.";
		defaultOreRecipes = config.getBoolean("Default Ore Recipes", category, defaultOreRecipes, comment);

		comment = "If TRUE, default Ingot -> Dust recipes will be created as part of the default recipe set.";
		defaultIngotRecipes = config.getBoolean("Default Ingot Recipes", category, defaultIngotRecipes, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}

	@Override
	public void initialize() {

		if (defaultOreRecipes) {
			for (String oreName : OreDictionary.getOreNames()) {
				if (oreName.startsWith(PREFIX_ORE) || oreName.startsWith(PREFIX_GEM)) {
					addDefaultOreRecipes(oreName.substring(3));
				} else if (oreName.startsWith(PREFIX_DUST)) {
					addDefaultOreRecipes(oreName.substring(4));
				}
			}
		}
	}
	// endregion
}
