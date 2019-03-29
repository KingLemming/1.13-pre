package cofh.thermal.core.block.machine;

import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.client.PacketTileState;
import cofh.lib.energy.EnergyStorageCoFH;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.Utils;
import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.FluidHelper.fluidsEqual;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;

public abstract class TileMachineProcess extends TileMachine {

	protected IMachineRecipe curRecipe;
	protected List<Integer> itemInputCounts = new ArrayList<>();
	protected List<Integer> fluidInputCounts = new ArrayList<>();

	protected int process;
	protected int processMax;

	protected float outputMod = 1.0F;
	protected float energyMod = 1.0F;

	protected int energyUse = 20;

	public TileMachineProcess(AbstractTileType type) {

		super(type);
		energyStorage = new EnergyStorageCoFH(20000);
	}

	@Override
	public void update() {

		boolean curActive = isActive;

		if (isActive) {
			processTick();
			if (canProcessFinish()) {
				processFinish();
				transferOutput();
				transferInput();
				if (!redstoneControl.getState() || !canProcessStart()) {
					processOff();
				} else {
					processStart();
				}
			} else if (energyStorage.isEmpty()) {
				processOff();
			}
		} else if (redstoneControl.getState()) {
			if (timeCheck()) {
				transferOutput();
				transferInput();
			}
			if (timeCheckQuarter() && canProcessStart()) {
				processStart();
				processTick();
				isActive = true;
			}
		}
		updateActiveState(curActive);
		// chargeEnergy();
	}

	// region PROCESS
	protected boolean canProcessStart() {

		if (energyStorage.isEmpty()) {
			return false;
		}
		if (!validateInputs()) {
			return false;
		}
		return validateOutputs();
	}

	protected boolean canProcessFinish() {

		return process <= 0 && validateInputs();
	}

	protected void processStart() {

		processMax = curRecipe.getEnergy(getInputSlots(), getInputTanks());
		process = processMax;

		if (cacheRenderFluid()) {
			PacketTileState.sendToClient(this);
		}
	}

	protected void processFinish() {

		if (!cacheRecipe()) {
			processOff();
			return;
		}
		resolveRecipe();
		markDirty();
		energyStorage.modifyAmount(-process);
	}

	protected void processOff() {

		process = 0;
		isActive = false;
		wasActive = true;
		clearRecipe();
		if (world != null) {
			timeTracker.markTime(world);
		}
	}

	protected int processTick() {

		if (process <= 0) {
			return 0;
		}
		int energy = Math.min(energyStorage.getEnergyStored(), energyUse);
		energyStorage.modifyAmount(-energy);
		process -= energy;
		return energy;
	}
	// endregion

	// region HELPERS
	protected boolean cacheRecipe() {

		return true;
	}

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
		List<? extends ItemStorageCoFH> slotInputs = getInputSlots();
		for (int i = 0; i < slotInputs.size(); i++) {
			int inputCount = itemInputCounts.get(i);
			if (inputCount > 0 && slotInputs.get(i).getItemStack().getCount() < inputCount) {
				return false;
			}
		}
		List<? extends FluidStorageCoFH> tankInputs = getInputTanks();
		for (int i = 0; i < tankInputs.size(); i++) {
			int inputCount = fluidInputCounts.get(i);
			FluidStack input = tankInputs.get(i).getFluidStack();
			if (inputCount > 0 && (input == null || input.amount < inputCount)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean validateOutputs() {

		// ITEMS
		List<? extends ItemStorageCoFH> slotOutputs = getOutputSlots();
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
		List<? extends FluidStorageCoFH> tankOutputs = getOutputTanks();
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

		List<? extends ItemStorageCoFH> slotInputs = getInputSlots();
		List<? extends ItemStorageCoFH> slotOutputs = getOutputSlots();
		List<? extends FluidStorageCoFH> tankInputs = getInputTanks();
		List<? extends FluidStorageCoFH> tankOutputs = getOutputTanks();

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

	// region GUI
	public int getScaledProgress(int scale) {

		if (!isActive || processMax <= 0 || process <= 0) {
			return 0;
		}
		return scale * (processMax - process) / processMax;
	}

	public int getScaledSpeed(int scale) {

		// TODO: Fix
		if (!isActive) {
			return 0;
		}
		return scale;
		//		double power = energyStorage.getEnergyStored() / energyConfig.energyRamp;
		//		power = MathHelper.clip(power, energyConfig.minPower, energyConfig.maxPower);
		//		return MathHelper.round(scale * power / energyConfig.maxPower);
	}
	// endregion

	// region NETWORK
	@Override
	public PacketBufferCoFH getGuiPacket(PacketBufferCoFH buffer) {

		super.getGuiPacket(buffer);

		buffer.writeInt(processMax);
		buffer.writeInt(process);

		return buffer;
	}

	@Override
	public void handleGuiPacket(PacketBufferCoFH buffer) {

		super.handleGuiPacket(buffer);

		processMax = buffer.readInt();
		process = buffer.readInt();
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		processMax = nbt.getInteger(TAG_PROCESS_MAX);
		process = nbt.getInteger(TAG_PROCESS);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setInteger(TAG_PROCESS_MAX, processMax);
		nbt.setInteger(TAG_PROCESS, process);

		return nbt;
	}
	// endregion

	// region ITileCallback
	@Override
	public void onInventoryChange(int slot) {

		if (Utils.isServerWorld(world) && slot < inventory.getInputSlots().size()) {
			if (isActive) {
				IMachineRecipe tempRecipe = curRecipe;
				if (!validateInputs() || tempRecipe != curRecipe) {
					processOff();
				}
			}
		}
	}
	// endregion
}
