package cofh.lib.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;

public class ShapelessFluidRecipeFactory implements IRecipeFactory {

	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {

		ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);

		return new ShapelessFluidRecipe(new ResourceLocation("cofh", "fluid_shapeless"), recipe.getRecipeOutput(), recipe.getIngredients().toArray());
	}

	// region RECIPE CLASS
	public static class ShapelessFluidRecipe extends ShapelessOreRecipe {

		public ShapelessFluidRecipe(ResourceLocation group, ItemStack result, Object... recipe) {

			super(group, result, recipe);
		}

		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {

			NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

			for (int i = 0; i < ret.size(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				IFluidHandlerItem handler = stack.getCount() > 1 ? FluidUtil.getFluidHandler(cloneStack(stack, 1)) : FluidUtil.getFluidHandler(stack);

				if (handler == null) {
					ret.set(i, ForgeHooks.getContainerItem(stack));
				} else {
					handler.drain(Fluid.BUCKET_VOLUME, true);
					ret.set(i, handler.getContainer().copy());
				}
			}
			return ret;
		}

		@Override
		public boolean isDynamic() {

			return true;
		}
	}
	// endregion
}
