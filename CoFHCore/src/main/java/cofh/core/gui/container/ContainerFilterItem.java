package cofh.core.gui.container;

import cofh.core.network.packet.server.PacketFilter;
import cofh.core.network.packet.server.PacketSecurity;
import cofh.core.util.CoreUtils;
import cofh.lib.gui.slot.SlotFalseCopy;
import cofh.lib.gui.slot.SlotLocked;
import cofh.lib.item.IFilterContainerItem;
import cofh.lib.util.IFilterable;
import cofh.lib.util.control.ISecurable;
import cofh.lib.util.filter.ItemFilterWrapper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.SecurityHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.helpers.StringHelper.localize;

public class ContainerFilterItem extends ContainerCoFH implements IFilterable, ISecurable {

	public static final String NAME = "info.cofh.filter";

	protected final ItemFilterWrapper containerWrapper;
	protected final EntityPlayer player;
	protected final int containerIndex;
	protected boolean valid = true;

	public int slots;
	public int rowSize;
	public int rows;

	public ContainerFilterItem(ItemStack stack, InventoryPlayer inventory) {

		IFilterContainerItem filterItem = (IFilterContainerItem) stack.getItem();
		slots = MathHelper.clamp(filterItem.getSizeFilter(stack), 3, 15);
		rowSize = MathHelper.clamp(slots / 3, 3, 5);
		rows = MathHelper.clamp(slots / rowSize, 1, 3);

		player = inventory.player;
		containerIndex = inventory.currentItem;
		containerWrapper = new ItemFilterWrapper(stack, slots);
		name = NAME;

		int xOffset = 62 - 9 * rowSize;
		int yOffset = 44 - 9 * rows;

		for (int i = 0; i < slots; i++) {
			int slot = i;
			addSlotToContainer(new SlotFalseCopy(containerWrapper, i, xOffset + i % rowSize * 18, yOffset + i / rowSize * 18) {
				@Override
				public boolean isItemValid(ItemStack stack) {

					return containerWrapper.isItemValidForSlot(slot, stack);
				}
			});
		}
		bindPlayerInventory(inventory);
	}

	@Override
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {

		int xOffset = getPlayerInventoryHorizontalOffset();
		int yOffset = getPlayerInventoryVerticalOffset();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			if (i == inventoryPlayer.currentItem) {
				addSlotToContainer(new SlotLocked(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			} else {
				addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			}
		}
	}

	@Override
	protected int getPlayerInventoryVerticalOffset() {

		return 84;
	}

	@Override
	protected int getSizeInventory() {

		return 0;
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

	// region IFilterable
	@Override
	public void setFlag(int flag, boolean value) {

		containerWrapper.getFilter().setFlag(flag, value);
		if (CoreUtils.isClient()) {
			PacketFilter.sendToServer(flag, value);
		}
		containerWrapper.markDirty();
	}

	public boolean getFlag(int flag) {

		return containerWrapper.getFilter().getFlag(flag);
	}
	// endregion

	// region ISecurable
	@Override
	public void setAccess(AccessMode access) {

		SecurityHelper.setAccess(getContainerStack(), access);
		onSlotChanged();
		if (CoreUtils.isClient()) {
			PacketSecurity.sendToServer(getAccess());
		}
	}

	@Override
	public void setOwner(GameProfile profile) {

	}

	@Override
	public AccessMode getAccess() {

		return SecurityHelper.getAccess(getContainerStack());
	}

	@Override
	public String getOwnerName() {

		return SecurityHelper.getOwnerName(getContainerStack());
	}

	@Override
	public GameProfile getOwner() {

		return SecurityHelper.getOwner(getContainerStack());
	}
	// endregion
}
