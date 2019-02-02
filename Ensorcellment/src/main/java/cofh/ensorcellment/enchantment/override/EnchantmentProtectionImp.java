package cofh.ensorcellment.enchantment.override;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentOverride;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentProtectionImp extends EnchantmentOverride {

	public static int protection = 4;

	public EnchantmentProtectionImp(String id) {

		super(id, Rarity.COMMON, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
	}

	@Override
	protected void config() {

		String category = "Override.Protection";
		String comment = "If TRUE, the Protection Enchantment is replaced with a more configurable version which works on more items, such as Horse Armor.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 4, 1, MAX_ENCHANT_LEVEL, comment);
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
	public boolean canApply(ItemStack stack) {

		return enable && (stack.getItem().getHorseArmorType(stack) != HorseArmorType.NONE || capabilityCompatible(stack));
	}
}
