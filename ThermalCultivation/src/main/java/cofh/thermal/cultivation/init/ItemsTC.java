package cofh.thermal.cultivation.init;

import cofh.core.item.ItemFoodCoFH;
import cofh.core.item.ItemSeedCoFH;
import cofh.core.item.ItemSeedFoodCoFH;
import cofh.thermal.core.init.CreativeTabsTSeries;
import cofh.thermal.cultivation.item.ItemWateringCan;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.Constants.GROUP_CROPS;
import static cofh.lib.util.Constants.GROUP_SEEDS;
import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.thermal.core.ThermalSeries.config;
import static cofh.thermal.core.ThermalSeries.registerItem;
import static cofh.thermal.cultivation.init.BlocksTC.*;

public class ItemsTC {

	private ItemsTC() {

	}

	// region REGISTRATION
	public static void registerItems() {

		registerTools();
		registerCrops();
		registerSeeds();
	}

	public static void registerCrops() {

		// cropBarley = registerItem("crop_barley", "cropBarley", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		// cropCorn = registerItem("crop_corn", "cropCorn", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropOnion = registerItem("crop_onion", "cropOnion", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropSadiroot = registerItem("crop_sadiroot", "cropSadiroot", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropSpinach = registerItem("crop_spinach", "cropSpinach", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropRice = registerItem("crop_rice", "cropRice", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));

		cropBellPepper = registerItem("crop_bell_pepper", "cropBellPepper", new ItemSeedFoodCoFH(2, 0.2F, plantBellPepper, GROUP_CROPS));
		// cropCoffee = registerItem("crop_coffee", "cropCoffee", new ItemSeedFoodCoFH(2, 0.2F, plantCoffee, GROUP_CROPS));
		cropGreenBean = registerItem("crop_green_bean", "cropGreenBean", new ItemSeedFoodCoFH(2, 0.2F, plantGreenBean, GROUP_CROPS));
		cropPeanut = registerItem("crop_peanut", "cropPeanut", new ItemSeedFoodCoFH(2, 0.2F, plantPeanut, GROUP_CROPS));
		cropStrawberry = registerItem("crop_strawberry", "cropStrawberry", new ItemSeedFoodCoFH(2, 0.2F, plantStrawberry, GROUP_CROPS));
		// cropTea = registerItem("crop_tea", "cropTea", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropTomato = registerItem("crop_tomato", "cropTomato", new ItemSeedFoodCoFH(2, 0.2F, plantTomato, GROUP_CROPS));
	}

	public static void registerSeeds() {

		// seedBarley = registerItem("seed_barley", "seedBarley", new ItemSeedCoFH(plantBarley, GROUP_SEEDS));
		// seedCorn = registerItem("seed_corn", "seedCorn", new ItemSeedCoFH(plantCorn, GROUP_SEEDS));
		seedOnion = registerItem("seed_onion", "seedOnion", new ItemSeedCoFH(plantOnion, GROUP_SEEDS));
		seedSadiroot = registerItem("seed_sadiroot", "seedSadiroot", new ItemSeedCoFH(plantSadiroot, GROUP_SEEDS));
		seedSpinach = registerItem("seed_spinach", "seedSpinach", new ItemSeedCoFH(plantSpinach, GROUP_SEEDS));
		seedRice = registerItem("seed_rice", "seedRice", new ItemSeedCoFH(plantRice, GROUP_SEEDS));

		seedBellPepper = registerItem("seed_bell_pepper", "seedBellPepper", new ItemSeedCoFH(plantBellPepper, GROUP_SEEDS));
		// seedCoffee = registerItem("seed_coffee", "seedCoffee", new ItemSeedCoFH(plantCoffee, GROUP_SEEDS));
		seedGreenBean = registerItem("seed_green_bean", "seedGreenBean", new ItemSeedCoFH(plantGreenBean, GROUP_SEEDS));
		seedPeanut = registerItem("seed_peanut", "seedPeanut", new ItemSeedCoFH(plantPeanut, GROUP_SEEDS));
		seedStrawberry = registerItem("seed_strawberry", "seedStrawberry", new ItemSeedCoFH(plantStrawberry, GROUP_SEEDS));
		// seedTea = registerItem("seed_tea", "seedTea", new ItemSeedCoFH(plantTea, GROUP_SEEDS));
		seedTomato = registerItem("seed_tomato", "seedTomato", new ItemSeedCoFH(plantTomato, GROUP_SEEDS));
	}

	public static void registerTools() {

		itemWateringCanBasic = createWateringCan("basic", 4000, 1, 40);
		//		itemWateringCanBasic = createWateringCan("basic", 12000, 2, 50);
		//		itemWateringCanBasic = createWateringCan("basic", 18000, 3, 60);
		//		itemWateringCanBasic = createWateringCan("basic", 40000, 4, 70);
		//		itemWateringCanBasic = createWateringCan("basic", 60000, 5, 80);
		itemWateringCanCreative = (ItemWateringCan) createWateringCan("creative", 2000, 5, 200).setRarity(EnumRarity.EPIC).setCreative(true);

		registerItem("watering_can_basic", itemWateringCanBasic, CreativeTabsTSeries.tabTools);
		registerItem("watering_can_creative", itemWateringCanCreative, CreativeTabsTSeries.tabTools);
	}
	// endregion

	// region HELPERS
	private static ItemWateringCan createWateringCan(String id, int fluidCapacity, int radius, int effectiveness) {

		String category = "Tools.WateringCan." + titleCase(id);
		String comment = "Adjust this value to set how much Water (mB) this Watering Can holds.";
		fluidCapacity = config.getInt("Water", category, fluidCapacity, Fluid.BUCKET_VOLUME * 2, Fluid.BUCKET_VOLUME * 1000, comment);

		comment = "Adjust this value to set the maximum radius for this Watering Can.";
		radius = config.getInt("Radius", category, radius, 0, 16, comment);

		comment = "Adjust this value to set the base effectiveness for this Watering Can.";
		effectiveness = config.getInt("Effectiveness", category, effectiveness, 1, 200, comment);

		return new ItemWateringCan(fluidCapacity, radius, effectiveness);
	}
	// endregion

	// region CROPS
	public static ItemStack cropBarley;
	public static ItemStack cropCorn;
	public static ItemStack cropOnion;
	public static ItemStack cropSadiroot;
	public static ItemStack cropSpinach;
	public static ItemStack cropRice;

	public static ItemStack cropBellPepper;
	public static ItemStack cropCoffee;
	public static ItemStack cropGreenBean;
	public static ItemStack cropPeanut;
	public static ItemStack cropStrawberry;
	public static ItemStack cropTomato;
	// endregion

	// region SEEDS
	public static ItemStack seedBarley;
	public static ItemStack seedCorn;
	public static ItemStack seedOnion;
	public static ItemStack seedSadiroot;
	public static ItemStack seedSpinach;
	public static ItemStack seedRice;

	public static ItemStack seedBellPepper;
	public static ItemStack seedCoffee;
	public static ItemStack seedGreenBean;
	public static ItemStack seedPeanut;
	public static ItemStack seedStrawberry;
	public static ItemStack seedTomato;
	// endregion

	// region TOOLS
	public static ItemWateringCan itemWateringCanBasic;
	//	public static ItemWateringCan itemWateringCanBasic;
	//	public static ItemWateringCan itemWateringCanBasic;
	//	public static ItemWateringCan itemWateringCanBasic;
	public static ItemWateringCan itemWateringCanCreative;
	// endregion
}
