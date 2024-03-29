package cofh.lib.util.helpers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;

import java.util.Map;

import static cofh.lib.util.Constants.TAG_COLORS;
import static cofh.lib.util.Constants.TAG_INDEX;

/**
 * Contains various helper functions to assist with colors.
 *
 * @author King Lemming
 */
public final class ColorHelper {

	private ColorHelper() {

	}

	public static final int DYE_BLACK = 0x191919;
	public static final int DYE_RED = 0xCC4C4C;
	public static final int DYE_GREEN = 0x667F33;
	public static final int DYE_BROWN = 0x7F664C;
	public static final int DYE_BLUE = 0x3366CC;
	public static final int DYE_PURPLE = 0xB266E5;
	public static final int DYE_CYAN = 0x4C99B2;
	public static final int DYE_LIGHT_GRAY = 0x999999;
	public static final int DYE_GRAY = 0x4C4C4C;
	public static final int DYE_PINK = 0xF2B2CC;
	public static final int DYE_LIME = 0x7FCC19;
	public static final int DYE_YELLOW = 0xE5E533;
	public static final int DYE_LIGHT_BLUE = 0x99B2F2;
	public static final int DYE_MAGENTA = 0xE57FD8;
	public static final int DYE_ORANGE = 0xF2B233;
	public static final int DYE_WHITE = 0xFFFFFF;

	public static final int[] DYE_COLORS = { DYE_BLACK, DYE_RED, DYE_GREEN, DYE_BROWN, DYE_BLUE, DYE_PURPLE, DYE_CYAN, DYE_LIGHT_GRAY, DYE_GRAY, DYE_PINK, DYE_LIME, DYE_YELLOW, DYE_LIGHT_BLUE, DYE_MAGENTA, DYE_ORANGE, DYE_WHITE };

	// Yes, this list is pre-localized to en_US and has no spaces. There are times when this is useful, such as in a
	// config file. Localization there is messy and not strictly required.
	public static final String[] WOOL_COLOR_CONFIG = { "White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black" };

	public static final Map<String, Integer> COLOR_MAP = new Object2ObjectOpenHashMap<>();

	static {
		COLOR_MAP.put("dyeBlack", DYE_BLACK);
		COLOR_MAP.put("dyeRed", DYE_RED);
		COLOR_MAP.put("dyeGreen", DYE_GREEN);
		COLOR_MAP.put("dyeBrown", DYE_BROWN);
		COLOR_MAP.put("dyeBlue", DYE_BLUE);
		COLOR_MAP.put("dyePurple", DYE_PURPLE);
		COLOR_MAP.put("dyeCyan", DYE_CYAN);
		COLOR_MAP.put("dyeLightGray", DYE_LIGHT_GRAY);
		COLOR_MAP.put("dyeGray", DYE_GRAY);
		COLOR_MAP.put("dyePink", DYE_PINK);
		COLOR_MAP.put("dyeLime", DYE_LIME);
		COLOR_MAP.put("dyeYellow", DYE_YELLOW);
		COLOR_MAP.put("dyeLightBlue", DYE_LIGHT_BLUE);
		COLOR_MAP.put("dyeMagenta", DYE_MAGENTA);
		COLOR_MAP.put("dyeOrange", DYE_ORANGE);
		COLOR_MAP.put("dyeWhite", DYE_WHITE);
	}

	public static int getDyeColor(int color) {

		return color < 0 || color > 15 ? 0xFFFFFF : DYE_COLORS[color];
	}

	public static boolean hasColor(ItemStack stack, int colorIndex) {

		if (stack.getSubCompound(TAG_COLORS) != null) {
			return stack.getSubCompound(TAG_COLORS).hasKey(TAG_INDEX + colorIndex);
		}
		return false;
	}

	public static int getColor(ItemStack stack, int colorIndex) {

		if (!hasColor(stack, colorIndex)) {
			return 0xFFFFFF;
		}
		return stack.getSubCompound(TAG_COLORS).getInteger(TAG_INDEX + colorIndex);
	}

}
