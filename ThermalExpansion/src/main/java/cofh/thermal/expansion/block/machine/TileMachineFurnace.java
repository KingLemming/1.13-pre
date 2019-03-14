package cofh.thermal.expansion.block.machine;

import cofh.lib.inventory.ItemStorageCoFH;
import cofh.thermal.core.block.machine.TileMachineProcess;
import cofh.thermal.expansion.init.MachinesTE;
import cofh.thermal.expansion.util.managers.machine.FurnaceRecipeManager;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public class TileMachineFurnace extends TileMachineProcess {

	protected ItemStorageCoFH inputSlot = new ItemStorageCoFH(FurnaceRecipeManager.instance()::validRecipe);
	protected ItemStorageCoFH outputSlot = new ItemStorageCoFH();

	public TileMachineFurnace() {

		super(MachinesTE.FURNACE);

		inventory.addSlot(inputSlot, INPUT);
		inventory.addSlot(outputSlot, OUTPUT);
	}

	@Override
	protected boolean cacheRecipe() {

		curRecipe = FurnaceRecipeManager.instance().getRecipe(getInputSlots(), getInputTanks());
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

		ItemStack output = outputSlot.getItemStack();
		if (output.isEmpty()) {
			return true;
		}
		ItemStack recipeOutput = curRecipe.getOutputItems(getInputSlots(), getInputTanks()).get(0);
		return itemsIdentical(output, recipeOutput) && output.getCount() < recipeOutput.getMaxStackSize();
	}
	// endregion
}
