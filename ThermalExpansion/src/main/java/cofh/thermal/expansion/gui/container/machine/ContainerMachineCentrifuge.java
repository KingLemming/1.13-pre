package cofh.thermal.expansion.gui.container.machine;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.gui.slot.SlotCoFH;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.lib.inventory.InvWrapper;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineCentrifuge extends ContainerTileAugmentable {

	public TileMachine tile;

	public ContainerMachineCentrifuge(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachine) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 44, 26));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 1, 107, 26));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 2, 125, 26));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 3, 107, 44));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 4, 125, 44));
		// addSlotToContainer(new SlotEnergy(myTile, myTile.getChargeSlot(), 8, 53));

		bindPlayerInventory(inventory);
	}

}
