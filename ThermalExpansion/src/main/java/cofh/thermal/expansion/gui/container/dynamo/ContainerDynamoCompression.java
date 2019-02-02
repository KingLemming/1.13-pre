package cofh.thermal.expansion.gui.container.dynamo;

import cofh.core.gui.container.ContainerTileAugmentable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerDynamoCompression extends ContainerTileAugmentable {

	// public TileDynamoSteam tile;

	public ContainerDynamoCompression(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);

		bindPlayerInventory(inventory);
	}
}
