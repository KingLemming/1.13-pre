package cofh.ensorcellment.enchantment.misc;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentSoulbound extends EnchantmentBase {

	public static boolean permanent = false;

	public EnchantmentSoulbound(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
	}

	@Override
	protected void config() {

		String category = "Enchantment.Soulbound";
		String comment = "If TRUE, the Soulbound Enchantment is available.";
		enable = Ensorcellment.config.getBoolean("EnableSoulbound", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 3, 1, MAX_ENCHANT_LEVEL, comment);

		comment = "If TRUE, the Soulbound Enchantment is permanent (and will remove excess levels when triggered).";
		permanent = Ensorcellment.config.getBoolean("PermanentSoulbound", category, permanent, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return 1 + (level - 1) * 5;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 50;
	}

	@Override
	public int getMaxLevel() {

		return permanent ? 1 : maxLevel;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {

		return super.canApplyTogether(ench) && ench != Enchantments.VANISHING_CURSE;
	}

}
