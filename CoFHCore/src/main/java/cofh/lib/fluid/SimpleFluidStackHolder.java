package cofh.lib.fluid;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class SimpleFluidStackHolder implements IFluidStackHolder {

	protected final FluidStack stack;

	public SimpleFluidStackHolder(FluidStack stack) {

		this.stack = stack;
	}

	@Override
	public void modify(int amount) {

		// This should NEVER, EVER be called.
	}

	@Override
	public void setFluidStack(FluidStack stack) {

		// This should NEVER, EVER be called.
	}

	@Nonnull
	@Override
	public FluidStack getFluidStack() {

		return stack;
	}

	@Override
	public int getFluidAmount() {

		if (isEmpty()) {
			return 0;
		}
		return stack.amount;
	}

	@Override
	public boolean isEmpty() {

		return stack == null || stack.amount <= 0;
	}

}
