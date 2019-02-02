package cofh.thermal.core.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class SoundsTSeries {

	private SoundsTSeries() {

	}

	// region REGISTRATION
	public static void registerSoundEvents() {

		blizzAmbient = registerSoundEvent("mob_blizz_ambient");
		blizzAttack = registerSoundEvent("mob_blizz_attack");

		blitzAmbient = registerSoundEvent("mob_blitz_ambient");
		blitzAttack = registerSoundEvent("mob_blitz_attack");

		basalzAmbient = registerSoundEvent("mob_basalz_ambient");
		basalzAttack = registerSoundEvent("mob_basalz_attack");

		magnetUse = registerSoundEvent("player_magnet_use");
	}
	// endregion

	// region HELPERS
	private static SoundEvent registerSoundEvent(String id) {

		SoundEvent sound = new SoundEvent(new ResourceLocation(ID_THERMAL_SERIES + ":" + id));
		sound.setRegistryName(id);
		ForgeRegistries.SOUND_EVENTS.register(sound);
		return sound;
	}
	// endregion

	// region REFERENCES
	public static SoundEvent blizzAmbient;
	public static SoundEvent blizzAttack;

	public static SoundEvent blitzAmbient;
	public static SoundEvent blitzAttack;

	public static SoundEvent basalzAmbient;
	public static SoundEvent basalzAttack;

	public static SoundEvent magnetUse;
	// endregion
}
