package cofh.thermal.expansion.block.machine;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.ExtruderRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.List;

import static cofh.lib.util.Constants.TANK_SMALL;
import static cofh.lib.util.StorageGroup.*;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

// TODO: Finish
public class TileMachineExtruder extends TileMachineProcess {

	protected FluidStorageCoFH lavaTank = new FluidStorageCoFH(TANK_SMALL).lockFluid(FluidRegistry.LAVA);
	protected FluidStorageCoFH waterTank = new FluidStorageCoFH(TANK_SMALL).lockFluid(FluidRegistry.WATER);

	public TileMachineExtruder() {

		super(MachinesTE.EXTRUDER);

		inventory.addSlot(OUTPUT);
		inventory.addSlot(INTERNAL);
		tankInv.addTank(lavaTank, INPUT);
		tankInv.addTank(waterTank, INPUT);
	}

	@Override
	protected List<? extends ItemStorageCoFH> getInputSlots() {

		return inventory.getInternalSlots();
	}

	@Override
	protected boolean cacheRecipe() {

		curRecipe = ExtruderRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
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
		return lavaTank.getFluidAmount() >= fluidInputCounts.get(0) && waterTank.getFluidAmount() >= fluidInputCounts.get(1);
	}

	@Override
	protected boolean validateOutputs() {

		ItemStack output = getOutputSlots().get(0).getItemStack();
		if (output.isEmpty()) {
			return true;
		}
		ItemStack recipeOutput = curRecipe.getOutputItems(getInputSlots(), getInputTanks()).get(0);
		return itemsIdentical(output, recipeOutput) && output.getCount() < recipeOutput.getMaxStackSize();
	}
	// endregion
}
