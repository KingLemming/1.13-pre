package cofh.ensorcellment.enchantment.looting;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentFarmer extends EnchantmentCoFH {

	public static int chance = 50;

	public EnchantmentFarmer(String id) {

		super(id, Rarity.VERY_RARE, EnumEnchantmentType.FISHING_ROD, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Farmer";
		String comment = "If TRUE, the Farmer's Bounty Enchantment is available for various Tools.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 2, 1, MAX_ENCHANT_LEVEL, comment);

		comment = "Adjust this value to set the chance of an additional drop per level of the Enchantment (in percentage).";
		chance = Ensorcellment.config.getInt("Effect Chance", category, chance, 1, 100, comment);
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {

		return 10 + (enchantmentLevel - 1) * 9;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {

		return this.getMinEnchantability(enchantmentLevel) + 15;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item instanceof ItemHoe || capabilityCompatible(stack));
	}

	@Override
	public boolean isTreasureEnchantment() {

		return true;
	}

}
