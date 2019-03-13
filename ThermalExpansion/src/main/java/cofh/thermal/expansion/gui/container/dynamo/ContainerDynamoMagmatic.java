package cofh.thermal.expansion.gui.container.dynamo;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.thermal.core.block.dynamo.TileDynamo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerDynamoMagmatic extends ContainerTileAugmentable {

	public TileDynamo tile;

	public ContainerDynamoMagmatic(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileDynamo) tile;

		bindPlayerInventory(inventory);
	}

}
