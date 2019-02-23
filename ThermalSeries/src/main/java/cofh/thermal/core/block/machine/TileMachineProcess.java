package cofh.thermal.core.block.machine;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.util.Utils;
import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE;
import static cofh.lib.util.helpers.FluidHelper.fluidsEqual;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public abstract class TileMachineProcess extends TileMachine {

	protected IMachineRecipe curRecipe;
	protected List<Integer> itemInputCounts = new ArrayList<>();
	protected List<Integer> fluidInputCounts = new ArrayList<>();

	public TileMachineProcess(AbstractTileType type) {

		super(type);
	}

	// region PROCESS
	@Override
	protected boolean canProcessStart() {

		if (energyStorage.isEmpty()) {
			return false;
		}
		if (!cacheRecipe()) {
			return false;
		}
		if (!validateInputs()) {
			return false;
		}
		return validateOutputs();
	}

	@Override
	protected void processStart() {

		processMax = curRecipe.getEnergy(getInputSlots(), getInputTanks());
		process = processMax;

		// TODO: Tile update packet.
		if (cacheRenderFluid()) {

		}
	}

	@Override
	protected void processFinish() {

		if (!cacheRecipe()) {
			processOff();
			return;
		}
		resolveRecipe();
		markDirty();
	}
	// endregion

	// region HELPERS
	@Override
	protected void clearRecipe() {

		curRecipe = null;
		itemInputCounts = new ArrayList<>();
		fluidInputCounts = new ArrayList<>();
	}

	@Override
	protected boolean validateInputs() {

		if (!cacheRecipe()) {
			return false;
		}
		List<? extends IItemStackHolder> slotInputs = getInputSlots();
		for (int i = 0; i < slotInputs.size(); i++) {
			if (slotInputs.get(i).getItemStack().getCount() < itemInputCounts.get(i)) {
				return false;
			}
		}
		List<? extends IFluidStackHolder> tankInputs = getInputTanks();
		for (int i = 0; i < tankInputs.size(); i++) {
			FluidStack input = tankInputs.get(i).getFluidStack();
			if (input == null || input.amount < fluidInputCounts.get(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean validateOutputs() {

		// ITEMS
		List<? extends IItemStackHolder> slotOutputs = getOutputSlots();
		List<ItemStack> recipeOutputItems = curRecipe.getOutputItems(getInputSlots(), getInputTanks());
		boolean[] used = new boolean[getOutputSlots().size()];
		for (ItemStack recipeOutput : recipeOutputItems) {
			boolean matched = false;
			for (int i = 0; i < slotOutputs.size(); i++) {
				if (used[i]) {
					continue;
				}
				ItemStack output = slotOutputs.get(i).getItemStack();
				if (output.getCount() >= output.getMaxStackSize()) {
					continue;
				}
				if (itemsIdentical(output, recipeOutput)) {
					used[i] = true;
					matched = true;
					break;
				}
			}
			if (!matched) {
				for (int i = 0; i < slotOutputs.size(); i++) {
					if (used[i]) {
						continue;
					}
					if (slotOutputs.get(i).isEmpty()) {
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
		// FLUIDS
		List<? extends IFluidStackHolder> tankOutputs = getOutputTanks();
		List<FluidStack> recipeOutputFluids = curRecipe.getOutputFluids(getInputSlots(), getInputTanks());
		used = new boolean[getOutputTanks().size()];
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
		return true;
	}

	protected void resolveRecipe() {

		List<? extends IItemStackHolder> slotInputs = getInputSlots();
		List<? extends IItemStackHolder> slotOutputs = getOutputSlots();
		List<? extends IFluidStackHolder> tankInputs = getInputTanks();
		List<? extends IFluidStackHolder> tankOutputs = getOutputTanks();

		List<ItemStack> recipeOutputItems = curRecipe.getOutputItems(slotInputs, tankInputs);
		List<FluidStack> recipeOutputFluids = curRecipe.getOutputFluids(slotInputs, tankInputs);
		List<Float> recipeOutputChances = curRecipe.getOutputItemChances(slotInputs, tankInputs);

		// Output Items
		for (int i = 0; i < recipeOutputItems.size(); i++) {
			ItemStack recipeOutput = recipeOutputItems.get(i);
			float chance = recipeOutputChances.get(i);
			int outputCount = chance <= BASE_CHANCE ? recipeOutput.getCount() : (int) chance;
			while (world.rand.nextFloat() < chance) {
				boolean matched = false;
				for (IItemStackHolder slotOutput : slotOutputs) {
					ItemStack output = slotOutput.getItemStack();
					if (itemsIdentical(output, recipeOutput) && output.getCount() < output.getMaxStackSize()) {
						output.grow(outputCount);
						matched = true;
						break;
					}
				}
				if (!matched) {
					for (IItemStackHolder slotOutput : slotOutputs) {
						if (slotOutput.isEmpty()) {
							slotOutput.setItemStack(cloneStack(recipeOutput, outputCount));
							break;
						}
					}
				}
				chance -= BASE_CHANCE * outputCount;
				outputCount = 1;
			}
		}

		// Output Fluids
		for (FluidStack recipeOutput : recipeOutputFluids) {
			boolean matched = false;
			for (IFluidStackHolder tankOutput : tankOutputs) {
				FluidStack output = tankOutput.getFluidStack();
				if (fluidsEqual(output, recipeOutput)) {
					output.amount += recipeOutput.amount;
					matched = true;
					break;
				}
			}
			if (!matched) {
				for (IFluidStackHolder tankOutput : tankOutputs) {
					if (tankOutput.isEmpty()) {
						tankOutput.setFluidStack(recipeOutput.copy());
						break;
					}
				}
			}
		}

		// Input Items
		for (int i = 0; i < itemInputCounts.size(); i++) {
			slotInputs.get(i).modify(-itemInputCounts.get(i));
		}

		// Input Fluids
		for (int i = 0; i < fluidInputCounts.size(); i++) {
			tankInputs.get(i).modify(-fluidInputCounts.get(i));
		}
	}
	// endregion

	// region ITileCallback
	@Override
	public void onInventoryChange(int slot) {

		if (Utils.isServerWorld(world) && slot < inventory.getInputSlots().size()) {
			if (isActive) {
				IMachineRecipe tempRecipe = curRecipe;
				int tempSubtype = curRecipe.getSubtype(getInputSlots(), getInputTanks());
				if (!validateInputs() || tempRecipe != curRecipe || tempSubtype != curRecipe.getSubtype(getInputSlots(), getInputTanks())) {
					processOff();
				}
			}
		}
	}
	// endregion
}
