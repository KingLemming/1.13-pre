package cofh.thermal.core.network;

import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.IPacketServer;
import cofh.core.network.packet.PacketBase;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.gui.container.ContainerLexiconTransmute;
import net.minecraft.entity.player.EntityPlayerMP;

import static cofh.thermal.core.init.ConfigTSeries.PACKET_LEXICON_TRANSMUTE;

public class PacketLexiconTransmute extends PacketBase<PacketLexiconTransmute> implements IPacketServer<PacketLexiconTransmute> {

	public static final byte ORE_PREV = 0;
	public static final byte ORE_NEXT = 1;
	public static final byte NAME_PREV = 2;
	public static final byte NAME_NEXT = 3;
	public static final byte TRANSMUTE = 4;

	public byte command;

	public PacketLexiconTransmute() {

		super(PACKET_LEXICON_TRANSMUTE, ThermalSeries.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		if (player.openContainer instanceof ContainerLexiconTransmute) {
			((ContainerLexiconTransmute) player.openContainer).handlePacket(this);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeByte(command);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		command = buf.readByte();
	}

	public static void sendToServer(byte command) {

		PacketLexiconTransmute packet = new PacketLexiconTransmute();
		packet.command = command;
		packet.sendToServer();
	}

}
