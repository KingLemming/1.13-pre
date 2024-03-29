package cofh.thermal.expansion.gui.container.machine.process;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.inventory.container.slot.SlotCoFH;
import cofh.lib.inventory.InvWrapper;
import cofh.thermal.expansion.block.machine.process.TileMachineCrucible;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineCrucible extends ContainerTileAugmentable {

	protected TileMachineCrucible tile;

	public ContainerMachineCrucible(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachineCrucible) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 53, 26));
		// addSlotToContainer(new SlotEnergy(myTile, myTile.getChargeSlot(), 8, 53));

		bindPlayerInventory(inventory);
	}

}
