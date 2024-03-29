package cofh.core.gui.container;

import cofh.lib.inventory.container.slot.SlotFalseCopy;
import cofh.lib.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ContainerCoFH extends Container {

	protected String name = "";
	protected boolean advClickLogic = true;

	public ContainerCoFH() {

	}

	// region HELPERS
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {

		int xOffset = getPlayerInventoryHorizontalOffset();
		int yOffset = getPlayerInventoryVerticalOffset();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
		}
	}

	protected int getPlayerInventoryHorizontalOffset() {

		return 8;
	}

	protected abstract int getPlayerInventoryVerticalOffset();

	protected abstract int getSizeInventory();

	protected boolean supportsShiftClick(EntityPlayer player, int slotIndex) {

		return supportsShiftClick(slotIndex);
	}

	protected boolean supportsShiftClick(int slotIndex) {

		return true;
	}

	protected boolean performMerge(int slotIndex, ItemStack stack) {

		int invBase = getSizeInventory();
		int invFull = inventorySlots.size();

		if (slotIndex < invBase) {
			return mergeItemStack(stack, invBase, invFull, true);
		}
		return mergeItemStack(stack, 0, invBase, false);
	}
	// endregion

	// region OVERRIDES
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		if (!supportsShiftClick(player, slotIndex)) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			if (!performMerge(slotIndex, stackInSlot)) {
				return ItemStack.EMPTY;
			}
			slot.onSlotChange(stackInSlot, stack);

			if (stackInSlot.getCount() <= 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.putStack(stackInSlot);
			}
			if (stackInSlot.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, stackInSlot);
		}
		return stack;
	}

	@Override
	public ItemStack slotClick(int slotId, int mouseButton, ClickType modifier, EntityPlayer player) {

		if (!advClickLogic) {
			return super.slotClick(slotId, mouseButton, modifier, player);
		}
		Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
		if (slot instanceof SlotFalseCopy) {
			if (mouseButton == 2) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.putStack(player.inventory.getItemStack().isEmpty() ? ItemStack.EMPTY : player.inventory.getItemStack().copy());
			}
			return player.inventory.getItemStack();
		}
		return super.slotClick(slotId, mouseButton, modifier, player);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void setAll(List<ItemStack> stacks) {

		for (int i = 0; i < stacks.size(); ++i) {
			putStackInSlot(i, stacks.get(i));
		}
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {

		return InventoryHelper.mergeItemStack(this.inventorySlots, stack, startIndex, endIndex, reverseDirection);
	}
	// endregion
}
