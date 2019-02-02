package cofh.core.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Packet sent FROM Clients TO Servers
 *
 * @author covers1624
 */
public interface IPacketServer<P extends IPacketServer<P>> extends IPacket<P> {

	/**
	 * Handle the packet on the server.
	 *
	 * @param player The player who sent the packet.
	 */
	void handleServer(EntityPlayerMP player);

	/**
	 * Send this packet to the server.
	 */
	@SideOnly (Side.CLIENT)
	default void sendToServer() {

		Minecraft.getMinecraft().getConnection().sendPacket(toFMLPacket());
	}

}
