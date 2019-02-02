package cofh.ensorcellment.enchantment.weapon;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentBase;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentLeech extends EnchantmentBase {

	public EnchantmentLeech(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Leech";
		String comment = "If TRUE, the Leech Enchantment is available for various Weapons.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 4, 1, MAX_ENCHANT_LEVEL, comment);
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

		Item item = stack.getItem();
		return enable && (item instanceof ItemSword || item instanceof ItemAxe || capabilityCompatible(stack));
	}

}
