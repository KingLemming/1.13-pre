package cofh.core.block.deco;

import cofh.core.block.BlockCoFH;
import cofh.lib.block.IDismantleable;
import cofh.lib.util.Utils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public class BlockGlassHard extends BlockCoFH implements IDismantleable {

	protected float[] beaconColorMult = new float[] { 1.0F, 1.0F, 1.0F };

	public BlockGlassHard() {

		this(Material.GLASS.getMaterialMapColor());
	}

	public BlockGlassHard(MapColor color) {

		super(Material.GLASS, color);

		setHardness(5.0F);
		setResistance(500.0F);
		setSoundType(SoundType.GLASS);
	}

	public BlockGlassHard setBeaconColorMult(float r, float g, float b) {

		beaconColorMult = new float[] { r, g, b };
		return this;
	}

	@Override
	public int quantityDropped(Random rand) {

		return 0;
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {

		return false;
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {

		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {

		return true;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		return true;
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
	public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {

		return beaconColorMult;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {

		return BlockRenderLayer.CUTOUT;
	}

	// region IDismantleable
	@Override
	public ArrayList<ItemStack> dismantleBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player, boolean returnDrops) {

		ItemStack dropBlock = new ItemStack(this);
		world.setBlockToAir(pos);

		if (!returnDrops) {
			Utils.dropDismantleStackIntoWorld(dropBlock, world, pos);
		}
		ArrayList<ItemStack> ret = new ArrayList<>();
		ret.add(dropBlock);
		return ret;
	}

	@Override
	public boolean canDismantle(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		return true;
	}
	// endregion
}
