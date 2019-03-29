package cofh.thermal.expansion.block.machine.process;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.CrucibleRecipeManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.TANK_MEDIUM;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;

public class TileMachineCrucible extends TileMachineProcess {

	protected ItemStorageCoFH inputSlot = new ItemStorageCoFH(CrucibleRecipeManager.instance()::validRecipe);
	protected FluidStorageCoFH outputTank = new FluidStorageCoFH(TANK_MEDIUM);

	public TileMachineCrucible() {

		super(MachinesTE.CRUCIBLE);

		inventory.addSlot(inputSlot, INPUT);
		tankInv.addTank(outputTank, OUTPUT);

		renderFluid = new FluidStack(FluidRegistry.LAVA, 0);
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
	protected boolean cacheRecipe() {

		curRecipe = CrucibleRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
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
