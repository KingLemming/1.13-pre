package cofh.core.gui.container;

import cofh.core.block.TileCoFH;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.tileentity.TileEntity;

public class ContainerTileAugmentable extends ContainerCoFH {

	protected final TileCoFH baseTile;

	public ContainerTileAugmentable(InventoryPlayer inventory, TileEntity tile) {

		baseTile = tile instanceof TileCoFH ? (TileCoFH) tile : null;
	}

	@Override
	protected int getPlayerInventoryVerticalOffset() {

		return 84;
	}

	@Override
	protected int getSizeInventory() {

		return baseTile == null ? 0 : baseTile.invSize();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {

		return baseTile == null || baseTile.isUsableByPlayer(player);
	}

	@Override
	public void detectAndSendChanges() {

		super.detectAndSendChanges();
		if (baseTile == null) {
			return;
		}
		for (IContainerListener listener : listeners) {
			baseTile.sendGuiNetworkData(this, listener);
		}
	}

	@Override
	public void updateProgressBar(int i, int j) {

		if (baseTile == null) {
			return;
		}
		baseTile.receiveGuiNetworkData(i, j);
	}

}
