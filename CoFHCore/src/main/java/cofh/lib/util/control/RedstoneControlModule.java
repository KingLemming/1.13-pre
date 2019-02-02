package cofh.lib.util.control;

import cofh.core.network.packet.PacketRedstoneControl;
import cofh.lib.util.Utils;
import net.minecraft.nbt.NBTTagCompound;

import static cofh.lib.util.Constants.*;

public class RedstoneControlModule implements IRedstoneControllable {

	protected IRedstoneControllableTile tile;

	protected int power;
	protected int threshold;
	protected ControlMode mode = ControlMode.DISABLED;

	public RedstoneControlModule(IRedstoneControllableTile tile) {

		this.tile = tile;
	}

	// region NBT
	public RedstoneControlModule readFromNBT(NBTTagCompound nbt) {

		power = nbt.getByte(TAG_RS_POWER);
		threshold = nbt.getByte(TAG_RS_THRESHOLD);
		mode = ControlMode.VALUES[nbt.getByte(TAG_RS_MODE)];

		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		nbt.setByte(TAG_RS_POWER, (byte) power);
		nbt.setByte(TAG_RS_THRESHOLD, (byte) threshold);
		nbt.setByte(TAG_RS_MODE, (byte) mode.ordinal());

		return nbt;
	}
	// endregion

	// region IRedstoneControl
	@Override
	public int getPower() {

		return power;
	}

	@Override
	public int getThreshold() {

		return threshold;
	}

	@Override
	public ControlMode getMode() {

		return mode;
	}

	@Override
	public void setPower(int power) {

		boolean prevState = getState();
		this.power = power;
		if (prevState != getState() && Utils.isClientWorld(tile.world())) {
			tile.callBlockUpdate();
		}
	}

	@Override
	public void setControl(int threshold, ControlMode mode) {

		this.threshold = threshold;
		this.mode = mode;
		if (Utils.isClientWorld(tile.world())) {
			PacketRedstoneControl.sendToServer(this.tile);
		} else {
			tile.onControlUpdate();
		}
	}
	// endregion
}
