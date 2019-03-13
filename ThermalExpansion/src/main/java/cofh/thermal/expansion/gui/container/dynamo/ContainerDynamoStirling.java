package cofh.thermal.expansion.gui.container.dynamo;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.gui.slot.SlotCoFH;
import cofh.lib.inventory.InvWrapper;
import cofh.thermal.core.block.dynamo.TileDynamo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerDynamoStirling extends ContainerTileAugmentable {

	public TileDynamo tile;

	public ContainerDynamoStirling(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileDynamo) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 44, 35));

		bindPlayerInventory(inventory);
	}

}
