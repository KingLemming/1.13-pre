package cofh.thermal.core.plugins.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class JEIPluginTSeries implements IModPlugin {

	private static final List<BaseRecipeCategory> CATEGORIES = new ArrayList<>();

	public static void addCategory(BaseRecipeCategory category) {

		if (!CATEGORIES.contains(category)) {
			CATEGORIES.add(category);
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {

		for (BaseRecipeCategory category : CATEGORIES) {
			category.registerCategory(registry);
		}
	}

	@Override
	public void register(IModRegistry registry) {

		for (BaseRecipeCategory category : CATEGORIES) {
			category.registerRecipes(registry);
		}
	}

}
