package cofh.enstoragement.gui.container;

import cofh.core.gui.container.ContainerCoFH;
import cofh.enstoragement.block.InvWrapperStrongbox;
import cofh.enstoragement.block.TileStrongbox;
import cofh.lib.inventory.container.slot.SlotCoFH;
import cofh.lib.util.helpers.MathHelper;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ChestContainer.RowSizeCallback;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;

import java.util.List;
import java.util.Map;

@ChestContainer ()
public class ContainerStrongbox extends ContainerCoFH {

	public static final String NAME = "item.enstoragement.strongbox.name";

	public int slots;
	public int rowSize;
	public int rows;
	TileStrongbox tile;
	IInventory tileInv;

	public ContainerStrongbox(InventoryPlayer inventory, TileEntity tile) {

		this.tile = (TileStrongbox) tile;
		this.tileInv = new InvWrapperStrongbox((TileStrongbox) tile);

		slots = tileInv.getSizeInventory();
		rowSize = MathHelper.clamp(slots, 1, 9);
		rows = MathHelper.clamp(slots / rowSize, 1, 9);
		int yOffset = rows == 1 ? 26 : 17;

		bindPlayerInventory(inventory);

		for (int i = 0; i < slots; i++) {
			addSlotToContainer(new SlotCoFH(tileInv, i, 8 + i % rowSize * 18, yOffset + i / rowSize * 18));
		}
	}

	@Override
	protected int getPlayerInventoryVerticalOffset() {

		return 30 + 18 * MathHelper.clamp(rows, 2, 9);
	}

	@Override
	protected int getSizeInventory() {

		return tileInv.getSizeInventory();
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {

		return tile.playerWithinDistance(playerIn, 64D);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {

		super.onContainerClosed(player);
		tileInv.closeInventory(player);
	}

	public TileEntity getTile() {

		return tile;
	}

	// region INVENTORY TWEAKS
	@ContainerSectionCallback
	@Optional.Method (modid = "inventorytweaks")
	public Map<ContainerSection, List<Slot>> getContainerSections() {

		Map<ContainerSection, List<Slot>> slotRefs = new Object2ObjectOpenHashMap<>();

		slotRefs.put(ContainerSection.INVENTORY, inventorySlots.subList(0, 36));
		slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(0, 27));
		slotRefs.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(27, 36));
		slotRefs.put(ContainerSection.CHEST, inventorySlots.subList(36, inventorySlots.size()));

		return slotRefs;
	}

	@RowSizeCallback
	@Optional.Method (modid = "inventorytweaks")
	public int getRowSize() {

		return rowSize;
	}
	// endregion
}
