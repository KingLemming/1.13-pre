package cofh.thermal.cultivation.init;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.addShapelessRecipe;
import static cofh.thermal.cultivation.init.ItemsTC.*;

public class RecipesTC {

	private RecipesTC() {

	}

	// region REGISTRATION
	public static void registerRecipes() {

		registerToolRecipes();
		registerSeedRecipes();
		registerFoodRecipes();
	}

	private static void registerFoodRecipes() {

	}

	private static void registerSeedRecipes() {

		addShapelessRecipe(cloneStack(seedCorn, 2), "cropCorn");

		addShapelessRecipe(cloneStack(seedBellPepper, 2), "cropBellPepper");
		addShapelessRecipe(cloneStack(seedGreenBean, 2), "cropGreenBean");
		addShapelessRecipe(cloneStack(seedPeanut, 2), "cropPeanut");
		addShapelessRecipe(cloneStack(seedStrawberry, 2), "cropStrawberry");
		addShapelessRecipe(cloneStack(seedTomato, 2), "cropTomato");

		addShapelessRecipe(cloneStack(seedCoffee, 2), "cropCoffee");
		addShapelessRecipe(cloneStack(seedTea, 2), "cropTea");
		addShapelessRecipe(cloneStack(seedHops, 2), "cropHops");
	}

	private static void registerToolRecipes() {

	}
	// endregion
}
