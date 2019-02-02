package cofh.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public class ItemStorageCoFH implements IItemHandler, IItemStackHolder {

	private final Predicate<ItemStack> validator;

	protected ItemStack item = ItemStack.EMPTY;

	public ItemStorageCoFH() {

		this(e -> true);
	}

	public ItemStorageCoFH(Predicate<ItemStack> validator) {

		this.validator = validator;
	}

	public boolean isItemValid(@Nonnull ItemStack stack) {

		return validator.test(stack);
	}

	// region NBT
	public ItemStorageCoFH readFromNBT(NBTTagCompound nbt) {

		item = new ItemStack(nbt);
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		item.writeToNBT(nbt);
		return nbt;
	}
	// endregion

	// region IItemStackHolder
	@Override
	public void modify(int quantity) {

		this.item.grow(quantity);

		if (this.item.isEmpty()) {
			this.item = ItemStack.EMPTY;
		}
	}

	@Override
	public void setItemStack(ItemStack item) {

		this.item = item;
	}

	@Override
	public ItemStack getItemStack() {

		return item;
	}

	@Override
	public boolean isEmpty() {

		return item.isEmpty();
	}
	// endregion

	// region IItemHandler
	@Override
	public int getSlots() {

		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {

		return item;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		if (item.isEmpty()) {
			if (!simulate) {
				setItemStack(stack);
			}
			return ItemStack.EMPTY;
		} else if (itemsIdentical(item, stack)) {
			int totalCount = item.getCount() + stack.getCount();
			int limit = getSlotLimit(0);
			if (totalCount <= limit) {
				if (!simulate) {
					item.setCount(totalCount);
				}
				return ItemStack.EMPTY;
			}
			if (!simulate) {
				item.setCount(limit);
			}
			return cloneStack(stack, totalCount - limit);
		}
		return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {

		if (amount <= 0 || item.isEmpty()) {
			return ItemStack.EMPTY;
		}
		int retCount = Math.min(item.getCount(), amount);
		ItemStack ret = cloneStack(item, retCount);
		if (!simulate) {
			item.shrink(retCount);
			if (item.isEmpty()) {
				setItemStack(ItemStack.EMPTY);
			}
		}
		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {

		return item.getMaxStackSize();
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

		return isItemValid(stack);
	}
	// endregion
}
