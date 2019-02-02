package cofh.thermal.core.init;

import cofh.lib.crafting.FluidIngredientFactory.FluidIngredient;
import cofh.thermal.core.util.managers.IManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.ID_THERMAL_FOUNDATION;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.*;
import static cofh.thermal.core.init.BlocksTSeries.storageCharcoal;
import static cofh.thermal.core.init.BlocksTSeries.storageCoalCoke;
import static cofh.thermal.core.init.ConfigTSeries.*;
import static cofh.thermal.core.init.ItemsTSeries.*;

public class RecipesTSeries {

	private RecipesTSeries() {

	}

	// region REGISTRATION
	public static void registerRecipes() {

		addShapedRecipe(utilWrench, "I I", " I ", " I ", 'I', "ingotIron");
		addShapelessRecipe(utilRedprint, "paper", "paper", "dustRedstone");
		addShapedRecipe(utilTomeLexicon, " L ", "GBI", " R ", 'B', Items.BOOK, 'G', "ingotGold", 'I', "ingotIron", 'L', "gemLapis", 'R', "dustRedstone");
		addShapedRecipe(utilTomeExperience, " L ", "EBE", " L ", 'B', Items.BOOK, 'E', "gemEmerald", 'L', "gemLapis");

		addConfigurableGearRecipe(gearIron, "ingotIron");
		addConfigurableGearRecipe(gearGold, "ingotGold");
		addConfigurableGearRecipe(gearDiamond, "gemDiamond");
		addConfigurableGearRecipe(gearEmerald, "gemEmerald");

		addConfigurableGearRecipe(gearSteel, "ingotSteel");
		addConfigurableGearRecipe(gearSignalum, "ingotSignalum");
		addConfigurableGearRecipe(gearLumium, "ingotLumium");
		addConfigurableGearRecipe(gearEnderium, "ingotEnderium");

		addTwoWayStorageRecipe(gemDiamond, "gemDiamond", nuggetDiamond, "nuggetDiamond");
		addTwoWayStorageRecipe(gemEmerald, "gemEmerald", nuggetEmerald, "nuggetEmerald");

		addTwoWayStorageRecipe(ingotSteel, "ingotSteel", nuggetSteel, "nuggetSteel");
		addTwoWayStorageRecipe(ingotSignalum, "ingotSignalum", nuggetSignalum, "nuggetSignalum");
		addTwoWayStorageRecipe(ingotLumium, "ingotLumium", nuggetLumium, "nuggetLumium");
		addTwoWayStorageRecipe(ingotEnderium, "ingotEnderium", nuggetEnderium, "nuggetEnderium");

		addTwoWayStorageRecipe(storageCharcoal, "blockCharcoal", itemCharcoal, "charcoal");
		addTwoWayStorageRecipe(storageCoalCoke, "blockFuelCoke", coalCoke, "fuelCoke");

		addShapelessRecipe(cloneStack(Items.GUNPOWDER), "dustCoal", "dustSulfur", "dustNiter", "dustNiter");
		addShapelessRecipe(cloneStack(Items.GUNPOWDER), "dustCharcoal", "dustSulfur", "dustNiter", "dustNiter");

		addSmelting(itemCoal, coalCoke, 0.2F);

		/* FLUID ALLOYS */
		if (Loader.isModLoaded(ID_THERMAL_FOUNDATION)) {
			addShapelessFluidRecipe(cloneStack(dustSignalum, 4), "dustCopper", "dustCopper", "dustCopper", "dustSilver", new FluidIngredient("redstone"));
			addShapelessFluidRecipe(cloneStack(dustLumium, 4), "dustTin", "dustTin", "dustTin", "dustSilver", new FluidIngredient("glowstone"));
			addShapelessFluidRecipe(cloneStack(dustEnderium, 4), "dustLead", "dustLead", "dustLead", "dustPlatinum", new FluidIngredient("ender"));
		} else {
			addShapelessFluidRecipe(cloneStack(dustSignalum, 4), "dustIron", "dustIron", "dustIron", "dustGold", new FluidIngredient("redstone"));
			addShapelessFluidRecipe(cloneStack(dustLumium, 4), "dustGold", "dustGold", "dustGold", "dustIron", new FluidIngredient("glowstone"));
			addShapelessFluidRecipe(cloneStack(dustEnderium, 4), "dustObsidian", "dustObsidian", "dustDiamond", "dustDiamond", new FluidIngredient("ender"));
		}

		/* BASIC GEARS */
		if (enableBasicGears) {
			addGearRecipe(gearWood, "stickWood");
			addGearRecipe(gearStone, "stone", "gearWood");
		}

		/* BASIC STEEL */
		if (enableBasicSteel) {
			addShapelessRecipe(cloneStack(dustSteel, 1), "dustIron", "dustCoal", "dustCoal", "dustCoal", "dustCoal");
			addShapelessRecipe(cloneStack(dustSteel, 1), "dustIron", "dustCharcoal", "dustCharcoal", "dustCharcoal", "dustCharcoal");

			addSmelting(dustSteel, ingotSteel);
		}

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
