/*
 * (C) 2014-2018 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.lib.fluid;

import cofh.lib.util.IResourceStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Reference implementation of {@link IFluidTank}. Use/extend this or implement your own.
 *
 * @author King Lemming, cpw (LiquidTank)
 */
public class FluidStorageCoFH implements IFluidTank, IFluidStackHolder, IResourceStorage {

	private final Predicate<FluidStack> validator;

	@Nullable
	protected FluidStack fluid;
	protected int capacity;
	protected boolean locked;
	protected IFluidTankProperties properties;

	public FluidStorageCoFH(int capacity) {

		this(null, capacity, e -> true);
	}

	public FluidStorageCoFH(int capacity, Predicate<FluidStack> validator) {

		this(null, capacity, validator);
	}

	public FluidStorageCoFH(Fluid fluid, int amount, int capacity) {

		this(new FluidStack(fluid, amount), capacity, e -> true);
	}

	public FluidStorageCoFH(FluidStack stack, int capacity) {

		this(stack, capacity, e -> true);
	}

	public FluidStorageCoFH(FluidStack stack, int capacity, Predicate<FluidStack> validator) {

		this.fluid = stack;
		this.capacity = capacity;
		this.validator = validator;
	}

	public FluidStorageCoFH setFluid(FluidStack fluid) {

		this.fluid = fluid;
		return this;
	}

	public FluidStorageCoFH setCapacity(int capacity) {

		this.capacity = capacity;
		return this;
	}

	public FluidStorageCoFH lockFluid(Fluid fluid) {

		locked = fluid != null;
		if (locked) {
			if (this.fluid == null || !this.fluid.getFluid().equals(fluid)) {
				this.fluid = new FluidStack(fluid, 0);
			}
		}
		return this;
	}

	// region HELPERS
	public void lock(boolean lock) {

		if (lock) {
			lock();
		} else {
			unlock();
		}
	}

	protected void lock() {

		if (locked || this.fluid == null) {
			return;
		}
		locked = true;
	}

	protected void unlock() {

		locked = false;
		if (this.getFluidAmount() <= 0) {
			this.fluid = null;
		}
	}

	public IFluidTankProperties getProperties() {

		if (properties == null) {
			properties = new FluidTankProperties(fluid, capacity);
		}
		return properties;
	}

	/**
	 * Method for parity with {@link IFluidHandler}
	 */
	public FluidStack drain(FluidStack resource, boolean doDrain) {

		if (resource == null || !resource.isFluidEqual(fluid)) {
			return null;
		}
		return drain(resource.amount, doDrain);
	}
	// endregion

	// region NBT
	public FluidStorageCoFH readFromNBT(NBTTagCompound nbt) {

		FluidStack fluid = null;
		locked = false;
		if (!nbt.hasKey("Empty")) {
			fluid = FluidStack.loadFluidStackFromNBT(nbt);
			locked = nbt.getBoolean("Lock") && fluid != null;
		}
		setFluid(fluid);
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		if (fluid != null) {
			fluid.writeToNBT(nbt);
			nbt.setBoolean("Lock", locked);
		} else {
			nbt.setString("Empty", "");
		}
		return nbt;
	}
	// endregion

	// region IFluidStackHolder
	@Override
	public void modify(int amount) {

		if (this.fluid == null) {
			// TODO: Error Logging - this should really never happen.
			return;
		}
		this.fluid.amount += amount;
		if (this.fluid.amount > capacity) {
			this.fluid.amount = capacity;
		} else if (this.fluid.amount < 0) {
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
	public boolean isEmpty() {

		return fluid == null || fluid.amount <= 0;
	}
	// endregion

	// region IFluidTank
	@Override
	public FluidStack getFluid() {

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
	public int getCapacity() {

		return capacity;
	}

	@Override
	public FluidTankInfo getInfo() {

		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {

		if (resource == null || !validator.test(resource)) {
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
	public FluidStack drain(int maxDrain, boolean doDrain) {

		if (fluid == null || locked && fluid.amount <= 0) {
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
				if (locked) {
					fluid.amount = 0;
				} else {
					fluid = null;
				}
			}
		}
		return stack;
	}
	// endregion

	// region IResourceStorage
	@Override
	public void modifyAmount(int amount) {

		if (!locked || fluid == null) {
			return;
		}
		this.fluid.amount += amount;
		if (this.fluid.amount > capacity) {
			this.fluid.amount = capacity;
		} else if (this.fluid.amount < 0) {
			this.fluid.amount = 0;
		}
	}

	@Override
	public boolean clear() {

		if (isEmpty()) {
			return false;
		}
		drain(capacity, true);
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
