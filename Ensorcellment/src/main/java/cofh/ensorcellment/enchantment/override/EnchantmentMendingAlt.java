package cofh.ensorcellment.enchantment.override;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentOverride;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

import static cofh.lib.util.Constants.ID_ENSORCELLMENT;

public class EnchantmentMendingAlt extends EnchantmentOverride {

	public static float anvilDamage = 0.03F;

	public EnchantmentMendingAlt(String id) {

		super(id, Rarity.RARE, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.values());
		setName(ID_ENSORCELLMENT + ".preservation");
	}

	@Override
	protected void config() {

		String category = "Override.Mending";
		String comment = "If TRUE, the Mending Enchantment is replaced with a new Enchantment - Preservation. This enchantment allows you to repair items at an Anvil without paying an increasing XP cost for every time you repair it. Additionally, these repairs have a much lower chance of damaging the anvil.";
		enable = Ensorcellment.config.getBoolean("Enable", category, false, comment);

		comment = "Adjust this value to set the chance of an Anvil being damaged when used to repair an item with Preservation (in percentage).";
		anvilDamage = Ensorcellment.config.getInt("Anvil Damage Chance", category, 3, 0, 12, comment) / 100F;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {

		return enchantmentLevel * 25;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {

		return getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean isTreasureEnchantment() {

		return true;
	}

}
