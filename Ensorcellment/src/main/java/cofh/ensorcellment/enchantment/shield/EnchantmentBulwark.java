package cofh.ensorcellment.enchantment.shield;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentBulwark extends EnchantmentCoFH {

	public EnchantmentBulwark(String id) {

		super(id, Rarity.COMMON, EnumEnchantmentType.BREAKABLE, new EntityEquipmentSlot[] { EntityEquipmentSlot.OFFHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Shield.Bulwark";
		String comment = "If TRUE, the Bulwark Enchantment is available for Shields.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);
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
