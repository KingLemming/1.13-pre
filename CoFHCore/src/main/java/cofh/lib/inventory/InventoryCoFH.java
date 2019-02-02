package cofh.lib.inventory;

import cofh.lib.block.ITileCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.TAG_INVENTORY;
import static cofh.lib.util.Constants.TAG_SLOT;

public class InventoryCoFH extends SimpleItemHandler {

	protected String tag;

	public InventoryCoFH(@Nullable ITileCallback tile) {

		this(tile, 0, TAG_INVENTORY);
	}

	public InventoryCoFH(@Nullable ITileCallback tile, int size) {

		this(tile, size, TAG_INVENTORY);
	}

	public InventoryCoFH(@Nullable ITileCallback tile, @Nonnull String tag) {

		this(tile, 0, tag);
	}

	public InventoryCoFH(@Nullable ITileCallback tile, @Nonnull List<ItemStorageCoFH> slots, @Nonnull String tag) {

		super(tile, slots);
		this.tag = tag;
	}

	public InventoryCoFH(@Nullable ITileCallback tile, int size, @Nonnull String tag) {

		super(tile, new ArrayList<>(size));
		this.tile = tile;
		this.tag = tag;
		for (int i = 0; i < size; i++) {
			slots.add(new ItemStorageCoFH());
		}
	}

	public void clear() {

		for (ItemStorageCoFH slot : slots) {
			slot.setItemStack(ItemStack.EMPTY);
		}
	}

	public void set(int slot, ItemStack stack) {

		slots.get(slot).setItemStack(stack);
	}

	public ItemStack get(int slot) {

		return slots.get(slot).getItemStack();
	}

	// region NBT
	public InventoryCoFH readFromNBT(NBTTagCompound nbt) {

		for (ItemStorageCoFH slot : slots) {
			slot.setItemStack(ItemStack.EMPTY);
		}
		NBTTagList list = nbt.getTagList(tag, 10);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			int slot = tag.getInteger(TAG_SLOT);
			if (slot >= 0 && slot < slots.size()) {
				slots.get(slot).readFromNBT(tag);
			}
		}
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		if (slots.size() <= 0) {
			return nbt;
		}
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < slots.size(); i++) {
			if (!slots.get(i).isEmpty()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger(TAG_SLOT, i);
				slots.get(i).writeToNBT(tag);
				list.appendTag(tag);
			}
		}
		if (list.tagCount() > 0) {
			nbt.setTag(tag, list);
		}
		return nbt;
	}
	// endregion
}
