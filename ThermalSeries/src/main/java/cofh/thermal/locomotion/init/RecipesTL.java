package cofh.thermal.locomotion.init;

import net.minecraft.init.Blocks;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.addShapedRecipe;
import static cofh.lib.util.helpers.RecipeHelper.addShapelessRecipe;
import static cofh.thermal.locomotion.init.BlocksTL.*;

public class RecipesTL {

	private RecipesTL() {

	}

	// region REGISTRATION
	public static void registerRecipes() {

		registerRailRecipes();
	}

	private static void registerRailRecipes() {

		addShapelessRecipe(railCrossover, Blocks.RAIL, Blocks.RAIL);

		addShapedRecipe(cloneStack(railWood, 16), "I I", "ISI", "I I", 'I', "plankWood", 'S', "stickWood");
		addShapelessRecipe(railWoodCrossover, railWood, railWood);

		addShapedRecipe(cloneStack(railReinforced, 16), "I I", "ISI", "I I", 'I', "ingotInvar", 'S', "stickWood");
		addShapelessRecipe(railReinforcedCrossover, railReinforced, railReinforced);

		addShapedRecipe(cloneStack(railLumium, 16), "I I", "ISI", "I I", 'I', "ingotLumium", 'S', "stickWood");
		addShapelessRecipe(railLumiumCrossover, railLumium, railLumium);
	}
	// endregion
}
