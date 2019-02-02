package cofh.core.network.packet;

import cofh.core.network.PacketBufferCoFH;
import cofh.core.network.PacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

/**
 * All packets implement this.
 *
 * @author covers1624
 */
public interface IPacket<P extends IPacket<P>> {

	/**
	 * The id for this packet.
	 * The packet id bounds are 0 < id < 0x80
	 *
	 * @return The id.
	 */
	byte getId();

	/**
	 * The PacketHandler that can handle this packet.
	 *
	 * @return The handler that handles this packet.
	 */
	PacketHandler getHandler();

	/**
	 * @return If this packet should be compressed.
	 */
	boolean shouldCompress();

	/**
	 * Enables compression on this packet.
	 *
	 * @return The same packet.
	 */
	P compress();

	/**
	 * Write the packet's data to the buffer.
	 *
	 * @param buf The buffer.
	 */
	void write(PacketBufferCoFH buf);

	/**
	 * Read the data from the packet's bufffer.
	 *
	 * @param buf The buffer.
	 */
	void read(PacketBufferCoFH buf);

	/**
	 * Converts this to an FMLProxyPacket for dispatching over vanilla's network.
	 *
	 * @return The new FMLProxyPacket
	 */
	default FMLProxyPacket toFMLPacket() {

		PacketBufferCoFH buf = new PacketBufferCoFH(Unpooled.buffer());
		buf.writeByte(getId());
		write(buf);
		PacketHandler.compressPacket(buf, this);
		return new FMLProxyPacket(buf, getHandler().getChannelName());
	}

}
