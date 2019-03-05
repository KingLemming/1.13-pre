package cofh.ensorcellment.enchantment.weapon;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentVorpal extends EnchantmentCoFH {

	public static int critChanceBase = 5;
	public static int critChance = 5;
	public static int critDamage = 10;
	public static int headChanceBase = 5;
	public static int headChance = 10;

	public EnchantmentVorpal(String id) {

		super(id, Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Vorpal";
		String comment = "If TRUE, the Vorpal Enchantment is available for various Weapons.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 3, 1, MAX_ENCHANT_LEVEL, comment);

		comment = "Adjust this value to set the base critical hit chance of the Enchantment (in percentage).";
		critChanceBase = Ensorcellment.config.getInt("Base Critical Chance", category, critChanceBase, 1, 100, comment);

		comment = "Adjust this value to set the critical hit chance per level of the Enchantment (in percentage).";
		critChance = Ensorcellment.config.getInt("Critical Chance / Level", category, critChance, 1, 100, comment);

		comment = "Adjust this value to set the critical hit damage multiplier.";
		critDamage = Ensorcellment.config.getInt("Critical Damage Multiplier", category, critDamage, 2, 1000, comment);

		comment = "Adjust this value to set the base critical hit chance for the Enchantment (in percentage).";
		headChanceBase = Ensorcellment.config.getInt("Base Head Drop Chance", category, headChanceBase, 1, 100, comment);

		comment = "Adjust this value to set the critical hit chance per level of the Enchantment (in percentage).";
		headChance = Ensorcellment.config.getInt("Head Drop Chance / Level", category, headChance, 1, 100, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return 15 + (level - 1) * 9;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item instanceof ItemSword || item instanceof ItemAxe || capabilityCompatible(stack));
	}

}
