package cofh.thermal.core.block.machine;

import cofh.core.block.BlockTileCoFH;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cofh.lib.util.Constants.FACING_HORIZONTAL;

public class BlockMachine extends BlockTileCoFH {

	public final Machine machine;

	public BlockMachine(Machine machine) {

		this.machine = machine;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_HORIZONTAL, EnumFacing.NORTH));
	}

	@Override
	protected BlockStateContainer createBlockState() {

		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		builder.add(FACING_HORIZONTAL);
		return builder.build();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		return machine.createTileEntity(world, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

		return getDefaultState().withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	// region 1.12
	@Override
	public int getMetaFromState(IBlockState state) {

		return state.getValue(FACING_HORIZONTAL).getIndex();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

		return state;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		EnumFacing facing = EnumFacing.getFront(meta);

		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING_HORIZONTAL, facing);
	}
	// endregion
}
