package cofh.core.fluid;

import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

import static cofh.lib.util.helpers.StringHelper.localize;

public class FluidPotion extends FluidCoFH {

	public static int DEFAULT_COLOR = 0xF800F8;

	public FluidPotion(String fluidName, String modName) {

		super(fluidName, modName);
	}

	@Override
	public int getColor() {

		return 0xFF000000 | DEFAULT_COLOR;
	}

	@Override
	public int getColor(FluidStack stack) {

		return 0xFF000000 | getPotionColor(stack);
	}

	public String getLocalizedName(FluidStack stack) {

		PotionType type = PotionUtils.getPotionTypeFromNBT(stack.tag);

		if (type == PotionTypes.EMPTY || type == PotionTypes.WATER) {
			return super.getLocalizedName(stack);
		}
		return localize(type.getNamePrefixed("potion.effect."));
	}

	public static int getPotionColor(FluidStack stack) {

		if (stack.tag != null && stack.tag.hasKey("CustomPotionColor", 99)) {
			return stack.tag.getInteger("CustomPotionColor");
		} else {
			return getPotionTypeFromNBT(stack.tag) == PotionTypes.EMPTY ? DEFAULT_COLOR : PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromTag(stack.tag));
		}
	}

	public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound tag) {

		return tag == null || !tag.hasKey("Potion") ? PotionTypes.EMPTY : PotionType.getPotionTypeForName(tag.getString("Potion"));
	}

}
