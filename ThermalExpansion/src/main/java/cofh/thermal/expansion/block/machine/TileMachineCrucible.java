package cofh.thermal.expansion.block.machine;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.CrucibleRecipeManager;

import static cofh.lib.util.Constants.TANK_MEDIUM;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;

public class TileMachineCrucible extends TileMachineProcess {

	public TileMachineCrucible() {

		super(MachinesTE.CRUCIBLE);

		inventory.addSlot(CrucibleRecipeManager.instance()::validRecipe, INPUT);
		tankInv.addTank(new FluidStorageCoFH(TANK_MEDIUM), OUTPUT);
	}

	@Override
	protected boolean cacheRecipe() {

		curRecipe = CrucibleRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
		if (curRecipe != null) {
			itemInputCounts = curRecipe.getInputItemCounts(getInputSlots(), getInputTanks());
		}
		return curRecipe != null;
	}

	//	@Override
	//	protected boolean validateInputs() {
	//
	//		if (!cacheRecipe()) {
	//			return false;
	//		}
	//		return getInputSlots().get(0).getItemStack().getCount() >= itemInputCounts.get(0);
	//	}
	//
	//	@Override
	//	protected boolean validateOutputs() {
	//
	//		if (getOutputTanks().get(0).isEmpty()) {
	//			return true;
	//		}
	//		FluidStack output = getOutputTanks().get(0).getFluidStack();
	//		FluidStack recipeOutput = curRecipe.getOutputFluids(getInputSlots(), getInputTanks()).get(0);
	//		if (getOutputTanks().get(0).getSpace() < recipeOutput.amount) {
	//			return false;
	//		}
	//		return FluidHelper.fluidsEqual(output, recipeOutput);
	//	}

}
