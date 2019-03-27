package cofh.enstoragement.gui.container;

import cofh.core.gui.container.ContainerInventoryItem;
import cofh.core.network.packet.server.PacketSecurity;
import cofh.core.util.CoreUtils;
import cofh.enstoragement.item.ItemSatchel;
import cofh.lib.gui.slot.SlotLocked;
import cofh.lib.util.control.ISecurable;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.SecurityHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

// @ChestContainer
public class ContainerSatchel extends ContainerInventoryItem implements ISecurable {

	public static final String NAME = "gui.enstoragement.satchel";

	public int slots;
	public int rowSize;
	public int rows;

	public ContainerSatchel(ItemStack stack, InventoryPlayer inventory) {

		super(stack, inventory);

		ItemSatchel satchelItem = (ItemSatchel) stack.getItem();

		name = NAME;
		slots = satchelItem.getSizeInventory(stack);
		rowSize = MathHelper.clamp(slots, 1, 9);
		rows = MathHelper.clamp(slots / rowSize, 1, 9);

		if (satchelItem.isCreative()) {
			bindCreativeInventory();
		} else {
			bindStandardInventory();
		}
		bindPlayerInventory(inventory);
	}

	protected void bindCreativeInventory() {

		addSlotToContainer(new Slot(containerWrapper, 0, 80, 26) {
			@Override
			public boolean isItemValid(ItemStack stack) {

				return containerWrapper.isItemValidForSlot(0, stack);
			}

			@Override
			public void putStack(ItemStack stack) {

				if (stack.isEmpty()) {
					return;
				}
				stack.setCount(stack.getMaxStackSize());
				inventory.setInventorySlotContents(this.getSlotIndex(), stack);
				onSlotChanged();
			}

			@Override
			public ItemStack decrStackSize(int amount) {

				return inventory.getStackInSlot(this.getSlotIndex()).copy();
			}
		});
	}

	protected void bindStandardInventory() {

		int yOffset = rows == 1 ? 26 : 17;

		for (int i = 0; i < slots; i++) {
			int slot = i;
			addSlotToContainer(new Slot(containerWrapper, i, 8 + i % rowSize * 18, yOffset + i / rowSize * 18) {
				@Override
				public boolean isItemValid(ItemStack stack) {

					return containerWrapper.isItemValidForSlot(slot, stack);
				}
			});
		}
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

		return 30 + 18 * MathHelper.clamp(rows, 2, 9);
	}

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
	public boolean setOwner(GameProfile profile) {

		return false;
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

	// region INVENTORY TWEAKS
	//	@ContainerSectionCallback
	//	@Optional.Method (modid = "inventorytweaks")
	//	public Map<ContainerSection, List<Slot>> getContainerSections() {
	//
	//		Map<ContainerSection, List<Slot>> slotRefs = new Object2ObjectOpenHashMap<>();
	//
	//		slotRefs.put(ContainerSection.INVENTORY, inventorySlots.subList(slots, 36 + slots));
	//		slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(slots, 27 + slots));
	//		slotRefs.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(27 + slots, inventorySlots.size()));
	//		slotRefs.put(ContainerSection.CHEST, inventorySlots.subList(0, slots));
	//
	//		return slotRefs;
	//	}
	//
	//	@RowSizeCallback
	//	@Optional.Method (modid = "inventorytweaks")
	//	public int getRowSize() {
	//
	//		return rowSize;
	//	}
	// endregion
}
