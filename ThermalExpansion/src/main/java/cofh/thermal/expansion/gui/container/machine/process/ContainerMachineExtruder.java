package cofh.thermal.expansion.gui.container.machine.process;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.lib.gui.slot.SlotViewOnly;
import cofh.lib.inventory.InvWrapper;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineExtruder extends ContainerTileAugmentable {

	protected TileMachine tile;

	public ContainerMachineExtruder(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachine) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotRemoveOnly(tileInv, 0, 134, 26));
		addSlotToContainer(new SlotViewOnly(tileInv, 1, 89, 53));
		// addSlotToContainer(new SlotEnergy(myTile, myTile.getChargeSlot(), 8, 53));

		bindPlayerInventory(inventory);
	}

}
