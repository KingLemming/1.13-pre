package cofh.thermal.core.init;

import cofh.lib.crafting.FluidIngredientFactory.FluidIngredient;
import cofh.thermal.core.util.managers.IManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.*;
import static cofh.thermal.core.init.BlocksTSeries.*;
import static cofh.thermal.core.init.ConfigTSeries.*;
import static cofh.thermal.core.init.ItemsTSeries.*;

public class RecipesTSeries {

	private RecipesTSeries() {

	}

	// region REGISTRATION
	public static void registerRecipes() {

		registerToolRecipes();
		registerVanillaComponentRecipes();
		registerThermalMetalRecipes();
		// registerExtraMetalRecipes();
	}

	private static void registerToolRecipes() {

		addShapedRecipe(utilWrench, "I I", " I ", " I ", 'I', "ingotIron");
		addShapelessRecipe(utilRedprint, "paper", "paper", "dustRedstone");
		addShapedRecipe(utilTomeLexicon, " L ", "GBI", " R ", 'B', Items.BOOK, 'G', "ingotGold", 'I', "ingotIron", 'L', "gemLapis", 'R', "dustRedstone");
		addShapedRecipe(utilTomeExperience, " L ", "EBE", " L ", 'B', Items.BOOK, 'E', "gemEmerald", 'L', "gemLapis");
	}

	private static void registerVanillaComponentRecipes() {

		addShapelessRecipe(cloneStack(Items.GUNPOWDER), "dustCoal", "dustSulfur", "dustNiter", "dustNiter");
		addShapelessRecipe(cloneStack(Items.GUNPOWDER), "dustCharcoal", "dustSulfur", "dustNiter", "dustNiter");

		// @formatter:off
		if (enableHorseArmorCrafting) {
			addShapedRecipe(cloneStack(Items.IRON_HORSE_ARMOR),
					"I I",
					"LCL",
					"I I",
					'C', "blockWool",
					'L', Items.LEATHER,
					'I', "ingotIron"
			);
			addShapedRecipe(cloneStack(Items.GOLDEN_HORSE_ARMOR),
					"I I",
					"LCL",
					"I I",
					'C', "blockWool",
					'L', Items.LEATHER,
					'I', "ingotGold"
			);
			addShapedRecipe(cloneStack(Items.DIAMOND_HORSE_ARMOR),
					"I I",
					"LCL",
					"I I",
					'C', "blockWool",
					'L', Items.LEATHER,
					'I', "gemDiamond"
			);
		}
		if (enableSaddleCrafting) {
			addShapedRecipe(cloneStack(Items.SADDLE),
					"LLL",
					"LIL",
					"I I",
					'I', "ingotIron",
					'L', Items.LEATHER
			);
		}
		// @formatter:on

		if (enableBasicGears) {
			addGearRecipe(gearWood, "stickWood");
			addGearRecipe(gearStone, "stone", "gearWood");
		}
		addConfigurableGearRecipe(gearIron, "ingotIron");
		addConfigurableGearRecipe(gearGold, "ingotGold");
		addConfigurableGearRecipe(gearDiamond, "gemDiamond");
		addConfigurableGearRecipe(gearEmerald, "gemEmerald");

		addTwoWayStorageRecipe(gemDiamond, "gemDiamond", nuggetDiamond, "nuggetDiamond");
		addTwoWayStorageRecipe(gemEmerald, "gemEmerald", nuggetEmerald, "nuggetEmerald");

		addTwoWayStorageRecipe(storageCharcoal, "blockCharcoal", itemCharcoal, "charcoal");
		addTwoWayStorageRecipe(storageCoalCoke, "blockFuelCoke", coalCoke, "fuelCoke");

		addSmelting(itemCoal, coalCoke, 0.2F);
		addSmelting(dustIron, ingotIron);
		addSmelting(dustGold, ingotGold);
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

		addTwoWayStorageRecipe(ingotSignalum, "ingotSignalum", nuggetSignalum, "nuggetSignalum");
		addTwoWayStorageRecipe(storageSignalum, "blockSignalum", ingotSignalum, "ingotSignalum");
		addConfigurableGearRecipe(gearSignalum, "ingotSignalum");

		addTwoWayStorageRecipe(ingotLumium, "ingotLumium", nuggetLumium, "nuggetLumium");
		addTwoWayStorageRecipe(storageLumium, "blockLumium", ingotLumium, "ingotLumium");
		addConfigurableGearRecipe(gearLumium, "ingotLumium");

		addTwoWayStorageRecipe(ingotEnderium, "ingotEnderium", nuggetEnderium, "nuggetEnderium");
		addTwoWayStorageRecipe(storageEnderium, "blockEnderium", ingotEnderium, "ingotEnderium");
		addConfigurableGearRecipe(gearEnderium, "ingotEnderium");

		addShapelessRecipe(cloneStack(dustInvar, 3), "dustIron", "dustIron", "dustNickel");
		addShapelessRecipe(cloneStack(dustConstantan, 2), "dustCopper", "dustNickel");
		addShapelessRecipe(cloneStack(dustElectrum, 2), "dustGold", "dustSilver");

		addShapelessFluidRecipe(cloneStack(dustSignalum, 4), "dustCopper", "dustCopper", "dustCopper", "dustSilver", new FluidIngredient("redstone"));
		//		addShapelessFluidRecipe(cloneStack(dustLumium, 4), "dustTin", "dustTin", "dustTin", "dustSilver", new FluidIngredient("glowstone"));
		//		addShapelessFluidRecipe(cloneStack(dustEnderium, 4), "dustLead", "dustLead", "dustLead", "dustPlatinum", new FluidIngredient("ender"));

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

	// region MANAGERS
	private static final List<IManager> MANAGERS = new ArrayList<>();

	public static void registerManager(IManager manager) {

		if (!MANAGERS.contains(manager)) {
			MANAGERS.add(manager);
		}
	}

	public static void initializeManagers() {

		for (IManager manager : MANAGERS) {
			manager.config();
			manager.initialize();
		}
	}

	public static void refreshManagers() {

		for (IManager manager : MANAGERS) {
			manager.refresh();
		}
	}
	// endregion

	// region HELPERS
	public static void addConfigurableGearRecipe(ItemStack gear, String ingot) {

		if (enableAlternateGears) {
			addGearRecipe(gear, ingot, "ingotIron");
		} else {
			addGearRecipe(gear, ingot);
		}
	}
	// endregion
}
