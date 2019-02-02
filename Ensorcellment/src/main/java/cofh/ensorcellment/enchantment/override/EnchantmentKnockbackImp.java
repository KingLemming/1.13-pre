package cofh.ensorcellment.enchantment.override;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentOverride;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentKnockbackImp extends EnchantmentOverride {

	public EnchantmentKnockbackImp(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		setName("knockback");
	}

	@Override
	protected void config() {

		String category = "Override.Knockback";
		String comment = "If TRUE, the Knockback Enchantment is replaced with a more configurable version which works on more items, such as Axes.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 2, 1, MAX_ENCHANT_LEVEL, comment);
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {

		return 5 + 20 * (enchantmentLevel - 1);
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

}
