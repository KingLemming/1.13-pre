package cofh.thermal.cultivation.plugins.jei.dynamo.gourmand;

import cofh.lib.util.helpers.StringHelper;
import cofh.lib.util.oredict.OreDictHelper;
import cofh.thermal.core.plugins.jei.BaseRecipeWrapper;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.cultivation.util.managers.dynamo.GourmandFuelManager;
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
import java.util.List;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static java.util.Collections.singletonList;

public class GourmandFuelWrapper extends BaseRecipeWrapper {

	protected List<List<ItemStack>> input;

	protected IDrawableAnimated duration;

	public GourmandFuelWrapper(IGuiHelper guiHelper, IDynamoFuel fuel) {

		List<ItemStack> fuelInput = new ArrayList<>();
		int oreID = GourmandFuelManager.instance().convertInput(fuel.getInputItems().get(0)).oreID;
		if (oreID != -1) {
			int count = fuel.getInputItems().get(0).getCount();
			for (ItemStack ore : OreDictionary.getOres(OreDictHelper.getOreName(oreID), false)) {
				fuelInput.add(cloneStack(ore, count));
			}
		} else {
			fuelInput.addAll(fuel.getInputItems());
		}
		input = singletonList(fuelInput);
		energy = fuel.getEnergy();

		IDrawableStatic progressDrawable = Drawables.getDrawables(guiHelper).getScaleFill(Drawables.SCALE_FLAME_GREEN);
		IDrawableStatic energyDrawable = Drawables.getDrawables(guiHelper).getEnergyFill();

		duration = guiHelper.createAnimatedDrawable(progressDrawable, 1000, StartDirection.TOP, true);
		// energyMeter = guiHelper.createAnimatedDrawable(energyDrawable, 1000, StartDirection.BOTTOM, false);
	}

	public GourmandFuelWrapper(IGuiHelper guiHelper, ItemStack stack, int energy) {

		this.input = singletonList(singletonList(stack));
		this.energy = energy;

		IDrawableStatic progressDrawable = Drawables.getDrawables(guiHelper).getScaleFill(Drawables.SCALE_FLAME_GREEN);
		IDrawableStatic energyDrawable = Drawables.getDrawables(guiHelper).getEnergyFill();

		duration = guiHelper.createAnimatedDrawable(progressDrawable, 1000, StartDirection.TOP, true);
		// energyMeter = guiHelper.createAnimatedDrawable(energyDrawable, 1000, StartDirection.BOTTOM, false);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {

		ingredients.setInputLists(VanillaTypes.ITEM, input);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		duration.draw(minecraft, 34, 43);
		// energyMeter.draw(minecraft, 2, 8);

		minecraft.fontRenderer.drawString(StringHelper.formatNumber(energy) + " RF", 96, (recipeHeight - 9) / 2, 0x808080);
	}

}
