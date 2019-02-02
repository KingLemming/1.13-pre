package cofh.core.key;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.IPacketServer;
import cofh.core.network.packet.PacketBase;
import net.minecraft.entity.player.EntityPlayerMP;

import static cofh.lib.util.Constants.PACKET_KEY;

public class PacketKey extends PacketBase<PacketKey> implements IPacketServer<PacketKey> {

	String bindUUID;

	public PacketKey() {

		super(PACKET_KEY, CoFHCore.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		if (KeyHandler.serverBinds.containsKey(bindUUID)) {
			KeyHandler.serverBinds.get(bindUUID).keyPressServer(player);
		} else {
			CoFHCore.log.error("Invalid Key Packet! Unregistered Server Key! UUID: " + bindUUID);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeString(bindUUID);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		bindUUID = buf.readString(32767);
	}

	public static void sendToServer(String bindUUID) {

		PacketKey packet = new PacketKey();
		packet.bindUUID = bindUUID;
		packet.sendToServer();
	}

}
