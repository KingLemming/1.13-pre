package cofh.ensorcellment.enchantment.digger;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;

public class EnchantmentSmelting extends EnchantmentCoFH {

	public EnchantmentSmelting(String id) {

		super(id, Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	}

	@Override
	protected void config() {

		String category = "Enchantment.Smelting";
		String comment = "If TRUE, the Smelting Enchantment is available for various Tools.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);
	}

	@Override
	public int getMinEnchantability(int level) {

		return 15;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item instanceof ItemTool || capabilityCompatible(stack));
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {

		return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH;
	}

	// region CONVERSION
	public static ItemStack getItemStack(ItemStack stack) {

		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		return result.isEmpty() ? ItemStack.EMPTY : ItemHelper.cloneStack(result, result.getCount() * stack.getCount());
	}
	// endregion
}
