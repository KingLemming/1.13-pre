package cofh.ensorcellment.enchantment.armor;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentAirWorker extends EnchantmentCoFH {

	public EnchantmentAirWorker(String id) {

		super(id, Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
	}

	@Override
	protected void config() {

		String category = "Enchantment.AirWorker";
		String comment = "If TRUE, the Air Affinity Enchantment is available for Helmets.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return 1;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 40;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		return enable && type != null && type.canEnchantItem(stack.getItem());
	}

}
