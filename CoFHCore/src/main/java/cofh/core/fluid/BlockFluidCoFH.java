package cofh.core.fluid;

import cofh.lib.render.particle.EntityDropParticleFX;
import cofh.lib.util.StateMapper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFluidCoFH extends BlockFluidClassic {

	protected final String modID;
	protected final String name;

	protected float particleRed = 1.0F;
	protected float particleGreen = 1.0F;
	protected float particleBlue = 1.0F;
	protected boolean shouldDisplaceFluids = false;

	public BlockFluidCoFH(Fluid fluid, Material material, String name, String modID) {

		super(fluid, material);

		this.name = name;
		this.modID = modID;
		this.displacements.put(this, false);

		//		int color = fluid.getColor();
		//		if (color != 0xFFFFFFFF) {
		//			this.particleRed = ((color >> 16) & 0xFF) / 255F;
		//			this.particleGreen = ((color >> 8) & 0xFF) / 255F;
		//			this.particleBlue = ((color >> 0) & 0xFF) / 255F;
		//		}
	}

	public BlockFluidCoFH setParticleColor(float particleRed, float particleGreen, float particleBlue) {

		this.particleRed = particleRed;
		this.particleGreen = particleGreen;
		this.particleBlue = particleBlue;

		return this;
	}

	@Override
	public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {

		if (this.density < 0) {
			return false;
		}
		if (testingHead) {
			return true;
		}
		//		if (iblockstate.getMaterial().equals(this.blockMaterial)) {
		//			if (yToTest <= blockpos.getY() + 1 + getQuantaPercentage(world, blockpos)) {
		//				return true;
		//			}
		//		}
		return super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {

		return false;
	}

	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {

		if (!shouldDisplaceFluids && world.getBlockState(pos).getMaterial().isLiquid()) {
			return false;
		}
		return super.canDisplace(world, pos);
	}

	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {

		if (!shouldDisplaceFluids && world.getBlockState(pos).getMaterial().isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, pos);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		double px = pos.getX() + rand.nextFloat();
		double py = pos.getY() - 1.05D;
		double pz = pos.getZ() + rand.nextFloat();

		if (density < 0) {
			py = pos.getY() + 2.10D;
		}
		if (rand.nextInt(20) == 0 && world.isSideSolid(pos.add(0, densityDir, 0), densityDir == -1 ? EnumFacing.UP : EnumFacing.DOWN) && !world.getBlockState(pos.add(0, 2 * densityDir, 0)).getMaterial().blocksMovement()) {
			Particle fx = new EntityDropParticleFX(world, px, py, pz, particleRed, particleGreen, particleBlue, densityDir);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
		}
	}

	@Override
	@SideOnly (Side.CLIENT)
	public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks) {

		return new Vec3d(getParticleRed(), getParticleGreen(), getParticleBlue());
	}

	public int getDensitySafe() {

		return density;
	}

	public int getDensityDirSafe() {

		return densityDir;
	}

	public float getParticleRed() {

		return particleRed;
	}

	public float getParticleGreen() {

		return particleGreen;
	}

	public float getParticleBlue() {

		return particleBlue;
	}

	// region MODEL
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		Item item = Item.getItemFromBlock(this);
		StateMapper mapper = new StateMapper(modID, "fluids", name);

		// Item Model
		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, mapper);
		// Block Model
		ModelLoader.setCustomStateMapper(this, mapper);
	}
	// endregion
}
