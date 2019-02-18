package cofh.core.block;

import cofh.lib.block.IDismantleable;
import cofh.lib.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

import static cofh.lib.util.Constants.MODEL_PROPERTIES;

public abstract class BlockTileCoFH extends Block implements IDismantleable {

	public BlockTileCoFH() {

		this(Material.IRON.getMaterialMapColor());
	}

	public BlockTileCoFH(MapColor color) {

		this(Material.IRON, color);
	}

	public BlockTileCoFH(Material blockMaterial, MapColor color) {

		super(blockMaterial, color);

		setHardness(3.0F);
		setResistance(5.0F);
		setSoundType(SoundType.METAL);
	}

	@Override
	protected BlockStateContainer createBlockState() {

		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		addBlockStateProperties(builder);
		return builder.build();
	}

	protected void addBlockStateProperties(BlockStateContainer.Builder builder) {

		builder.add(MODEL_PROPERTIES);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

		drops.addAll(dropDelegate(getItemStackTag(world, pos), world, pos, fortune));
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {

		super.harvestBlock(world, player, pos, state, te, stack);
		world.setBlockToAir(pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {

		return true;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

		if (willHarvest) {
			return true; // If it will harvest, delay deletion of the block until after getDrops
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		float hardness = super.getPlayerRelativeBlockHardness(state, player, world, pos);
		return tile instanceof TileCoFH ? ((TileCoFH) tile).getPlayerRelativeBlockHardness(hardness, state, player) : hardness;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {

		Item item = Item.getItemFromBlock(this);

		if (item == Items.AIR) {
			return ItemStack.EMPTY;
		}
		ItemStack retStack = new ItemStack(item);
		retStack.setTagCompound(getItemStackTag(world, pos));

		return retStack;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		return null;
	}

	// region PASSTHROUGHS
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH) {
			((TileCoFH) tile).onBlockBroken(world, pos, state);
		}
		if (tile != null) {
			world.removeTileEntity(pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH) {
			((TileCoFH) tile).onNeighborBlockChange();
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH && !tile.isInvalid()) {
			return ((TileCoFH) tile).openGui(player);
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH) {
			((TileCoFH) tile).onBlockPlacedBy(world, pos, state, placer, stack);
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCoFH) {
			((TileCoFH) tile).onNeighborTileChange(neighbor);
		}
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileCoFH ? ((TileCoFH) tile).getComparatorInputOverride() : super.getComparatorInputOverride(state, world, pos);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileCoFH ? ((TileCoFH) tile).getLightValue() : super.getLightValue(state, world, pos);
	}

	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileCoFH ? ((TileCoFH) tile).getBlockHardness(blockHardness) : super.getBlockHardness(state, world, pos);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileCoFH ? ((TileCoFH) tile).getExplosionResistance(blockResistance) : super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileCoFH ? ((TileCoFH) tile).getExtendedState(state) : state;
	}
	// endregion

	// region HELPERS
	protected NBTTagCompound getItemStackTag(IBlockAccess world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileCoFH ? ((TileCoFH) tile).getItemStackTag() : null;

		// TODO: Fix
		//		if (tile instanceof TileNameable && (!((TileNameable) tile).customName.isEmpty())) {
		//			retTag = ItemHelper.setItemStackTagName(retTag, ((TileNameable) tile).customName);
		//		}
		//		if (tile instanceof TileAugmentableSecure) {
		//			retTag.setBoolean(CoreProps.CREATIVE, ((TileAugmentableSecure) tile).isCreative);
		//			retTag.setByte(CoreProps.LEVEL, (byte) ((TileAugmentableSecure) tile).getLevel());
		//			if (((TileAugmentableSecure) tile).isSecured()) {
		//				retTag = SecurityHelper.setItemStackTagSecure(retTag, (ISecurable) tile);
		//			}
		//		}
		//		if (tile instanceof IAugmentable) {
		//			retTag = AugmentHelper.setItemStackTagAugments(retTag, (IAugmentable) tile);
		//		}
		//		if (tile instanceof IRedstoneControl) {
		//			retTag = RedstoneControlHelper.setItemStackTagRS(retTag, (IRedstoneControl) tile);
		//		}
		//		if (tile instanceof TileReconfigurable) {
		//			retTag = ReconfigurableHelper.setItemStackTagReconfig(retTag, (TileReconfigurable) tile);
		//		}
		//		if (tile instanceof IEnergyHandler) {
		//			retTag.setInteger(CoreProps.ENERGY, ((IEnergyHandler) tile).getEnergyStored(null));
		//		}
	}

	protected ArrayList<ItemStack> dropDelegate(NBTTagCompound nbt, IBlockAccess world, BlockPos pos, int fortune) {

		ItemStack dropBlock = new ItemStack(this);
		if (nbt != null) {
			dropBlock.setTagCompound(nbt);
		}
		ArrayList<ItemStack> ret = new ArrayList<>();
		ret.add(dropBlock);
		return ret;
	}

	protected ArrayList<ItemStack> dismantleDelegate(NBTTagCompound nbt, World world, BlockPos pos, EntityPlayer player, boolean returnDrops) {

		ItemStack dropBlock = new ItemStack(this);
		if (nbt != null) {
			dropBlock.setTagCompound(nbt);
		}
		world.setBlockToAir(pos);
		if (!returnDrops) {
			Utils.dropDismantleStackIntoWorld(dropBlock, world, pos);
		}
		ArrayList<ItemStack> ret = new ArrayList<>();
		ret.add(dropBlock);
		return ret;
	}
	// endregion

	// region IDismantleable
	@Override
	public ArrayList<ItemStack> dismantleBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player, boolean returnDrops) {

		return dismantleDelegate(getItemStackTag(world, pos), world, pos, player, returnDrops);
	}

	@Override
	public boolean canDismantle(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		TileEntity tile = world.getTileEntity(pos);
		return !(tile instanceof TileCoFH) || ((TileCoFH) tile).canDismantle(world, pos, state, player);
	}
	// endregion
}
