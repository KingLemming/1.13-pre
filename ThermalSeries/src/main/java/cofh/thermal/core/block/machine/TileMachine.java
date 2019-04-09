package cofh.thermal.core.block.machine;

import cofh.core.network.PacketBufferCoFH;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.StorageGroup;
import cofh.lib.util.control.IReconfigurableTile;
import cofh.lib.util.control.ITransferControllableTile;
import cofh.lib.util.control.ReconfigControlModule;
import cofh.lib.util.control.TransferControlModule;
import cofh.lib.util.helpers.InventoryHelper;
import cofh.thermal.core.block.AbstractTileBase;
import cofh.thermal.core.block.AbstractTileType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.control.IReconfigurable.SideConfig.SIDE_INPUT;
import static cofh.lib.util.control.IReconfigurable.SideConfig.SIDE_OUTPUT;
import static cofh.lib.util.helpers.BlockHelper.*;
import static net.minecraftforge.energy.CapabilityEnergy.ENERGY;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public abstract class TileMachine extends AbstractTileBase implements ITickable, ITransferControllableTile, IReconfigurableTile {

	protected EnumFacing facing;
	protected FluidStack renderFluid;

	protected ReconfigControlModule reconfigControl = new ReconfigControlModule(this);
	protected TransferControlModule transferControl = new TransferControlModule(this);

	protected boolean allow6WayRotation = false;

	public TileMachine(AbstractTileType type) {

		super(type);

		reconfigControl.setSideConfig(new SideConfig[] { SIDE_OUTPUT, SIDE_OUTPUT, SIDE_INPUT, SIDE_INPUT, SIDE_INPUT, SIDE_INPUT });
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

		EnumFacing prevFacing = facing;

		if (allow6WayRotation) {
			facing = getBlockState().getValue(FACING_HORIZONTAL);
			if (prevFacing == null || facing == prevFacing) {
				return;
			}
			int iPrev = prevFacing.ordinal();
			int iFace = facing.ordinal();
			SideConfig[] sides = new SideConfig[6];

			// TODO: 6-way facing logic.
			//			if (iPrev == SIDE_RIGHT[iFace]) {
			//				for (int i = 0; i < 6; i++) {
			//					sides[i] = reconfigControl.getSideConfig()[ROTATE_CLOCK_Y[i]];
			//				}
			//			} else if (iPrev == SIDE_LEFT[iFace]) {
			//				for (int i = 0; i < 6; i++) {
			//					sides[i] = reconfigControl.getSideConfig()[ROTATE_COUNTER_Y[i]];
			//				}
			//			} else if (iPrev == SIDE_OPPOSITE[iFace]) {
			//				for (int i = 0; i < 6; i++) {
			//					sides[i] = reconfigControl.getSideConfig()[INVERT_AROUND_Y[i]];
			//				}
			//			}
			reconfigControl.setSideConfig(sides);
		} else {
			facing = getBlockState().getValue(FACING_HORIZONTAL);
			if (prevFacing == null || facing == prevFacing) {
				return;
			}
			int iPrev = prevFacing.ordinal();
			int iFace = facing.ordinal();
			SideConfig[] sides = new SideConfig[6];

			if (iPrev == SIDE_RIGHT[iFace]) {
				for (int i = 0; i < 6; i++) {
					sides[i] = reconfigControl.getSideConfig()[ROTATE_CLOCK_Y[i]];
				}
			} else if (iPrev == SIDE_LEFT[iFace]) {
				for (int i = 0; i < 6; i++) {
					sides[i] = reconfigControl.getSideConfig()[ROTATE_COUNTER_Y[i]];
				}
			} else if (iPrev == SIDE_OPPOSITE[iFace]) {
				for (int i = 0; i < 6; i++) {
					sides[i] = reconfigControl.getSideConfig()[INVERT_AROUND_Y[i]];
				}
			}
			reconfigControl.setSideConfig(sides);
		}
	}

	// region HELPERS
	protected boolean cacheRenderFluid() {

		return false;
	}

	public FluidStack getRenderFluid() {

		return renderFluid;
	}

	// TODO: Finish
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
	// endregion

	// region NETWORK
	@Override
	public PacketBufferCoFH getControlPacket(PacketBufferCoFH buffer) {

		super.getControlPacket(buffer);

		reconfigControl.writeToBuffer(buffer);
		transferControl.writeToBuffer(buffer);

		buffer.writeFluidStack(renderFluid);

		return buffer;
	}

	@Override
	public PacketBufferCoFH getGuiPacket(PacketBufferCoFH buffer) {

		super.getGuiPacket(buffer);

		buffer.writeFluidStack(renderFluid);

		return buffer;
	}

	@Override
	public PacketBufferCoFH getStatePacket(PacketBufferCoFH buffer) {

		super.getControlPacket(buffer);

		buffer.writeFluidStack(renderFluid);

		return buffer;
	}

	@Override
	public void handleControlPacket(PacketBufferCoFH buffer) {

		super.handleControlPacket(buffer);

		reconfigControl.readFromBuffer(buffer);
		transferControl.readFromBuffer(buffer);

		renderFluid = buffer.readFluidStack();
	}

	@Override
	public void handleGuiPacket(PacketBufferCoFH buffer) {

		super.handleGuiPacket(buffer);

		renderFluid = buffer.readFluidStack();
	}

	@Override
	public void handleStatePacket(PacketBufferCoFH buffer) {

		super.handleControlPacket(buffer);

		renderFluid = buffer.readFluidStack();
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		reconfigControl.readFromNBT(nbt.getCompoundTag(TAG_SIDE_CONFIG));
		transferControl.readFromNBT(nbt.getCompoundTag(TAG_TRANSFER));

		renderFluid = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(TAG_RENDER_FLUID));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setTag(TAG_SIDE_CONFIG, reconfigControl.writeToNBT(new NBTTagCompound()));
		nbt.setTag(TAG_TRANSFER, transferControl.writeToNBT(new NBTTagCompound()));

		if (renderFluid != null) {
			nbt.setTag(TAG_RENDER_FLUID, renderFluid.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	// endregion

	// region MODULES
	@Override
	public ReconfigControlModule reconfigControl() {

		return reconfigControl;
	}

	@Override
	public TransferControlModule transferControl() {

		return transferControl;
	}
	// endregion

	// region CAPABILITIES
	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		if (capability == ENERGY) {
			return ENERGY.cast(energyStorage);
		}
		if (capability == ITEM_HANDLER_CAPABILITY) {
			switch (reconfigControl.getSideConfig(facing)) {
				case SIDE_NONE:
					return ITEM_HANDLER_CAPABILITY.cast(EmptyHandler.INSTANCE);
				case SIDE_INPUT:
					return ITEM_HANDLER_CAPABILITY.cast(inventory.getHandler(StorageGroup.INPUT));
				case SIDE_OUTPUT:
					return ITEM_HANDLER_CAPABILITY.cast(inventory.getHandler(StorageGroup.OUTPUT));
				default:
					return ITEM_HANDLER_CAPABILITY.cast(inventory.getHandler(StorageGroup.ACCESSIBLE));
			}
		}
		if (capability == FLUID_HANDLER_CAPABILITY) {
			switch (reconfigControl.getSideConfig(facing)) {
				case SIDE_NONE:
					return FLUID_HANDLER_CAPABILITY.cast(EmptyFluidHandler.INSTANCE);
				case SIDE_INPUT:
					return FLUID_HANDLER_CAPABILITY.cast(tankInv.getHandler(StorageGroup.INPUT));
				case SIDE_OUTPUT:
					return FLUID_HANDLER_CAPABILITY.cast(tankInv.getHandler(StorageGroup.OUTPUT));
				default:
					return FLUID_HANDLER_CAPABILITY.cast(tankInv.getHandler(StorageGroup.ACCESSIBLE));
			}
		}
		return super.getCapability(capability, facing);
	}
	// endregion
}
