package cofh.thermal.expansion.plugins.jei.dynamo.stirling;

import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class StirlingFuelCategory extends BaseRecipeCategory<StirlingFuelWrapper> {

	public StirlingFuelCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Dynamos.Stirling", category, enable, comment);
	}

	@Override
	public void registerRecipes(IModRegistry registry) {

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, StirlingFuelWrapper fuelWrapper, IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.set(0, inputs.get(0));

	}

}
