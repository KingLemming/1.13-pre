package cofh.thermal.foundation.entity.projectile;

import cofh.lib.util.Utils;
import cofh.thermal.foundation.entity.monster.EntityBlitz;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cofh.lib.util.Constants.ENTITY_TRACKING_DISTANCE;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class EntityBlitzBolt extends EntityThrowable {

	public static final String NAME = "blitz_bolt";
	public static PotionEffect blitzEffect = new PotionEffectBlitz(5 * 20, 2);

	public static void initialize(int id) {

		ForgeRegistries.ENTITIES.register(EntityEntryBuilder.create()//
				.entity(EntityBlitzBolt.class)//
				.id(new ResourceLocation(ID_THERMAL_SERIES + ":" + NAME), id)//
				.name(ID_THERMAL_SERIES + "." + NAME)//
				.tracker(ENTITY_TRACKING_DISTANCE, 1, true)//
				.build());
	}

	public EntityBlitzBolt(World world) {

		super(world);
	}

	public EntityBlitzBolt(World world, EntityLivingBase thrower) {

		super(world, thrower);
	}

	public EntityBlitzBolt(World world, double x, double y, double z) {

		super(world, x, y, z);
	}

	@Override
	protected float getGravityVelocity() {

		return 0.005F;
	}

	@Override
	protected void onImpact(RayTraceResult traceResult) {

		if (Utils.isServerWorld(world)) {
			if (traceResult.entityHit != null) {
				if (traceResult.entityHit instanceof EntityBlitz) {
					traceResult.entityHit.attackEntityFrom(DamageSourceBlitz.causeDamage(this, getThrower()), 0);
				} else {
					if (traceResult.entityHit.attackEntityFrom(DamageSourceBlitz.causeDamage(this, getThrower()), 5F) && traceResult.entityHit instanceof EntityLivingBase) {
						EntityLivingBase living = (EntityLivingBase) traceResult.entityHit;

						if (EntityBlitz.effect) {
							living.addPotionEffect(new PotionEffect(EntityBlitzBolt.blitzEffect));
						}
					}
				}
			}
			for (int i = 0; i < 8; i++) {
				world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, this.rand.nextDouble(), this.rand.nextDouble(), this.rand.nextDouble());
			}
			setDead();
		}
	}

	@Override
	@SideOnly (Side.CLIENT)
	public int getBrightnessForRender() {

		return 0xF000F0;
	}

	// region DAMAGE SOURCE
	protected static class DamageSourceBlitz extends EntityDamageSource {

		public DamageSourceBlitz() {

			this(null);
		}

		public DamageSourceBlitz(Entity source) {

			super(EntityBlitz.NAME, source);
		}

		public static DamageSource causeDamage(EntityBlitzBolt entityProj, Entity entitySource) {

			return (new EntityDamageSourceIndirect(EntityBlitz.NAME, entityProj, entitySource == null ? entityProj : entitySource)).setProjectile();
		}
	}

	protected static class PotionEffectBlitz extends PotionEffect {

		public PotionEffectBlitz(Potion potion, int duration, int amplifier, boolean isAmbient) {

			super(potion, duration, amplifier, isAmbient, true);
			getCurativeItems().clear();
		}

		public PotionEffectBlitz(int duration, int amplifier) {

			this(MobEffects.BLINDNESS, duration, amplifier, false);
		}
	}
	// endregion
}
