package cofh.thermal.expansion.gui.slot;

import cofh.lib.gui.slot.SlotFalseCopy;
import cofh.thermal.core.util.managers.LexiconManager;
import cofh.thermal.expansion.block.machine.TileMachineLexicon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotLexicon extends SlotFalseCopy {

	protected TileMachineLexicon tile;

	public SlotLexicon(TileMachineLexicon tile, IInventory inventoryIn, int index, int xPosition, int yPosition) {

		super(inventoryIn, index, xPosition, yPosition);
		this.tile = tile;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return stack == ItemStack.EMPTY || LexiconManager.validOre(stack) && !tile.hasPreferredStack(stack);
	}

	@Override
	public void onSlotChanged() {

		tile.updatePreferredStacks();
		tile.markDirty();
	}

}
