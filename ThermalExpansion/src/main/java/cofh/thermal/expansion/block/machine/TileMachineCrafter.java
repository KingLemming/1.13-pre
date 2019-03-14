package cofh.thermal.expansion.block.machine;

import cofh.core.network.PacketBufferCoFH;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.core.block.machine.TileMachineProcess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.TAG_RENDER_FLUID;
import static cofh.lib.util.Constants.TANK_SMALL;
import static cofh.lib.util.StorageGroup.*;

// TODO: Finish
public class TileMachineCrafter extends TileMachineProcess {

	private FluidStack renderFluid = new FluidStack(FluidRegistry.WATER, 0);

	protected FluidStorageCoFH inputTank = new FluidStorageCoFH(TANK_SMALL);

	public TileMachineCrafter(AbstractTileType type) {

		super(type);

		inventory.addSlot(INPUT, 18);
		inventory.addSlot(OUTPUT);
		inventory.addSlot(INTERNAL, 9);
		tankInv.addTank(inputTank, INPUT);
	}

	@Override
	public FluidStack getRenderFluid() {

		return renderFluid;
	}

	// region NETWORK
	@Override
	public PacketBufferCoFH getGuiPacket(PacketBufferCoFH buffer) {

		super.getGuiPacket(buffer);

		buffer.writeFluidStack(renderFluid);

		return buffer;
	}

	@Override
	public void handleGuiPacket(PacketBufferCoFH buffer) {

		super.handleGuiPacket(buffer);

		renderFluid = buffer.readFluidStack();
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		renderFluid = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(TAG_RENDER_FLUID));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		if (renderFluid != null) {
			nbt.setTag(TAG_RENDER_FLUID, renderFluid.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	// endregion
}
