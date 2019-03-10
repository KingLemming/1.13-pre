package cofh.thermal.expansion.block.machine;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.inventory.IItemStackHolder;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.InsolatorRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static cofh.lib.util.Constants.TANK_SMALL;
import static cofh.lib.util.StorageGroup.*;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public class TileMachineInsolator extends TileMachineProcess {

	protected FluidStorageCoFH waterTank = new FluidStorageCoFH(TANK_SMALL).lockFluid(FluidRegistry.WATER);

	public TileMachineInsolator() {

		super(MachinesTE.INSOLATOR);

		inventory.addSlot(InsolatorRecipeManager.instance()::validRecipe, INPUT);
		inventory.addSlot(InsolatorRecipeManager.instance()::validCatalyst, CATALYST);
		inventory.addSlot(OUTPUT, 4);

		tankInv.addTank(waterTank, INPUT);
	}

	@Override
	public FluidStack getRenderFluid() {

		return InsolatorRecipeManager.defaultPlantFluid;
	}

	@Override
	protected boolean cacheRecipe() {

		curRecipe = InsolatorRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
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
		return getInputSlots().get(0).getItemStack().getCount() >= itemInputCounts.get(0) && waterTank.getFluidAmount() >= fluidInputCounts.get(0);
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
		return true;
	}
	// endregion
}
