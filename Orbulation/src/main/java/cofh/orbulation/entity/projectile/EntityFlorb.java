package cofh.orbulation.entity.projectile;

import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import cofh.orbulation.util.FlorbUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static cofh.lib.util.Constants.*;

public class EntityFlorb extends EntityThrowable {

	public static final String NAME = "florb";
	private static DataParameter<String> FLUID_NAME = EntityDataManager.createKey(EntityFlorb.class, DataSerializers.STRING);

	private static ItemStack blockCheck = new ItemStack(Blocks.STONE);

	protected float gravity = 0.03F;
	protected Fluid fluid;

	public static void initialize(int id) {

		ForgeRegistries.ENTITIES.register(EntityEntryBuilder.create()//
				.entity(EntityFlorb.class)//
				.id(new ResourceLocation(ID_ORBULATION + ":" + NAME), id)//
				.name(ID_ORBULATION + "." + NAME)//
				.tracker(ENTITY_TRACKING_DISTANCE, 1, true)//
				.build());
	}

	public EntityFlorb(World world) {

		super(world);
	}

	public EntityFlorb(World world, EntityLivingBase thrower, Fluid fluid) {

		super(world, thrower);
		this.fluid = fluid;

		setGravity();
		this.dataManager.set(FLUID_NAME, fluid.getName());
	}

	public EntityFlorb(World world, double x, double y, double z, Fluid fluid) {

		super(world, x, y, z);
		this.fluid = fluid;

		setGravity();
		this.dataManager.set(FLUID_NAME, fluid.getName());
	}

	private void setGravity() {

		if (fluid.getDensity() < 0) {
			this.gravity = MathHelper.minF(0.01F, 0.03F + 0.03F * fluid.getDensity() / 1000F);
		}
	}

	@Override
	protected void entityInit() {

		super.entityInit();
		dataManager.register(FLUID_NAME, "");
	}

	@Override
	protected float getGravityVelocity() {

		return gravity;
	}

	public Fluid getFluid() {

		return fluid;
	}

	@Override
	public void onEntityUpdate() {

		if (fluid == null && Utils.isClientWorld(world)) {
			fluid = FluidRegistry.getFluid(dataManager.get(FLUID_NAME));
		}
		super.onEntityUpdate();
	}

	@Override
	protected void onImpact(RayTraceResult traceResult) {

		BlockPos pos = traceResult.getBlockPos();

		if (traceResult.entityHit != null) {
			pos = traceResult.entityHit.getPosition().add(0, 1, 0);
			traceResult.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0F);
		}

		if (traceResult.sideHit != null && !world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
			pos = pos.offset(traceResult.sideHit);
		}
		if (Utils.isServerWorld(world)) {
			if (traceResult.sideHit != null && getThrower() instanceof EntityPlayer && !((EntityPlayer) getThrower()).canPlayerEdit(pos, traceResult.sideHit, blockCheck)) {
				FlorbUtils.dropFlorb(getFluid(), world, pos);
				this.setDead();
				return;
			}
			Block block = fluid.getBlock();
			IBlockState state = world.getBlockState(pos);

			if ("water".equals(fluid.getName())) {
				block = Blocks.FLOWING_WATER;
			} else if ("lava".equals(fluid.getName())) {
				block = Blocks.FLOWING_LAVA;
			} else if (block == null) {
				block = Blocks.FLOWING_WATER;
			}
			if (world.isAirBlock(pos) || state.getMaterial() == Material.FIRE || state.getBlock().isReplaceable(world, pos)) {
				if (!fluid.getName().equals("water") || !BiomeDictionary.hasType(world.getBiome(pos), Type.NETHER)) {
					world.setBlockState(pos, block.getDefaultState(), 3);
					world.notifyBlockUpdate(pos, state, state, 3);
				}
			} else {
				FlorbUtils.dropFlorb(getFluid(), world, pos);
			}
			this.setDead();
		}
	}

	// region NBT
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {

		super.readEntityFromNBT(nbt);

		fluid = FluidRegistry.getFluid(nbt.getString(TAG_FLUID));

		if (fluid == null || fluid.getBlock() == null) {
			fluid = FluidRegistry.WATER;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {

		super.writeEntityToNBT(nbt);

		nbt.setString(TAG_FLUID, fluid.getName());
	}
	// endregion
}
