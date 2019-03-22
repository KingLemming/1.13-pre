package cofh.thermal.expansion.block.machine.process;

import cofh.core.network.PacketBufferCoFH;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.CentrifugeRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static cofh.lib.util.Constants.TAG_RENDER_FLUID;
import static cofh.lib.util.Constants.TANK_SMALL;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public class TileMachineCentrifuge extends TileMachineProcess {

	protected FluidStack renderFluid = new FluidStack(FluidRegistry.WATER, 0);

	protected ItemStorageCoFH inputSlot = new ItemStorageCoFH(CentrifugeRecipeManager.instance()::validRecipe);
	protected FluidStorageCoFH outputTank = new FluidStorageCoFH(TANK_SMALL);

	public TileMachineCentrifuge() {

		super(MachinesTE.CENTRIFUGE);

		inventory.addSlot(inputSlot, INPUT);
		inventory.addSlot(OUTPUT, 4);
		tankInv.addTank(outputTank, OUTPUT);
	}

	@Override
	protected boolean cacheRenderFluid() {

		if (curRecipe == null) {
			return false;
		}
		FluidStack prevFluid = renderFluid;
		List<FluidStack> recipeOutputFluids = curRecipe.getOutputFluids(getInputSlots(), getInputTanks());
		renderFluid = recipeOutputFluids.isEmpty() ? null : new FluidStack(recipeOutputFluids.get(0), 0);
		return FluidHelper.fluidsEqual(renderFluid, prevFluid);
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

	@Override
	protected boolean cacheRecipe() {

		curRecipe = CentrifugeRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
		if (curRecipe != null) {
			itemInputCounts = curRecipe.getInputItemCounts(getInputSlots(), getInputTanks());
		}
		return curRecipe != null;
	}

	// region OPTIMIZATION
	@Override
	protected boolean validateInputs() {

		if (!cacheRecipe()) {
			return false;
		}
		return inputSlot.getCount() >= itemInputCounts.get(0);
	}

	@Override
	protected boolean validateOutputs() {

		List<? extends ItemStorageCoFH> slotOutputs = getOutputSlots();
		List<ItemStack> recipeOutputItems = curRecipe.getOutputItems(getInputSlots(), getInputTanks());
		boolean[] used = new boolean[getOutputSlots().size()];
		for (ItemStack recipeOutput : recipeOutputItems) {
			boolean matched = false;
			for (int j = 0; j < slotOutputs.size(); j++) {
				if (used[j]) {
					continue;
				}
				ItemStack output = slotOutputs.get(j).getItemStack();
				if (output.getCount() >= output.getMaxStackSize()) {
					continue;
				}
				if (itemsIdentical(output, recipeOutput)) {
					used[j] = true;
					matched = true;
					break;
				}
			}
			if (!matched) {
				for (int j = 0; j < slotOutputs.size(); j++) {
					if (used[j]) {
						continue;
					}
					if (slotOutputs.get(j).isEmpty()) {
						used[j] = true;
						matched = true;
						break;
					}
				}
			}
			if (!matched) {
				return false;
			}
		}
		List<FluidStack> recipeOutputFluids = curRecipe.getOutputFluids(getInputSlots(), getInputTanks());
		if (recipeOutputFluids.isEmpty()) {
			return true;
		}
		if (outputTank.isEmpty()) {
			return true;
		}
		FluidStack output = outputTank.getFluidStack();
		FluidStack recipeOutput = curRecipe.getOutputFluids(getInputSlots(), getInputTanks()).get(0);
		if (outputTank.getSpace() < recipeOutput.amount) {
			return false;
		}
		return FluidHelper.fluidsEqual(output, recipeOutput);
	}
	// endregion
}
