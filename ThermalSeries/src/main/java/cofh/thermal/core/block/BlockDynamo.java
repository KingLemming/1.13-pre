package cofh.thermal.core.block;

import cofh.core.block.TileCoFH;
import cofh.lib.util.helpers.FluidHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockDynamo extends BlockAbstractTile6Way {

	public BlockDynamo(AbstractTileType type) {

		super(type);
	}

	@Override
	public boolean isFullCube(IBlockState state) {

		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {

		return false;
	}

	// region HELPERS
	protected boolean onBlockActivatedDelegate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		TileCoFH tile = (TileCoFH) world.getTileEntity(pos);
		if (tile == null || !tile.canAccess(player)) {
			return false;
		}
		if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
			ItemStack heldItem = player.getHeldItem(hand);
			IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			if (FluidHelper.isFluidHandler(heldItem)) {
				FluidHelper.interactWithHandler(heldItem, handler, player, hand);
				return true;
			}
		}
		return false;
	}
	// endregion
}
