package cofh.thermal.expansion.gui.container.dynamo;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.thermal.expansion.block.dynamo.TileDynamoMagmatic;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerDynamoMagmatic extends ContainerTileAugmentable {

	public TileDynamoMagmatic tile;

	public ContainerDynamoMagmatic(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileDynamoMagmatic) tile;

		bindPlayerInventory(inventory);
	}

}
