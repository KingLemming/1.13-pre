package cofh.thermal.core.block.dynamo;

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

import static cofh.lib.util.Constants.FACING_ALL;

public class BlockDynamo extends BlockTileCoFH {

	public final Dynamo dynamo;

	public BlockDynamo(Dynamo dynamo) {

		this.dynamo = dynamo;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_ALL, EnumFacing.UP));
	}

	@Override
	protected void addBlockStateProperties(BlockStateContainer.Builder builder) {

		super.addBlockStateProperties(builder);
		builder.add(FACING_ALL);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		return dynamo.createTileEntity(world, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

		return getDefaultState().withProperty(FACING_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	// region 1.12
	@Override
	public int getMetaFromState(IBlockState state) {

		return state.getValue(FACING_ALL).getIndex();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

		return state;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		EnumFacing facing = EnumFacing.getFront(meta);
		return getDefaultState().withProperty(FACING_ALL, facing);
	}
	// endregion
}
