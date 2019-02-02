/*
 * (C) 2014-2018 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.lib.fluid;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.TAG_FLUID;

/**
 * Implement this interface on Item classes that support external manipulation of their internal
 * fluid storage.
 *
 * NOTE: Use of NBT data on the containing ItemStack is encouraged.
 *
 * @author King Lemming
 */
public interface IFluidContainerItem {

	default int getFluidAmount(ItemStack container) {

		FluidStack fluid = getFluid(container);
		return fluid == null ? 0 : fluid.amount;
	}

	default int getSpace(ItemStack container) {

		return getCapacity(container) - getFluidAmount(container);
	}

	default int getScaledFluidStored(ItemStack container, int scale) {

		return MathHelper.round((long) getFluidAmount(container) * scale / getCapacity(container));
	}

	/**
	 * @param container ItemStack which is the fluid container.
	 * @return FluidStack representing the fluid in the container, null if the container is empty.
	 */
	default FluidStack getFluid(ItemStack container) {

		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}
		if (!container.getTagCompound().hasKey(TAG_FLUID)) {
			return null;
		}
		return FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag(TAG_FLUID));
	}

	/**
	 * @param container ItemStack which is the fluid container.
	 * @return Capacity of this fluid container.
	 */
	int getCapacity(ItemStack container);

	/**
	 * @param container ItemStack which is the fluid container.
	 * @param resource  FluidStack attempting to fill the container.
	 * @param doFill    If FALSE, the fill will only be simulated.
	 * @return Amount of fluid that was (or would have been, if simulated) filled into the
	 * container.
	 */
	int fill(ItemStack container, FluidStack resource, boolean doFill);

	/**
	 * @param container ItemStack which is the fluid container.
	 * @param maxDrain  Maximum amount of fluid to be removed from the container.
	 * @param doDrain   If FALSE, the drain will only be simulated.
	 * @return Amount of fluid that was (or would have been, if simulated) drained from the
	 * container.
	 */
	FluidStack drain(ItemStack container, int maxDrain, boolean doDrain);

}
