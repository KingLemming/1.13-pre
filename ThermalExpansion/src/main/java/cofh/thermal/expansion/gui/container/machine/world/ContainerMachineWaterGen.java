package cofh.thermal.expansion.gui.container.machine.world;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineWaterGen extends ContainerTileAugmentable {

	protected TileMachine tile;

	public ContainerMachineWaterGen(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachine) tile;

		bindPlayerInventory(inventory);
	}

}
