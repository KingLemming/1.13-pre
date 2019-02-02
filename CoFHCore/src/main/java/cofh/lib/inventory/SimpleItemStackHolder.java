package cofh.lib.inventory;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SimpleItemStackHolder implements IItemStackHolder {

	protected ItemStack stack;

	public SimpleItemStackHolder(ItemStack stack) {

		this.stack = stack;
	}

	@Override
	public void modify(int quantity) {

		// This should NEVER, EVER be called.
	}

	@Override
	public void setItemStack(ItemStack stack) {

		// This should NEVER, EVER be called.
	}

	@Nonnull
	@Override
	public ItemStack getItemStack() {

		return stack;
	}

	@Override
	public boolean isEmpty() {

		return stack.isEmpty();
	}

}
