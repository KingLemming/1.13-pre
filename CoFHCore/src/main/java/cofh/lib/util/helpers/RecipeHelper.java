package cofh.lib.util.helpers;

import cofh.lib.crafting.ShapedFluidRecipeFactory.ShapedFluidRecipe;
import cofh.lib.crafting.ShapelessFluidRecipeFactory.ShapelessFluidRecipe;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;

public class RecipeHelper {

	private RecipeHelper() {

	}

	public static ItemStack getCraftingResult(InventoryCrafting inv, World world) {

		return CraftingManager.findMatchingResult(inv, world);
	}

	// region CRAFTING
	public static void addShapedRecipe(ItemStack output, Object... input) {

		ResourceLocation location = getNameForRecipe(output);
		CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(input);
		ShapedRecipes recipe = new ShapedRecipes(output.getItem().getRegistryName().toString(), primer.width, primer.height, primer.input, output);
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}

	public static void addShapedFluidRecipe(ItemStack output, Object... input) {

		ResourceLocation location = getNameForRecipe(output);
		CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(input);
		ShapedFluidRecipe recipe = new ShapedFluidRecipe(location, output, primer);
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}

	public static void addShapelessRecipe(ItemStack output, Object... input) {

		ResourceLocation location = getNameForRecipe(output);
		ShapelessRecipes recipe = new ShapelessRecipes(location.getResourceDomain(), output, buildInput(input));
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}

	public static void addShapelessFluidRecipe(ItemStack output, Object... input) {

		ResourceLocation location = getNameForRecipe(output);
		ShapelessFluidRecipe recipe = new ShapelessFluidRecipe(location, output, input);
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}
	// endregion

	// region SMELTING
	public static void addSmelting(Block input, ItemStack output, float xp) {

		GameRegistry.addSmelting(input, output, xp);
	}

	public static void addSmelting(Item input, ItemStack output, float xp) {

		GameRegistry.addSmelting(input, output, xp);
	}

	public static void addSmelting(ItemStack input, ItemStack output, float xp) {

		GameRegistry.addSmelting(input, output, xp);
	}

	public static void addSmelting(ItemStack input, ItemStack output) {

		addSmelting(input, output, 0F);
	}

	public static void addSmelting(Item input, ItemStack output) {

		addSmelting(input, output, 0F);
	}

	public static void addSmelting(Block input, ItemStack output) {

		addSmelting(input, output, 0F);
	}
	// endregion

	// region GEARS
	public static void addGearRecipe(ItemStack gear, String ingot) {

		addShapedRecipe(gear, " X ", "X X", " X ", 'X', ingot);
	}

	public static void addGearRecipe(ItemStack gear, String ingot, String center) {

		addShapedRecipe(gear, " X ", "XIX", " X ", 'X', ingot, 'I', center);
	}
	// endregion

	// region STORAGE
	public static void addStorageRecipe(ItemStack one, String nine) {

		addShapedRecipe(one, "XXX", "XXX", "XXX", 'X', nine);
	}

	public static void addStorageRecipe(ItemStack one, ItemStack nine) {

		addShapedRecipe(one, "XXX", "XXX", "XXX", 'X', cloneStack(nine, 1));
	}

	public static void addReverseStorageRecipe(ItemStack nine, String one) {

		addShapelessRecipe(cloneStack(nine, 9), one);
	}

	public static void addReverseStorageRecipe(ItemStack nine, ItemStack one) {

		addShapelessRecipe(cloneStack(nine, 9), cloneStack(one, 1));
	}

	public static void addTwoWayStorageRecipe(ItemStack one, String one_ore, ItemStack nine, String nine_ore) {

		addStorageRecipe(one, nine_ore);
		addReverseStorageRecipe(nine, one_ore);
	}
	// endregion

	// region HELPERS
	public static ResourceLocation getNameForRecipe(ItemStack output) {

		ModContainer activeContainer = Loader.instance().activeModContainer();
		ResourceLocation baseLoc = new ResourceLocation(activeContainer.getModId(), output.getItem().getRegistryName().getResourcePath());
		ResourceLocation recipeLoc = baseLoc;
		int index = 0;
		while (CraftingManager.REGISTRY.containsKey(recipeLoc)) {
			index++;
			recipeLoc = new ResourceLocation(activeContainer.getModId(), baseLoc.getResourcePath() + "_" + index);
		}
		return recipeLoc;
	}

	public static NonNullList<Ingredient> buildInput(Object[] input) {

		NonNullList<Ingredient> list = NonNullList.create();

		for (Object obj : input) {
			if (obj instanceof Ingredient) {
				list.add((Ingredient) obj);
			} else {
				Ingredient ingredient = CraftingHelper.getIngredient(obj);

				if (ingredient == null) {
					ingredient = Ingredient.EMPTY;
				}
				list.add(ingredient);
			}
		}
		return list;
	}
	// endregion
}
