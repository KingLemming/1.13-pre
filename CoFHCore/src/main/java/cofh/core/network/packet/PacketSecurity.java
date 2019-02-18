package cofh.core.network.packet;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.lib.util.control.ISecurable;
import cofh.lib.util.control.ISecurable.AccessMode;
import net.minecraft.entity.player.EntityPlayerMP;

import static cofh.lib.util.Constants.PACKET_SECURITY;

public class PacketSecurity extends PacketBase<PacketSecurity> implements IPacketServer<PacketSecurity> {

	protected byte mode;

	public PacketSecurity() {

		super(PACKET_SECURITY, CoFHCore.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		if (player.openContainer instanceof ISecurable) {
			((ISecurable) player.openContainer).setAccess(AccessMode.VALUES[mode]);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeByte(mode);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		mode = buf.readByte();
	}

	public static void sendToServer(AccessMode accessMode) {

		PacketSecurity packet = new PacketSecurity();
		packet.mode = (byte) accessMode.ordinal();
		packet.sendToServer();
	}

}
