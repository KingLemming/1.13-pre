package cofh.thermal.cultivation.init;

import cofh.core.item.ItemFoodCoFH;
import cofh.thermal.core.init.CreativeTabsTSeries;
import cofh.thermal.cultivation.item.ItemWateringCan;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.GROUP_CROPS;
import static cofh.thermal.core.ThermalSeries.registerItem;

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

		cropOnion = registerItem("crop_onion", "cropOnion", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));

		cropBellPepper = registerItem("crop_bell_pepper", "cropBellPepper", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropGreenBean = registerItem("crop_green_bean", "cropGreenBean", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropSpinach = registerItem("crop_spinach", "cropSpinach", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
		cropTomato = registerItem("crop_tomato", "cropTomato", new ItemFoodCoFH(2, 0.2F, GROUP_CROPS));
	}

	public static void registerSeeds() {

	}

	public static void registerTools() {

		itemWateringCanBasic = new ItemWateringCan(4000, 1, 40);

		toolWateringCanBasic = registerItem("watering_can_basic", itemWateringCanBasic, CreativeTabsTSeries.tabTools);
	}
	// endregion

	// region CROPS
	public static ItemStack cropOnion;

	public static ItemStack cropBellPepper;
	public static ItemStack cropGreenBean;
	public static ItemStack cropSpinach;
	public static ItemStack cropTomato;

	public static ItemStack cropCorn;
	public static ItemStack cropRice;

	public static ItemStack cropStrawberry;
	// endregion

	// region SEEDS

	// endregion

	// region TOOLS
	public static ItemWateringCan itemWateringCanBasic;

	public static ItemStack toolWateringCanBasic;
	// endregion
}
