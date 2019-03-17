package cofh.core.block;

import cofh.lib.util.Utils;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

import static cofh.lib.util.Constants.AGE_TALL;

public class BlockCropTall extends BlockCrop {

	protected int splitAge = 4;
	protected int splitOffset;

	public BlockCropTall() {

		super();
		this.splitOffset = getSplitAge() + getMaximumAge() - getHarvestAge();
	}

	public BlockCropTall(Material blockMaterialIn, EnumPlantType type) {

		super(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), type);
	}

	public BlockCropTall(Material blockMaterialIn, MapColor blockMapColorIn, EnumPlantType type) {

		super(blockMaterialIn, blockMapColorIn, type);
	}

	public BlockCropTall setSplitAge(int splitAge) {

		this.splitAge = splitAge;
		this.splitOffset = getSplitAge() + getMaximumAge() - getHarvestAge();

		return this;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {

		if (state.getBlock() == this) {
			IBlockState below = worldIn.getBlockState(pos.down());
			return isTop(state) ? below.getBlock() == this && !isTop(below) : below.getBlock().canSustainPlant(below, worldIn, pos.down(), EnumFacing.UP, this);
		}
		return true;
	}

	protected boolean isTop(IBlockState state) {

		return getAge(state) > getMaximumAge();
	}

	protected int getSplitAge() {

		return splitAge;
	}

	protected int getSplitOffset() {

		return splitOffset;
	}

	@Override
	protected PropertyInteger getAgeProperty() {

		return AGE_TALL;
	}

	@Override
	protected BlockStateContainer createBlockState() {

		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		builder.add(getAgeProperty());
		return builder.build();
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

		if (!worldIn.isAreaLoaded(pos, 1) || isTop(state)) {
			return;
		}
		BlockPos above = pos.up();
		if (worldIn.getBlockState(above).getBlock() != this && !worldIn.isAirBlock(above)) {
			return;
		}
		if (worldIn.getLightFromNeighbors(pos.up()) >= reqLight) {
			if (!canHarvest(state)) {
				int age = getAge(state);
				float growthChance = getGrowthChance(this, worldIn, pos);
				if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
					int newAge = age + 1 > getMaximumAge() ? getHarvestAge() : age + 1;
					worldIn.setBlockState(pos, this.withAge(newAge), 2);
					if (newAge >= getSplitAge()) {
						worldIn.setBlockState(pos.up(), this.withAge(newAge + getSplitOffset()), 2);
					}
					ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
				}
			}
		}
	}

	//	@Override
	//	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
	//
	//		if (isTop(state)) {
	//			BlockPos below = pos.down();
	//			if (worldIn.getBlockState(below).getBlock() == this) {
	//				worldIn.setBlockState(below, Blocks.AIR.getDefaultState(), 2);
	//				Utils.dropItemStackIntoWorldWithVelocity(getCrop(), worldIn, below);
	//			}
	//		} else {
	//			BlockPos above = pos.up();
	//			if (worldIn.getBlockState(above).getBlock() == this) {
	//				worldIn.setBlockState(above, Blocks.AIR.getDefaultState(), 2);
	//				Utils.dropItemStackIntoWorldWithVelocity(getCrop(), worldIn, above);
	//			}
	//		}
	//		super.onBlockHarvested(worldIn, pos, state, player);
	//	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {

		return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
	}

	// region IGrowable
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

		if (canHarvest(state)) {
			return;
		}
		if (isTop(state)) {
			int age = this.getAge(state) - getSplitOffset();
			int boost = this.getBonemealAgeIncrease(worldIn);
			int newAge = age + boost > getMaximumAge() ? getHarvestAge() : age + boost;
			worldIn.setBlockState(pos, this.withAge(newAge + getSplitOffset()), 2);
			worldIn.setBlockState(pos.down(), this.withAge(newAge), 2);
		} else {
			BlockPos above = pos.up();
			if (worldIn.getBlockState(above).getBlock() != this && !worldIn.isAirBlock(above)) {
				return;
			}
			int age = this.getAge(state);
			int boost = this.getBonemealAgeIncrease(worldIn);
			int newAge = age + boost > getMaximumAge() ? getHarvestAge() : age + boost;
			worldIn.setBlockState(pos, this.withAge(newAge), 2);
			if (newAge >= getSplitAge()) {
				worldIn.setBlockState(above, this.withAge(newAge + getSplitOffset()), 2);
			}
		}
	}
	// endregion

	// region IHarvestable
	@Override
	public boolean canHarvest(IBlockState state) {

		return getAge(state) == getHarvestAge() + (isTop(state) ? getSplitOffset() : 0);
	}

	@Override
	public boolean harvest(World world, BlockPos pos, IBlockState state, int fortune) {

		if (!canHarvest(state)) {
			return false;
		}
		if (Utils.isClientWorld(world)) {
			return true;
		}
		if (getPostHarvestAge() >= 0) {
			Utils.dropItemStackIntoWorldWithVelocity(getCrop(), world, pos);
			if (isTop(state)) {
				world.setBlockState(pos, this.withAge(getPostHarvestAge() + getSplitOffset()), 2);
				world.setBlockState(pos.down(), this.withAge(getPostHarvestAge()), 2);
				Utils.dropItemStackIntoWorldWithVelocity(getCrop(), world, pos.down());
			} else {
				world.setBlockState(pos, this.withAge(getPostHarvestAge()), 2);
				world.setBlockState(pos.up(), this.withAge(getPostHarvestAge() + getSplitOffset()), 2);
				Utils.dropItemStackIntoWorldWithVelocity(getCrop(), world, pos.up());
			}
		} else {
			world.destroyBlock(pos, true);
			world.destroyBlock(isTop(state) ? pos.down() : pos.up(), true);
		}
		return true;
	}
	// endregion
}
