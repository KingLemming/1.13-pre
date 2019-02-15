package cofh.thermal.core.block.machine;

import cofh.core.block.TileCoFH;
import cofh.core.network.packet.PacketTile;
import cofh.lib.energy.EnergyStorageCoFH;
import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.fluid.TankArrayManaged;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.inventory.InventoryCoFH;
import cofh.lib.inventory.InventoryManaged;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.StorageGroup;
import cofh.lib.util.TimeTracker;
import cofh.lib.util.Utils;
import cofh.lib.util.control.*;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.init.ConfigTSeries;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

import static cofh.lib.util.Constants.*;

public abstract class TileMachine extends TileCoFH implements ITickable, ISecurableTile, IRedstoneControllableTile, ITransferControllableTile {

	protected final Machine machine;

	protected TimeTracker timeTracker = new TimeTracker();
	protected InventoryCoFH augments = new InventoryCoFH(this, TAG_AUGMENTS);
	protected InventoryManaged inventory = new InventoryManaged(this, TAG_INVENTORY);
	protected TankArrayManaged tankInv = new TankArrayManaged(this, TAG_TANK_ARRAY);
	protected EnergyStorageCoFH energyStorage = new EnergyStorageCoFH(0);

	protected SecurityControlModule securityControl = new SecurityControlModule(this);
	protected RedstoneControlModule redstoneControl = new RedstoneControlModule(this);
	protected TransferControlModule transferControl = new TransferControlModule(this);

	protected int process;
	protected int processMax;

	protected float outputMod = 1.0F;
	protected float energyMod = 1.0F;

	public boolean isActive;
	public boolean wasActive;

	public TileMachine(Machine machine) {

		this.machine = machine;
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
				energyStorage.modifyAmount(-process);

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
			if (timeCheck4() && canProcessStart()) {
				processStart();
				processTick();
				isActive = true;
			}
		}
		updateActiveState(curActive);
		// chargeEnergy();
	}

	// region PASSTHROUGHS
	@Override
	protected void onBlockBroken(World world, BlockPos pos, IBlockState state) {

		for (ItemStorageCoFH slot : inventory.getAccessibleSlots()) {
			ItemStack stack = slot.getItemStack();
			if (stack.getCount() > stack.getMaxStackSize()) {
				Utils.dropItemStackIntoWorldWithVelocity(stack.splitStack(stack.getCount() - stack.getMaxStackSize()), world, pos);
			}
			Utils.dropItemStackIntoWorldWithVelocity(slot.getItemStack(), world, pos);
		}
	}

	@Override
	protected int getLightValue() {

		return isActive ? machine.getLight() : 0;
	}
	// endregion

	// region GUI
	@Override
	public Object getGuiClient(InventoryPlayer inventory) {

		return machine.getGuiClient(inventory, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {

		return machine.getGuiServer(inventory, this);
	}

	@Override
	public boolean openGui(EntityPlayer player) {

		if (canAccess(player)) {
			player.openGui(ThermalSeries.instance, GUI_TILE, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		if (Utils.isServerWorld(world)) {
			player.sendMessage(new TextComponentTranslation("chat.cofh.secure.warning", getOwnerName()));
		}
		return false;
	}
	// endregion

	// region PROCESS
	protected boolean canProcessStart() {

		return false;
	}

	protected boolean canProcessFinish() {

		return process <= 0 && validateInputs();
	}

	protected void processStart() {

	}

	protected void processFinish() {

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
		int energy = calcEnergy();
		energyStorage.modifyAmount(-energy);
		process -= energy;
		return energy;
	}
	// endregion

	// region HELPERS
	public int invSize() {

		return inventory.getSlots();
	}

	public InventoryCoFH getAugments() {

		return augments;
	}

	public InventoryCoFH getInventory() {

		return inventory;
	}

	protected boolean cacheRecipe() {

		return true;
	}

	protected void clearRecipe() {

	}

	protected void transferInput() {

		if (!transferControl.getTransferIn()) {
			return;
		}
	}

	protected void transferOutput() {

		if (!transferControl.getTransferOut()) {
			return;
		}
	}

	protected void updateActiveState(boolean curActive) {

		if (!wasActive && curActive != isActive || wasActive && timeTracker.hasDelayPassed(world, ConfigTSeries.tileUpdateDelay)) {
			if (machine.getLight() != 0) {
				updateLighting();
			}
			PacketTile.sendToClient(this);
		}
	}

	protected boolean validateInputs() {

		return true;
	}

	protected boolean validateOutputs() {

		return true;
	}

	protected int calcEnergy() {

		// TODO: Fix
		//		if (energyStorage.getEnergyStored() >= energyConfig.maxPowerLevel) {
		//			return energyConfig.maxPower;
		//		}
		//		if (energyStorage.getEnergyStored() < energyConfig.minPowerLevel) {
		//			return Math.min(energyConfig.minPower, energyStorage.getEnergyStored());
		//		}
		//		return energyStorage.getEnergyStored() / energyConfig.energyRamp;
		return 20;
	}

	protected List<? extends IItemStackHolder> getInputSlots() {

		return inventory.getInputSlots();
	}

	protected List<? extends IFluidStackHolder> getInputTanks() {

		return tankInv.getInputTanks();
	}

	protected List<? extends IItemStackHolder> getOutputSlots() {

		return inventory.getOutputSlots();
	}

	protected List<? extends IFluidStackHolder> getOutputTanks() {

		return tankInv.getOutputTanks();
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);
		augments.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
		tankInv.readFromNBT(nbt);
		energyStorage.readFromNBT(nbt);

		nbt.setTag(TAG_SECURITY, securityControl.writeToNBT(new NBTTagCompound()));
		nbt.setTag(TAG_REDSTONE, redstoneControl.writeToNBT(new NBTTagCompound()));
		nbt.setTag(TAG_TRANSFER, transferControl.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);
		augments.writeToNBT(nbt);
		inventory.writeToNBT(nbt);
		tankInv.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);

		securityControl.readFromNBT(nbt.getCompoundTag(TAG_SECURITY));
		redstoneControl.readFromNBT(nbt.getCompoundTag(TAG_REDSTONE));
		transferControl.readFromNBT(nbt.getCompoundTag(TAG_TRANSFER));
		return nbt;
	}
	// endregion

	// region ITileCallback
	@Override
	public void onInventoryChange(int slot) {

		if (Utils.isServerWorld(world) && slot < inventory.getInputSlots().size()) {
			if (isActive && !validateInputs()) {
				processOff();
			}
		}
	}

	@Override
	public void onControlUpdate() {

		PacketTile.sendToClient(this);
	}
	// endregion

	// region MODULES
	@Override
	public SecurityControlModule securityControl() {

		return securityControl;
	}

	@Override
	public RedstoneControlModule redstoneControl() {

		return redstoneControl;
	}

	@Override
	public TransferControlModule transferControl() {

		return transferControl;
	}
	// endregion

	// region CAPABILITIES
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.hasSlots();
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return tankInv.hasTanks();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		// TODO: Add Sidedness
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory.getHandler(StorageGroup.ALL));
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankInv.getHandler(StorageGroup.ALL));
		}
		return super.getCapability(capability, facing);
	}
	// endregion
}
