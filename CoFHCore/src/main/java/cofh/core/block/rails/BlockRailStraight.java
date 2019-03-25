package cofh.core.block.rails;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.RAIL_STRAIGHT;

public class BlockRailStraight extends BlockRailDefault {

	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {

		return RAIL_STRAIGHT;
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
	public IBlockState withRotation(IBlockState state, Rotation rot) {

		IProperty<EnumRailDirection> shape = getShapeProperty();

		switch (rot) {
			case CLOCKWISE_180:
				switch (state.getValue(shape)) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
				}
			case COUNTERCLOCKWISE_90:
				switch (state.getValue(shape)) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
					case NORTH_SOUTH:
						return state.withProperty(shape, EnumRailDirection.EAST_WEST);
					case EAST_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_SOUTH);
				}
			case CLOCKWISE_90:
				switch (state.getValue(shape)) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
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

		IProperty<EnumRailDirection> shape = getShapeProperty();
		EnumRailDirection direction = state.getValue(shape);

		switch (mirrorIn) {
			case LEFT_RIGHT:
				switch (direction) {
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
				}

			case FRONT_BACK:
				switch (direction) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
				}
		}
		return super.withMirror(state, mirrorIn);
	}

}
