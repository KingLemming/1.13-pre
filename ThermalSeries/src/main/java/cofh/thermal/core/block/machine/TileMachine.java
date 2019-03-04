package cofh.thermal.core.block.machine;

import cofh.core.network.PacketBufferCoFH;
import cofh.lib.util.StorageGroup;
import cofh.lib.util.control.ITransferControllableTile;
import cofh.lib.util.control.TransferControlModule;
import cofh.thermal.core.block.AbstractTileBase;
import cofh.thermal.core.block.AbstractTileType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import static cofh.lib.util.Constants.TAG_TRANSFER;

public abstract class TileMachine extends AbstractTileBase implements ITickable, ITransferControllableTile {

	protected TransferControlModule transferControl = new TransferControlModule(this);

	public TileMachine(AbstractTileType type) {

		super(type);
	}

	// region HELPERS
	protected boolean cacheRenderFluid() {

		return false;
	}

	public FluidStack getRenderFluid() {

		return null;
	}

	protected void transferInput() {

		if (!transferControl.getTransferIn()) {
			return;
		}
	}

	protected void transferOutput() {

		if (!transferControl.getTransferOut()) {
			return;
		}
	}

	protected boolean validateInputs() {

		return true;
	}

	protected boolean validateOutputs() {

		return true;
	}

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
	// endregion

	// region GUI
	public int getScaledProgress(int scale) {

		return isActive ? scale : 0;
	}

	public int getScaledSpeed(int scale) {

		return isActive ? scale : 0;
	}
	// endregion

	// region NETWORK
	@Override
	public PacketBufferCoFH getControlPacket(PacketBufferCoFH buffer) {

		super.getControlPacket(buffer);

		transferControl.writeToBuffer(buffer);

		return buffer;
	}

	@Override
	public void handleControlPacket(PacketBufferCoFH buffer) {

		super.handleControlPacket(buffer);

		transferControl.readFromBuffer(buffer);
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		transferControl.readFromNBT(nbt.getCompoundTag(TAG_TRANSFER));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setTag(TAG_TRANSFER, transferControl.writeToNBT(new NBTTagCompound()));

		return nbt;
	}
	// endregion

	// region MODULES
	@Override
	public TransferControlModule transferControl() {

		return transferControl;
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
