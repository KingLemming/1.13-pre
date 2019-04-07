package cofh.thermal.expansion.block.machine;

import cofh.core.util.oredict.OreDictionaryArbiter;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.core.util.managers.LexiconManager;
import cofh.thermal.expansion.init.MachinesTE;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

import static cofh.lib.util.StorageGroup.*;

public class TileMachineFactorizer extends TileMachine {

	protected Map<String, ItemStack> preferredStacks = new Object2ObjectOpenHashMap<>();

	protected ItemStorageCoFH inputSlot = new ItemStorageCoFH(this::validConversion);
	protected ItemStorageCoFH outputSlot = new ItemStorageCoFH();

	public TileMachineFactorizer() {

		super(MachinesTE.LEXICON);

		inventory.addSlot(inputSlot, INPUT);
		inventory.addSlot(outputSlot, OUTPUT);
		inventory.addSlot(INTERNAL, 15);
	}

	@Override
	public void update() {

		if (!timeCheckHalf()) {
			return;
		}
		transferInput();

		boolean curActive = isActive;

		if (isActive) {
			transmute();

			if (!redstoneControl.getState()) {
				isActive = false;
			}
		} else if (redstoneControl.getState()) {
			isActive = true;
		}
		transferOutput();

		updateActiveState(curActive);
	}

	// region HELPERS
	public void updatePreferredStacks() {

		preferredStacks.clear();

		for (ItemStorageCoFH slot : inventory.getInternalSlots()) {
			if (!slot.isEmpty()) {
				ItemStack stack = slot.getItemStack();
				preferredStacks.put(OreDictionaryArbiter.getOreName(stack), stack);
			}
		}
	}

	protected void transmute() {

		if (inputSlot.isEmpty() || outputSlot.isFull()) {
			return;
		}
		ItemStack input = inputSlot.getItemStack();
		ItemStack output = outputSlot.getItemStack();

		if (hasPreferredStack(input)) {
			ItemStack transmuteStack = ItemHelper.cloneStack(getPreferredStack(input), input.getCount());

			if (output.isEmpty()) {
				inputSlot.setItemStack(ItemStack.EMPTY);
				outputSlot.setItemStack(transmuteStack);
			} else if (output.isItemEqual(transmuteStack)) {
				int total = output.getCount() + transmuteStack.getCount();
				if (total <= output.getMaxStackSize()) {
					inputSlot.setItemStack(ItemStack.EMPTY);
					output.grow(transmuteStack.getCount());
				} else {
					input.shrink(output.getMaxStackSize() - output.getCount());
					output.setCount(output.getMaxStackSize());
				}
			}
		}
	}

	public boolean hasPreferredStack(ItemStack stack) {

		return preferredStacks.containsKey(OreDictionaryArbiter.getOreName(stack));
	}

	public ItemStack getPreferredStack(ItemStack stack) {

		return preferredStacks.get(OreDictionaryArbiter.getOreName(stack));
	}

	public boolean validConversion(ItemStack stack) {

		return LexiconManager.validOre(stack) && hasPreferredStack(stack) && !ItemHelper.itemsIdentical(stack, getPreferredStack(stack));
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		updatePreferredStacks();
	}
	// endregion
}
