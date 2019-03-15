package cofh.thermal.expansion.plugins.jei.machine.brewer;

import cofh.lib.util.helpers.FluidHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.gui.client.machine.GuiMachineBrewer;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.machine.BrewerRecipeManager;
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
import static cofh.lib.util.Constants.TANK_SMALL;

public class BrewerRecipeCategory extends BaseRecipeCategory<BrewerRecipeWrapper> {

	protected IDrawableStatic progress;
	protected IDrawableStatic speed;

	protected IDrawableStatic tankInput;
	protected IDrawableStatic tankOutput;

	protected IDrawableStatic inputOverlay;
	protected IDrawableStatic outputOverlay;

	public BrewerRecipeCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Machines.Brewer", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiMachineBrewer.TEXTURE, 26, 11, 72, 62, 0, 0, 16, 76);
		energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.machine_brewer.name");

		progress = Drawables.getDrawables(guiHelper).getProgress(Drawables.PROGRESS_DROP);
		speed = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_ALCHEMY);
		tankInput = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_SMALL);
		tankOutput = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_LARGE);

		inputOverlay = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_SMALL);
		outputOverlay = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_LARGE);

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
		registry.addRecipeClickArea(GuiMachineBrewer.class, 112, 34, 24, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.machineBrewer, uId);
	}

	private static List<BrewerRecipeWrapper> getRecipes(IGuiHelper guiHelper) {

		List<BrewerRecipeWrapper> recipes = new ArrayList<>();

		for (IMachineRecipe recipe : BrewerRecipeManager.instance().getRecipeList()) {
			recipes.add(new BrewerRecipeWrapper(guiHelper, recipe));
		}
		return recipes;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		progress.draw(minecraft, 98, 23);
		speed.draw(minecraft, 46, 23);
		tankInput.draw(minecraft, 21, 14);
		tankOutput.draw(minecraft, 133, 0);
		energyMeter.draw(minecraft, 2, 8);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, BrewerRecipeWrapper recipeWrapper, IIngredients ingredients) {

		List<List<ItemStack>> inputItems = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<FluidStack>> inputFluids = ingredients.getInputs(VanillaTypes.FLUID);
		List<List<FluidStack>> outputs = ingredients.getOutputs(VanillaTypes.FLUID);

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiItemStacks.init(0, true, 69, 22);
		guiFluidStacks.init(0, true, 22, 15, 16, 32, TANK_SMALL, false, inputOverlay);
		guiFluidStacks.init(1, false, 134, 1, 16, 60, TANK_MEDIUM, false, outputOverlay);

		guiItemStacks.set(0, inputItems.get(0));
		guiFluidStacks.set(0, inputFluids.get(0));
		guiFluidStacks.set(1, outputs.get(0));

		guiFluidStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {

			if (FluidHelper.hasPotionTag(ingredient)) {
				FluidHelper.addPotionTooltip(ingredient, tooltip);
			}
		});
	}
	// endregion
}
