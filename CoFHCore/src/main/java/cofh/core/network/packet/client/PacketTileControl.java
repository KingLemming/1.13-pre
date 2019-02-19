package cofh.core.network.packet.client;

import cofh.core.CoFHCore;
import cofh.core.block.TileCoFH;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.IPacketClient;
import cofh.core.network.packet.PacketBase;
import cofh.core.util.CoreUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.NETWORK_UPDATE_DISTANCE;
import static cofh.lib.util.Constants.PACKET_CONTROL;

public class PacketTileControl extends PacketBase<PacketTileControl> implements IPacketClient<PacketTileControl> {

	protected BlockPos pos;
	protected PacketBufferCoFH buffer;

	public PacketTileControl() {

		super(PACKET_CONTROL, CoFHCore.packetHandler);
	}

	@Override
	public void handleClient() {

		World world = CoreUtils.getClientPlayer().world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH) {
			((TileCoFH) tile).handleControlPacket(buffer);
			IBlockState state = tile.getWorld().getBlockState(pos);
			tile.getWorld().notifyBlockUpdate(pos, state, state, 3);
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

		PacketTileControl packet = new PacketTileControl();
		packet.pos = tile.pos();
		packet.buffer = tile.getControlPacket(new PacketBufferCoFH(Unpooled.buffer()));
		packet.sendToAllAround(packet.pos, NETWORK_UPDATE_DISTANCE, tile.world().provider.getDimension());
	}

}
