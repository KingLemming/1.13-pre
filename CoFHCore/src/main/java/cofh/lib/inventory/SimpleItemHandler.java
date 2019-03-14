package cofh.lib.inventory;

import cofh.lib.block.ITileCallback;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Basic Item Handler implementation using CoFH Item Storage objects.
 */
public class SimpleItemHandler implements IItemHandler {

	protected ITileCallback tile;
	protected List<ItemStorageCoFH> slots;

	public SimpleItemHandler(@Nullable ITileCallback tile, @Nonnull List<ItemStorageCoFH> slots) {

		this.tile = tile;
		this.slots = slots;
	}

	public boolean hasSlots() {

		return slots.size() > 0;
	}

	public boolean isEmpty() {

		for (ItemStorageCoFH slot : slots) {
			if (!slot.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	// region IItemHandler
	@Override
	public int getSlots() {

		return slots.size();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {

		if (slot < 0 || slot > getSlots()) {
			return ItemStack.EMPTY;
		}
		return slots.get(slot).getItemStack();
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

		if (slot < 0 || slot > getSlots()) {
			return stack;
		}
		ItemStack ret = slots.get(slot).insertItem(slot, stack, simulate);

		if (tile != null && !simulate) {
			tile.onInventoryChange(slot);
		}
		return ret;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {

		if (slot < 0 || slot > getSlots()) {
			return ItemStack.EMPTY;
		}
		ItemStack ret = slots.get(slot).extractItem(slot, amount, simulate);

		if (tile != null && !simulate) {
			tile.onInventoryChange(slot);
		}
		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {

		return slots.get(slot).getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

		return slots.get(slot).isItemValid(slot, stack);
	}
	// endregion
}
