package cofh.thermal.expansion.plugins.jei.machine.pulverizer;

import cofh.lib.util.oredict.OreDictHelper;
import cofh.thermal.core.plugins.jei.BaseRecipeWrapper;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.util.managers.machine.PulverizerRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static java.util.Collections.singletonList;

public class PulverizerRecipeWrapper extends BaseRecipeWrapper {

	protected List<List<ItemStack>> inputs;
	protected List<ItemStack> outputs;
	protected List<Float> chances;

	protected IDrawableAnimated progress;
	protected IDrawableAnimated speed;

	public PulverizerRecipeWrapper(IGuiHelper guiHelper, IMachineRecipe recipe) {

		List<ItemStack> recipeInputs = new ArrayList<>();
		int oreID = PulverizerRecipeManager.instance().convertInput(recipe.getInputItems().get(0)).oreID;
		if (oreID != -1) {
			int count = recipe.getInputItems().get(0).getCount();
			for (ItemStack ore : OreDictionary.getOres(OreDictHelper.getOreName(oreID), false)) {
				recipeInputs.add(cloneStack(ore, count));
			}
		} else {
			recipeInputs.addAll(recipe.getInputItems());
		}
		inputs = singletonList(recipeInputs);
		outputs = new ArrayList<>(recipe.getOutputItems(Collections.emptyList(), Collections.emptyList()));
		energy = recipe.getEnergy(Collections.emptyList(), Collections.emptyList());
		chances = recipe.getOutputItemChances(Collections.emptyList(), Collections.emptyList());

		for (int i = 0; i < outputs.size(); i++) {
			float chance = chances.get(i);
			if (chance > 1.0F) {
				outputs.get(i).setCount((int) chance);
			}
		}
		IDrawableStatic progressDrawable = Drawables.getDrawables(guiHelper).getProgressFill(Drawables.PROGRESS_ARROW);
		IDrawableStatic speedDrawable = Drawables.getDrawables(guiHelper).getScaleFill(Drawables.SCALE_CRUSH);
		IDrawableStatic energyDrawable = Drawables.getDrawables(guiHelper).getEnergyFill();

		progress = guiHelper.createAnimatedDrawable(progressDrawable, energy / 10, StartDirection.LEFT, false);
		speed = guiHelper.createAnimatedDrawable(speedDrawable, 1000, StartDirection.TOP, true);
		energyMeter = guiHelper.createAnimatedDrawable(energyDrawable, 1000, StartDirection.TOP, true);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {

		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutputs(VanillaTypes.ITEM, outputs);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		progress.draw(minecraft, 62, 14);
		speed.draw(minecraft, 34, 24);
		energyMeter.draw(minecraft, 2, 8);
	}

}
