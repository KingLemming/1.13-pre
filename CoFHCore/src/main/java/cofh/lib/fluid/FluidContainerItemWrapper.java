/*
 * (C) 2014-2018 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.lib.fluid;

import cofh.lib.util.helpers.FluidHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidContainerItemWrapper implements ICapabilityProvider {

	final ItemStack stack;
	final IFluidContainerItem container;
	final boolean canFill;
	final boolean canDrain;

	final IFluidHandlerItem fluidCap;

	public FluidContainerItemWrapper(ItemStack stackIn, IFluidContainerItem containerIn, boolean canFillIn, boolean canDrainIn) {

		this.stack = stackIn;
		this.container = containerIn;
		this.canFill = canFillIn;
		this.canDrain = canDrainIn;

		this.fluidCap = new IFluidHandlerItem() {

			@Override
			public IFluidTankProperties[] getTankProperties() {

				return new IFluidTankProperties[] { new FluidTankProperties(container.getFluid(stack), container.getCapacity(stack), canFill, canDrain) };
			}

			@Override
			public int fill(FluidStack resource, boolean doFill) {

				if (!canFill) {
					return 0;
				}
				return container.fill(stack, resource, doFill);
			}

			@Nullable
			@Override
			public FluidStack drain(FluidStack resource, boolean doDrain) {

				if (!canDrain) {
					return null;
				}
				if (FluidHelper.fluidsEqual(resource, container.getFluid(stack))) {
					return container.drain(stack, resource.amount, doDrain);
				}
				return null;
			}

			@Nullable
			@Override
			public FluidStack drain(int maxDrain, boolean doDrain) {

				if (!canDrain) {
					return null;
				}
				return container.drain(stack, maxDrain, doDrain);
			}

			@Nonnull
			@Override
			public ItemStack getContainer() {

				return stack;
			}
		};
	}

	public FluidContainerItemWrapper(ItemStack stackIn, IFluidContainerItem containerIn) {

		this(stackIn, containerIn, true, true);
	}

	// region ICapabilityProvider
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		if (!hasCapability(capability, facing)) {
			return null;
		}
		return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(fluidCap);
	}
	// endregion
}
