package cofh.core.network.packet;

import cofh.core.CoFHCore;
import cofh.core.network.PacketBufferCoFH;
import cofh.core.util.ChatHelper;
import cofh.core.util.CoreUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;

import static cofh.lib.util.Constants.PACKET_CHAT;

public class PacketIndexedChat extends PacketBase<PacketIndexedChat> implements IPacketClient<PacketIndexedChat> {

	protected int index;
	protected String message;

	public PacketIndexedChat() {

		super(PACKET_CHAT, CoFHCore.packetHandler);
	}

	@Override
	public void handleClient() {

		CoreUtils.addIndexedChatMessage(ChatHelper.fromJSON(message), index);
	}

	@Override
	public void write(PacketBufferCoFH buf) {

		buf.writeInt(index);
		buf.writeString(message);
	}

	@Override
	public void read(PacketBufferCoFH buf) {

		index = buf.readInt();
		message = buf.readString(Short.MAX_VALUE);
	}

	public static void sendToClient(ITextComponent chat, int index, EntityPlayerMP player) {

		PacketIndexedChat packet = new PacketIndexedChat();
		packet.index = index;
		packet.message = ChatHelper.toJSON(chat);
		packet.sendToPlayer(player);
	}

}
