package cofh.lib.util.helpers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHelper {

	private BlockHelper() {

	}

	// region TILE ENTITIES
	public static TileEntity getAdjacentTileEntity(World world, BlockPos pos, EnumFacing dir) {

		pos = pos.offset(dir);
		return world == null || !world.isBlockLoaded(pos) ? null : world.getTileEntity(pos);
	}

	public static TileEntity getAdjacentTileEntity(World world, BlockPos pos, int side) {

		return world == null ? null : getAdjacentTileEntity(world, pos, EnumFacing.VALUES[side]);
	}

	public static TileEntity getAdjacentTileEntity(TileEntity refTile, EnumFacing dir) {

		return refTile == null ? null : getAdjacentTileEntity(refTile.getWorld(), refTile.getPos(), dir);
	}
	// endregion

	// region ROTATION

	// endregion
}
