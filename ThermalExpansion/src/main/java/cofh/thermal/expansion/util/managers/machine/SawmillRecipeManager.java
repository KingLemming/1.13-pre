package cofh.thermal.expansion.util.managers.machine;

import cofh.lib.inventory.InventoryCraftingFalse;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermal.core.init.ItemsTSeries;
import cofh.thermal.core.util.managers.SimpleItemRecipeManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.getCraftingResult;
import static cofh.thermal.core.ThermalSeries.config;
import static java.util.Arrays.asList;

public class SawmillRecipeManager extends SimpleItemRecipeManager {

	private static final SawmillRecipeManager INSTANCE = new SawmillRecipeManager();
	protected static final int DEFAULT_ENERGY = 2000;
	protected static float logMultiplier = 1.5F;
	protected static boolean defaultLogRecipes = true;

	public static SawmillRecipeManager instance() {

		return INSTANCE;
	}

	private SawmillRecipeManager() {

		super(DEFAULT_ENERGY, 4, 0);

		defaultValidator.addPrefix(PREFIX_ORE);
		defaultValidator.addPrefix(PREFIX_INGOT);
		defaultValidator.addPrefix(PREFIX_NUGGET);
		defaultValidator.addPrefix(PREFIX_CROP);

		defaultValidator.addExact("treeSapling");
		defaultValidator.addExact("treeLeaves");
	}

	// region IManager
	@Override
	public void config() {

		String category = "Machines.Sawmill";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		comment = "Adjust this value to change the default Log -> Plank Multiplier for this machine.";
		logMultiplier = config.getFloat("Log -> Plank Multiplier", category, logMultiplier, 1.0F, 8.0F, comment);

		comment = "If TRUE, default Log processing recipes will be created based on registered Logs and Planks.";
		defaultLogRecipes = config.getBoolean("Default Log Recipes", category, defaultLogRecipes, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}

	@Override
	public void initialize() {

		int energy = (int) (defaultEnergy * 0.40F);

		if (defaultLogRecipes) {
			InventoryCraftingFalse tempCrafting = new InventoryCraftingFalse(2, 2);
			for (ItemStack logWood : OreDictionary.getOres("logWood", false)) {
				Block logBlock = Block.getBlockFromItem(logWood.getItem());
				if (ItemHelper.getItemDamage(logWood) == OreDictionary.WILDCARD_VALUE) {
					NonNullList<ItemStack> logVariants = NonNullList.create();
					logBlock.getSubBlocks(logBlock.getCreativeTabToDisplayOn(), logVariants);
					for (ItemStack log : logVariants) {
						tempCrafting.setInventorySlotContents(0, log);
						ItemStack resultEntry = getCraftingResult(tempCrafting, null);
						if (!resultEntry.isEmpty()) {
							ItemStack result = cloneStack(resultEntry, (int) (resultEntry.getCount() * logMultiplier));
							addRecipe(energy, log, asList(result, ItemsTSeries.dustWood), asList(BASE_CHANCE_LOCKED, BASE_CHANCE));
						}
					}
				} else {
					ItemStack log = cloneStack(logWood, 1);
					tempCrafting.setInventorySlotContents(0, log);
					ItemStack resultEntry = getCraftingResult(tempCrafting, null);
					if (!resultEntry.isEmpty()) {
						ItemStack result = cloneStack(resultEntry, (int) (resultEntry.getCount() * logMultiplier));
						addRecipe(energy, log, asList(result, ItemsTSeries.dustWood), asList(BASE_CHANCE_LOCKED, BASE_CHANCE));
					}
				}
			}
		}
	}
	// endregion
}
