package cofh.ensorcellment.enchantment.shield;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentBase;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentPhalanx extends EnchantmentBase {

	public static final double SPEED = 1.2D;

	public EnchantmentPhalanx(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.BREAKABLE, new EntityEquipmentSlot[] { EntityEquipmentSlot.OFFHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Shield.Phalanx";
		String comment = "If TRUE, the Phalanx Enchantment is available for Shields.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 2, 1, MAX_ENCHANT_LEVEL, comment);
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

		return enable && (stack.getItem().isShield(stack, null) || capabilityCompatible(stack));
	}

}
