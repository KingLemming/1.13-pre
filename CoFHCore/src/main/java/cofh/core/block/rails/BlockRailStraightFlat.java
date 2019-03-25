package cofh.core.block.rails;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.RAIL_STRAIGHT_FLAT;

public class BlockRailStraightFlat extends BlockRailDefault {

	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {

		return RAIL_STRAIGHT_FLAT;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

		//		if (!worldIn.isRemote) {
		//			state = this.updateDir(worldIn, pos, state, true);
		//
		//			if (this.isPowered) {
		//				state.neighborChanged(worldIn, pos, this, pos);
		//			}
		//		}
	}

	@Override
	public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {

		return false;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {

		IProperty<EnumRailDirection> shape = getShapeProperty();

		switch (rot) {
			case COUNTERCLOCKWISE_90:
				switch (state.getValue(shape)) {
					case NORTH_SOUTH:
						return state.withProperty(shape, EnumRailDirection.EAST_WEST);
					case EAST_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_SOUTH);
				}
			case CLOCKWISE_90:
				switch (state.getValue(shape)) {
					case NORTH_SOUTH:
						return state.withProperty(shape, EnumRailDirection.EAST_WEST);
					case EAST_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_SOUTH);
				}
			default:
				return state;
		}
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {

		return state;
	}

}
