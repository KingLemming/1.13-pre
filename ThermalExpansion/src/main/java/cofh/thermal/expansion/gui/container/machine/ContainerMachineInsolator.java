package cofh.thermal.expansion.gui.container.machine;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.gui.slot.SlotCoFH;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.lib.inventory.InvWrapper;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineInsolator extends ContainerTileAugmentable {

	public TileMachine tile;

	public ContainerMachineInsolator(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachine) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 62, 17));
		addSlotToContainer(new SlotCoFH(tileInv, 1, 62, 53));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 2, 116, 17));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 3, 134, 17));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 4, 116, 35));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 5, 134, 35));
		// addSlotToContainer(new SlotEnergy(myTile, myTile.getChargeSlot(), 8, 53));

		bindPlayerInventory(inventory);
	}

}
