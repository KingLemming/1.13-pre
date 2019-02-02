package cofh.thermal.core.plugins.jei;

import cofh.lib.util.helpers.StringHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecipeWrapper implements IRecipeWrapper {

	protected int energy;

	protected IDrawableAnimated energyMeter;

	@Nullable
	public List<String> getTooltipStrings(int mouseX, int mouseY) {

		List<String> tooltip = new ArrayList<>();

		if (energyMeter != null && mouseX > 2 && mouseX < 15 && mouseY > 8 && mouseY < 49) {
			tooltip.add(StringHelper.localize("info.cofh.energy") + ": " + StringHelper.formatNumber(energy) + " RF");
		}
		return tooltip;
	}

}
