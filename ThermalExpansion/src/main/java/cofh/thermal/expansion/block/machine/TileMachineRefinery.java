package cofh.thermal.expansion.block.machine;

import cofh.core.network.PacketBufferCoFH;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.RefineryRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.lib.util.helpers.FluidHelper.fluidsEqual;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public class TileMachineRefinery extends TileMachineProcess {

	private FluidStack renderFluid = new FluidStack(FluidRegistry.WATER, 0);

	protected ItemStorageCoFH outputSlot = new ItemStorageCoFH();
	protected FluidStorageCoFH inputTank = new FluidStorageCoFH(TANK_SMALL, RefineryRecipeManager.instance()::validRecipe);
	protected FluidStorageCoFH outputTankA = new FluidStorageCoFH(TANK_MEDIUM);
	protected FluidStorageCoFH outputTankB = new FluidStorageCoFH(TANK_MEDIUM);

	public TileMachineRefinery() {

		super(MachinesTE.REFINERY);

		inventory.addSlot(outputSlot, OUTPUT);
		tankInv.addTank(inputTank, INPUT);
		tankInv.addTank(outputTankA, OUTPUT);
		tankInv.addTank(outputTankB, OUTPUT);
	}

	@Override
	protected boolean cacheRenderFluid() {

		if (curRecipe == null) {
			return false;
		}
		FluidStack prevFluid = renderFluid;

		if (inputTank.isEmpty()) {
			// This should definitely never happen, but who knows.
			return false;
		}
		renderFluid = new FluidStack(inputTank.getFluidStack(), 0);
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

		curRecipe = RefineryRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
		if (curRecipe != null) {
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
		return inputTank.getFluidAmount() >= fluidInputCounts.get(0);
	}

	@Override
	protected boolean validateOutputs() {

		List<? extends FluidStorageCoFH> tankOutputs = getOutputTanks();
		List<FluidStack> recipeOutputFluids = curRecipe.getOutputFluids(getInputSlots(), getInputTanks());
		boolean[] used = new boolean[getOutputTanks().size()];
		for (FluidStack recipeOutput : recipeOutputFluids) {
			boolean matched = false;
			for (int i = 0; i < tankOutputs.size(); i++) {
				if (used[i] || tankOutputs.get(i).getSpace() <= 0) {
					continue;
				}
				FluidStack output = tankOutputs.get(i).getFluidStack();
				if (fluidsEqual(output, recipeOutput)) {
					used[i] = true;
					matched = true;
					break;
				}
			}
			if (!matched) {
				for (int i = 0; i < tankOutputs.size(); i++) {
					if (used[i]) {
						continue;
					}
					if (tankOutputs.get(i).isEmpty()) {
						used[i] = true;
						matched = true;
						break;
					}
				}
			}
			if (!matched) {
				return false;
			}
		}
		ItemStack output = outputSlot.getItemStack();
		if (output.isEmpty()) {
			return true;
		}
		ItemStack recipeOutput = curRecipe.getOutputItems(getInputSlots(), getInputTanks()).get(0);
		return itemsIdentical(output, recipeOutput) && output.getCount() < recipeOutput.getMaxStackSize();
	}
	// endregion
}
