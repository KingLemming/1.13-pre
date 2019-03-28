package cofh.thermal.expansion.plugins.jei.machine.pulverizer;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.gui.client.machine.process.GuiMachinePulverizer;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.machine.PulverizerRecipeManager;
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

import static cofh.lib.util.Constants.BASE_CHANCE;

public class PulverizerRecipeCategory extends BaseRecipeCategory<PulverizerRecipeWrapper> {

	protected IDrawableStatic progress;
	protected IDrawableStatic speed;

	public PulverizerRecipeCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Machines.Pulverizer", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiMachinePulverizer.TEXTURE, 26, 11, 124, 62, 0, 0, 16, 24);
		energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.machine_pulverizer.name");

		progress = Drawables.getDrawables(guiHelper).getProgress(Drawables.PROGRESS_ARROW);
		speed = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_CRUSH);

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
		registry.addRecipeClickArea(GuiMachinePulverizer.class, 72, 25, 24, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.machinePulverizer, uId);
	}

	private static List<PulverizerRecipeWrapper> getRecipes(IGuiHelper guiHelper) {

		List<PulverizerRecipeWrapper> recipes = new ArrayList<>();

		for (IMachineRecipe recipe : PulverizerRecipeManager.instance().getRecipeList()) {
			recipes.add(new PulverizerRecipeWrapper(guiHelper, recipe));
		}
		return recipes;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		progress.draw(minecraft, 62, 14);
		speed.draw(minecraft, 34, 24);
		energyMeter.draw(minecraft, 2, 8);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PulverizerRecipeWrapper recipeWrapper, IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 33, 5);
		guiItemStacks.init(1, false, 96, 5);
		guiItemStacks.init(2, false, 114, 5);
		guiItemStacks.init(3, false, 96, 23);
		guiItemStacks.init(4, false, 114, 23);

		guiItemStacks.set(0, inputs.get(0));
		for (int i = 0; i < outputs.size(); i++) {
			guiItemStacks.set(i + 1, outputs.get(i));
		}
		guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (!recipeWrapper.chances.isEmpty() && slotIndex >= 1 && slotIndex <= 4) {
				float chance = recipeWrapper.chances.get(slotIndex - 1);
				if (chance < BASE_CHANCE) {
					tooltip.add(StringHelper.localize("info.cofh.chance") + ": " + (int) (100 * chance) + "%");
				} else {
					chance -= (int) chance;
					if (chance > 0) {
						tooltip.add(StringHelper.localize("info.cofh.chance_additional") + ": " + (int) (100 * chance) + "%");
					}
				}
			}
		});
	}
	// endregion
}
