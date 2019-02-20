package cofh.thermal.core.block.dynamo;

import cofh.lib.util.StorageGroup;
import cofh.thermal.core.block.AbstractTileBase;
import cofh.thermal.core.block.AbstractTileType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import static cofh.lib.util.Constants.TAG_PROCESS;
import static cofh.lib.util.Constants.TAG_PROCESS_MAX;

public class TileDynamo extends AbstractTileBase implements ITickable {

	protected int process;
	protected int processMax;

	public TileDynamo(AbstractTileType type) {

		super(type);
	}

	@Override
	public void update() {

		boolean curActive = isActive;

		if (isActive) {
			processTick();
			if (canProcessFinish()) {
				if (!redstoneControl.getState() || !canProcessStart()) {
					processOff();
				} else {
					processStart();
				}
			}
		} else if (redstoneControl.getState()) {
			if (timeCheck()) {
				if (canProcessStart()) {
					processStart();
					processTick();
					isActive = true;
				}
			}
		}
		if (timeCheck()) {

			if (!isActive) {
				processIdle();
			}
		}
		updateActiveState(curActive);

		// TODO: Fix
		//		if (timeCheck()) {
		//			int curScale = energyStorage.getEnergyStored() > 0 ? 1 + getScaledEnergyStored(14) : 0;
		//			if (curScale != compareTracker) {
		//				compareTracker = curScale;
		//				callNeighborTileChange();
		//			}
		//			if (!cached) {
		//				updateAdjacentHandlers();
		//			}
		//			if (!isActive) {
		//				processIdle();
		//			}
		//		}
	}

	// region PROCESS
	protected boolean canProcessStart() {

		return false;
	}

	protected boolean canProcessFinish() {

		return process <= 0;
		//return processRem <= 0 && hasValidInput();
	}

	protected void processStart() {

	}

	protected void processFinish() {

	}

	protected void processIdle() {

	}

	protected void processOff() {

		isActive = false;
		wasActive = true;
		if (world != null) {
			timeTracker.markTime(world);
		}
	}

	protected int processTick() {

		if (process <= 0) {
			return 0;
		}
		int energy = calcEnergy();
		energyStorage.modifyAmount(-energy);
		process -= energy;
		transferEnergy();

		return energy;

		// TODO: Fix
		//		lastEnergy = calcEnergy();
		//		energyStorage.modifyEnergyStored(lastEnergy);
		//		fuelRF -= lastEnergy;
		//		transferEnergy();
		//
		//		return lastEnergy;
	}
	// endregion

	// region HELPERS
	protected int calcEnergy() {

		// TODO: Fix
		//		if (energyStorage.getEnergyStored() >= energyConfig.maxPowerLevel) {
		//			return energyConfig.maxPower;
		//		}
		//		if (energyStorage.getEnergyStored() < energyConfig.minPowerLevel) {
		//			return Math.min(energyConfig.minPower, energyStorage.getEnergyStored());
		//		}
		//		return energyStorage.getEnergyStored() / energyConfig.energyRamp;
		return 20;
	}

	protected void transferEnergy() {

	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		processMax = nbt.getInteger(TAG_PROCESS_MAX);
		process = nbt.getInteger(TAG_PROCESS);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setInteger(TAG_PROCESS_MAX, processMax);
		nbt.setInteger(TAG_PROCESS, process);

		return nbt;
	}
	// endregion

	// region CAPABILITIES
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.hasSlots();
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return tankInv.hasTanks();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		// TODO: Add Sidedness
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory.getHandler(StorageGroup.ALL));
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankInv.getHandler(StorageGroup.ALL));
		}
		return super.getCapability(capability, facing);
	}
	// endregion
}
