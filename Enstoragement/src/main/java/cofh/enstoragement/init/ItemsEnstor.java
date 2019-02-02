package cofh.enstoragement.init;

import cofh.enstoragement.item.ItemSatchel;
import net.minecraft.item.ItemStack;

import static cofh.enstoragement.Enstoragement.registerItem;

public class ItemsEnstor {

	private ItemsEnstor() {

	}

	// region REGISTRATION
	public static void registerItems() {

		itemSatchelLeather = new ItemSatchel(9, 3);
		itemSatchelIron = new ItemSatchel(18, 6);
		itemSatchelGold = new ItemSatchel(27, 9);
		itemSatchelDiamond = new ItemSatchel(36, 12);
		itemSatchelEmerald = new ItemSatchel(45, 15);

		itemSatchelCreative = (ItemSatchel) new ItemSatchel(1, 0).setCreative(true);

		satchelLeather = registerItem("satchel_leather", itemSatchelLeather);
		satchelIron = registerItem("satchel_iron", itemSatchelIron);
		satchelGold = registerItem("satchel_gold", itemSatchelGold);
		satchelDiamond = registerItem("satchel_diamond", itemSatchelDiamond);
		satchelEmerald = registerItem("satchel_emerald", itemSatchelEmerald);

		satchelCreative = registerItem("satchel_creative", itemSatchelCreative);
	}
	// endregion

	// region SATCHELS
	public static ItemSatchel itemSatchelLeather;
	public static ItemSatchel itemSatchelIron;
	public static ItemSatchel itemSatchelGold;
	public static ItemSatchel itemSatchelDiamond;
	public static ItemSatchel itemSatchelEmerald;

	public static ItemSatchel itemSatchelCreative;

	public static ItemStack satchelLeather;
	public static ItemStack satchelIron;
	public static ItemStack satchelGold;
	public static ItemStack satchelDiamond;
	public static ItemStack satchelEmerald;

	public static ItemStack satchelCreative;
	// endregion
}
