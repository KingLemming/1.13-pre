package cofh.thermal.expansion.plugins.jei.machine.refinery;

import cofh.lib.util.helpers.FluidHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.gui.client.machine.GuiMachineRefinery;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.machine.RefineryRecipeManager;
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
import static net.minecraftforge.fluids.Fluid.BUCKET_VOLUME;

public class RefineryRecipeCategory extends BaseRecipeCategory<RefineryRecipeWrapper> {

	protected IDrawableStatic progress;
	protected IDrawableStatic speed;

	protected IDrawableStatic tankInput;
	protected IDrawableStatic tankOutputA;
	protected IDrawableStatic tankOutputB;

	protected IDrawableStatic inputOverlay;
	protected IDrawableStatic outputOverlayA;
	protected IDrawableStatic outputOverlayB;

	public RefineryRecipeCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Machines.Refinery", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiMachineRefinery.TEXTURE, 26, 11, 124, 62, 0, 0, 16, 24);
		energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.machine_refinery.name");

		progress = Drawables.getDrawables(guiHelper).getProgress(Drawables.PROGRESS_DROP);
		speed = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_FLAME);
		tankInput = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_SMALL);
		tankOutputA = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_MEDIUM);
		tankOutputB = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK_MEDIUM);

		inputOverlay = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_SMALL);
		outputOverlayA = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_MEDIUM);
		outputOverlayB = Drawables.getDrawables(guiHelper).getTankOverlay(Drawables.TANK_MEDIUM);

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
		registry.addRecipeClickArea(GuiMachineRefinery.class, 65, 35, 24, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.machineRefinery, uId);
	}

	private static List<RefineryRecipeWrapper> getRecipes(IGuiHelper guiHelper) {

		List<RefineryRecipeWrapper> recipes = new ArrayList<>();

		for (IMachineRecipe recipe : RefineryRecipeManager.instance().getRecipeList()) {
			recipes.add(new RefineryRecipeWrapper(guiHelper, recipe));
		}
		return recipes;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		progress.draw(minecraft, 57, 23);
		speed.draw(minecraft, 29, 40);
		tankInput.draw(minecraft, 28, 5);
		tankOutputA.draw(minecraft, 125, 11);
		tankOutputB.draw(minecraft, 143, 11);
		energyMeter.draw(minecraft, 2, 8);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RefineryRecipeWrapper recipeWrapper, IIngredients ingredients) {

		List<List<FluidStack>> inputFluids = ingredients.getInputs(VanillaTypes.FLUID);
		List<List<FluidStack>> outputFluids = ingredients.getOutputs(VanillaTypes.FLUID);
		List<List<ItemStack>> outputItems = ingredients.getOutputs(VanillaTypes.ITEM);

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiItemStacks.init(0, true, 96, 23);
		guiFluidStacks.init(0, true, 29, 6, 16, 32, BUCKET_VOLUME, false, inputOverlay);
		guiFluidStacks.init(1, false, 126, 12, 16, 40, BUCKET_VOLUME, false, outputOverlayA);
		guiFluidStacks.init(2, false, 144, 12, 16, 40, BUCKET_VOLUME, false, outputOverlayB);

		if (!outputItems.isEmpty()) {
			guiItemStacks.set(0, outputItems.get(0));
		}
		guiFluidStacks.set(0, inputFluids.get(0));

		for (int i = 0; i < outputFluids.size(); i++) {
			guiFluidStacks.set(i + 1, outputFluids.get(i));
		}
		guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (!recipeWrapper.chances.isEmpty() && slotIndex == 0) {
				float chance = Math.abs(recipeWrapper.chances.get(slotIndex));
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
