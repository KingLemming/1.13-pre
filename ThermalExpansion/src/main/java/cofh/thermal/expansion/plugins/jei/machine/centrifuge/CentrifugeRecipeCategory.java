package cofh.thermal.expansion.plugins.jei.machine.centrifuge;

import cofh.lib.util.helpers.FluidHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.plugins.jei.BaseRecipeCategory;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.gui.client.machine.GuiMachineCentrifuge;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.util.managers.machine.CentrifugeRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE;
import static cofh.lib.util.Constants.TANK_SMALL;

public class CentrifugeRecipeCategory extends BaseRecipeCategory<CentrifugeRecipeWrapper> {

	protected IDrawableStatic progress;
	protected IDrawableStatic speed;
	protected IDrawableStatic tank;
	protected IDrawableStatic tankOverlay;

	public CentrifugeRecipeCategory(String uId) {

		super(uId);
	}

	@Override
	public void registerCategory(IRecipeCategoryRegistration registry) {

		String category = "Plugins.JEI";
		String comment = "If TRUE, this JEI Category is visible.";
		enable = ThermalSeries.configClient.getBoolean("Machines.Centrifuge", category, enable, comment);

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		background = guiHelper.createDrawable(GuiMachineCentrifuge.TEXTURE, 26, 11, 124, 62, 0, 0, 16, 24);
		energyMeter = Drawables.getDrawables(guiHelper).getEnergyEmpty();
		localizedName = StringHelper.localize("tile.thermal.machine_centrifuge.name");

		progress = Drawables.getDrawables(guiHelper).getProgress(Drawables.PROGRESS_ARROW);
		speed = Drawables.getDrawables(guiHelper).getScale(Drawables.SCALE_SPIN);
		tank = Drawables.getDrawables(guiHelper).getTank(Drawables.TANK);
		tankOverlay = Drawables.getDrawables(guiHelper).getTankSmallOverlay(Drawables.TANK);

		registry.addRecipeCategories(this);
	}

	public void registerRecipes(IModRegistry registry) {

		if (!enable) {
			return;
		}
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipes(getRecipes(guiHelper), uId);
		registry.addRecipeClickArea(GuiMachineCentrifuge.class, 72, 34, 24, 16, uId);
		registry.addRecipeCatalyst(BlocksTE.machineCentrifuge, uId);
	}

	public static List<CentrifugeRecipeWrapper> getRecipes(IGuiHelper guiHelper) {

		List<CentrifugeRecipeWrapper> recipes = new ArrayList<>();

		for (IMachineRecipe recipe : CentrifugeRecipeManager.instance().getRecipeList()) {
			recipes.add(new CentrifugeRecipeWrapper(guiHelper, recipe));
		}
		return recipes;
	}

	// region IRecipeCategory
	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

		progress.draw(minecraft, 62, 23);
		speed.draw(minecraft, 34, 33);
		tank.draw(minecraft, 140, 0);
		energyMeter.draw(minecraft, 2, 8);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CentrifugeRecipeWrapper recipeWrapper, IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		List<List<FluidStack>> outputFluids = ingredients.getOutputs(FluidStack.class);

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiItemStacks.init(0, true, 33, 14);
		guiItemStacks.init(1, false, 96, 14);
		guiItemStacks.init(2, false, 114, 14);
		guiItemStacks.init(3, false, 96, 32);
		guiItemStacks.init(4, false, 114, 32);

		guiFluidStacks.init(0, false, 141, 1, 16, 60, TANK_SMALL, false, tankOverlay);

		guiItemStacks.set(0, inputs.get(0));

		if (!outputs.isEmpty()) {
			for (int i = 0; i < outputs.size(); i++) {
				guiItemStacks.set(i + 1, outputs.get(i));
			}
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

		if (!outputFluids.isEmpty()) {
			guiFluidStacks.set(0, outputFluids.get(0));
		}
		guiFluidStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {

			if (FluidHelper.hasPotionTag(ingredient)) {
				FluidHelper.addPotionTooltip(ingredient, tooltip);
			}
		});
	}
	// endregion
}
