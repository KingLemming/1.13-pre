package cofh.thermal.expansion.gui.container.dynamo;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.thermal.expansion.block.dynamo.TileDynamoCompression;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerDynamoCompression extends ContainerTileAugmentable {

	public TileDynamoCompression tile;

	public ContainerDynamoCompression(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileDynamoCompression) tile;

		bindPlayerInventory(inventory);
	}

}
