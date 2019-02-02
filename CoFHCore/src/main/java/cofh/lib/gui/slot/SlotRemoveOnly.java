package cofh.lib.gui.slot;

import cofh.lib.inventory.SlotCoFH;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Slot which players can only remove items from.
 */
public class SlotRemoveOnly extends SlotCoFH {

	public SlotRemoveOnly(IInventory inventoryIn, int index, int xPosition, int yPosition) {

		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return false;
	}

}
