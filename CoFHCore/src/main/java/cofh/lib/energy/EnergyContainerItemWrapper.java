/*
 * (C) 2014-2018 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.lib.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * This class provides a simple way to wrap an IEnergyContainerItem to allow for capability support. It will seamlessly allow for Forge Energy to be supported in addition to RF.
 *
 * @author King Lemming
 */
public class EnergyContainerItemWrapper implements ICapabilityProvider {

	final ItemStack stack;
	final IEnergyContainerItem container;
	final boolean canExtract;
	final boolean canReceive;

	final IEnergyStorage energyCap;

	public EnergyContainerItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn, boolean extractIn, boolean receiveIn) {

		this.stack = stackIn;
		this.container = containerIn;
		this.canExtract = extractIn;
		this.canReceive = receiveIn;

		this.energyCap = new IEnergyStorage() {

			@Override
			public int receiveEnergy(int maxReceive, boolean simulate) {

				if (!canReceive) {
					return 0;
				}
				return container.receiveEnergy(stack, maxReceive, simulate);
			}

			@Override
			public int extractEnergy(int maxExtract, boolean simulate) {

				if (!canExtract) {
					return 0;
				}
				return container.extractEnergy(stack, maxExtract, simulate);
			}

			@Override
			public int getEnergyStored() {

				return container.getEnergyStored(stack);
			}

			@Override
			public int getMaxEnergyStored() {

				return container.getMaxEnergyStored(stack);
			}

			@Override
			public boolean canExtract() {

				return canExtract;
			}

			@Override
			public boolean canReceive() {

				return canReceive;
			}
		};
	}

	public EnergyContainerItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn) {

		this(stackIn, containerIn, true, true);
	}

	// region ICapabilityProvider
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		return capability == CapabilityEnergy.ENERGY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		if (!hasCapability(capability, facing)) {
			return null;
		}
		return CapabilityEnergy.ENERGY.cast(energyCap);
	}
	// endregion
}
