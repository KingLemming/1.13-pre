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

import static cofh.lib.util.Constants.FACING_HORIZONTAL;
import static cofh.lib.util.Constants.TAG_TRANSFER;

public abstract class TileMachine extends AbstractTileBase implements ITickable, ITransferControllableTile {

	protected EnumFacing facing;

	protected TransferControlModule transferControl = new TransferControlModule(this);

	public TileMachine(AbstractTileType type) {

		super(type);
	}

	@Override
	public void updateContainingBlockInfo() {

		super.updateContainingBlockInfo();
		updateFacing();
	}

	protected EnumFacing getFacing() {

		if (facing == null) {
			updateFacing();
		}
		return facing;
	}

	protected void updateFacing() {

		facing = getBlockState().getValue(FACING_HORIZONTAL);
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
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		// TODO: Add Sidedness
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory.getHandler(StorageGroup.ACCESSIBLE));
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankInv.getHandler(StorageGroup.ACCESSIBLE));
		}
		return super.getCapability(capability, facing);
	}
	// endregion
}
