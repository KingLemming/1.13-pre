package cofh.core.network.packet;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.lib.util.control.IRedstoneControllable;
import cofh.lib.util.control.IRedstoneControllable.ControlMode;
import cofh.lib.util.control.IRedstoneControllableTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.PACKET_REDSTONE_CONTROL;

public class PacketRedstoneControl extends PacketBase<PacketRedstoneControl> implements IPacketServer<PacketRedstoneControl> {

	protected BlockPos pos;
	protected int threshold;
	protected byte mode;

	public PacketRedstoneControl() {

		super(PACKET_REDSTONE_CONTROL, CoFHCore.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		World world = player.world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IRedstoneControllableTile) {
			((IRedstoneControllableTile) tile).setControl(threshold, ControlMode.VALUES[mode]);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeBlockPos(pos);
		buf.writeInt(threshold);
		buf.writeByte(mode);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		pos = buf.readBlockPos();
		threshold = buf.readInt();
		mode = buf.readByte();
	}

	public static void sendToServer(IRedstoneControllableTile tile) {

		PacketRedstoneControl packet = new PacketRedstoneControl();
		IRedstoneControllable control = tile.redstoneControl();
		packet.pos = tile.pos();
		packet.threshold = control.getThreshold();
		packet.mode = (byte) control.getMode().ordinal();
		packet.sendToServer();
	}

}
