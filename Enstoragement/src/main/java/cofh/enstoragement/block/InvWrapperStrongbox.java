package cofh.enstoragement.block;

import cofh.lib.inventory.InvWrapper;
import cofh.lib.item.IInventoryContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InvWrapperStrongbox extends InvWrapper {

	protected TileStrongbox strongbox;

	public InvWrapperStrongbox(TileStrongbox strongbox) {

		super(strongbox.getInventory());
		this.strongbox = strongbox;
	}

	// region IInventory
	@Override
	public void openInventory(EntityPlayer player) {

		if (player.isSpectator()) {
			return;
		}
		if (strongbox.numUsingPlayers < 0) {
			strongbox.numUsingPlayers = 0;
		}
		++strongbox.numUsingPlayers;
		strongbox.getWorld().addBlockEvent(strongbox.getPos(), strongbox.getBlockType(), 1, strongbox.numUsingPlayers);
		strongbox.callNeighborStateChange();
	}

	@Override
	public void closeInventory(EntityPlayer player) {

		if (player.isSpectator()) {
			return;
		}
		if (strongbox.getBlockType() instanceof BlockStrongbox) {
			--strongbox.numUsingPlayers;
			strongbox.getWorld().addBlockEvent(strongbox.getPos(), strongbox.getBlockType(), 1, strongbox.numUsingPlayers);
			strongbox.callNeighborStateChange();
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {

		return stack.isEmpty() || !(stack.getItem() instanceof IInventoryContainerItem) || ((IInventoryContainerItem) stack.getItem()).getSizeInventory(stack) <= 0;
	}
	// endregion
}
