package cofh.thermal.core.block;

import cofh.core.block.BlockTileCoFH;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cofh.lib.util.Constants.ACTIVE;
import static cofh.lib.util.Constants.FACING_HORIZONTAL;

public class BlockAbstractTile4Way extends BlockTileCoFH {

	public final AbstractTileType type;

	public BlockAbstractTile4Way(AbstractTileType type) {

		this.type = type;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_HORIZONTAL, EnumFacing.NORTH).withProperty(ACTIVE, false));
	}

	@Override
	protected void addBlockStateProperties(BlockStateContainer.Builder builder) {

		super.addBlockStateProperties(builder);
		builder.add(FACING_HORIZONTAL);
		builder.add(ACTIVE);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		return type.createTileEntity(world, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

		return getDefaultState().withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite()).withProperty(ACTIVE, false);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	// region 1.12
	@Override
	public int getMetaFromState(IBlockState state) {

		return state.getValue(FACING_HORIZONTAL).getHorizontalIndex() + (state.getValue(ACTIVE) ? 4 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		return getDefaultState().withProperty(FACING_HORIZONTAL, EnumFacing.getHorizontal(meta)).withProperty(ACTIVE, meta >= 4);
	}
	// endregion
}
