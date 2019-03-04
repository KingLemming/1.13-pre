package cofh.core.block;

import cofh.core.fluid.BlockFluidCoFH;
import cofh.core.render.particle.EntityDropParticleFX;
import cofh.lib.util.Utils;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockOreFluid extends BlockCoFH {

	protected BlockFluidCoFH fluid;

	public BlockOreFluid(BlockFluidCoFH fluid) {

		this(Material.ROCK.getMaterialMapColor(), fluid);
	}

	public BlockOreFluid(MapColor color, BlockFluidCoFH fluid) {

		this(Material.ROCK, color, fluid);
	}

	public BlockOreFluid(Material blockMaterial, MapColor color, BlockFluidCoFH fluid) {

		super(blockMaterial, color);
		this.fluid = fluid;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		return true;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

		ItemStack stack = player.getHeldItemMainhand();

		if (player.capabilities.isCreativeMode || willHarvest && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
			return world.setBlockState(pos, net.minecraft.init.Blocks.AIR.getDefaultState(), 3);
		}
		if (Utils.isServerWorld(world)) {
			this.onBlockHarvested(world, pos, state, player);
		}
		return world.setBlockState(new BlockPos(pos), fluid.getDefaultState().withProperty(BlockFluidCoFH.LEVEL, 1), 3);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		BlockPos north = pos.add(0, 0, -1);
		BlockPos south = pos.add(0, 0, 1);
		BlockPos west = pos.add(-1, 0, 0);
		BlockPos east = pos.add(1, 0, 0);

		if (world.isAirBlock(north)) {
			world.setBlockState(north, fluid.getDefaultState().withProperty(BlockFluidCoFH.LEVEL, 1), 3);
		}
		if (world.isAirBlock(south)) {
			world.setBlockState(south, fluid.getDefaultState().withProperty(BlockFluidCoFH.LEVEL, 1), 3);
		}
		if (world.isAirBlock(west)) {
			world.setBlockState(west, fluid.getDefaultState().withProperty(BlockFluidCoFH.LEVEL, 1), 3);
		}
		if (world.isAirBlock(east)) {
			world.setBlockState(east, fluid.getDefaultState().withProperty(BlockFluidCoFH.LEVEL, 1), 3);
		}
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		double px = pos.getX() + rand.nextFloat();
		double py = pos.getY() - 0.05D;
		double pz = pos.getZ() + rand.nextFloat();

		int density = fluid.getDensitySafe();
		int densityDir = fluid.getDensityDirSafe();

		if (density < 0) {
			py = pos.getY() + 1.10D;
		}
		if (rand.nextInt(20) == 0 && !world.isSideSolid(pos.add(0, densityDir, 0), densityDir == -1 ? EnumFacing.UP : EnumFacing.DOWN) && !world.getBlockState(pos.add(0, densityDir, 0)).getMaterial().blocksMovement()) {
			Particle fx = new EntityDropParticleFX(world, px, py, pz, fluid.getParticleRed(), fluid.getParticleGreen(), fluid.getParticleBlue(), densityDir);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
		}
	}

}
