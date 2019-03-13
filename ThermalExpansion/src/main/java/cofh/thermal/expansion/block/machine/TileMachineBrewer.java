package cofh.thermal.expansion.block.machine;

import cofh.core.network.PacketBufferCoFH;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.core.init.FluidsTSeries;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.BrewerRecipeManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;

public class TileMachineBrewer extends TileMachineProcess {

	private FluidStack renderFluid = new FluidStack(FluidsTSeries.fluidPotion, 0);

	protected FluidStorageCoFH inputTank = new FluidStorageCoFH(TANK_SMALL, BrewerRecipeManager.instance()::validFluid);

	public TileMachineBrewer() {

		super(MachinesTE.BREWER);

		inventory.addSlot(BrewerRecipeManager.instance()::validItem, INPUT);
		tankInv.addTank(inputTank, INPUT);
		tankInv.addTank(new FluidStorageCoFH(TANK_MEDIUM), OUTPUT);
	}

	@Override
	protected boolean cacheRenderFluid() {

		if (curRecipe == null) {
			return false;
		}
		FluidStack prevFluid = renderFluid;
		renderFluid = new FluidStack(curRecipe.getOutputFluids(getInputSlots(), getInputTanks()).get(0), 0);
		return FluidHelper.fluidsEqual(renderFluid, prevFluid);
	}

	@Override
	public FluidStack getRenderFluid() {

		return renderFluid;
	}

	// region NETWORK
	@Override
	public PacketBufferCoFH getControlPacket(PacketBufferCoFH buffer) {

		super.getControlPacket(buffer);

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
	public void handleControlPacket(PacketBufferCoFH buffer) {

		super.handleControlPacket(buffer);

		renderFluid = buffer.readFluidStack();
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

	@Override
	protected boolean cacheRecipe() {

		curRecipe = BrewerRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
		if (curRecipe != null) {
			itemInputCounts = curRecipe.getInputItemCounts(getInputSlots(), getInputTanks());
			fluidInputCounts = curRecipe.getInputFluidCounts(getInputSlots(), getInputTanks());
		}
		return curRecipe != null;
	}

	// region OPTIMIZATION
	@Override
	protected boolean validateInputs() {

		if (!cacheRecipe()) {
			return false;
		}
		return getInputSlots().get(0).getItemStack().getCount() >= itemInputCounts.get(0) && inputTank.getFluidAmount() >= fluidInputCounts.get(0);
	}

	@Override
	protected boolean validateOutputs() {

		if (getOutputTanks().get(0).isEmpty()) {
			return true;
		}
		FluidStack output = getOutputTanks().get(0).getFluidStack();
		FluidStack recipeOutput = curRecipe.getOutputFluids(getInputSlots(), getInputTanks()).get(0);
		if (getOutputTanks().get(0).getSpace() < recipeOutput.amount) {
			return false;
		}
		return FluidHelper.fluidsEqual(output, recipeOutput);
	}
	// endregion
}