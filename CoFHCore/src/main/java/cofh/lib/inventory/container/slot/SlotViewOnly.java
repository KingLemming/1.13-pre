package cofh.lib.inventory.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Slot which displays an item.
 */
public class SlotViewOnly extends SlotCoFH {

	public SlotViewOnly(IInventory inventoryIn, int index, int xPosition, int yPosition) {

		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return false;
	}

	@Override
	public boolean isHere(IInventory inventory, int slot) {

		return false;
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {

		return false;
	}

}
