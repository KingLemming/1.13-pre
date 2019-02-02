package cofh.thermal.expansion.block.machine;

import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.FurnaceRecipeManager;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public class TileMachineFurnace extends TileMachineProcess {

	public TileMachineFurnace() {

		super(MachinesTE.FURNACE);

		inventory.addSlot(FurnaceRecipeManager.instance()::validRecipe, INPUT);
		inventory.addSlot(OUTPUT);
	}

	@Override
	protected boolean cacheRecipe() {

		curRecipe = FurnaceRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
		if (curRecipe != null) {
			itemInputCounts = curRecipe.getInputItemCounts(getInputSlots(), getInputTanks());
		}
		return curRecipe != null;
	}

	@Override
	protected boolean validateInputs() {

		if (!cacheRecipe()) {
			return false;
		}
		return getInputSlots().get(0).getItemStack().getCount() >= itemInputCounts.get(0);
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

}
