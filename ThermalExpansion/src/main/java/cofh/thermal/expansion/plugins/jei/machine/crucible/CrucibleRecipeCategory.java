package cofh.thermal.expansion.plugins.jei.machine.crucible;

import cofh.lib.util.helpers.FluidHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.gui.client.machine.process.GuiMachineCrucible;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.machine.CrucibleRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.TANK_MEDIUM;

public class CrucibleRecipeCategory extends BaseRecipeCategory<CrucibleRecipeWrapper> {

	protected IDrawableStatic progress;
	protected IDrawableStatic speed;
	protected IDrawableStatic tank;
	protected IDrawableStatic tankOverlay;

	public CrucibleRecipeCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Machines.Crucible", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiMachineCrucible.TEXTURE, 26, 11, 124, 62, 0, 0, 16, 24);
		energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.machine_crucible.name");

		progress = Drawables.getDrawables(guiHelper).getProgress(Drawables.PROGRESS_DROP);
		speed = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_FLAME);
		tank = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_LARGE);
		tankOverlay = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_LARGE);

		registry.addRecipeCategories(this);
	}

	@Override
	public void registerRecipes(IModRegistry registry) {

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipes(getRecipes(guiHelper), uId);
		registry.addRecipeClickArea(GuiMachineCrucible.class, 103, 34, 24, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.machineCrucible, uId);
	}

	private static List<CrucibleRecipeWrapper> getRecipes(IGuiHelper guiHelper) {

		List<CrucibleRecipeWrapper> recipes = new ArrayList<>();

		for (IMachineRecipe recipe : CrucibleRecipeManager.instance().getRecipeList()) {
			recipes.add(new CrucibleRecipeWrapper(guiHelper, recipe));
		}
		return recipes;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		progress.draw(minecraft, 69, 23);
		speed.draw(minecraft, 43, 33);
		tank.draw(minecraft, 105, 0);
		energyMeter.draw(minecraft, 2, 8);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CrucibleRecipeWrapper recipeWrapper, IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<FluidStack>> outputs = ingredients.getOutputs(VanillaTypes.FLUID);

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiItemStacks.init(0, true, 42, 14);
		guiFluidStacks.init(0, false, 106, 1, 16, 60, TANK_MEDIUM, false, tankOverlay);

		guiItemStacks.set(0, inputs.get(0));
		guiFluidStacks.set(0, outputs.get(0));

		guiFluidStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {

			if (FluidHelper.hasPotionTag(ingredient)) {
				FluidHelper.addPotionTooltip(ingredient, tooltip);
			}
		});
	}
	// endregion
}
