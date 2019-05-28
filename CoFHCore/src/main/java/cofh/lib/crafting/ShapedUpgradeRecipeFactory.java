package cofh.lib.crafting;

import cofh.lib.item.INBTCopyIngredient;
import cofh.lib.util.helpers.ItemHelper;
import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

import static cofh.lib.util.Constants.ID_COFH;

public class ShapedUpgradeRecipeFactory implements IRecipeFactory {

	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {

		ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

		ShapedPrimer primer = new ShapedPrimer();
		primer.width = recipe.getRecipeWidth();
		primer.height = recipe.getRecipeHeight();
		primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
		primer.input = recipe.getIngredients();

		return new ShapedUpgradeRecipe(new ResourceLocation(ID_COFH, "upgrade_shaped"), recipe.getRecipeOutput(), primer);
	}

	// region RECIPE CLASS
	public static class ShapedUpgradeRecipe extends ShapedOreRecipe {

		public ShapedUpgradeRecipe(ResourceLocation group, ItemStack result, ShapedPrimer primer) {

			super(group, result, primer);
		}

		@Override
		@Nonnull
		public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {

			ItemStack inputStack = ItemStack.EMPTY;
			ItemStack outputStack = output.copy();

			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack stack = inv.getStackInSlot(i);

				if (!stack.isEmpty()) {
					if (stack.getItem() instanceof INBTCopyIngredient) {
						inputStack = stack;
					}
				}
			}
			if (inputStack.isEmpty()) {
				return ItemStack.EMPTY;
			}
			return ItemHelper.copyTag(outputStack, inputStack);
		}

		@Override
		public boolean isDynamic() {

			return true;
		}
	}
	// endregion
}
