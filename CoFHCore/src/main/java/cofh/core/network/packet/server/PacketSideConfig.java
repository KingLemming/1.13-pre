package cofh.core.network.packet.server;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.IPacketServer;
import cofh.core.network.packet.PacketBase;
import cofh.lib.util.control.IReconfigurable.SideConfig;
import cofh.lib.util.control.IReconfigurableTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.PACKET_SIDE_CONFIG;
import static cofh.lib.util.control.IReconfigurable.SideConfig.SIDE_NONE;

public class PacketSideConfig extends PacketBase<PacketSideConfig> implements IPacketServer<PacketSideConfig> {

	protected BlockPos pos;
	protected SideConfig[] sides = { SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE };

	public PacketSideConfig() {

		super(PACKET_SIDE_CONFIG, CoFHCore.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		World world = player.world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IReconfigurableTile) {
			((IReconfigurableTile) tile).reconfigControl().setSideConfig(sides);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeBlockPos(pos);

		byte[] bSides = new byte[6];
		for (int i = 0; i < 6; i++) {
			bSides[i] = (byte) sides[i].ordinal();
		}
		buf.writeByteArray(bSides);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		pos = buf.readBlockPos();

		byte[] bSides = buf.readByteArray(6);
		if (bSides.length == 6) {
			for (int i = 0; i < 6; i++) {
				if (bSides[i] > SideConfig.VALUES.length) {
					bSides[i] = 0;
				}
				sides[i] = SideConfig.VALUES[bSides[i]];
			}
		}
	}

	public static void sendToServer(IReconfigurableTile tile) {

		PacketSideConfig packet = new PacketSideConfig();
		packet.pos = tile.pos();
		packet.sides = tile.reconfigControl().getSideConfig();
		packet.sendToServer();
	}

}
