package cofh.core.block.rails;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static cofh.lib.util.Constants.RAIL_SINGLE;

public class BlockRailCrossover extends BlockRailDefault {

	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {

		return RAIL_SINGLE;
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
	public EnumRailDirection getRailDirection(IBlockAccess world, BlockPos pos, IBlockState state, @Nullable EntityMinecart cart) {

		if (cart != null) {
			if (Math.abs(cart.motionX) > 0) {
				return EnumRailDirection.EAST_WEST;
			} else if (Math.abs(cart.motionZ) > 0) {
				return EnumRailDirection.NORTH_SOUTH;
			}
		}
		return state.getValue(getShapeProperty());
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {

		return state;
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {

		return state;
	}

}
