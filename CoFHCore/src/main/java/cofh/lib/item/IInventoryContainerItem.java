package cofh.lib.item;

import net.minecraft.item.ItemStack;

/**
 * Implement this interface on Item classes that are themselves inventories.
 *
 * @author King Lemming
 */
public interface IInventoryContainerItem {

	/**
	 * Get the size of this inventory of this container item.
	 */
	int getSizeInventory(ItemStack container);

	/**
	 * Get the stacksize limit for items in this inventory.
	 */
	default int getInventoryStackLimit(ItemStack container) {

		return 64;
	}

}
