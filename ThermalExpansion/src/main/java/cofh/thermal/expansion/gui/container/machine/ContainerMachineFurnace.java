package cofh.thermal.expansion.gui.container.machine;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.inventory.InvWrapper;
import cofh.lib.gui.slot.SlotCoFH;
import cofh.thermal.expansion.block.machine.TileMachineFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineFurnace extends ContainerTileAugmentable {

	protected TileMachineFurnace tile;

	public ContainerMachineFurnace(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachineFurnace) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 53, 26));
		addSlotToContainer(new SlotFurnaceOutput(inventory.player, tileInv, 1, 116, 35));
		// addSlotToContainer(new SlotEnergy(myTile, myTile.getChargeSlot(), 8, 53));

		bindPlayerInventory(inventory);
	}

}
