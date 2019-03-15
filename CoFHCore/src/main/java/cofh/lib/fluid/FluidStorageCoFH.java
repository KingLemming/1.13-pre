/*
 * (C) 2014-2018 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.lib.fluid;

import cofh.lib.util.IResourceStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Implementation of a Fluid Storage object. Does NOT implement {@link IFluidTank}.
 *
 * @author King Lemming
 */
public class FluidStorageCoFH implements IFluidHandler, IFluidStackHolder, IResourceStorage {

	protected Predicate<FluidStack> validator;
	@Nullable
	protected FluidStack fluid = null;
	protected int capacity;
	protected IFluidTankProperties properties;

	public FluidStorageCoFH(int capacity) {

		this(capacity, e -> true);
	}

	public FluidStorageCoFH(int capacity, Predicate<FluidStack> validator) {

		this.capacity = capacity;
		this.validator = validator;
	}

	public FluidStorageCoFH setCapacity(int capacity) {

		this.capacity = capacity;
		return this;
	}

	public FluidStorageCoFH setValidator(Predicate<FluidStack> validator) {

		if (validator != null) {
			this.validator = validator;
		}
		return this;
	}

	public boolean isFluidValid(FluidStack stack) {

		return validator.test(stack);
	}

	public int getCapacity() {

		return capacity;
	}

	public IFluidTankProperties getProperties() {

		if (properties == null) {
			properties = new FluidTankProperties(fluid, capacity);
		}
		return properties;
	}

	// region NBT
	public FluidStorageCoFH readFromNBT(NBTTagCompound nbt) {

		FluidStack fluid = null;
		if (!nbt.hasKey("Empty")) {
			fluid = FluidStack.loadFluidStackFromNBT(nbt);
		}
		setFluidStack(fluid);
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		if (fluid != null) {
			fluid.writeToNBT(nbt);
		} else {
			nbt.setBoolean("Empty", true);
		}
		return nbt;
	}
	// endregion

	// region IFluidStackHolder
	@Override
	public void modify(int amount) {

		if (this.fluid == null) {
			return;
		}
		this.fluid.amount += amount;
		if (this.fluid.amount > capacity) {
			this.fluid.amount = capacity;
		} else if (this.fluid.amount <= 0) {
			this.fluid = null;
		}
	}

	@Override
	public void setFluidStack(FluidStack stack) {

		this.fluid = stack;
	}

	@Override
	public FluidStack getFluidStack() {

		return fluid;
	}

	@Override
	public int getFluidAmount() {

		if (fluid == null) {
			return 0;
		}
		return fluid.amount;
	}

	@Override
	public boolean isEmpty() {

		return fluid == null || fluid.amount <= 0;
	}
	// endregion

	// region IFluidHandler
	@Override
	public IFluidTankProperties[] getTankProperties() {

		return new IFluidTankProperties[] { getProperties() };
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {

		if (resource == null || !isFluidValid(resource)) {
			return 0;
		}
		if (!doFill) {
			if (fluid == null) {
				return Math.min(capacity, resource.amount);
			}
			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}
			return Math.min(capacity - fluid.amount, resource.amount);
		}
		if (fluid == null) {
			fluid = new FluidStack(resource, Math.min(capacity, resource.amount));
			return fluid.amount;
		}
		if (!fluid.isFluidEqual(resource)) {
			return 0;
		}
		int filled = capacity - fluid.amount;

		if (resource.amount < filled) {
			fluid.amount += resource.amount;
			filled = resource.amount;
		} else {
			fluid.amount = capacity;
		}
		return filled;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {

		if (resource == null || !resource.isFluidEqual(fluid)) {
			return null;
		}
		return drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {

		if (fluid == null) {
			return null;
		}
		int drained = maxDrain;
		if (fluid.amount < drained) {
			drained = fluid.amount;
		}
		FluidStack stack = new FluidStack(fluid, drained);
		if (doDrain) {
			fluid.amount -= drained;
			if (fluid.amount <= 0) {
				fluid = null;
			}
		}
		return stack;
	}
	// endregion

	// region IResourceStorage
	@Override
	public void modifyAmount(int amount) {

		modify(amount);
	}

	@Override
	public boolean clear() {

		if (isEmpty()) {
			return false;
		}
		fluid = null;
		return true;
	}

	@Override
	public int getSpace() {

		if (fluid == null) {
			return capacity;
		}
		return fluid.amount >= capacity ? 0 : capacity - fluid.amount;
	}

	@Override
	public int getStored() {

		return getFluidAmount();
	}

	@Override
	public int getMaxStored() {

		return getCapacity();
	}

	@Override
	public String getUnit() {

		return "mB";
	}
	// endregion
}
