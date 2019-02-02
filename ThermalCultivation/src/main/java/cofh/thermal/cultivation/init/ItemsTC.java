package cofh.thermal.cultivation.init;

import cofh.thermal.core.init.CreativeTabsTSeries;
import cofh.thermal.cultivation.item.ItemWateringCan;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.ThermalSeries.registerItem;

public class ItemsTC {

	private ItemsTC() {

	}

	// region REGISTRATION
	public static void registerItems() {

		itemWateringCanBasic = new ItemWateringCan(4000, 1, 40);

		toolWateringCanBasic = registerItem("watering_can_basic", itemWateringCanBasic, CreativeTabsTSeries.tabTools);
	}
	// endregion

	// region REFERENCES
	public static ItemWateringCan itemWateringCanBasic;

	public static ItemStack toolWateringCanBasic;
	// endregion
}
