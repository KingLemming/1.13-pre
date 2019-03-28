package cofh.thermal.foundation.init;

import cofh.thermal.foundation.entity.monster.EntityBasalz;
import cofh.thermal.foundation.entity.monster.EntityBlitz;
import cofh.thermal.foundation.entity.monster.EntityBlizz;
import cofh.thermal.foundation.entity.projectile.EntityBasalzBolt;
import cofh.thermal.foundation.entity.projectile.EntityBlitzBolt;
import cofh.thermal.foundation.entity.projectile.EntityBlizzBolt;

public class EntitiesTF {

	private EntitiesTF() {

	}

	// region REGISTRATION
	public static void registerEntities() {

		EntityBlizz.initialize(0);
		EntityBlitz.initialize(1);
		EntityBasalz.initialize(2);

		EntityBlizzBolt.initialize(16);
		EntityBlitzBolt.initialize(17);
		EntityBasalzBolt.initialize(18);
	}
	// endregion
}
