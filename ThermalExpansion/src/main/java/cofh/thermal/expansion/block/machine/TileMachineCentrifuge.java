package cofh.thermal.expansion.block.machine;

import cofh.core.network.PacketBufferCoFH;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.CentrifugeRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static cofh.lib.util.Constants.TANK_SMALL;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public class TileMachineCentrifuge extends TileMachineProcess {

	private FluidStack renderFluid = new FluidStack(FluidRegistry.LAVA, 0);

	public TileMachineCentrifuge() {

		super(MachinesTE.CENTRIFUGE);

		inventory.addSlot(CentrifugeRecipeManager.instance()::validRecipe, INPUT);
		inventory.addSlot(OUTPUT, 4);
		tankInv.addTank(new FluidStorageCoFH(TANK_SMALL), OUTPUT);
	}

	@Override
	protected boolean cacheRenderFluid() {

		if (curRecipe == null) {
			return false;
		}
		FluidStack prevFluid = renderFluid;
		List<FluidStack> recipeOutputFluids = curRecipe.getOutputFluids(getInputSlots(), getInputTanks());
		renderFluid = recipeOutputFluids.isEmpty() ? null : recipeOutputFluids.get(0);
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
		return getInputSlots().get(0).getItemStack().getCount() >= itemInputCounts.get(0);
	}

	@Override
	protected boolean validateOutputs() {

		List<? extends IItemStackHolder> slotOutputs = getOutputSlots();
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
