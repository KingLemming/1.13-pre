package cofh.thermal.core.plugins.jei;

import cofh.thermal.core.ThermalSeries;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class BaseRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {

	protected final String uId;
	protected boolean enable = true;
	protected IDrawableStatic background;
	protected IDrawableStatic energyMeter;
	protected IDrawableStatic icon;
	protected String localizedName;

	public BaseRecipeCategory(String uId) {

		this.uId = uId;
	}

	public void registerCategory(IRecipeCategoryRegistration registry) {

	}

	public void registerRecipes(IModRegistry registry) {

	}

	// region IRecipeCategory
	@Nonnull
	@Override
	public String getUid() {

		return uId;
	}

	@Nonnull
	@Override
	public String getTitle() {

		return localizedName;
	}

	@Override
	public String getModName() {

		return ThermalSeries.MOD_NAME;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {

		return background;
	}

	@Nullable
	@Override
	public IDrawable getIcon() {

		return icon;
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {

		return Collections.emptyList();
	}
	// endregion
}
