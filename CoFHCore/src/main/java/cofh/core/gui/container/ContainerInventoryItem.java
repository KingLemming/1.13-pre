package cofh.core.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.helpers.StringHelper.localize;

public abstract class ContainerInventoryItem extends ContainerCoFH {

	protected final InventoryContainerItemWrapper containerWrapper;
	protected final EntityPlayer player;
	protected final int containerIndex;
	protected boolean valid = true;

	public ContainerInventoryItem(ItemStack stack, InventoryPlayer inventory) {

		player = inventory.player;
		containerIndex = inventory.currentItem;
		containerWrapper = new InventoryContainerItemWrapper(stack);

		advClickLogic = false;
	}

	@Override
	protected int getSizeInventory() {

		return containerWrapper.getSizeInventory();
	}

	@Override
	public void detectAndSendChanges() {

		ItemStack item = player.inventory.mainInventory.get(containerIndex);
		if (item.isEmpty() || item.getItem() != containerWrapper.getContainerItem()) {
			valid = false;
			return;
		}
		super.detectAndSendChanges();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {

		onSlotChanged();
		if (containerWrapper.isDirty() && !valid) {
			player.inventory.setItemStack(ItemStack.EMPTY);
		}
		return valid;
	}

	// region HELPERS
	public void onSlotChanged() {

		ItemStack item = player.inventory.mainInventory.get(containerIndex);
		if (valid && !item.isEmpty() && item.getItem() == containerWrapper.getContainerItem()) {
			player.inventory.mainInventory.set(containerIndex, containerWrapper.getContainerStack());
		}
	}

	public String getInventoryName() {

		return containerWrapper.hasCustomName() ? containerWrapper.getName() : localize(name);
	}

	public ItemStack getContainerStack() {

		return containerWrapper.getContainerStack();
	}
	// endregion
}
