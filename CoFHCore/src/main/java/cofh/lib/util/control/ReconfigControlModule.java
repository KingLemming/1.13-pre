package cofh.lib.util.control;

import cofh.core.network.PacketBufferCoFH;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import static cofh.lib.util.Constants.TAG_SIDES;
import static cofh.lib.util.control.IReconfigurable.SideConfig.SIDE_ACCESSIBLE;
import static cofh.lib.util.control.IReconfigurable.SideConfig.SIDE_NONE;

public class ReconfigControlModule implements IReconfigurable {

	protected IReconfigurableTile tile;
	protected boolean enabled;

	protected SideConfig[] sides = { SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE };

	public ReconfigControlModule(IReconfigurableTile tile) {

		this(tile, true);
	}

	public ReconfigControlModule(IReconfigurableTile tile, boolean enabled) {

		this.tile = tile;
		this.enabled = enabled;
	}

	public SideConfig[] getSideConfig() {

		return sides;
	}

	public void setSideConfig(SideConfig[] sides) {

		this.sides = sides;
	}

	// region NETWORK
	public void readFromBuffer(PacketBufferCoFH buffer) {

		for (int i = 0; i < 6; i++) {
			sides[i] = SideConfig.VALUES[buffer.readByte()];
		}
	}

	public void writeToBuffer(PacketBufferCoFH buffer) {

		for (int i = 0; i < 6; i++) {
			buffer.writeByte(sides[i].ordinal());
		}
	}
	// endregion

	// region NBT
	public ReconfigControlModule readFromNBT(NBTTagCompound nbt) {

		byte[] bSides = nbt.getByteArray(TAG_SIDES);
		if (bSides.length == 6) {
			for (int i = 0; i < 6; i++) {
				if (bSides[i] > SideConfig.VALUES.length) {
					bSides[i] = 0;
				}
				sides[i] = SideConfig.VALUES[bSides[i]];
			}
		}
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		byte[] bSides = new byte[6];
		for (int i = 0; i < 6; i++) {
			bSides[i] = (byte) sides[i].ordinal();
		}
		nbt.setByteArray(TAG_SIDES, bSides);
		return nbt;
	}
	// endregion

	// region IReconfigurable
	@Override
	public boolean isReconfigurable() {

		return enabled;
	}

	@Override
	public SideConfig get(EnumFacing side) {

		if (side == null) {
			return SIDE_ACCESSIBLE;
		}
		return sides[side.ordinal()];
	}

	@Override
	public boolean prev(EnumFacing side) {

		if (side == null) {
			return false;
		}
		sides[side.ordinal()] = sides[side.ordinal()].prev();
		return true;
	}

	@Override
	public boolean next(EnumFacing side) {

		if (side == null) {
			return false;
		}
		sides[side.ordinal()] = sides[side.ordinal()].next();
		return true;
	}

	@Override
	public boolean set(EnumFacing side, SideConfig config) {

		if (side == null || config == null) {
			return false;
		}
		sides[side.ordinal()] = config;
		return true;
	}

	@Override
	public boolean clear() {

		sides = new SideConfig[] { SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE };
		return true;
	}
	// endregion
}
