package cofh.enstoragement.block;

import net.minecraft.item.ItemStack;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;

public class InvWrapperStrongboxCreative extends InvWrapperStrongbox {

	public InvWrapperStrongboxCreative(TileStrongbox strongbox) {

		super(strongbox);
	}

	// region IInventory
	@Override
	public ItemStack decrStackSize(int slot, int amount) {

		return cloneStack(inventory.get(slot), amount);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {

		return cloneStack(inventory.get(slot));
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {

		if (stack.isEmpty()) {
			return;
		}
		stack.setCount(stack.getMaxStackSize());
		inventory.set(slot, stack);
	}
	// endregion
}
