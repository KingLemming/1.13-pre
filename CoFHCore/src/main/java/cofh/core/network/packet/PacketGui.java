package cofh.core.network.packet;

import cofh.core.CoFHCore;
import cofh.core.block.TileCoFH;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.util.CoreUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.PACKET_GUI;

public class PacketGui extends PacketBase<PacketGui> implements IPacketClient<PacketGui> {

	protected BlockPos pos;
	protected PacketBufferCoFH buffer;

	public PacketGui() {

		super(PACKET_GUI, CoFHCore.packetHandler);
	}

	@Override
	public void handleClient() {

		World world = CoreUtils.getClientPlayer().world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH) {
			((TileCoFH) tile).handleGuiPacket(buffer);
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

	public static void sendToClient(TileCoFH tile, EntityPlayerMP player) {

		PacketGui packet = new PacketGui();
		packet.pos = tile.pos();
		packet.buffer = tile.getGuiPacket(new PacketBufferCoFH(Unpooled.buffer()));
		packet.sendToPlayer(player);
	}

}
