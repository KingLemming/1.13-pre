package cofh.lib.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static cofh.lib.util.Constants.TAG_COLORS;
import static cofh.lib.util.Constants.TAG_INDEX;

public interface IColorableItem extends INBTCopyIngredient {

	default void applyColor(ItemStack item, int color, int colorIndex) {

		if (item.getTagCompound() == null) {
			item.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound colorTag = item.getTagCompound().getCompoundTag(TAG_COLORS);
		colorTag.setInteger(TAG_INDEX + colorIndex, color);
	}

	default void removeColor(ItemStack item, int colorIndex) {

		if (item.getTagCompound() == null || item.getSubCompound(TAG_COLORS) == null) {
			return;
		}
		NBTTagCompound colorTag = item.getTagCompound().getCompoundTag(TAG_COLORS);
		colorTag.removeTag(TAG_INDEX + colorIndex);
		if (colorTag.hasNoTags()) {
			item.removeSubCompound(TAG_COLORS);
		}
	}

	default void removeAllColors(ItemStack item) {

		if (item.getSubCompound(TAG_COLORS) != null) {
			item.removeSubCompound(TAG_COLORS);
		}
	}

}
