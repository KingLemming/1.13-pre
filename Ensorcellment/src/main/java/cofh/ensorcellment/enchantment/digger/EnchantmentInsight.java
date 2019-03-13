package cofh.ensorcellment.enchantment.digger;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentInsight extends EnchantmentCoFH {

	public static int experience = 4;

	public EnchantmentInsight(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Insight";
		String comment = "If TRUE, the Insight Enchantment is available for various Tools and Weapons.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.get("Max Level", category, 3, 1, MAX_ENCHANT_LEVEL, comment);

		comment = "Adjust this to change the base experience awarded per level of the Enchantment.";
		experience = Ensorcellment.config.get("Experience", category, experience, 1, 1000, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return 10 + (level - 1) * 9;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemBow || capabilityCompatible(stack));
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {

		return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH;
	}

}
