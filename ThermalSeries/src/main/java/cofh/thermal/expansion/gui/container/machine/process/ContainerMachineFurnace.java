package cofh.thermal.expansion.gui.container.machine.process;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.gui.slot.SlotCoFH;
import cofh.lib.inventory.InvWrapper;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineFurnace extends ContainerTileAugmentable {

	protected TileMachine tile;

	public ContainerMachineFurnace(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachine) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 53, 26));
		addSlotToContainer(new SlotFurnaceOutput(inventory.player, tileInv, 1, 116, 35));
		// addSlotToContainer(new SlotEnergy(myTile, myTile.getChargeSlot(), 8, 53));

		bindPlayerInventory(inventory);
	}

}
