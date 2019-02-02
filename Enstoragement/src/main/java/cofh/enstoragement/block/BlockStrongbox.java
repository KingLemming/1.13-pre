package cofh.enstoragement.block;

import cofh.core.block.BlockTileCoFH;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockStrongbox extends BlockTileCoFH {

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		return new TileStrongbox(18);
	}

}
