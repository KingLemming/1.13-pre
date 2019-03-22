package cofh.thermal.foundation.init;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.*;
import static cofh.thermal.core.init.RecipesTSeries.addConfigurableGearRecipe;
import static cofh.thermal.foundation.init.BlocksTF.*;
import static cofh.thermal.foundation.init.ItemsTF.*;

public class RecipesTF {

	private RecipesTF() {

	}

	// region REGISTRATION
	public static void registerRecipes() {

		registerThermalMetalRecipes();
		registerExtraMetalRecipes();
	}

	private static void registerThermalMetalRecipes() {

		addTwoWayStorageRecipe(ingotCopper, "ingotCopper", nuggetCopper, "nuggetCopper");
		addTwoWayStorageRecipe(storageCopper, "blockCopper", ingotCopper, "ingotCopper");
		addConfigurableGearRecipe(gearCopper, "ingotCopper");

		addTwoWayStorageRecipe(ingotSilver, "ingotSilver", nuggetSilver, "nuggetSilver");
		addTwoWayStorageRecipe(storageSilver, "blockSilver", ingotSilver, "ingotSilver");
		addConfigurableGearRecipe(gearSilver, "ingotSilver");

		addTwoWayStorageRecipe(ingotNickel, "ingotNickel", nuggetNickel, "nuggetNickel");
		addTwoWayStorageRecipe(storageNickel, "blockNickel", ingotNickel, "ingotNickel");
		addConfigurableGearRecipe(gearNickel, "ingotNickel");

		addTwoWayStorageRecipe(ingotInvar, "ingotInvar", nuggetInvar, "nuggetInvar");
		addTwoWayStorageRecipe(storageInvar, "blockInvar", ingotInvar, "ingotInvar");
		addConfigurableGearRecipe(gearInvar, "ingotInvar");

		addTwoWayStorageRecipe(ingotConstantan, "ingotConstantan", nuggetConstantan, "nuggetConstantan");
		addTwoWayStorageRecipe(storageConstantan, "blockConstantan", ingotConstantan, "ingotConstantan");
		addConfigurableGearRecipe(gearConstantan, "ingotConstantan");

		addTwoWayStorageRecipe(ingotElectrum, "ingotElectrum", nuggetElectrum, "nuggetElectrum");
		addTwoWayStorageRecipe(storageElectrum, "blockElectrum", ingotElectrum, "ingotElectrum");
		addConfigurableGearRecipe(gearElectrum, "ingotElectrum");

		addShapelessRecipe(cloneStack(dustInvar, 3), "dustIron", "dustIron", "dustNickel");
		addShapelessRecipe(cloneStack(dustConstantan, 2), "dustCopper", "dustNickel");
		addShapelessRecipe(cloneStack(dustElectrum, 2), "dustGold", "dustSilver");

		addSmelting(oreCopper, ingotCopper, 0.6F);
		addSmelting(oreSilver, ingotSilver, 0.9F);
		addSmelting(oreNickel, ingotNickel, 1.0F);

		addSmelting(dustCopper, ingotCopper);
		addSmelting(dustSilver, ingotSilver);
		addSmelting(dustNickel, ingotNickel);

		addSmelting(dustInvar, ingotInvar);
		addSmelting(dustConstantan, ingotConstantan);
		addSmelting(dustElectrum, ingotElectrum);
	}

	private static void registerExtraMetalRecipes() {

		addTwoWayStorageRecipe(ingotTin, "ingotTin", nuggetTin, "nuggetTin");
		addTwoWayStorageRecipe(storageTin, "blockTin", ingotTin, "ingotTin");
		addConfigurableGearRecipe(gearTin, "ingotTin");

		addTwoWayStorageRecipe(ingotAluminum, "ingotAluminum", nuggetAluminum, "nuggetAluminum");
		addTwoWayStorageRecipe(storageAluminum, "blockAluminum", ingotAluminum, "ingotAluminum");
		addConfigurableGearRecipe(gearAluminum, "ingotAluminum");

		addTwoWayStorageRecipe(ingotLead, "ingotLead", nuggetLead, "nuggetLead");
		addTwoWayStorageRecipe(storageLead, "blockLead", ingotLead, "ingotLead");
		addConfigurableGearRecipe(gearLead, "ingotLead");

		addTwoWayStorageRecipe(ingotPlatinum, "ingotPlatinum", nuggetPlatinum, "nuggetPlatinum");
		addTwoWayStorageRecipe(storagePlatinum, "blockPlatinum", ingotPlatinum, "ingotPlatinum");
		addConfigurableGearRecipe(gearPlatinum, "ingotPlatinum");

		addTwoWayStorageRecipe(ingotIridium, "ingotIridium", nuggetIridium, "nuggetIridium");
		addTwoWayStorageRecipe(storageIridium, "blockIridium", ingotIridium, "ingotIridium");
		addConfigurableGearRecipe(gearIridium, "ingotIridium");

		/* BASIC STEEL */
		//		if (enableBasicSteel) {
		//			addShapelessRecipe(cloneStack(dustSteel, 1), "dustIron", "dustCoal", "dustCoal", "dustCoal", "dustCoal");
		//			addShapelessRecipe(cloneStack(dustSteel, 1), "dustIron", "dustCharcoal", "dustCharcoal", "dustCharcoal", "dustCharcoal");
		//
		//			addSmelting(dustSteel, ingotSteel);
		//		}
		addShapelessRecipe(cloneStack(dustBronze, 4), "dustCopper", "dustCopper", "dustCopper", "dustTin");

		addSmelting(oreTin, ingotTin, 0.7F);
		addSmelting(oreAluminum, ingotAluminum, 0.7F);
		addSmelting(oreLead, ingotLead, 0.8F);
		addSmelting(orePlatinum, ingotPlatinum, 1.0F);
		addSmelting(oreIridium, ingotIridium, 1.0F);

		addSmelting(dustTin, ingotTin);
		addSmelting(dustAluminum, ingotAluminum);
		addSmelting(dustLead, ingotLead);
		addSmelting(dustPlatinum, ingotPlatinum);
		addSmelting(dustIridium, ingotIridium);
	}
	// endregion
}
