package cofh.thermal.expansion.util.managers.machine;

import cofh.thermal.core.util.managers.SimpleCatalystRecipeManager;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.util.recipes.InsolatorPlantRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import static cofh.lib.util.Constants.PREFIX_CROP;
import static cofh.lib.util.Constants.PREFIX_SEED;
import static cofh.lib.util.helpers.ItemHelper.itemsEqualWithMetadata;
import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.thermal.core.ThermalSeries.config;
import static cofh.thermal.core.init.ParsersTSeries.getPreferredOre;
import static java.util.Arrays.asList;

public class InsolatorRecipeManager extends SimpleCatalystRecipeManager {

	private static final InsolatorRecipeManager INSTANCE = new InsolatorRecipeManager();
	protected static final int DEFAULT_ENERGY = 20000;
	protected static float plantMultiplier = 2.0F;
	protected static float tuberMultiplier = 2.5F;
	protected static boolean defaultPlantRecipes = true;
	public static FluidStack defaultPlantFluid = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME / 2);

	public static InsolatorRecipeManager instance() {

		return INSTANCE;
	}

	private InsolatorRecipeManager() {

		super(DEFAULT_ENERGY, 4, 0);

		defaultValidator.addPrefix(PREFIX_SEED);
	}

	public float getPlantMultiplier() {

		return plantMultiplier;
	}

	public float getTuberMultiplier() {

		return tuberMultiplier;
	}

	// region PLANT RECIPES
	public IMachineRecipe addPlantRecipe(int energy, ItemStack input, ItemStack output, float chance) {

		if (maxOutputItems <= 0 || input.isEmpty() || output.isEmpty() || energy <= 0 || validRecipe(input)) {
			return null;
		}
		energy = (energy * scaleFactor) / 100;

		InsolatorPlantRecipe recipe = new InsolatorPlantRecipe(energy, input, output, chance);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}

	public IMachineRecipe addPlantRecipe(int energy, ItemStack input, List<ItemStack> output, List<Float> chance) {

		if (input.isEmpty() || output.isEmpty() || output.size() > maxOutputItems || energy <= 0 || validRecipe(input)) {
			return null;
		}
		for (ItemStack stack : output) {
			if (stack.isEmpty()) {
				return null;
			}
		}
		energy = (energy * scaleFactor) / 100;

		InsolatorPlantRecipe recipe = new InsolatorPlantRecipe(energy, input, output, chance);
		if (hasCustomOreID(input)) {
			customMap.put(customInput(input), recipe);
		} else {
			defaultMap.put(defaultInput(input), recipe);
		}
		return recipe;
	}
	// endregion

	// region HELPERS
	private void addDefaultPlantRecipes(String oreType) {

		if (oreType == null || oreType.isEmpty()) {
			return;
		}
		String suffix = titleCase(oreType);

		String seedName = PREFIX_SEED + suffix;
		String cropName = PREFIX_CROP + suffix;

		ItemStack seed = getPreferredOre(seedName);
		ItemStack crop = getPreferredOre(cropName);

		int energy = defaultEnergy;
		boolean isTuber = itemsEqualWithMetadata(seed, crop);

		if (isTuber) {
			addPlantRecipe(energy, seed, crop, tuberMultiplier);
		} else {
			addPlantRecipe(energy, seed, asList(crop, seed), asList(plantMultiplier, 1.1F));
		}
	}
	// endregion

	// region IManager
	@Override
	public void config() {

		String category = "Machines.Insolator";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		comment = "If TRUE, default Plant processing recipes will be created based on registered Crops and Seeds.";
		defaultPlantRecipes = config.getBoolean("Default Seed Recipes", category, defaultPlantRecipes, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}

	@Override
	public void initialize() {

		if (defaultPlantRecipes) {
			for (String oreName : OreDictionary.getOreNames()) {
				if (oreName.startsWith(PREFIX_SEED) || oreName.startsWith(PREFIX_CROP)) {
					addDefaultPlantRecipes(oreName.substring(4));
				}
			}
		}
	}
	// endregion
}
