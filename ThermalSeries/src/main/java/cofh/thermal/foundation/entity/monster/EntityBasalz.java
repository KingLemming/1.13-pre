package cofh.thermal.foundation.entity.monster;

import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.init.SoundsTSeries;
import cofh.thermal.foundation.entity.projectile.EntityBasalzBolt;
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

public class EntityBasalz extends EntityElemental {

	public static final String NAME = "basalz";
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

		validBiomes.addAll(BiomeDictionary.getBiomes(Type.MOUNTAIN));
		validBiomes.addAll(BiomeDictionary.getBiomes(Type.WASTELAND));
		validBiomes.removeAll(BiomeDictionary.getBiomes(Type.NETHER));
		validBiomes.removeAll(BiomeDictionary.getBiomes(Type.END));

		EntityEntryBuilder builder = EntityEntryBuilder.create()//
				.entity(EntityBasalz.class)//
				.id(new ResourceLocation(ID_THERMAL_SERIES + ":" + NAME), id)//
				.name(ID_THERMAL_SERIES + "." + NAME)//
				.tracker(ENTITY_TRACKING_DISTANCE, 1, true);

		if (enableEgg) {
			builder.egg(0x606060, 0xB3ABA3);
		}
		if (enableSpawn && !disableAllHostileMobSpawns) {
			builder.spawn(EnumCreatureType.MONSTER, spawnWeight, spawnMin, spawnMax, validBiomes);
		}
		ForgeRegistries.ENTITIES.register(builder.build());
	}

	public static void config() {

		String category = "Mobs.Basalz";
		String comment;

		comment = "If TRUE, Basalzes will appear naturally.";
		enableSpawn = ThermalSeries.config.getBoolean("Enable Spawn", category, enableSpawn, comment);

		comment = "If TRUE, Basalzes will have a spawn egg item created.";
		enableEgg = ThermalSeries.config.getBoolean("Enable Egg", category, enableEgg, comment);

		comment = "This sets the maximum light level Basalzes can spawn at.";
		spawnLightLevel = ThermalSeries.config.getInt("Light Level", category, spawnLightLevel, 0, 15, comment);

		comment = "This sets the minimum number of Basalzes that spawn in a group.";
		spawnMin = ThermalSeries.config.getInt("Min Group Size", category, spawnMin, 1, 10, comment);

		comment = "This sets the maximum number of Basalzes that spawn in a group.";
		spawnMax = ThermalSeries.config.getInt("Max Group Size", category, spawnMax, spawnMin, 24, comment);

		comment = "This sets the relative spawn weight for Basalzes.";
		spawnWeight = ThermalSeries.config.getInt("Spawn Weight", category, spawnWeight, 1, 100, comment);

		comment = "If TRUE, Basalz attacks will inflict Weakness.";
		effect = ThermalSeries.config.getBoolean("Effect", category, effect, comment);
	}

	public EntityBasalz(World world) {

		super(world);

		ambientParticle = EnumParticleTypes.TOWN_AURA;
		ambientSound = SoundsTSeries.basalzAmbient;
	}

	@Nullable
	protected ResourceLocation getLootTable() {

		return lootTable;
	}

	@Override
	protected void initEntityAI() {

		tasks.addTask(4, new AIBasalzBoltAttack(this));
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
	static class AIBasalzBoltAttack extends EntityAIBase {

		private final EntityBasalz basalz;
		private int attackStep;
		private int attackTime;

		public AIBasalzBoltAttack(EntityBasalz entity) {

			basalz = entity;
			setMutexBits(3);
		}

		@Override
		public boolean shouldExecute() {

			EntityLivingBase target = basalz.getAttackTarget();
			return target != null && target.isEntityAlive();
		}

		@Override
		public void startExecuting() {

			this.attackStep = 0;
		}

		@Override
		public void resetTask() {

			this.basalz.setInAttackMode(false);
		}

		@Override
		public void updateTask() {

			--this.attackTime;
			EntityLivingBase target = this.basalz.getAttackTarget();
			double d0 = this.basalz.getDistanceSq(target);

			if (d0 < 4.0D) {
				if (attackTime <= 0) {
					attackTime = 20;
					basalz.attackEntityAsMob(target);
				}

				basalz.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 1.0D);
			} else if (d0 < getFollowDistance() * getFollowDistance()) {

				if (attackTime <= 0) {
					++attackStep;

					if (attackStep == 1) {
						attackTime = 60;
						basalz.setInAttackMode(true);
					} else if (attackStep <= 4) {
						attackTime = 6;
					} else {
						attackTime = 100;
						attackStep = 0;
						basalz.setInAttackMode(false);
					}

					if (attackStep > 1) {
						basalz.world.playEvent(null, 1009, new BlockPos((int) basalz.posX, (int) basalz.posY, (int) basalz.posZ), 0);

						for (int i = 0; i < 1; ++i) {
							EntityBasalzBolt bolt = new EntityBasalzBolt(basalz.world, basalz);
							bolt.shoot(target.posX - basalz.posX, target.posY - basalz.posY, target.posZ - basalz.posZ, 1.0F, 1.0F);
							bolt.posY = basalz.posY + basalz.height / 2.0F + 0.5D;
							basalz.playSound(SoundsTSeries.basalzAttack, 2.0F, (basalz.rand.nextFloat() - basalz.rand.nextFloat()) * 0.2F + 1.0F);
							basalz.world.spawnEntity(bolt);
						}
					}
				}
				basalz.getLookHelper().setLookPositionWithEntity(target, 10.0F, 10.0F);
			} else {
				basalz.getNavigator().clearPath();
				basalz.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 1.0D);
			}
			super.updateTask();
		}

		private double getFollowDistance() {

			IAttributeInstance attribute = this.basalz.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return attribute == null ? 16.0D : attribute.getAttributeValue();
		}
	}

}
