package cofh.core.network.packet;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.lib.util.IFilterable;
import net.minecraft.entity.player.EntityPlayerMP;

import static cofh.lib.util.Constants.PACKET_FILTER;

public class PacketFilter extends PacketBase<PacketFilter> implements IPacketServer<PacketFilter> {

	protected int flag;
	protected boolean value;

	public PacketFilter() {

		super(PACKET_FILTER, CoFHCore.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		if (player.openContainer instanceof IFilterable) {
			((IFilterable) player.openContainer).setFlag(flag, value);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeInt(flag);
		buf.writeBoolean(value);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		flag = buf.readInt();
		value = buf.readBoolean();
	}

	public static void sendToServer(int flag, boolean value) {

		PacketFilter packet = new PacketFilter();
		packet.flag = flag;
		packet.value = value;
		packet.sendToServer();
	}

}
