package cofh.thermal.foundation.init;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.*;
import static cofh.thermal.core.init.ItemsTSeries.*;
import static cofh.thermal.core.init.RecipesTSeries.addConfigurableGearRecipe;
import static cofh.thermal.foundation.init.BlocksTF.*;
import static cofh.thermal.foundation.init.ItemsTF.*;

public class RecipesTF {

	private RecipesTF() {

	}

	// region REGISTRATION
	public static void registerRecipes() {

		addShapelessRecipe(cloneStack(dustBronze, 4), "dustCopper", "dustCopper", "dustCopper", "dustTin");
		addShapelessRecipe(cloneStack(dustInvar, 3), "dustIron", "dustIron", "dustNickel");
		addShapelessRecipe(cloneStack(dustElectrum, 2), "dustGold", "dustSilver");
		addShapelessRecipe(cloneStack(dustConstantan, 2), "dustCopper", "dustNickel");

		addConfigurableGearRecipe(gearCopper, "ingotCopper");
		addConfigurableGearRecipe(gearTin, "ingotTin");
		addConfigurableGearRecipe(gearLead, "ingotLead");
		addConfigurableGearRecipe(gearSilver, "ingotSilver");
		addConfigurableGearRecipe(gearNickel, "ingotNickel");
		addConfigurableGearRecipe(gearPlatinum, "ingotPlatinum");

		//		addGearRecipes(gearAluminum, "ingotAluminum");
		//		addGearRecipes(gearIridium, "ingotIridium");

		addConfigurableGearRecipe(gearBronze, "ingotBronze");
		addConfigurableGearRecipe(gearInvar, "ingotInvar");
		addConfigurableGearRecipe(gearElectrum, "ingotElectrum");
		addConfigurableGearRecipe(gearConstantan, "ingotConstantan");

		addTwoWayStorageRecipe(storageCopper, "blockCopper", ingotCopper, "ingotCopper");
		addTwoWayStorageRecipe(storageTin, "blockTin", ingotTin, "ingotTin");
		addTwoWayStorageRecipe(storageLead, "blockLead", ingotLead, "ingotLead");
		addTwoWayStorageRecipe(storageSilver, "blockSilver", ingotSilver, "ingotSilver");
		addTwoWayStorageRecipe(storageNickel, "blockNickel", ingotNickel, "ingotNickel");
		addTwoWayStorageRecipe(storagePlatinum, "blockPlatinum", ingotPlatinum, "ingotPlatinum");

		//		addTwoWayStorageRecipe(blockAluminum, "blockAluminum", ingotAluminum, "ingotAluminum");
		//		addTwoWayStorageRecipe(blockIridium, "blockIridium", ingotIridium, "ingotIridium");

		addTwoWayStorageRecipe(ingotCopper, "ingotCopper", nuggetCopper, "nuggetCopper");
		addTwoWayStorageRecipe(ingotTin, "ingotTin", nuggetTin, "nuggetTin");
		addTwoWayStorageRecipe(ingotLead, "ingotLead", nuggetLead, "nuggetLead");
		addTwoWayStorageRecipe(ingotSilver, "ingotSilver", nuggetSilver, "nuggetSilver");
		addTwoWayStorageRecipe(ingotNickel, "ingotNickel", nuggetNickel, "nuggetNickel");
		addTwoWayStorageRecipe(ingotPlatinum, "ingotPlatinum", nuggetPlatinum, "nuggetPlatinum");

		//		addTwoWayStorageRecipe(ingotAluminum, "ingotAluminum", nuggetAluminum, "nuggetAluminum");
		//		addTwoWayStorageRecipe(ingotIridium, "ingotIridium", nuggetIridium, "nuggetIridium");

		addTwoWayStorageRecipe(ingotBronze, "ingotBronze", nuggetBronze, "nuggetBronze");
		addTwoWayStorageRecipe(ingotInvar, "ingotInvar", nuggetInvar, "nuggetInvar");
		addTwoWayStorageRecipe(ingotElectrum, "ingotElectrum", nuggetElectrum, "nuggetElectrum");
		addTwoWayStorageRecipe(ingotConstantan, "ingotConstantan", nuggetConstantan, "nuggetConstantan");

		addSmelting(oreCopper, ingotCopper, 0.6F);
		addSmelting(oreTin, ingotTin, 0.7F);
		addSmelting(oreLead, ingotLead, 0.8F);
		addSmelting(oreSilver, ingotSilver, 0.9F);
		addSmelting(oreNickel, ingotNickel, 1.0F);
		addSmelting(orePlatinum, ingotPlatinum, 1.0F);

		//		addSmelting(oreAluminum, ingotAluminum, 0.6F);
		//		addSmelting(oreIridium, ingotIridium, 1.2F);

		addSmelting(dustIron, ingotIron);
		addSmelting(dustGold, ingotGold);

		addSmelting(dustCopper, ingotCopper);
		addSmelting(dustTin, ingotTin);
		addSmelting(dustLead, ingotLead);
		addSmelting(dustSilver, ingotSilver);
		addSmelting(dustNickel, ingotNickel);
		addSmelting(dustPlatinum, ingotPlatinum);

		//      addSmelting(dustAluminum, ingotAluminum);
		//      addSmelting(dustIridium, ingotIridium);
	}
	// endregion
}
