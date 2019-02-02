package cofh.thermal.core.network;

import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.packet.IPacketServer;
import cofh.core.network.packet.PacketBase;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.gui.container.ContainerLexiconStudy;
import net.minecraft.entity.player.EntityPlayerMP;

import static cofh.thermal.core.init.ConfigTSeries.PACKET_LEXICON_STUDY;

public class PacketLexiconStudy extends PacketBase<PacketLexiconStudy> implements IPacketServer<PacketLexiconStudy> {

	public static final byte ORE_PREV = 0;
	public static final byte ORE_NEXT = 1;
	public static final byte SET_PREFERRED = 2;
	public static final byte CLEAR_PREFERRED = 3;
	public static final byte SELECT_ORE = 4;

	public byte command;
	public String oreName = "";

	public PacketLexiconStudy() {

		super(PACKET_LEXICON_STUDY, ThermalSeries.packetHandler);
	}

	@Override
	public void handleServer(EntityPlayerMP player) {

		if (player.openContainer instanceof ContainerLexiconStudy) {
			((ContainerLexiconStudy) player.openContainer).handlePacket(this);
		}
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeByte(command);
		buf.writeString(oreName);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		command = buf.readByte();
		oreName = buf.readString(32767);
	}

	public static void sendToServer(byte command) {

		PacketLexiconStudy packet = new PacketLexiconStudy();
		packet.command = command;
		packet.sendToServer();
	}

	public static void sendToServer(String oreName) {

		PacketLexiconStudy packet = new PacketLexiconStudy();
		packet.command = SELECT_ORE;
		packet.oreName = oreName;
		packet.sendToServer();
	}

}
