package cofh.core.gui.container;

import cofh.lib.item.IInventoryContainerItem;
import cofh.lib.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;

import static cofh.lib.util.Constants.TAG_INVENTORY;
import static cofh.lib.util.Constants.TAG_SLOT;

public class InventoryContainerItemWrapper implements IInventory {

	protected final IInventoryContainerItem inventoryItem;
	protected final ItemStack inventoryStack;
	protected final int maxStackSize;

	protected NBTTagCompound tag;
	protected ItemStack[] inventory;
	protected boolean dirty = false;

	public InventoryContainerItemWrapper(ItemStack stack) {

		inventoryStack = stack;
		inventoryItem = (IInventoryContainerItem) inventoryStack.getItem();
		maxStackSize = inventoryItem.getInventoryStackLimit(inventoryStack);

		inventory = new ItemStack[getSizeInventory()];
		Arrays.fill(inventory, ItemStack.EMPTY);

		loadInventory();
		markDirty();
	}

	protected void loadInventory() {

		if (!inventoryStack.hasTagCompound() || !inventoryStack.getTagCompound().hasKey(TAG_INVENTORY)) {
			inventoryStack.setTagInfo(TAG_INVENTORY, new NBTTagCompound());
		}
		tag = inventoryStack.getTagCompound().getCompoundTag(TAG_INVENTORY);
		loadStacks();
	}

	protected void loadStacks() {

		for (int i = inventory.length; i-- > 0; ) {
			if (tag.hasKey(TAG_SLOT + i)) {
				inventory[i] = new ItemStack(tag.getCompoundTag(TAG_SLOT + i));
			} else {
				inventory[i] = ItemStack.EMPTY;
			}
		}
	}

	protected void saveStacks() {

		for (int i = inventory.length; i-- > 0; ) {
			if (inventory[i].isEmpty()) {
				tag.removeTag(TAG_SLOT + i);
			} else {
				tag.setTag(TAG_SLOT + i, inventory[i].writeToNBT(new NBTTagCompound()));
			}
		}
		inventoryStack.setTagInfo(TAG_INVENTORY, tag);
	}

	public boolean isDirty() {

		boolean r = dirty;
		dirty = false;
		return r;
	}

	public Item getContainerItem() {

		return inventoryStack.getItem();
	}

	public ItemStack getContainerStack() {

		saveStacks();
		return inventoryStack;
	}

	// region IWorldNameable
	@Override
	public String getName() {

		return inventoryStack.getDisplayName();
	}

	@Override
	public boolean hasCustomName() {

		return inventoryStack.hasDisplayName();
	}

	@Override
	public ITextComponent getDisplayName() {

		return new TextComponentString(inventoryStack.getDisplayName());
	}
	// endregion

	// region IInventory
	@Override
	public int getSizeInventory() {

		return inventoryItem.getSizeInventory(inventoryStack);
	}

	@Override
	public boolean isEmpty() {

		return InventoryHelper.isEmpty(inventory);
	}

	@Override
	public ItemStack getStackInSlot(int i) {

		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {

		ItemStack s = inventory[i];
		if (s.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack r = s.splitStack(j);
		if (s.getCount() <= 0) {
			inventory[i] = ItemStack.EMPTY;
			r.grow(s.getCount());
		}
		return r;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {

		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {

		inventory[i] = itemstack;
	}

	@Override
	public int getInventoryStackLimit() {

		return maxStackSize;
	}

	@Override
	public void markDirty() {

		saveStacks();
		dirty = true;
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

		markDirty();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {

		return stack.isEmpty() || !(stack.getItem() instanceof IInventoryContainerItem) || ((IInventoryContainerItem) stack.getItem()).getSizeInventory(stack) <= 0;
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

	}
	// endregion
}
