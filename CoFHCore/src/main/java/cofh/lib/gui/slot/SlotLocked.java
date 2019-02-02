package cofh.lib.gui.slot;

import cofh.lib.inventory.SlotCoFH;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * A slot that can only be used to display an item and disallows editing.
 */
public class SlotLocked extends SlotCoFH {

	public SlotLocked(IInventory inventoryIn, int index, int xPosition, int yPosition) {

		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {

		return false;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return false;
	}

}
