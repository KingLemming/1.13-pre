package cofh.core.network.packet;

import cofh.core.CoFHCore;
import cofh.core.block.TileCoFH;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.util.CoreUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.NETWORK_UPDATE_DISTANCE;
import static cofh.lib.util.Constants.PACKET_TILE;

public class PacketTile extends PacketBase<PacketTile> implements IPacketClient<PacketTile> {

	BlockPos pos;
	PacketBufferCoFH buffer;

	public PacketTile() {

		super(PACKET_TILE, CoFHCore.packetHandler);
	}

	@Override
	public void handleClient() {

		World world = CoreUtils.getClientPlayer().world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH) {
			((TileCoFH) tile).handleTilePacket(buffer);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeBlockPos(pos);
		buf.writeBytes(buffer);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		buffer = buf;
		pos = buffer.readBlockPos();
	}

	public static void sendToClient(TileCoFH tile) {

		PacketTile packet = new PacketTile();
		packet.pos = tile.pos();
		packet.buffer = tile.getTilePacket(new PacketBufferCoFH(Unpooled.buffer()));
		packet.sendToAllAround(packet.pos, NETWORK_UPDATE_DISTANCE, tile.world().provider.getDimension());
	}

}
