package cofh.core.network.packet;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

/**
 * Packet sent FROM Servers TO Clients
 *
 * @author covers1624
 */
public interface IPacketClient<P extends IPacketClient<P>> extends IPacket<P> {

	/**
	 * Handle the packet on the client side.
	 */
	void handleClient();

	/**
	 * Sends this packet to all clients on the server.
	 */
	default void sendToClients() {

		PlayerList list = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
		list.sendPacketToAllPlayers(toFMLPacket());
	}

	/**
	 * Sends this packet to the specified player.
	 *
	 * @param playerMP The player to send the packet to.
	 */
	default void sendToPlayer(EntityPlayerMP playerMP) {

		playerMP.connection.sendPacket(toFMLPacket());
	}

	/**
	 * Sends this packet to all players in the specified dimension.
	 *
	 * @param dim The dimension.
	 */
	default void sendToDimension(int dim) {

		PlayerList list = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
		list.sendPacketToAllPlayersInDimension(toFMLPacket(), dim);
	}

	/**
	 * Sends this packet to all server operators.
	 */
	default void sendToOps() {

		FMLProxyPacket packet = null;
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		PlayerList playerList = server.getPlayerList();
		for (EntityPlayerMP player : playerList.getPlayers()) {
			if (playerList.canSendCommands(player.getGameProfile())) {
				if (packet == null) { //So we don't serialize multiple times.
					packet = toFMLPacket();
				}
				player.connection.sendPacket(packet);
			}
		}
	}

	// region sendToAllAround

	/**
	 * Sends this packet to all players around the specified point.
	 * This method is an overload of {@link #sendToAllAround(double, double, double, double, int)}
	 *
	 * @param pos   The pos.
	 * @param range The range.
	 * @param dim   The dimension
	 */
	default void sendToAllAround(BlockPos pos, double range, int dim) {

		sendToAllAround(pos.getX(), pos.getY(), pos.getZ(), range, dim);
	}

	/**
	 * Sends this packet to all players around the specified point.
	 * This method is an overload of {@link #sendToAllAround(double, double, double, double, int)}
	 *
	 * @param pos   The pos.
	 * @param range The range.
	 * @param dim   The dimension
	 */
	default void sendToAllAround(Vec3d pos, double range, int dim) {

		sendToAllAround(pos.x, pos.y, pos.z, range, dim);
	}

	/**
	 * Sends this packet to all players around the specified point.
	 *
	 * @param x     The x position.
	 * @param y     The y position.
	 * @param z     The z position.
	 * @param range The range.
	 * @param dim   The dimension
	 */
	default void sendToAllAround(double x, double y, double z, double range, int dim) {

		PlayerList list = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
		list.sendToAllNearExcept(null, x, y, z, range, dim, toFMLPacket());
	}
	// endregion

	// region sendToChunk

	/**
	 * Sends this packet to all clients watching the chunk.
	 * This method is an overload of {@link #sendToChunk(WorldServer, BlockPos)}
	 *
	 * @param tile The tile, used as a reference for the chunk.
	 */
	default void sendToChunk(TileEntity tile) {

		sendToChunk((WorldServer) tile.getWorld(), tile.getPos());
	}

	/**
	 * Sends this packet to all clients watching the chunk.
	 * This method is an overload of {@link #sendToChunk(WorldServer, int, int)}
	 *
	 * @param world The world the chunk is in.
	 * @param pos   The pos, This is a position of a block, not a chunk.
	 */
	default void sendToChunk(WorldServer world, BlockPos pos) {

		sendToChunk(world, pos.getX() >> 4, pos.getZ() >> 4);
	}

	/**
	 * Sends this packet to all clients watching the chunk.
	 * This method is an overload of {@link #sendToChunk(WorldServer, int, int)}
	 *
	 * @param world The world.
	 * @param pos   The pos.
	 */
	default void sendToChunk(WorldServer world, ChunkPos pos) {

		sendToChunk(world, pos.x, pos.z);
	}

	/**
	 * Sends this packet to all clients watching the chunk.
	 *
	 * @param world  The world.
	 * @param chunkX The chunk's X coord.
	 * @param chunkZ The chunk'z Z coord.
	 */
	default void sendToChunk(WorldServer world, int chunkX, int chunkZ) {

		PlayerChunkMapEntry entry = world.getPlayerChunkMap().getEntry(chunkX, chunkZ);
		if (entry != null) {
			entry.sendPacket(toFMLPacket());
		}
	}
	// endregion
}
