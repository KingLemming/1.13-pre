package cofh.core.network.packet.server;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.IPacketServer;
import cofh.core.network.packet.PacketBase;
import cofh.lib.util.control.ISecurable.AccessMode;
import cofh.lib.util.control.ISecurableTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.PACKET_SECURITY_CONTROL;

public class PacketSecurityControl extends PacketBase<PacketSecurityControl> implements IPacketServer<PacketSecurityControl> {

	protected BlockPos pos;
	protected byte mode;

	public PacketSecurityControl() {

		super(PACKET_SECURITY_CONTROL, CoFHCore.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		World world = player.world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof ISecurableTile) {
			((ISecurableTile) tile).setAccess(AccessMode.VALUES[mode]);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeBlockPos(pos);
		buf.writeByte(mode);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		pos = buf.readBlockPos();
		mode = buf.readByte();
	}

	public static void sendToServer(ISecurableTile tile) {

		PacketSecurityControl packet = new PacketSecurityControl();
		packet.pos = tile.pos();
		packet.mode = (byte) tile.securityControl().getAccess().ordinal();
		packet.sendToServer();
	}

}
