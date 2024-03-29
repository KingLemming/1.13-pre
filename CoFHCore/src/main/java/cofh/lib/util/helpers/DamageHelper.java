package cofh.lib.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

/**
 * This class contains helper functions related to Damage types that CoFH mods add.
 *
 * @author King Lemming
 */
public class DamageHelper {

	private DamageHelper() {

	}

	public static final DamageSourcePyrotheum PYROTHEUM = new DamageSourcePyrotheum();
	public static final DamageSourceCryotheum CRYOTHEUM = new DamageSourceCryotheum();
	public static final DamageSourceFlux FLUX = new DamageSourceFlux();

	// region DAMAGE SOURCES
	public static class DamageSourcePyrotheum extends DamageSource {

		protected DamageSourcePyrotheum() {

			super("pyrotheum");
			this.setDamageBypassesArmor();
			this.setFireDamage();
		}
	}

	public static class DamageSourceCryotheum extends DamageSource {

		protected DamageSourceCryotheum() {

			super("cryotheum");
			this.setDamageBypassesArmor();
		}
	}

	public static class DamageSourceFlux extends DamageSource {

		protected DamageSourceFlux() {

			super("flux");
			this.setDamageBypassesArmor();
		}
	}

	public static class EntityDamageSourcePyrotheum extends EntityDamageSource {

		public EntityDamageSourcePyrotheum(String type, Entity entity) {

			super(type, entity);
			this.setDamageBypassesArmor();
			this.setFireDamage();
		}
	}

	public static class EntityDamageSourceFlux extends EntityDamageSource {

		public EntityDamageSourceFlux(String type, Entity entity) {

			super(type, entity);
			this.setDamageBypassesArmor();
		}
	}
	// endregion

	// region HELPERS
	public static DamageSource causeEntityPyrotheumDamage(String type, Entity entity) {

		return new EntityDamageSourcePyrotheum(type, entity);
	}

	public static DamageSource causePlayerPyrotheumDamage(EntityPlayer entityPlayer) {

		return new EntityDamageSourcePyrotheum("player", entityPlayer);
	}

	public static DamageSource causeEntityFluxDamage(String type, Entity entity) {

		return new EntityDamageSourceFlux(type, entity);
	}

	public static DamageSource causePlayerFluxDamage(EntityPlayer entityPlayer) {

		return new EntityDamageSourceFlux("player", entityPlayer);
	}
	// endregion
}
