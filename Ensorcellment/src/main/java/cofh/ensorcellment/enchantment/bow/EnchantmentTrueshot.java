package cofh.ensorcellment.enchantment.bow;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import cofh.lib.item.IToolBow;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantmentTrueshot extends EnchantmentCoFH {

	public EnchantmentTrueshot(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Trueshot";
		String comment = "If TRUE, the Trueshot Enchantment is available for various Bows.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return 1 + (level - 1) * 10;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item == Items.BOW || item instanceof IToolBow || capabilityCompatible(stack));
	}

}
