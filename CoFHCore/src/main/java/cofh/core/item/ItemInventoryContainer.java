package cofh.core.item;

import cofh.lib.item.IInventoryContainerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cofh.lib.util.Constants.GROUP_UTILS;
import static cofh.lib.util.Constants.TAG_ACCESSIBLE;

public class ItemInventoryContainer extends ItemCoFH implements IInventoryContainerItem {

	protected int invSize;
	protected int stackLimit;

	public ItemInventoryContainer(int invSize) {

		this(invSize, 64);
	}

	public ItemInventoryContainer(int invSize, int stackLimit) {

		super(GROUP_UTILS);

		setMaxStackSize(1);
		setNoRepair();

		this.invSize = invSize;
		this.stackLimit = stackLimit;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {

		return !isCreative();
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean isFull3D() {

		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return 10;
	}

	// region HELPERS
	public static boolean needsDefaultTag(ItemStack stack) {

		return stack.getTagCompound() == null || !stack.getTagCompound().hasKey(TAG_ACCESSIBLE);
	}

	public static ItemStack setDefaultTag(ItemStack stack) {

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setBoolean(TAG_ACCESSIBLE, true);
		return stack;
	}
	// endregion

	// region IInventoryContainerItem
	@Override
	public int getSizeInventory(ItemStack container) {

		return invSize;
	}

	@Override
	public int getInventoryStackLimit(ItemStack container) {

		return stackLimit;
	}
	// endregion
}
