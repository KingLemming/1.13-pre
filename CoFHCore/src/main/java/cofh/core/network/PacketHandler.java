package cofh.core.network;

import cofh.core.network.packet.IPacket;
import cofh.core.network.packet.IPacketClient;
import cofh.core.network.packet.IPacketServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.function.Supplier;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author covers1624
 */
public class PacketHandler {

	private static Logger log = LogManager.getLogger("cofh.PacketHandler");
	private static EnumMap<Side, Supplier<Supplier<IThreadListener>>> listenerMap = new EnumMap<>(Side.class);

	public static final boolean DEBUG = Boolean.getBoolean("cofh.network.debug");

	static {
		//This is server safe, because lambda magic.
		listenerMap.put(Side.CLIENT, () -> Minecraft::getMinecraft);
		listenerMap.put(Side.SERVER, () -> () -> FMLCommonHandler.instance().getMinecraftServerInstance());
	}

	private final String channelName;
	private Byte2ObjectMap<Supplier<IPacket>> packets = new Byte2ObjectArrayMap<>(127);

	public PacketHandler(String channelName) {

		this.channelName = channelName;
		NetworkRegistry.INSTANCE.newChannel(channelName, new CustomInboundHandler());
	}

	@SuppressWarnings ("unchecked")
	public <T extends IPacket> void registerPacket(int id, Supplier<? super T> constructor) {

		if (id <= 0 || id >= 0x80) {
			throw new IllegalArgumentException(String.format("Packet id(%s) not within bounds id <= 0 || id >= 0x80", id));
		}
		packets.put((byte) id, (Supplier<IPacket>) constructor);
		log.debug("Channel {}, Register packet, ID: {}", channelName, id);
	}

	public String getChannelName() {

		return channelName;
	}

	// region COMPRESSION

	/**
	 * INTERNAL!!
	 * YOU SHOULD NOT NEED TO USE THIS EVER.
	 *
	 * Attempts to compress the data in the buffer for the specified packet,
	 * if the data would be larger when compressed, it does nothing.
	 *
	 * @param buf    The buffer to compress.
	 * @param packet The owning packet.
	 */
	public static void compressPacket(PacketBufferCoFH buf, IPacket<?> packet) {

		if (packet.shouldCompress() || buf.readableBytes() > 32000) {
			Deflater deflater = new Deflater();
			try {
				buf.readerIndex(1);
				int len = buf.readableBytes();
				deflater.setInput(buf.array(), buf.readerIndex(), len);
				deflater.finish();
				byte[] out = new byte[len];
				int clen = deflater.deflate(out);
				if (clen >= len - 5 || !deflater.finished()) {
					if (DEBUG) {
						log.info("Skipping compression for packet. Gets larger.");
					}
					//Not worth compressing, gets larger.
					return;
				}
				buf.clear();
				//Write the new id + the compressed mask.
				buf.writeByte(packet.getId() | 0x80);
				buf.writeVarInt(len);//The length of the uncompressed data.
				buf.writeBytes(out);
			} catch (Exception e) {
				throw new EncoderException(e);
			} finally {
				deflater.end();
				buf.readerIndex(0);
			}
		}
	}

	/**
	 * INTERNAL!!
	 * YOU SHOULD NOT NEED TO USE THIS EVER.
	 *
	 * Attempts to decompress a packet that has been compressed,
	 * looking for a specific marker on the index byte.
	 *
	 * @param buf The buffer to decompress.
	 */
	public static void decompressPacket(PacketBufferCoFH buf) {

		int id = buf.readUnsignedByte();
		Inflater inflater = null;
		try {
			if ((id & 0x80) > 0) {
				int len = buf.readVarInt();
				byte[] out = new byte[len];
				inflater = new Inflater();
				inflater.setInput(buf.array(), buf.readerIndex(), buf.readableBytes());
				inflater.inflate(out);
				buf.clear();
				buf.writeByte(id & 0x7F);
				buf.writeBytes(out);
			}
		} catch (Exception e) {
			throw new EncoderException(e);
		} finally {
			if (inflater != null) {
				inflater.end();
			}
			buf.readerIndex(0);
		}
	}
	// endregion

	@Sharable
	public class CustomInboundHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {

			Side side = ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get();
			listenerMap.get(side).get().get().addScheduledTask(() -> {
				PacketBufferCoFH buf = new PacketBufferCoFH(Unpooled.buffer()); // Copy the msg's payload to a new array-backed buffer.
				ByteBuf payload = msg.payload();
				buf.setBytes(0, payload);
				buf.writerIndex(payload.writerIndex());
				buf.readerIndex(0);

				if (DEBUG) {
					debug(buf, side, "PreDecompress");
				}
				decompressPacket(buf);

				if (DEBUG) {
					debug(buf, side, "PostDecompress");
				}
				int type = buf.readUnsignedByte();
				IPacket packet = packets.get((byte) (type & 0x7F)).get();
				packet.read(buf);
				switch (side) {
					case CLIENT:
						((IPacketClient) packet).handleClient();
						break;
					case SERVER:
						INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
						((IPacketServer) packet).handleServer(((NetHandlerPlayServer) netHandler).player);
						break;
				}

			});
		}

		// Dumps some info about the packet.
		private void debug(PacketBuffer buf, Side side, String state) {

			buf.markReaderIndex();
			int id = buf.readUnsignedByte();
			buf.resetReaderIndex();

			int type = id & 0x7F;
			int compressed = id & 0x80;

			log.info("Inbound packet {}, Channel: {}, Side: {}, Size: {}, Type: {}, Compressed: {}, Bits: {}",//
					state,//
					channelName,//
					side,//
					buf.readableBytes(),//
					type,//
					compressed > 0 ? "true" : "false",//
					Integer.toBinaryString(id)//
			);
		}
	}

}
