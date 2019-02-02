package cofh.ensorcellment.enchantment.override;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentOverride;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentFrostWalkerImp extends EnchantmentOverride {

	public EnchantmentFrostWalkerImp(String id) {

		super(id, Rarity.RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
		setName("frostWalker");
	}

	@Override
	protected void config() {

		String category = "Override.FrostWalker";
		String comment = "If TRUE, the Frost Walker Enchantment is replaced with a more configurable version which works on more items, such as Horse Armor.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 2, 1, MAX_ENCHANT_LEVEL, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return level * 10;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 15;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (type.canEnchantItem(item) || item.getHorseArmorType(stack) != HorseArmorType.NONE || capabilityCompatible(stack));
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {

		return super.canApplyTogether(ench) && ench != Enchantments.DEPTH_STRIDER;
	}

	@Override
	public boolean isTreasureEnchantment() {

		return true;
	}

}