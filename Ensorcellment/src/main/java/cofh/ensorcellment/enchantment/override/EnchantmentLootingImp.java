package cofh.ensorcellment.enchantment.override;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentOverride;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentLootingImp extends EnchantmentOverride {

	public EnchantmentLootingImp(String id) {

		super(id, Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		setName("lootBonus");
	}

	@Override
	protected void config() {

		String category = "Override.Looting";
		String comment = "If TRUE, the Looting Enchantment is replaced with a more configurable version which works on more items, such as Axes.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.get("Max Level", category, 3, 1, MAX_ENCHANT_LEVEL, comment);
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {

		return 15 + (enchantmentLevel - 1) * 9;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {

		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item instanceof ItemSword || item instanceof ItemAxe || capabilityCompatible(stack));
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {

		return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH;
	}

}
