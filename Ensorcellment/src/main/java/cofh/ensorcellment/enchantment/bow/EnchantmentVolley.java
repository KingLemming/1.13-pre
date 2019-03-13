package cofh.ensorcellment.enchantment.bow;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import cofh.lib.item.IToolBow;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentVolley extends EnchantmentCoFH {

	public EnchantmentVolley(String id) {

		super(id, Rarity.UNCOMMON, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Volley";
		String comment = "If TRUE, the Volley Enchantment is available for various Bows.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.get("Max Level", category, 4, 1, MAX_ENCHANT_LEVEL, comment);
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
