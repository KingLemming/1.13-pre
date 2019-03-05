package cofh.thermal.expansion.gui.container.dynamo;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.inventory.InvWrapper;
import cofh.lib.gui.slot.SlotCoFH;
import cofh.thermal.expansion.block.dynamo.TileDynamoSteam;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerDynamoSteam extends ContainerTileAugmentable {

	public TileDynamoSteam tile;

	public ContainerDynamoSteam(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileDynamoSteam) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 44, 35));

		bindPlayerInventory(inventory);
	}

}
