package cofh.thermal.expansion.util.managers;

import cofh.lib.util.comparison.ComparableItemStack;
import cofh.thermal.core.util.managers.IManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;
import static cofh.thermal.core.ThermalSeries.config;
import static cofh.thermal.core.init.ParsersTSeries.getPreferredOre;
import static cofh.thermal.expansion.util.managers.FactorizerManager.FactorizerMode.*;

public class FactorizerManager implements IManager {

	protected Map<ComparableItemStack, FactorizerRecipe> recipeMapSmall = new Object2ObjectOpenHashMap<>();
	protected Map<ComparableItemStack, FactorizerRecipe> recipeMapSmallRev = new Object2ObjectOpenHashMap<>();
	protected Map<ComparableItemStack, FactorizerRecipe> recipeMapLarge = new Object2ObjectOpenHashMap<>();
	protected Map<ComparableItemStack, FactorizerRecipe> recipeMapLargeRev = new Object2ObjectOpenHashMap<>();

	protected static boolean defaultMetalRecipes = true;
	protected static boolean defaultSmallRecipes = true;
	protected static boolean defaultLargeRecipes = true;

	public static final int SMALL_SIZE = 4;
	public static final int LARGE_SIZE = 9;

	private static final FactorizerManager INSTANCE = new FactorizerManager();

	public enum FactorizerMode {
		SMALL, SMALL_REVERSE, LARGE, LARGE_REVERSE
	}

	public static FactorizerManager instance() {

		return INSTANCE;
	}

	private FactorizerManager() {

	}

	public boolean recipeExists(ItemStack input, FactorizerMode mode) {

		return getRecipe(input, mode) != null;
	}

	public FactorizerRecipe getRecipe(ItemStack input, FactorizerMode mode) {

		ComparableItemStack query = new ComparableItemStack(input);

		switch (mode) {
			case SMALL:
				return recipeMapSmall.get(query);
			case SMALL_REVERSE:
				return recipeMapSmallRev.get(query);
			case LARGE:
				return recipeMapLarge.get(query);
			case LARGE_REVERSE:
				return recipeMapLargeRev.get(query);
			default:
				return null;
		}
	}

	// region HELPERS
	public FactorizerRecipe addRecipe(ItemStack input, ItemStack output, FactorizerMode mode) {

		if (input.isEmpty() || output.isEmpty() || recipeExists(input, mode)) {
			return null;
		}
		FactorizerRecipe recipe = new FactorizerRecipe(input, output);

		switch (mode) {
			case SMALL:
				recipeMapSmall.put(new ComparableItemStack(input), recipe);
				break;
			case SMALL_REVERSE:
				recipeMapSmallRev.put(new ComparableItemStack(input), recipe);
				break;
			case LARGE:
				recipeMapLarge.put(new ComparableItemStack(input), recipe);
				break;
			case LARGE_REVERSE:
				recipeMapLargeRev.put(new ComparableItemStack(input), recipe);
				break;
		}
		return recipe;
	}

	private void addDefaultTwoWayRecipe(ItemStack input, ItemStack output, FactorizerMode mode) {

		if (input.isEmpty() || output.isEmpty() || recipeExists(input, mode)) {
			return;
		}
		switch (mode) {
			case SMALL:
				ItemStack inputStack = cloneStack(input, SMALL_SIZE);
				addRecipe(inputStack, output, SMALL);
				addRecipe(output, inputStack, SMALL_REVERSE);
				break;
			case LARGE:
				inputStack = cloneStack(input, LARGE_SIZE);
				addRecipe(inputStack, output, LARGE);
				addRecipe(output, inputStack, LARGE_REVERSE);
				break;
			default:
		}
	}
	// endregion

	// region IManager
	@Override
	public void config() {

		String category = "Machines.Factorizer";
		String comment;

		comment = "If TRUE, default Metal conversion recipes will be created based on registered Ores and Items.";
		defaultMetalRecipes = config.getBoolean("Default Metal Recipes", category, defaultMetalRecipes, comment);

		comment = "If TRUE, default Small (2x2) recipes will be created as part of the default recipe set.";
		defaultSmallRecipes = config.getBoolean("Default Small Recipes", category, defaultSmallRecipes, comment);

		comment = "If TRUE, default Large (3x3) recipes will be created as part of the default recipe set.";
		defaultLargeRecipes = config.getBoolean("Default Large Recipes", category, defaultLargeRecipes, comment);
	}

	@Override
	public void initialize() {

		if (defaultMetalRecipes) {
			String oreSuffix;
			for (String oreName : OreDictionary.getOreNames()) {
				if (oreName.startsWith(PREFIX_INGOT)) {
					oreSuffix = oreName.substring(5);
					addDefaultTwoWayRecipe(getPreferredOre(PREFIX_INGOT + oreSuffix), getPreferredOre(PREFIX_BLOCK + oreSuffix), LARGE);
					addDefaultTwoWayRecipe(getPreferredOre(PREFIX_NUGGET + oreSuffix), getPreferredOre(PREFIX_INGOT + oreSuffix), LARGE);
				}
			}
		}

		if (defaultSmallRecipes) {
			for (IRecipe recipe : CraftingManager.REGISTRY) {
				if (recipe instanceof ShapedRecipes) {
					ShapedRecipes target = (ShapedRecipes) recipe;
					if (target.recipeItems.size() == SMALL_SIZE) {
						if (target.recipeItems.get(0).getMatchingStacks().length > 0) {
							boolean match = true;
							for (int i = 1; i < target.recipeItems.size(); i++) {
								match &= target.recipeItems.get(i).getMatchingStacks().length > 0 && itemsIdentical(target.recipeItems.get(0).getMatchingStacks()[0], target.recipeItems.get(i).getMatchingStacks()[0]);
							}
							if (match) {
								addDefaultTwoWayRecipe(target.recipeItems.get(0).getMatchingStacks()[0], target.getRecipeOutput(), SMALL);
							}
						}
					}
				} else if (recipe instanceof ShapelessRecipes) {
					ShapelessRecipes target = (ShapelessRecipes) recipe;
					if (target.recipeItems.size() == SMALL_SIZE) {
						if (target.recipeItems.get(0).getMatchingStacks().length > 0) {
							boolean match = true;
							for (int i = 1; i < target.recipeItems.size(); i++) {
								match &= target.recipeItems.get(i).getMatchingStacks().length > 0 && itemsIdentical(target.recipeItems.get(0).getMatchingStacks()[0], target.recipeItems.get(i).getMatchingStacks()[0]);
							}
							if (match) {
								addDefaultTwoWayRecipe(target.recipeItems.get(0).getMatchingStacks()[0], target.getRecipeOutput(), SMALL);
							}
						}
					}
				}
			}
		}

		if (defaultLargeRecipes) {
			for (IRecipe recipe : CraftingManager.REGISTRY) {
				if (recipe instanceof ShapedRecipes) {
					ShapedRecipes target = (ShapedRecipes) recipe;
					if (target.recipeItems.size() == LARGE_SIZE) {
						if (target.recipeItems.get(0).getMatchingStacks().length > 0) {
							boolean match = true;
							for (int i = 1; i < target.recipeItems.size(); i++) {
								match &= target.recipeItems.get(i).getMatchingStacks().length > 0 && itemsIdentical(target.recipeItems.get(0).getMatchingStacks()[0], target.recipeItems.get(i).getMatchingStacks()[0]);
							}
							if (match) {
								addDefaultTwoWayRecipe(target.recipeItems.get(0).getMatchingStacks()[0], target.getRecipeOutput(), LARGE);
							}
						}
					}
				} else if (recipe instanceof ShapelessRecipes) {
					ShapelessRecipes target = (ShapelessRecipes) recipe;
					if (target.recipeItems.size() == LARGE_SIZE) {
						if (target.recipeItems.get(0).getMatchingStacks().length > 0) {
							boolean match = true;
							for (int i = 1; i < target.recipeItems.size(); i++) {
								match &= target.recipeItems.get(i).getMatchingStacks().length > 0 && itemsIdentical(target.recipeItems.get(0).getMatchingStacks()[0], target.recipeItems.get(i).getMatchingStacks()[0]);
							}
							if (match) {
								addDefaultTwoWayRecipe(target.recipeItems.get(0).getMatchingStacks()[0], target.getRecipeOutput(), LARGE);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void refresh() {

		Map<ComparableItemStack, FactorizerRecipe> tempMapSmall = new Object2ObjectOpenHashMap<>(recipeMapSmall.size());
		Map<ComparableItemStack, FactorizerRecipe> tempMapSmallRev = new Object2ObjectOpenHashMap<>(recipeMapSmallRev.size());
		Map<ComparableItemStack, FactorizerRecipe> tempMapLarge = new Object2ObjectOpenHashMap<>(recipeMapLarge.size());
		Map<ComparableItemStack, FactorizerRecipe> tempMapLargeRev = new Object2ObjectOpenHashMap<>(recipeMapLargeRev.size());
		FactorizerRecipe tempRecipe;

		for (Map.Entry<ComparableItemStack, FactorizerRecipe> entry : recipeMapSmall.entrySet()) {
			tempRecipe = entry.getValue();
			ComparableItemStack input = new ComparableItemStack(tempRecipe.getInput());
			tempMapSmall.put(input, tempRecipe);
		}
		for (Map.Entry<ComparableItemStack, FactorizerRecipe> entry : recipeMapSmallRev.entrySet()) {
			tempRecipe = entry.getValue();
			ComparableItemStack input = new ComparableItemStack(tempRecipe.getInput());
			tempMapSmallRev.put(input, tempRecipe);
		}
		for (Map.Entry<ComparableItemStack, FactorizerRecipe> entry : recipeMapLarge.entrySet()) {
			tempRecipe = entry.getValue();
			ComparableItemStack input = new ComparableItemStack(tempRecipe.getInput());
			tempMapLarge.put(input, tempRecipe);
		}
		for (Map.Entry<ComparableItemStack, FactorizerRecipe> entry : recipeMapLargeRev.entrySet()) {
			tempRecipe = entry.getValue();
			ComparableItemStack input = new ComparableItemStack(tempRecipe.getInput());
			tempMapLargeRev.put(input, tempRecipe);
		}
		recipeMapSmall.clear();
		recipeMapSmallRev.clear();
		recipeMapLarge.clear();
		recipeMapLargeRev.clear();

		recipeMapSmall = tempMapSmall;
		recipeMapSmallRev = tempMapSmallRev;
		recipeMapLarge = tempMapLarge;
		recipeMapLargeRev = tempMapLargeRev;
	}
	// endregion

	// region RECIPE CLASS
	static class FactorizerRecipe {

		private final ItemStack input;
		private final ItemStack output;

		public FactorizerRecipe(ItemStack input, ItemStack output) {

			this.input = input;
			this.output = output;
		}

		public ItemStack getInput() {

			return input;
		}

		public ItemStack getOutput() {

			return output;
		}

	}
	// endregion
}
