package cofh.thermal.expansion.plugins.jei.machine.crucible;

import cofh.lib.util.helpers.RenderHelper;
import cofh.lib.util.oredict.OreDictHelper;
import cofh.thermal.core.plugins.jei.BaseRecipeWrapper;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.util.managers.machine.CrucibleRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static java.util.Collections.singletonList;

public class CrucibleRecipeWrapper extends BaseRecipeWrapper {

	protected List<List<ItemStack>> inputs;
	protected List<FluidStack> outputs;

	protected IDrawableAnimated progressMask;
	protected IDrawableAnimated progress;
	protected IDrawableAnimated speed;

	public CrucibleRecipeWrapper(IGuiHelper guiHelper, IMachineRecipe recipe) {

		List<ItemStack> recipeInputs = new ArrayList<>();
		int oreID = CrucibleRecipeManager.instance().convertInput(recipe.getInputItems().get(0)).oreID;
		if (oreID != -1) {
			int count = recipe.getInputItems().get(0).getCount();
			for (ItemStack ore : OreDictionary.getOres(OreDictHelper.getOreName(oreID), false)) {
				recipeInputs.add(cloneStack(ore, count));
			}
		} else {
			recipeInputs.addAll(recipe.getInputItems());
		}
		inputs = singletonList(recipeInputs);
		outputs = new ArrayList<>(recipe.getOutputFluids(Collections.emptyList(), Collections.emptyList()));
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

		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutputs(VanillaTypes.FLUID, outputs);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		RenderHelper.drawFluid(69, 23, outputs.get(0), 24, 16);

		progressMask.draw(minecraft, 69, 23);
		progress.draw(minecraft, 69, 23);
		speed.draw(minecraft, 43, 33);
		energyMeter.draw(minecraft, 2, 8);
	}

}
