package cofh.thermal.expansion.plugins.jei.dynamo.numismatic;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoNumismatic;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.dynamo.NumismaticFuelManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class NumismaticFuelCategory extends BaseRecipeCategory<NumismaticFuelWrapper> {

	protected IDrawableStatic duration;

	public NumismaticFuelCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Dynamos.Numismatic", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiDynamoNumismatic.TEXTURE, 26, 11, 70, 62, 0, 0, 16, 78);
		// energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.dynamo_numismatic.name");

		duration = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_FLAME_GREEN);

		registry.addRecipeCategories(this);
	}

	@Override
	public void registerRecipes(IModRegistry registry) {

		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipes(getFuels(guiHelper), uId);
		registry.addRecipeClickArea(GuiDynamoNumismatic.class, 115, 35, 16, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.dynamoNumismatic, uId);
	}

	private static List<NumismaticFuelWrapper> getFuels(IGuiHelper guiHelper) {

		List<NumismaticFuelWrapper> fuels = new ArrayList<>();

		for (IDynamoFuel fuel : NumismaticFuelManager.instance().getFuelList()) {
			fuels.add(new NumismaticFuelWrapper(guiHelper, fuel));
		}
		return fuels;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		// energyMeter.draw(minecraft, 2, 8);
		duration.draw(minecraft, 34, 43);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, NumismaticFuelWrapper fuelWrapper, IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 33, 23);
		guiItemStacks.set(0, inputs.get(0));
	}
	// endregion
}
