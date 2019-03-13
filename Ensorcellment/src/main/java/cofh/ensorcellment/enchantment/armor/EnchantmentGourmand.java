package cofh.ensorcellment.enchantment.armor;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentGourmand extends EnchantmentCoFH {

	public static int chance = 20;

	public EnchantmentGourmand(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Gourmand";
		String comment = "If TRUE, the Gourmand Enchantment is available for Helmets.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.get("Max Level", category, 3, 1, MAX_ENCHANT_LEVEL, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return 5 + (level - 1) * 10;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		return enable && type != null && type.canEnchantItem(stack.getItem());
	}

}
