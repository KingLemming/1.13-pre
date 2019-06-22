package cofh.thermal.expansion.gui.container.machine;

import cofh.core.gui.container.ContainerTileAugmentable;
import cofh.lib.inventory.container.slot.SlotCoFH;
import cofh.lib.inventory.container.slot.SlotRemoveOnly;
import cofh.lib.inventory.InvWrapper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.block.machine.TileMachineLexicon;
import cofh.thermal.expansion.gui.slot.SlotLexicon;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineLexicon extends ContainerTileAugmentable {

	protected TileMachine tile;

	public ContainerMachineLexicon(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);
		this.tile = (TileMachine) tile;
		IInventory tileInv = new InvWrapper(this.tile.getInventory());

		addSlotToContainer(new SlotCoFH(tileInv, 0, 17, 35));
		addSlotToContainer(new SlotRemoveOnly(tileInv, 1, 143, 35));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 5; j++) {
				addSlotToContainer(new SlotLexicon((TileMachineLexicon) this.tile, tileInv, 2 + j + i * 5, 44 + j * 18, 17 + i * 18));
			}
		}
		bindPlayerInventory(inventory);
	}

}
