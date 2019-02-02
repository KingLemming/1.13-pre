package cofh.lib.fluid;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IFluidStackHolder {

	void modify(int amount);

	void setFluidStack(FluidStack stack);

	@Nullable
	FluidStack getFluidStack();

	int getCapacity();

	int getSpace();

	boolean isEmpty();

}
