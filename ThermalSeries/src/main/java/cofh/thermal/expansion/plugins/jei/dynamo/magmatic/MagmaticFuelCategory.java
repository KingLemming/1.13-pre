package cofh.thermal.expansion.plugins.jei.dynamo.magmatic;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoMagmatic;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.dynamo.MagmaticFuelManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.TANK_SMALL;

public class MagmaticFuelCategory extends BaseRecipeCategory<MagmaticFuelWrapper> {

	protected IDrawableStatic duration;
	protected IDrawableStatic tank;
	protected IDrawableStatic tankOverlay;

	public MagmaticFuelCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Dynamos.Magmatic", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiDynamoMagmatic.TEXTURE, 26, 11, 70, 62, 0, 0, 16, 78);
		// energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.dynamo_magmatic.name");

		duration = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_FLAME);
		tank = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_SMALL);
		tankOverlay = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_SMALL);

		registry.addRecipeCategories(this);
	}

	@Override
	public void registerRecipes(IModRegistry registry) {

		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipes(getFuels(guiHelper), uId);
		registry.addRecipeClickArea(GuiDynamoMagmatic.class, 115, 35, 16, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.dynamoMagmatic, uId);
	}

	private static List<MagmaticFuelWrapper> getFuels(IGuiHelper guiHelper) {

		List<MagmaticFuelWrapper> fuels = new ArrayList<>();

		for (IDynamoFuel fuel : MagmaticFuelManager.instance().getFuelList()) {
			fuels.add(new MagmaticFuelWrapper(guiHelper, fuel));
		}
		return fuels;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		// energyMeter.draw(minecraft, 2, 8);
		duration.draw(minecraft, 34, 43);
		tank.draw(minecraft, 33, 7);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MagmaticFuelWrapper fuelWrapper, IIngredients ingredients) {

		List<List<FluidStack>> inputs = ingredients.getInputs(VanillaTypes.FLUID);
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiFluidStacks.init(0, true, 34, 8, 16, 32, TANK_SMALL, false, tankOverlay);
		guiFluidStacks.set(0, inputs.get(0));
	}
	// endregion
}
