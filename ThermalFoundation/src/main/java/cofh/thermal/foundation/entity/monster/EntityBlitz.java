package cofh.thermal.foundation.entity.monster;

import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.init.SoundsTSeries;
import cofh.thermal.foundation.entity.projectile.EntityBlitzBolt;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import static cofh.lib.util.Constants.ENTITY_TRACKING_DISTANCE;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.thermal.core.init.ConfigTSeries.disableAllHostileMobSpawns;

public class EntityBlitz extends EntityElemental {

	public static final String NAME = "blitz";
	protected static ResourceLocation lootTable;

	protected static boolean enableSpawn = true;
	protected static boolean enableEgg = true;

	protected static int spawnLightLevel = 8;
	protected static int spawnWeight = 10;
	protected static int spawnMin = 1;
	protected static int spawnMax = 4;

	public static boolean effect = true;

	public static void initialize(int id) {

		config();

		lootTable = LootTableList.register(new ResourceLocation(ID_THERMAL_SERIES, "entities/" + NAME));

		if (!enableSpawn) {
			return;
		}
		Set<Biome> validBiomes = new HashSet<>();

		validBiomes.addAll(BiomeDictionary.getBiomes(Type.SANDY));
		validBiomes.addAll(BiomeDictionary.getBiomes(Type.SAVANNA));
		validBiomes.removeAll(BiomeDictionary.getBiomes(Type.NETHER));
		validBiomes.removeAll(BiomeDictionary.getBiomes(Type.END));

		EntityEntryBuilder builder = EntityEntryBuilder.create()//
				.entity(EntityBlitz.class)//
				.id(new ResourceLocation(ID_THERMAL_SERIES + ":" + NAME), id)//
				.name(ID_THERMAL_SERIES + "." + NAME)//
				.tracker(ENTITY_TRACKING_DISTANCE, 1, true);

		if (enableEgg) {
			builder.egg(0xF0F8FF, 0xFFEFD5);
		}
		if (enableSpawn && !disableAllHostileMobSpawns) {
			builder.spawn(EnumCreatureType.MONSTER, spawnWeight, spawnMin, spawnMax, validBiomes);
		}
		ForgeRegistries.ENTITIES.register(builder.build());
	}

	public static void config() {

		String category = "Mob.Blitz";
		String comment;

		comment = "If TRUE, Blitzes will appear naturally.";
		enableSpawn = ThermalSeries.config.getBoolean("Enable Spawn", category, enableSpawn, comment);

		comment = "If TRUE, Blitzes will have a spawn egg item created.";
		enableEgg = ThermalSeries.config.getBoolean("Enable Egg", category, enableEgg, comment);

		comment = "This sets the maximum light level Blitzes can spawn at.";
		spawnLightLevel = ThermalSeries.config.getInt("Light Level", category, spawnLightLevel, 0, 15, comment);

		comment = "This sets the minimum number of Blitzes that spawn in a group.";
		spawnMin = ThermalSeries.config.getInt("Min Group Size", category, spawnMin, 1, 10, comment);

		comment = "This sets the maximum number of Blitzes that spawn in a group.";
		spawnMax = ThermalSeries.config.getInt("Max Group Size", category, spawnMax, spawnMin, 24, comment);

		comment = "This sets the relative spawn weight for Blitzes.";
		spawnWeight = ThermalSeries.config.getInt("Spawn Weight", category, spawnWeight, 1, 100, comment);

		comment = "If TRUE, Blitz attacks will inflict Blindness.";
		effect = ThermalSeries.config.getBoolean("Effect", category, effect, comment);
	}

	public EntityBlitz(World world) {

		super(world);

		ambientParticle = EnumParticleTypes.CLOUD;
		ambientSound = SoundsTSeries.blitzAmbient;
	}

	@Nullable
	protected ResourceLocation getLootTable() {

		return lootTable;
	}

	@Override
	protected void initEntityAI() {

		tasks.addTask(4, new AIBlitzBoltAttack(this));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(7, new EntityAIWander(this, 1.0D));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	protected int getSpawnLightLevel() {

		return spawnLightLevel;
	}

	/* ATTACK */
	static class AIBlitzBoltAttack extends EntityAIBase {

		private final EntityBlitz blitz;
		private int attackStep;
		private int attackTime;

		public AIBlitzBoltAttack(EntityBlitz entity) {

			blitz = entity;
			setMutexBits(3);
		}

		@Override
		public boolean shouldExecute() {

			EntityLivingBase target = blitz.getAttackTarget();
			return target != null && target.isEntityAlive();
		}

		@Override
		public void startExecuting() {

			attackStep = 0;
		}

		@Override
		public void resetTask() {

			blitz.setInAttackMode(false);
		}

		@Override
		public void updateTask() {

			--attackTime;
			EntityLivingBase target = blitz.getAttackTarget();
			double d0 = blitz.getDistanceSq(target);

			if (d0 < 4.0D) {
				if (attackTime <= 0) {
					attackTime = 20;
					blitz.attackEntityAsMob(target);
				}

				blitz.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 1.0D);
			} else if (d0 < getFollowDistance() * getFollowDistance()) {

				if (attackTime <= 0) {
					++attackStep;

					if (attackStep == 1) {
						attackTime = 60;
						blitz.setInAttackMode(true);
					} else if (attackStep <= 4) {
						attackTime = 6;
					} else {
						attackTime = 100;
						attackStep = 0;
						blitz.setInAttackMode(false);
					}

					if (attackStep > 1) {
						blitz.world.playEvent(null, 1009, new BlockPos((int) blitz.posX, (int) blitz.posY, (int) blitz.posZ), 0);

						for (int i = 0; i < 1; ++i) {
							EntityBlitzBolt bolt = new EntityBlitzBolt(blitz.world, blitz);
							bolt.posY = blitz.posY + blitz.height / 2.0F + 0.5D;
							bolt.shoot(target.posX - blitz.posX, target.posY - blitz.posY, target.posZ - blitz.posZ, 1.0F, 1.0F);
							blitz.playSound(SoundsTSeries.blitzAttack, 2.0F, (blitz.rand.nextFloat() - blitz.rand.nextFloat()) * 0.2F + 1.0F);
							blitz.world.spawnEntity(bolt);
						}
					}
				}
				blitz.getLookHelper().setLookPositionWithEntity(target, 10.0F, 10.0F);
			} else {
				blitz.getNavigator().clearPath();
				blitz.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 1.0D);
			}
			super.updateTask();
		}

		private double getFollowDistance() {

			IAttributeInstance attribute = this.blitz.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return attribute == null ? 16.0D : attribute.getAttributeValue();
		}
	}

}
