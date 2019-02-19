package cofh.core.network.packet.server;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.IPacketServer;
import cofh.core.network.packet.PacketBase;
import cofh.lib.util.control.ITransferControllable;
import cofh.lib.util.control.ITransferControllableTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.PACKET_TRANSFER_CONTROL;

public class PacketTransferControl extends PacketBase<PacketTransferControl> implements IPacketServer<PacketTransferControl> {

	protected BlockPos pos;
	protected boolean transferIn;
	protected boolean transferOut;

	public PacketTransferControl() {

		super(PACKET_TRANSFER_CONTROL, CoFHCore.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		World world = player.world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof ITransferControllableTile) {
			((ITransferControllableTile) tile).setControl(transferIn, transferOut);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeBlockPos(pos);
		buf.writeBoolean(transferIn);
		buf.writeBoolean(transferOut);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		pos = buf.readBlockPos();
		transferIn = buf.readBoolean();
		transferOut = buf.readBoolean();
	}

	public static void sendToServer(ITransferControllableTile tile) {

		PacketTransferControl packet = new PacketTransferControl();
		ITransferControllable control = tile.transferControl();
		packet.pos = tile.pos();
		packet.transferIn = control.getTransferIn();
		packet.transferOut = control.getTransferOut();
		packet.sendToServer();
	}

}
