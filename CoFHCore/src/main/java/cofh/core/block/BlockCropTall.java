package cofh.core.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

import static cofh.lib.util.Constants.AGE_TALL;

public class BlockCropTall extends BlockCrop {

	protected int splitAge = 4;

	public BlockCropTall() {

		super();
	}

	public BlockCropTall(Material blockMaterialIn, EnumPlantType type) {

		super(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), type);
	}

	public BlockCropTall(Material blockMaterialIn, MapColor blockMapColorIn, EnumPlantType type) {

		super(blockMaterialIn, blockMapColorIn, type);
	}

	protected PropertyInteger getAgeProperty() {

		return AGE_TALL;
	}

	protected boolean isHarvestable(IBlockState state) {

		return getAge(state) == getHarvestAge() + (isTop(state) ? getSplitAge() : 0);
	}

	protected boolean isTop(IBlockState state) {

		return getAge(state) > getMaximumAge();
	}

	public int getSplitAge() {

		return 4;
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
			if (!isHarvestable(state)) {
				int age = getAge(state);
				float growthChance = getGrowthChance(this, worldIn, pos);

				if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
					int newAge = age + 1 > getMaximumAge() ? getHarvestAge() : age + 1;
					worldIn.setBlockState(pos, this.withAge(newAge), 2);

					if (newAge >= getSplitAge()) {
						worldIn.setBlockState(pos.up(), this.withAge(newAge + getSplitAge()), 2);
					}
					ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
				}
			}
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {

		if (isTop(state)) {
			BlockPos below = pos.down();
			if (worldIn.getBlockState(below).getBlock() == this) {
				worldIn.setBlockState(below, Blocks.AIR.getDefaultState(), 2);
			}
		} else {
			BlockPos above = pos.up();
			if (worldIn.getBlockState(above).getBlock() == this) {
				worldIn.setBlockState(above, Blocks.AIR.getDefaultState(), 2);
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {

		return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
	}

	// region IGrowable
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

		if (isHarvestable(state)) {
			return;
		}
		if (isTop(state)) {
			int age = this.getAge(state) - getSplitAge();
			int boost = this.getBonemealAgeIncrease(worldIn);
			int newAge = age + boost > getMaximumAge() ? getHarvestAge() : age + boost;
			worldIn.setBlockState(pos, this.withAge(newAge + getSplitAge()), 2);
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
				worldIn.setBlockState(above, this.withAge(newAge + getSplitAge()), 2);
			}
		}
	}
	// endregion
}
