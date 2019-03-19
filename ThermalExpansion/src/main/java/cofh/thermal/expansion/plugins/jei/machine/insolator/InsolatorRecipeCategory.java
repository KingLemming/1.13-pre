package cofh.thermal.expansion.plugins.jei.machine.insolator;

import cofh.lib.util.helpers.FluidHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.gui.client.machine.process.GuiMachineInsolator;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.machine.InsolatorRecipeManager;
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

import static cofh.lib.util.Constants.BASE_CHANCE;
import static cofh.lib.util.Constants.TANK_SMALL;

public class InsolatorRecipeCategory extends BaseRecipeCategory<InsolatorRecipeWrapper> {

	protected IDrawableStatic progress;
	protected IDrawableStatic speed;
	protected IDrawableStatic tank;
	protected IDrawableStatic tankOverlay;

	public InsolatorRecipeCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Machines.Insolator", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiMachineInsolator.TEXTURE, 26, 11, 124, 62, 0, 0, 16, 24);
		energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.machine_insolator.name");

		progress = Drawables.getDrawables(guiHelper).getProgress(Drawables.PROGRESS_ARROW);
		speed = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_SUN);
		tank = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_MEDIUM);
		tankOverlay = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_MEDIUM);

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
		registry.addRecipeClickArea(GuiMachineInsolator.class, 85, 25, 24, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.machineInsolator, uId);
	}

	private static List<InsolatorRecipeWrapper> getRecipes(IGuiHelper guiHelper) {

		List<InsolatorRecipeWrapper> recipes = new ArrayList<>();

		for (IMachineRecipe recipe : InsolatorRecipeManager.instance().getRecipeList()) {
			recipes.add(new InsolatorRecipeWrapper(guiHelper, recipe));
		}
		return recipes;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		progress.draw(minecraft, 74, 14);
		speed.draw(minecraft, 52, 24);
		tank.draw(minecraft, 27, 11);
		energyMeter.draw(minecraft, 2, 8);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, InsolatorRecipeWrapper recipeWrapper, IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<FluidStack>> inputFluids = ingredients.getInputs(VanillaTypes.FLUID);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiItemStacks.init(0, true, 51, 5);
		guiItemStacks.init(1, false, 105, 5);
		guiItemStacks.init(2, false, 123, 5);
		guiItemStacks.init(3, false, 105, 23);
		guiItemStacks.init(4, false, 123, 23);

		guiFluidStacks.init(0, false, 28, 12, 16, 40, TANK_SMALL, false, tankOverlay);

		guiItemStacks.set(0, inputs.get(0));
		guiFluidStacks.set(0, inputFluids.get(0));

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
		guiFluidStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {

			if (FluidHelper.hasPotionTag(ingredient)) {
				FluidHelper.addPotionTooltip(ingredient, tooltip);
			}
		});
	}
	// endregion
}
