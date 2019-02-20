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

import static cofh.lib.util.Constants.*;

public abstract class TileMachine extends AbstractTileBase implements ITickable, ITransferControllableTile {

	protected TransferControlModule transferControl = new TransferControlModule(this);

	protected int process;
	protected int processMax;

	protected float outputMod = 1.0F;
	protected float energyMod = 1.0F;

	public TileMachine(AbstractTileType type) {

		super(type);
	}

	@Override
	public void update() {

		boolean curActive = isActive;

		// TODO: Remove
		energyStorage.receiveEnergy(200, false);

		if (isActive) {
			processTick();
			if (canProcessFinish()) {
				processFinish();
				transferOutput();
				transferInput();
				energyStorage.modifyAmount(-process);

				if (!redstoneControl.getState() || !canProcessStart()) {
					processOff();
				} else {
					processStart();
				}
			} else if (energyStorage.isEmpty()) {
				processOff();
			}
		} else if (redstoneControl.getState()) {
			if (timeCheck()) {
				transferOutput();
				transferInput();
			}
			if (timeCheck4() && canProcessStart()) {
				processStart();
				processTick();
				isActive = true;
			}
		}
		updateActiveState(curActive);
		// chargeEnergy();
	}

	// region PROCESS
	protected boolean canProcessStart() {

		return false;
	}

	protected boolean canProcessFinish() {

		return process <= 0 && validateInputs();
	}

	protected void processStart() {

	}

	protected void processFinish() {

	}

	protected void processOff() {

		process = 0;
		isActive = false;
		wasActive = true;
		clearRecipe();
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
		return energy;
	}
	// endregion

	// region HELPERS
	protected boolean cacheRenderFluid() {

		return false;
	}

	protected boolean cacheRecipe() {

		return true;
	}

	protected void clearRecipe() {

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
	public FluidStack getRenderFluid() {

		return null;
	}

	public int getScaledProgress(int scale) {

		if (!isActive || processMax <= 0 || process <= 0) {
			return 0;
		}
		return scale * (processMax - process) / processMax;
	}

	public int getScaledSpeed(int scale) {

		// TODO: Fix
		if (!isActive) {
			return 0;
		}
		return scale;
		//		double power = energyStorage.getEnergyStored() / energyConfig.energyRamp;
		//		power = MathHelper.clip(power, energyConfig.minPower, energyConfig.maxPower);
		//		return MathHelper.round(scale * power / energyConfig.maxPower);
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
	public PacketBufferCoFH getGuiPacket(PacketBufferCoFH buffer) {

		super.getGuiPacket(buffer);

		buffer.writeInt(processMax);
		buffer.writeInt(process);

		return buffer;
	}

	@Override
	public void handleControlPacket(PacketBufferCoFH buffer) {

		super.handleControlPacket(buffer);

		transferControl.readFromBuffer(buffer);
	}

	@Override
	public void handleGuiPacket(PacketBufferCoFH buffer) {

		super.handleGuiPacket(buffer);

		processMax = buffer.readInt();
		process = buffer.readInt();
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		processMax = nbt.getInteger(TAG_PROCESS_MAX);
		process = nbt.getInteger(TAG_PROCESS);

		nbt.setTag(TAG_TRANSFER, transferControl.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setInteger(TAG_PROCESS_MAX, processMax);
		nbt.setInteger(TAG_PROCESS, process);

		transferControl.readFromNBT(nbt.getCompoundTag(TAG_TRANSFER));

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
