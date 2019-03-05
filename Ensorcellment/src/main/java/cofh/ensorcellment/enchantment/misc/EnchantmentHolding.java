package cofh.ensorcellment.enchantment.misc;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentHolding extends EnchantmentCoFH {

	public EnchantmentHolding(String id) {

		super(id, Rarity.COMMON, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
	}

	@Override
	protected void config() {

		String category = "Enchantment.Holding";
		String comment = "If TRUE, the Holding Enchantment is available for various Storage Items.";
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

		return enable && capabilityCompatible(stack);
	}

}
