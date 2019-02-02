package cofh.lib.inventory;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IItemStackHolder {

	void modify(int quantity);

	void setItemStack(@Nonnull ItemStack stack);

	@Nonnull
	ItemStack getItemStack();

	boolean isEmpty();

}
