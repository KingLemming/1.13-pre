package cofh.core.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.util.HashMap;

public abstract class BlockFluidInteractive extends BlockFluidCoFH {

	private final HashMap<Block, IBlockState> blockCollisionMap = new HashMap<>();
	private final HashMap<IBlockState, IBlockState> stateCollisionMap = new HashMap<>();

	public BlockFluidInteractive(Fluid fluid, Material material, String name, String modID) {

		super(fluid, material, name, modID);
	}

	public boolean addInteraction(Block pre, Block post) {

		if (pre == null || post == null) {
			return false;
		}
		return addInteraction(pre.getDefaultState(), post.getDefaultState(), true);
	}

	public boolean addInteraction(IBlockState pre, Block post) {

		return addInteraction(pre, post.getDefaultState(), false);
	}

	public boolean addInteraction(IBlockState pre, IBlockState post) {

		return addInteraction(pre, post, false);
	}

	public boolean addInteraction(IBlockState pre, IBlockState post, boolean useBlock) {

		if (pre == null || post == null) {
			return false;
		}
		if (useBlock) {
			blockCollisionMap.put(pre.getBlock(), post);
		} else {
			stateCollisionMap.put(pre, post);
		}
		return true;
	}

	protected IBlockState getInteraction(IBlockState state) {

		if (stateCollisionMap.containsKey(state)) {
			return stateCollisionMap.get(state);
		}
		return blockCollisionMap.get(state.getBlock());
	}

	protected boolean hasInteraction(IBlockState state) {

		return stateCollisionMap.containsKey(state) || blockCollisionMap.containsKey(state.getBlock());
	}

	protected void interactWithSurroundings(World world, BlockPos pos) {

		if (world.getBlockState(pos).getBlock() != this) {
			return;
		}
		for (EnumFacing face : EnumFacing.VALUES) {
			interactWithBlock(world, pos.offset(face));
		}
		//Corners
		interactWithBlock(world, pos.add(-1, 0, -1));
		interactWithBlock(world, pos.add(-1, 0, 1));
		interactWithBlock(world, pos.add(1, 0, -1));
		interactWithBlock(world, pos.add(1, 0, 1));
	}

	protected abstract void interactWithBlock(World world, BlockPos pos);

}
