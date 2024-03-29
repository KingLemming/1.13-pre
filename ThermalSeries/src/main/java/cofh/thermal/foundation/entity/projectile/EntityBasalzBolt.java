package cofh.thermal.foundation.entity.projectile;

import cofh.lib.util.Utils;
import cofh.thermal.foundation.entity.monster.EntityBasalz;
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

public class EntityBasalzBolt extends EntityThrowable {

	public static final String NAME = "basalz_bolt";
	public static PotionEffect basalzEffect = new PotionEffectBasalz(5 * 20, 2);

	public static void initialize(int id) {

		ForgeRegistries.ENTITIES.register(EntityEntryBuilder.create()//
				.entity(EntityBasalzBolt.class)//
				.id(new ResourceLocation(ID_THERMAL_SERIES + ":" + NAME), id)//
				.name(ID_THERMAL_SERIES + "." + NAME)//
				.tracker(ENTITY_TRACKING_DISTANCE, 1, true)//
				.build());
	}

	public EntityBasalzBolt(World world) {

		super(world);
	}

	public EntityBasalzBolt(World world, EntityLivingBase thrower) {

		super(world, thrower);
	}

	public EntityBasalzBolt(World world, double x, double y, double z) {

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
				if (traceResult.entityHit instanceof EntityBasalz) {
					traceResult.entityHit.attackEntityFrom(DamageSourceBasalz.causeDamage(this, getThrower()), 0);
				} else {
					if (traceResult.entityHit.attackEntityFrom(DamageSourceBasalz.causeDamage(this, getThrower()), 5F) && traceResult.entityHit instanceof EntityLivingBase) {
						EntityLivingBase living = (EntityLivingBase) traceResult.entityHit;

						if (EntityBasalz.effect) {
							living.addPotionEffect(new PotionEffect(EntityBasalzBolt.basalzEffect));
						}
					}
				}
			}
			for (int i = 0; i < 8; i++) {
				world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, this.rand.nextDouble(), this.rand.nextDouble(), this.rand.nextDouble());
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
	protected static class DamageSourceBasalz extends EntityDamageSource {

		public DamageSourceBasalz() {

			this(null);
		}

		public DamageSourceBasalz(Entity source) {

			super(EntityBasalz.NAME, source);
		}

		public static DamageSource causeDamage(EntityBasalzBolt entityProj, Entity entitySource) {

			return (new EntityDamageSourceIndirect(EntityBasalz.NAME, entityProj, entitySource == null ? entityProj : entitySource)).setProjectile();
		}
	}

	protected static class PotionEffectBasalz extends PotionEffect {

		public PotionEffectBasalz(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn) {

			super(potionIn, durationIn, amplifierIn, ambientIn, showParticlesIn);
			getCurativeItems().clear();
		}

		public PotionEffectBasalz(int duration, int amplifier) {

			this(MobEffects.WEAKNESS, duration, amplifier, false, true);
		}
	}
	// endregion
}
