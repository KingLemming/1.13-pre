package cofh.core.block.crops;

import cofh.core.block.BlockCoFH;
import cofh.lib.block.IHarvestable;
import cofh.lib.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

import static cofh.lib.util.Constants.AGE;
import static cofh.lib.util.Constants.CROPS_AABB;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;

public class BlockCrop extends BlockCoFH implements IGrowable, IPlantable, IHarvestable {

	protected final EnumPlantType type;
	protected int reqLight = 9;

	protected ItemStack crop = ItemStack.EMPTY;
	protected ItemStack seed = ItemStack.EMPTY;

	public BlockCrop() {

		this(Material.PLANTS, EnumPlantType.Crop);
	}

	public BlockCrop(Material blockMaterial, EnumPlantType type) {

		this(blockMaterial, blockMaterial.getMaterialMapColor(), type);
	}

	public BlockCrop(Material blockMaterial, MapColor color, EnumPlantType type) {

		super(blockMaterial, color);
		this.type = type;

		this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
	}

	public BlockCrop setRequiredLight(int reqLight) {

		this.reqLight = reqLight;
		return this;
	}

	public BlockCrop setCrop(ItemStack crop) {

		this.crop = crop;
		return this;
	}

	public BlockCrop setSeed(ItemStack seed) {

		this.seed = seed;
		return this;
	}

	protected PropertyInteger getAgeProperty() {

		return AGE;
	}

	protected int getAge(IBlockState state) {

		return state.getValue(this.getAgeProperty());
	}

	protected ItemStack getCrop() {

		return crop;
	}

	protected ItemStack getSeed() {

		return seed;
	}

	protected boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {

		if (state.getBlock() == this) {
			IBlockState below = worldIn.getBlockState(pos.down());
			return below.getBlock().canSustainPlant(below, worldIn, pos.down(), EnumFacing.UP, this);
		}
		return true;
	}

	protected int getHarvestAge() {

		return 7;
	}

	protected int getMaximumAge() {

		return 7;
	}

	protected int getPostHarvestAge() {

		return -1;
	}

	public IBlockState withAge(int age) {

		return this.getDefaultState().withProperty(this.getAgeProperty(), age);
	}

	@Override
	protected BlockStateContainer createBlockState() {

		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		builder.add(getAgeProperty());
		return builder.build();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		return harvest(worldIn, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, playerIn.getHeldItem(hand)));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

		if (!canBlockStay(worldIn, pos, state)) {
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

		if (!worldIn.isAreaLoaded(pos, 1)) {
			return;
		}
		if (worldIn.getLightFromNeighbors(pos.up()) >= reqLight) {
			if (!canHarvest(state)) {
				int age = getAge(state);
				float growthChance = getGrowthChance(this, worldIn, pos);
				if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
					int newAge = age + 1 > getMaximumAge() ? getHarvestAge() : age + 1;
					worldIn.setBlockState(pos, this.withAge(newAge), 2);
					ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
				}
			}
		}
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {

		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {

		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		return CROPS_AABB[MathHelper.clamp(state.getValue(this.getAgeProperty()), 0, 7)];
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {

		return NULL_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state) {

		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {

		return false;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

		boolean noSeed = getSeed().isEmpty();
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;

		if (canHarvest(state)) {
			if (getCrop().isEmpty()) {
				super.getDrops(drops, world, pos, state, fortune);
			} else {
				ItemStack crop = getCrop();
				int cropCount = getCrop().getCount();
				if (noSeed) {
					for (int i = 0; i < 2; i++) {
						if (rand.nextFloat() < 0.5F) {
							cropCount++;
						}
					}
				}
				drops.add(cloneStack(crop, cropCount));
			}
		}
		if (noSeed) {
			return;
		}
		int age = getAge(state);
		int seedCount = 1;
		if (age >= getHarvestAge()) {
			for (int i = 0; i < 3 + fortune; i++) {
				if (rand.nextFloat() < 0.5F) {
					seedCount++;
				}
			}
		}
		drops.add(cloneStack(getSeed(), seedCount));
	}

	// region HELPERS
	protected int getBonemealAgeIncrease(World worldIn) {

		return MathHelper.getInt(worldIn.rand, 2, 5);
	}

	protected float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {

		float f = 1.0F;
		BlockPos blockpos = pos.down();
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));
				if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable) blockIn)) {
					f1 = 1.0F;
					if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(i, 0, j))) {
						f1 = 3.0F;
					}
				}
				if (i != 0 || j != 0) {
					f1 /= 4.0F;
				}
				f += f1;
			}
		}
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.south();
		BlockPos blockpos3 = pos.west();
		BlockPos blockpos4 = pos.east();
		boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
		boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();

		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();
			if (flag2) {
				f /= 2.0F;
			}
		}
		return f;
	}
	// endregion

	// region IGrowable
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {

		return !canHarvest(state);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {

		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

		if (canHarvest(state)) {
			return;
		}
		int age = this.getAge(state);
		int boost = this.getBonemealAgeIncrease(worldIn);

		int newAge = age + boost > getMaximumAge() ? getHarvestAge() : age + boost;
		worldIn.setBlockState(pos, this.withAge(newAge), 2);
	}
	// endregion

	// region IPlantable
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {

		return type;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {

		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) {
			return getDefaultState();
		}
		return state;
	}
	// endregion

	// region IHarvestable
	@Override
	public boolean canHarvest(IBlockState state) {

		return getAge(state) == getHarvestAge();
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
			world.setBlockState(pos, this.withAge(getPostHarvestAge()), 2);
		} else {
			world.destroyBlock(pos, true);
		}
		return true;
	}
	// endregion

	// region 1.12
	@Override
	public int getMetaFromState(IBlockState state) {

		return this.getAge(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		return this.withAge(meta);
	}
	// endregion
}
