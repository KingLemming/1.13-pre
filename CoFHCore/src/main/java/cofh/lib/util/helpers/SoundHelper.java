package cofh.lib.util.helpers;

import cofh.lib.audio.SoundBase;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Contains various helper functions to assist with Sound manipulation.
 *
 * @author King Lemming
 */
public class SoundHelper {

	private SoundHelper() {

	}

	private static SoundHandler manager() {

		return FMLClientHandler.instance().getClient().getSoundHandler();
	}

	/**
	 * This allows you to have some tricky functionality with Tile Entities. Just be sure you aren't dumb.
	 */
	public static void playSound(Object sound) {

		if (sound instanceof ISound) {
			manager().playSound((ISound) sound);
		}
	}

	public static void playSound(ISound sound) {

		manager().playSound(sound);
	}

	public static void playSound(String soundName, SoundCategory category, float x, float y, float z, float volume, float pitch) {

		manager().playSound(new SoundBase(soundName, category, volume, pitch, x, y, z));
	}

	public static void playClickSound(float pitch) {

		playClickSound(0.3F, pitch);
	}

	public static void playClickSound(float volume, float pitch) {

		manager().playSound(new SoundBase(SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.3F, pitch));
	}

}
