package cofh.lib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InvWrapper implements IInventory {

	protected InventoryCoFH inventory;

	public InvWrapper(InventoryCoFH inventory) {

		this.inventory = inventory;
	}

	// region IInventory
	@Override
	public int getSizeInventory() {

		return inventory.getSlots();
	}

	@Override
	public boolean isEmpty() {

		return inventory.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {

		return inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {

		ItemStack inSlot = inventory.get(index);
		if (inSlot.isEmpty()) {
			return ItemStack.EMPTY;
		}
		if (inSlot.getCount() <= count) {
			count = inSlot.getCount();
		}
		ItemStack stack = inSlot.splitStack(count);
		if (inSlot.getCount() <= 0) {
			inventory.set(index, ItemStack.EMPTY);
		}
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {

		ItemStack stack = inventory.get(index);
		inventory.set(index, ItemStack.EMPTY);
		inventory.tile.onInventoryChange(index);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		inventory.set(index, stack);
		inventory.tile.onInventoryChange(index);
	}

	@Override
	public int getInventoryStackLimit() {

		return 64;
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {

		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {

		return inventory.isItemValid(index, stack);
	}

	@Override
	public int getField(int id) {

		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {

		return 0;
	}

	@Override
	public void clear() {

		inventory.clear();
	}
	// endregion

	// region IWorldNameable
	@Override
	public String getName() {

		return null;
	}

	@Override
	public boolean hasCustomName() {

		return false;
	}

	@Override
	public ITextComponent getDisplayName() {

		return new TextComponentString(getName());
	}
	// endregion
}
