package cofh.lib.audio;

import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is essentially a PositionedSound class with a ton more constructors. Because Mojang didn't provide enough.
 *
 * @author skyboy, brandon3055
 */
@SideOnly (Side.CLIENT)
public class SoundBase extends PositionedSound {

	public SoundBase(SoundBase other) {

		this(other.getSoundLocation(), other.category, other.volume, other.pitch, other.repeat, other.repeatDelay, other.xPosF, other.yPosF, other.zPosF, other.attenuationType);
	}

	// region SoundEvent
	public SoundBase(SoundEvent sound, SoundCategory category) {

		this(sound.getSoundName(), category, 1.0F, 1.0F, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(SoundEvent sound, SoundCategory category, float volume) {

		this(sound.getSoundName(), category, volume, 1.0F, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(SoundEvent sound, SoundCategory category, float volume, float pitch) {

		this(sound.getSoundName(), category, volume, pitch, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(SoundEvent sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay) {

		this(sound.getSoundName(), category, volume, pitch, repeat, repeatDelay, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(SoundEvent sound, SoundCategory category, float volume, float pitch, double x, double y, double z) {

		this(sound.getSoundName(), category, volume, pitch, false, 0, x, y, z, AttenuationType.LINEAR);
	}

	public SoundBase(SoundEvent sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay, double x, double y, double z) {

		this(sound.getSoundName(), category, volume, pitch, repeat, repeatDelay, x, y, z, AttenuationType.LINEAR);
	}

	public SoundBase(SoundEvent sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay, double x, double y, double z, AttenuationType attenuation) {

		this(sound.getSoundName(), category, volume, pitch, repeat, repeatDelay, x, y, z, attenuation);
	}
	// endregion

	// region String
	public SoundBase(String sound, SoundCategory category) {

		this(new ResourceLocation(sound), category, 1.0F, 1.0F, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(String sound, SoundCategory category, float volume) {

		this(new ResourceLocation(sound), category, volume, 1.0F, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(String sound, SoundCategory category, float volume, float pitch) {

		this(new ResourceLocation(sound), category, volume, pitch, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(String sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay) {

		this(new ResourceLocation(sound), category, volume, pitch, repeat, repeatDelay, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(String sound, SoundCategory category, float volume, float pitch, double x, double y, double z) {

		this(new ResourceLocation(sound), category, volume, pitch, false, 0, x, y, z, AttenuationType.LINEAR);
	}

	public SoundBase(String sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay, double x, double y, double z) {

		this(new ResourceLocation(sound), category, volume, pitch, repeat, repeatDelay, x, y, z, AttenuationType.LINEAR);
	}

	public SoundBase(String sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay, double x, double y, double z, AttenuationType attenuation) {

		this(new ResourceLocation(sound), category, volume, pitch, repeat, repeatDelay, x, y, z, attenuation);
	}
	// endregion

	// region ResourceLocation
	public SoundBase(ResourceLocation sound, SoundCategory category) {

		this(sound, category, 1.0F, 1.0F, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(ResourceLocation sound, SoundCategory category, float volume) {

		this(sound, category, volume, 1.0F, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(ResourceLocation sound, SoundCategory category, float volume, float pitch) {

		this(sound, category, volume, pitch, false, 0, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(ResourceLocation sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay) {

		this(sound, category, volume, pitch, repeat, repeatDelay, 0, 0, 0, AttenuationType.NONE);
	}

	public SoundBase(ResourceLocation sound, SoundCategory category, float volume, float pitch, double x, double y, double z) {

		this(sound, category, volume, pitch, false, 0, x, y, z, AttenuationType.LINEAR);
	}

	public SoundBase(ResourceLocation sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay, double x, double y, double z) {

		this(sound, category, volume, pitch, repeat, repeatDelay, x, y, z, AttenuationType.LINEAR);
	}

	public SoundBase(ResourceLocation sound, SoundCategory category, float volume, float pitch, boolean repeat, int repeatDelay, double x, double y, double z, AttenuationType attenuation) {

		super(sound, category);
		this.volume = volume;
		this.pitch = pitch;
		this.repeat = repeat;
		this.repeatDelay = repeatDelay;
		this.xPosF = (float) x;
		this.yPosF = (float) y;
		this.zPosF = (float) z;
		this.attenuationType = attenuation;
	}
	// endregion
}
