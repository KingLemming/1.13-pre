package cofh.thermal.expansion.plugins.jei.machine.refinery;

import cofh.lib.util.helpers.RenderHelper;
import cofh.thermal.core.plugins.jei.BaseRecipeWrapper;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RefineryRecipeWrapper extends BaseRecipeWrapper {

	protected List<FluidStack> inputFluids;
	protected List<FluidStack> outputFluids;
	protected List<ItemStack> outputItems;
	protected List<Float> chances;

	protected IDrawableAnimated progressMask;
	protected IDrawableAnimated progress;
	protected IDrawableAnimated speed;

	public RefineryRecipeWrapper(IGuiHelper guiHelper, IMachineRecipe recipe) {

		inputFluids = new ArrayList<>(recipe.getInputFluids());
		outputFluids = new ArrayList<>(recipe.getOutputFluids(Collections.emptyList(), Collections.emptyList()));
		outputItems = new ArrayList<>(recipe.getOutputItems(Collections.emptyList(), Collections.emptyList()));
		energy = recipe.getEnergy(Collections.emptyList(), Collections.emptyList());

		IDrawableStatic progressMaskDrawable = Drawables.getDrawables(guiHelper).getProgress(Drawables.PROGRESS_DROP);
		IDrawableStatic progressDrawable = Drawables.getDrawables(guiHelper).getProgressFill(Drawables.PROGRESS_DROP);
		IDrawableStatic speedDrawable = Drawables.getDrawables(guiHelper).getScaleFill(Drawables.SCALE_FLAME);
		IDrawableStatic energyDrawable = Drawables.getDrawables(guiHelper).getEnergyFill();

		progressMask = guiHelper.createAnimatedDrawable(progressMaskDrawable, energy / 10, StartDirection.LEFT, true);
		progress = guiHelper.createAnimatedDrawable(progressDrawable, energy / 10, StartDirection.LEFT, false);
		speed = guiHelper.createAnimatedDrawable(speedDrawable, 1000, StartDirection.TOP, true);
		energyMeter = guiHelper.createAnimatedDrawable(energyDrawable, 1000, StartDirection.TOP, true);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {

		ingredients.setInputs(VanillaTypes.FLUID, inputFluids);
		ingredients.setOutputs(VanillaTypes.FLUID, outputFluids);
		ingredients.setOutputs(VanillaTypes.ITEM, outputItems);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		RenderHelper.drawFluid(57, 23, inputFluids.get(0), 24, 16);

		progressMask.draw(minecraft, 57, 23);
		progress.draw(minecraft, 57, 23);
		speed.draw(minecraft, 29, 40);
		energyMeter.draw(minecraft, 2, 8);
	}

}
