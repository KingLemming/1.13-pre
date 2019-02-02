package cofh.lib.util.helpers;

import cofh.lib.item.IMultiModeItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Arrays;

public class ItemHelper {

	private ItemHelper() {

	}

	/**
	 * This prevents an overridden getDamage() call from messing up metadata acquisition.
	 */
	public static int getItemDamage(ItemStack stack) {

		return Items.DIAMOND.getDamage(stack);
	}

	// region CLONESTACK
	public static ItemStack cloneStack(Item item) {

		return cloneStack(item, 1);
	}

	public static ItemStack cloneStack(Block block) {

		return cloneStack(block, 1);
	}

	public static ItemStack cloneStack(Item item, int stackSize) {

		if (item == null) {
			return ItemStack.EMPTY;
		}
		return new ItemStack(item, stackSize);
	}

	public static ItemStack cloneStack(Block block, int stackSize) {

		if (block == null) {
			return ItemStack.EMPTY;
		}
		return new ItemStack(block, stackSize);
	}

	public static ItemStack cloneStack(ItemStack stack, int stackSize) {

		if (stack.isEmpty() || stackSize <= 0) {
			return ItemStack.EMPTY;
		}
		ItemStack retStack = stack.copy();
		retStack.setCount(stackSize);

		return retStack;
	}

	public static ItemStack cloneStack(ItemStack stack) {

		return stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
	}
	// endregion

	// region COMPARISON
	public static boolean itemsIdentical(ItemStack stackA, ItemStack stackB) {

		return itemsEqual(stackA, stackB) && getItemDamage(stackA) == getItemDamage(stackB) && doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound());
	}

	public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB) {

		return itemsEqual(stackA, stackB) && getItemDamage(stackA) == getItemDamage(stackB);
	}

	public static boolean itemsEqual(ItemStack stackA, ItemStack stackB) {

		return itemsEqual(stackA.getItem(), stackB.getItem());
	}

	public static boolean itemsEqual(Item itemA, Item itemB) {

		return itemA != null && itemB != null && (itemA == itemB || itemA.equals(itemB));
	}

	public static boolean doNBTsMatch(NBTTagCompound nbtA, NBTTagCompound nbtB) {

		return nbtA == null && nbtB == null || nbtA != null && nbtB != null && nbtA.equals(nbtB);
	}

	/**
	 * Compares item, meta, size and nbt of two stacks while ignoring nbt tag keys provided.
	 * This is useful in shouldCauseReequipAnimation overrides.
	 *
	 * @param stackA          first stack to compare
	 * @param stackB          second stack to compare
	 * @param nbtTagsToIgnore tag keys to ignore when comparing the stacks
	 */
	public static boolean areItemStacksEqualIgnoreTags(ItemStack stackA, ItemStack stackB, String... nbtTagsToIgnore) {

		if (stackA.isEmpty() && stackB.isEmpty()) {
			return true;
		}
		if (stackA.isEmpty() && !stackB.isEmpty()) {
			return false;
		}
		if (!stackA.isEmpty() && stackB.isEmpty()) {
			return false;
		}
		if (stackA.getItem() != stackB.getItem()) {
			return false;
		}
		if (stackA.getItemDamage() != stackB.getItemDamage()) {
			return false;
		}
		if (stackA.getCount() != stackB.getCount()) {
			return false;
		}
		if (stackA.getTagCompound() == null && stackB.getTagCompound() == null) {
			return true;
		}
		if (stackA.getTagCompound() == null || stackB.getTagCompound() == null) {
			return false;
		}
		int numberOfKeys = stackA.getTagCompound().getKeySet().size();
		if (numberOfKeys != stackB.getTagCompound().getKeySet().size()) {
			return false;
		}

		NBTTagCompound tagA = stackA.getTagCompound();
		NBTTagCompound tagB = stackB.getTagCompound();

		String[] keys = new String[numberOfKeys];
		keys = tagA.getKeySet().toArray(keys);

		a:
		for (int i = 0; i < numberOfKeys; i++) {
			for (int j = 0; j < nbtTagsToIgnore.length; j++) {
				if (nbtTagsToIgnore[j].equals(keys[i])) {
					continue a;
				}
			}
			if (!tagA.getTag(keys[i]).equals(tagB.getTag(keys[i]))) {
				return false;
			}
		}
		return true;
	}
	// endregion

	// region INVENTORY
	public static void readInventoryFromNBT(NBTTagCompound nbt, ItemStack[] inventory) {

		NBTTagList list = nbt.getTagList("Inventory", 10);
		inventory = new ItemStack[inventory.length];
		Arrays.fill(inventory, ItemStack.EMPTY);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			int slot = tag.getInteger("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = new ItemStack(tag);
			}
		}
	}

	public static void writeInventoryToNBT(NBTTagCompound nbt, ItemStack[] inventory) {

		if (inventory.length <= 0) {
			return;
		}
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			if (!inventory[i].isEmpty()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("Slot", i);
				inventory[i].writeToNBT(tag);
				list.appendTag(tag);
			}
		}
		if (list.tagCount() > 0) {
			nbt.setTag("Inventory", list);
		}
	}
	// endregion

	// region HELD ITEMS
	public static boolean isPlayerHoldingSomething(EntityPlayer player) {

		return !getHeldStack(player).isEmpty();
	}

	//	public static ItemStack getMainhandStack(EntityPlayer player) {
	//
	//		return player.getHeldItemMainhand();
	//	}
	//
	//	public static ItemStack getOffhandStack(EntityPlayer player) {
	//
	//		return player.getHeldItemOffhand();
	//	}

	public static ItemStack getHeldStack(EntityPlayer player) {

		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty()) {
			stack = player.getHeldItemOffhand();
		}
		return stack;
	}
	// endregion

	// region MULTIMODE
	public static ItemStack getHeldMultiModeStack(EntityPlayer player) {

		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty() || !(stack.getItem() instanceof IMultiModeItem)) {
			stack = player.getHeldItemOffhand();
		}
		return stack;
	}

	public static boolean isPlayerHoldingMultiModeItem(EntityPlayer player) {

		if (!isPlayerHoldingSomething(player)) {
			return false;
		}
		ItemStack heldItem = player.getHeldItemMainhand();
		if (heldItem.getItem() instanceof IMultiModeItem) {
			return true;
		} else {
			heldItem = player.getHeldItemOffhand();
			return heldItem.getItem() instanceof IMultiModeItem;
		}
	}

	public static boolean incrHeldMultiModeItemState(EntityPlayer player) {

		if (!isPlayerHoldingSomething(player)) {
			return false;
		}
		ItemStack heldItem = player.getHeldItemMainhand();
		Item equipped = heldItem.getItem();
		if (equipped instanceof IMultiModeItem) {
			IMultiModeItem multiModeItem = (IMultiModeItem) equipped;
			return multiModeItem.incrMode(heldItem);
		} else {
			heldItem = player.getHeldItemOffhand();
			equipped = heldItem.getItem();
			IMultiModeItem multiModeItem = (IMultiModeItem) equipped;
			return multiModeItem.incrMode(heldItem);
		}
	}

	public static boolean decrHeldMultiModeItemState(EntityPlayer player) {

		if (!isPlayerHoldingSomething(player)) {
			return false;
		}
		ItemStack heldItem = player.getHeldItemMainhand();
		Item equipped = heldItem.getItem();
		if (equipped instanceof IMultiModeItem) {
			IMultiModeItem multiModeItem = (IMultiModeItem) equipped;
			return multiModeItem.incrMode(heldItem);
		} else {
			heldItem = player.getHeldItemOffhand();
			equipped = heldItem.getItem();
			IMultiModeItem multiModeItem = (IMultiModeItem) equipped;
			return multiModeItem.incrMode(heldItem);
		}
	}

	public static boolean setHeldMultiModeItemState(EntityPlayer player, int mode) {

		if (!isPlayerHoldingSomething(player)) {
			return false;
		}
		ItemStack heldItem = player.getHeldItemMainhand();
		Item equipped = heldItem.getItem();
		if (equipped instanceof IMultiModeItem) {
			IMultiModeItem multiModeItem = (IMultiModeItem) equipped;
			return multiModeItem.setMode(heldItem, mode);
		} else {
			heldItem = player.getHeldItemOffhand();
			equipped = heldItem.getItem();
			IMultiModeItem multiModeItem = (IMultiModeItem) equipped;
			return multiModeItem.setMode(heldItem, mode);
		}
	}
	// endregion
}
