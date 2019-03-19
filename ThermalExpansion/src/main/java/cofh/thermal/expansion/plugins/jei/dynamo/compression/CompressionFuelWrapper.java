package cofh.thermal.expansion.plugins.jei.dynamo.compression;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.plugins.jei.BaseRecipeWrapper;
import cofh.thermal.core.plugins.jei.Drawables;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static cofh.thermal.core.util.managers.SimpleFluidFuelManager.ENERGY_FACTOR;
import static java.util.Collections.singletonList;

public class CompressionFuelWrapper extends BaseRecipeWrapper {

	protected List<List<FluidStack>> input;

	protected IDrawableAnimated duration;

	public CompressionFuelWrapper(IGuiHelper guiHelper, IDynamoFuel fuel) {

		input = singletonList(fuel.getInputFluids());
		energy = fuel.getEnergy() * ENERGY_FACTOR;

		IDrawableStatic progressDrawable = Drawables.getDrawables(guiHelper).getScaleFill(Drawables.SCALE_FLAME);
		IDrawableStatic energyDrawable = Drawables.getDrawables(guiHelper).getEnergyFill();

		duration = guiHelper.createAnimatedDrawable(progressDrawable, 1000, StartDirection.TOP, true);
		// energyMeter = guiHelper.createAnimatedDrawable(energyDrawable, 1000, StartDirection.BOTTOM, false);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {

		ingredients.setInputLists(VanillaTypes.FLUID, input);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		duration.draw(minecraft, 34, 43);
		// energyMeter.draw(minecraft, 2, 8);

		minecraft.fontRenderer.drawString(StringHelper.formatNumber(energy) + " RF", 96, (recipeHeight - 9) / 2, 0x808080);
	}

}
