package cofh.orbulation.entity.projectile;

import cofh.lib.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;

import static cofh.lib.util.Constants.ENTITY_TRACKING_DISTANCE;
import static cofh.lib.util.Constants.ID_ORBULATION;
import static cofh.orbulation.util.MorbUtils.*;
import static net.minecraft.util.math.MathHelper.wrapDegrees;

public class EntityMorb extends EntityThrowable {

	public static final String NAME = "morb";
	private static DataParameter<Byte> TYPE = EntityDataManager.createKey(EntityMorb.class, DataSerializers.BYTE);
	private static DataParameter<NBTTagCompound> ENTITY_DATA = EntityDataManager.createKey(EntityMorb.class, DataSerializers.COMPOUND_TAG);

	private static ItemStack blockCheck = new ItemStack(Blocks.STONE);

	protected boolean drop = true;
	protected byte type = -1;
	protected NBTTagCompound entityData;

	public static void initialize(int id) {

		ForgeRegistries.ENTITIES.register(EntityEntryBuilder.create()//
				.entity(EntityMorb.class)//
				.id(new ResourceLocation(ID_ORBULATION + ":" + NAME), id)//
				.name(ID_ORBULATION + "." + NAME)//
				.tracker(ENTITY_TRACKING_DISTANCE, 1, true)//
				.build());
	}

	public EntityMorb(World world) {

		super(world);
	}

	public EntityMorb(World world, EntityLivingBase thrower, boolean reusable, NBTTagCompound entityData) {

		super(world, thrower);
		this.type = (byte) (reusable ? 1 : 0);
		this.entityData = entityData;

		if (thrower instanceof EntityPlayer && ((EntityPlayer) thrower).capabilities.isCreativeMode || entityData != null && entityData.getBoolean(GENERIC)) {
			drop = false;
		}
		this.dataManager.set(TYPE, type);
		this.dataManager.set(ENTITY_DATA, entityData);
	}

	public EntityMorb(World world, double x, double y, double z, boolean reusable, NBTTagCompound entityData) {

		super(world, x, y, z);
		this.type = (byte) (reusable ? 1 : 0);
		this.entityData = entityData;

		if (thrower instanceof EntityPlayer && ((EntityPlayer) thrower).capabilities.isCreativeMode || entityData != null && entityData.getBoolean(GENERIC)) {
			drop = false;
		}
		this.dataManager.set(TYPE, type);
		this.dataManager.set(ENTITY_DATA, entityData);
	}

	@Override
	protected void entityInit() {

		super.entityInit();
		dataManager.register(TYPE, type);
		dataManager.register(ENTITY_DATA, new NBTTagCompound());
	}

	public int getType() {

		return type;
	}

	public NBTTagCompound getStoredEntity() {

		return entityData;
	}

	@Override
	public void onEntityUpdate() {

		if (type < 0 && Utils.isClientWorld(world)) {
			type = dataManager.get(TYPE);
		}
		if ((entityData == null || !entityData.hasKey("id")) && Utils.isClientWorld(world)) {
			entityData = dataManager.get(ENTITY_DATA);
		}
		super.onEntityUpdate();
	}

	@Override
	protected void onImpact(RayTraceResult traceResult) {

		if (entityData == null || !entityData.hasKey("id")) {
			attemptCapture(traceResult);
		} else {
			attemptRelease(traceResult);
		}
	}

	// region HELPERS
	private void attemptCapture(RayTraceResult traceResult) {

		BlockPos pos = new BlockPos(traceResult.hitVec);

		if (Utils.isServerWorld(world)) {
			boolean noAccess = traceResult.sideHit != null && getThrower() instanceof EntityPlayer && !((EntityPlayer) getThrower()).canPlayerEdit(pos, traceResult.sideHit, blockCheck);

			if (traceResult.entityHit == null || EntityList.getKey(traceResult.entityHit) == null || !VALID_MOBS.contains(EntityList.getKey(traceResult.entityHit).toString()) || noAccess) {
				dropMorb(type, null, world, pos);
				this.setDead();
				return;
			}
			NBTTagCompound tag = traceResult.entityHit.serializeNBT();
			((WorldServer) world).spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 2, 0, 0, 0, 0.0, 0);
			dropMorb(type, tag, world, pos);
			traceResult.entityHit.setDead();
			this.setDead();
		}
	}

	private void attemptRelease(RayTraceResult traceResult) {

		BlockPos pos = new BlockPos(traceResult.hitVec);

		if (traceResult.entityHit != null) {
			pos = traceResult.entityHit.getPosition().add(0, 1, 0);
			traceResult.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0F);
		}
		if (Utils.isServerWorld(world)) {
			if (traceResult.sideHit != null && getThrower() instanceof EntityPlayer && !((EntityPlayer) getThrower()).canPlayerEdit(pos, traceResult.sideHit, blockCheck)) {
				dropMorb(type, entityData, world, pos);
				this.setDead();
				return;
			}
			double x = traceResult.hitVec.x;
			double y = traceResult.hitVec.y;
			double z = traceResult.hitVec.z;

			if (traceResult.sideHit != null) {
				x += traceResult.sideHit.getFrontOffsetX();
				z += traceResult.sideHit.getFrontOffsetZ();
			}
			spawnCreature(world, entityData, x, y, z);

			if (drop && (world.rand.nextInt(100) < reuseChance || type > 0)) {
				dropMorb(type, null, world, pos);
			}
			this.setDead();
		}
	}

	private static Entity spawnCreature(World world, NBTTagCompound entityData, double x, double y, double z) {

		if (entityData.getBoolean(GENERIC)) {
			spawnGenericCreature(world, new ResourceLocation(entityData.getString("id")), x, y, z);
		} else {
			Entity entity = EntityList.createEntityFromNBT(entityData, world);
			if (entity == null) {
				return null;
			}
			entity.setLocationAndAngles(x, y, z, wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
			world.spawnEntity(entity);
			if (entity instanceof EntityLiving) {
				((EntityLiving) entity).playLivingSound();
			}
		}
		return null;
	}

	@Nullable
	private static Entity spawnGenericCreature(World worldIn, @Nullable ResourceLocation entityID, double x, double y, double z) {

		Entity entity = null;

		if (entityID != null) {
			entity = EntityList.createEntityByIDFromName(entityID, worldIn);

			if (entity instanceof EntityLiving) {
				EntityLiving entityliving = (EntityLiving) entity;
				entity.setLocationAndAngles(x, y, z, wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
				worldIn.spawnEntity(entity);
				entityliving.playLivingSound();
			}
		}
		return entity;
	}
	// endregion

	// region NBT
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {

		super.readEntityFromNBT(nbt);

		drop = nbt.getBoolean("Drop");
		type = nbt.getByte("Type");
		entityData = nbt.getCompoundTag("EntityData");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {

		super.writeEntityToNBT(nbt);

		nbt.setBoolean("Drop", drop);
		nbt.setByte("Type", type);
		nbt.setTag("EntityData", entityData);
	}
	// endregion
}
