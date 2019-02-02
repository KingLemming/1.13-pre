package cofh.redstonearsenal.init;

import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.*;
import static cofh.redstonearsenal.init.BlocksRSA.blockFluxCrystal;
import static cofh.redstonearsenal.init.BlocksRSA.blockFluxInfused;
import static cofh.redstonearsenal.init.ItemsRSA.*;

public class RecipesRSA {

	private RecipesRSA() {

	}

	// region REGISTRATION
	public static void registerRecipes() {

		addGearRecipe(gearFluxInfused, "ingotFluxInfused");

		addTwoWayStorageRecipe(ingotFluxInfused, "ingotFluxInfused", nuggetFluxInfused, "nuggetFluxInfused");

		addTwoWayStorageRecipe(blockFluxInfused, "blockFluxInfused", ingotFluxInfused, "ingotFluxInfused");
		addTwoWayStorageRecipe(blockFluxCrystal, "blockFluxCrystal", gemFluxCrystal, "gemFluxCrystal");

		if (OreDictHelper.oreNameExists("dustObsidian")) {
			addShapedRecipe(rodObsidian, "  O", " B ", "O  ", 'B', Items.BLAZE_POWDER, 'O', "dustObsidian");
		} else {
			addShapedRecipe(rodObsidian, "  O", " B ", "O  ", 'B', Items.BLAZE_POWDER, 'O', Blocks.OBSIDIAN);
		}
		addShapedRecipe(rodObsidianFlux, "  O", " B ", "O  ", 'B', rodObsidian, 'O', "gemFluxCrystal");
		addShapedRecipe(plateArmorFlux, " I ", "IGI", " I ", 'G', "gemFluxCrystal", 'I', "plateFluxInfused");

		// TODO: Add ThermEx compat
		if (true) { //(!Loader.isModLoaded(ID_THERMAL_EXPANSION)) {
			if (OreDictHelper.oreNameExists("dustElectrum")) {
				addShapelessRecipe(dustFluxInfused, "dustElectrum", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone");
			} else {
				addShapelessRecipe(dustFluxInfused, "ingotGold", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone");
			}
			addShapelessRecipe(gemFluxCrystal, "gemDiamond", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone");
			addShapedRecipe(cloneStack(plateFluxInfused, 4), "II", "II", 'I', "ingotFluxInfused");

			addSmelting(dustFluxInfused, ingotFluxInfused);
		} else {
			if (OreDictHelper.oreNameExists("dustElectrum")) {

			} else {

			}
		}
	}
	// endregion
}
