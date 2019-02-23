package cofh.thermal.core.block;

import cofh.core.block.TileCoFH;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.client.PacketTileControl;
import cofh.core.network.packet.client.PacketTileState;
import cofh.lib.energy.EnergyStorageCoFH;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.fluid.TankArrayManaged;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.inventory.InventoryCoFH;
import cofh.lib.inventory.InventoryManaged;
import cofh.lib.inventory.ItemStorageCoFH;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

import static cofh.lib.util.Constants.*;

public abstract class AbstractTileBase extends TileCoFH implements ISecurableTile, IRedstoneControllableTile {

	protected final AbstractTileType type;

	protected TimeTracker timeTracker = new TimeTracker();
	protected InventoryManaged inventory = new InventoryManaged(this, TAG_INVENTORY);
	protected TankArrayManaged tankInv = new TankArrayManaged(this, TAG_TANK_ARRAY);
	protected EnergyStorageCoFH energyStorage = new EnergyStorageCoFH(0);

	protected SecurityControlModule securityControl = new SecurityControlModule(this);
	protected RedstoneControlModule redstoneControl = new RedstoneControlModule(this);

	public boolean isActive;
	public boolean wasActive;

	public AbstractTileBase(AbstractTileType type) {

		this.type = type;
	}

	// region HELPERS
	public int invSize() {

		return inventory.getSlots();
	}

	public InventoryCoFH getInventory() {

		return inventory;
	}

	protected void updateActiveState(boolean curActive) {

		if (!wasActive && curActive != isActive || wasActive && (timeTracker.hasDelayPassed(world, ConfigTSeries.tileUpdateDelay) || timeTracker.notSet())) {
			wasActive = false;
			world.setBlockState(pos, getBlockState().withProperty(ACTIVE, isActive));
			if (type.getLight() != 0) {
				updateLighting();
			}
			PacketTileState.sendToClient(this);
		}
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
	protected void onNeighborBlockChange() {

		redstoneControl.setPower(world.isBlockIndirectlyGettingPowered(pos));
	}

	@Override
	protected int getLightValue() {

		return isActive ? type.getLight() : 0;
	}
	// endregion

	// region GUI
	public ItemStorageCoFH getSlot(int slot) {

		return inventory.getSlot(slot);
	}

	public FluidStorageCoFH getTank(int tank) {

		return tankInv.getTank(tank);
	}

	public EnergyStorageCoFH getEnergyStorage() {

		return energyStorage;
	}

	@Override
	public Object getGuiClient(InventoryPlayer inventory) {

		return type.getGuiClient(inventory, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {

		return type.getGuiServer(inventory, this);
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

	// region NETWORK
	@Override
	public PacketBufferCoFH getControlPacket(PacketBufferCoFH buffer) {

		super.getControlPacket(buffer);

		securityControl.writeToBuffer(buffer);
		redstoneControl.writeToBuffer(buffer);

		return buffer;
	}

	@Override
	public PacketBufferCoFH getGuiPacket(PacketBufferCoFH buffer) {

		super.getGuiPacket(buffer);

		buffer.writeBoolean(isActive);
		buffer.writeInt(energyStorage.getMaxEnergyStored());
		buffer.writeInt(energyStorage.getEnergyStored());

		for (int i = 0; i < tankInv.getTanks(); i++) {
			buffer.writeFluidStack(tankInv.get(i));
		}
		return buffer;
	}

	@Override
	public PacketBufferCoFH getStatePacket(PacketBufferCoFH buffer) {

		super.getControlPacket(buffer);

		buffer.writeBoolean(isActive);

		return buffer;
	}

	@Override
	public void handleControlPacket(PacketBufferCoFH buffer) {

		super.handleControlPacket(buffer);

		securityControl.readFromBuffer(buffer);
		redstoneControl.readFromBuffer(buffer);
	}

	@Override
	public void handleGuiPacket(PacketBufferCoFH buffer) {

		super.handleGuiPacket(buffer);

		isActive = buffer.readBoolean();
		energyStorage.setCapacity(buffer.readInt());
		energyStorage.setEnergyStored(buffer.readInt());

		for (int i = 0; i < tankInv.getTanks(); i++) {
			tankInv.set(i, buffer.readFluidStack());
		}
	}

	@Override
	public void handleStatePacket(PacketBufferCoFH buffer) {

		super.handleControlPacket(buffer);

		isActive = buffer.readBoolean();

		if (type.getLight() != 0) {
			world.checkLight(pos);
		}
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		isActive = nbt.getBoolean(TAG_ACTIVE);
		wasActive = nbt.getBoolean(TAG_ACTIVE_TRACK);

		inventory.readFromNBT(nbt);
		tankInv.readFromNBT(nbt);
		energyStorage.readFromNBT(nbt);

		securityControl.readFromNBT(nbt.getCompoundTag(TAG_SECURITY));
		redstoneControl.readFromNBT(nbt.getCompoundTag(TAG_REDSTONE));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setBoolean(TAG_ACTIVE, isActive);
		nbt.setBoolean(TAG_ACTIVE_TRACK, wasActive);

		inventory.writeToNBT(nbt);
		tankInv.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);

		nbt.setTag(TAG_SECURITY, securityControl.writeToNBT(new NBTTagCompound()));
		nbt.setTag(TAG_REDSTONE, redstoneControl.writeToNBT(new NBTTagCompound()));

		return nbt;
	}
	// endregion

	// region ITileCallback
	@Override
	public void onControlUpdate() {

		PacketTileControl.sendToClient(this);
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
}
