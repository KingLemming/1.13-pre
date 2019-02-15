package cofh.thermal.core.block.dynamo;

import cofh.core.block.TileCoFH;
import cofh.core.network.packet.PacketTile;
import cofh.lib.energy.EnergyStorageCoFH;
import cofh.lib.fluid.TankArrayManaged;
import cofh.lib.inventory.InventoryCoFH;
import cofh.lib.inventory.InventoryManaged;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.StorageGroup;
import cofh.lib.util.TimeTracker;
import cofh.lib.util.Utils;
import cofh.lib.util.control.IRedstoneControllableTile;
import cofh.lib.util.control.ISecurableTile;
import cofh.lib.util.control.RedstoneControlModule;
import cofh.lib.util.control.SecurityControlModule;
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

import static cofh.lib.util.Constants.*;

public class TileDynamo extends TileCoFH implements ITickable, ISecurableTile, IRedstoneControllableTile {

	protected final Dynamo dynamo;

	protected TimeTracker timeTracker = new TimeTracker();
	protected InventoryCoFH augments = new InventoryCoFH(this, TAG_AUGMENTS);
	protected InventoryManaged inventory = new InventoryManaged(this, TAG_INVENTORY);
	protected TankArrayManaged tankInv = new TankArrayManaged(this, TAG_TANK_ARRAY);
	protected EnergyStorageCoFH energyStorage = new EnergyStorageCoFH(0);

	protected SecurityControlModule securityControl = new SecurityControlModule(this);
	protected RedstoneControlModule redstoneControl = new RedstoneControlModule(this);

	protected int fuel;
	protected int fuelMax;

	public boolean isActive;
	public boolean wasActive;

	public TileDynamo(Dynamo dynamo) {

		this.dynamo = dynamo;
	}

	@Override
	public void update() {

		boolean curActive = isActive;

		if (isActive) {
			processTick();
			if (canProcessFinish()) {
				if (!redstoneControl.getState() || !canProcessStart()) {
					processOff();
				} else {
					processStart();
				}
			}
		} else if (redstoneControl.getState()) {
			if (timeCheck()) {
				if (canProcessStart()) {
					processStart();
					processTick();
					isActive = true;
				}
			}
		}
		if (timeCheck()) {

			if (!isActive) {
				processIdle();
			}
		}
		updateActiveState(curActive);

		// TODO: Fix
		//		if (timeCheck()) {
		//			int curScale = energyStorage.getEnergyStored() > 0 ? 1 + getScaledEnergyStored(14) : 0;
		//			if (curScale != compareTracker) {
		//				compareTracker = curScale;
		//				callNeighborTileChange();
		//			}
		//			if (!cached) {
		//				updateAdjacentHandlers();
		//			}
		//			if (!isActive) {
		//				processIdle();
		//			}
		//		}
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

		return isActive ? 7 : 0;
	}
	// endregion

	// region GUI
	@Override
	public Object getGuiClient(InventoryPlayer inventory) {

		return dynamo.getGuiClient(inventory, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {

		return dynamo.getGuiServer(inventory, this);
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

		return fuel <= 0;
		//return processRem <= 0 && hasValidInput();
	}

	protected void processStart() {

	}

	protected void processFinish() {

	}

	protected void processIdle() {

	}

	protected void processOff() {

		isActive = false;
		wasActive = true;
		if (world != null) {
			timeTracker.markTime(world);
		}
	}

	protected int processTick() {

		if (fuel <= 0) {
			return 0;
		}
		int energy = calcEnergy();
		energyStorage.modifyAmount(-energy);
		fuel -= energy;
		transferEnergy();

		return energy;

		// TODO: Fix
		//		lastEnergy = calcEnergy();
		//		energyStorage.modifyEnergyStored(lastEnergy);
		//		fuelRF -= lastEnergy;
		//		transferEnergy();
		//
		//		return lastEnergy;
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

	protected void transferEnergy() {

	}

	protected void updateActiveState(boolean curActive) {

		if (!wasActive && curActive != isActive || wasActive && timeTracker.hasDelayPassed(world, ConfigTSeries.tileUpdateDelay)) {
			updateLighting();
			PacketTile.sendToClient(this);
		}
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
		return nbt;
	}
	// endregion

	// region ITileCallback
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
