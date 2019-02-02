package cofh.ensorcellment.enchantment.override;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentOverride;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

import java.util.Random;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentThornsImp extends EnchantmentOverride {

	public EnchantmentThornsImp(String id) {

		super(id, Rarity.VERY_RARE, EnumEnchantmentType.ARMOR, EntityEquipmentSlot.values());
		setName("thorns");
	}

	@Override
	protected void config() {

		String category = "Override.Thorns";
		String comment = "If TRUE, the Thorns Enchantment is replaced with a more configurable version which works on more items, such as Shields and Horse Armor.";
		enable = Ensorcellment.config.getBoolean("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.getInt("Max Level", category, 3, 1, MAX_ENCHANT_LEVEL, comment);
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {

		return 10 + 20 * (enchantmentLevel - 1);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {

		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item instanceof ItemArmor || item.isShield(stack, null) || item.getHorseArmorType(stack) != HorseArmorType.NONE || capabilityCompatible(stack));
	}

	// region HELPERS
	@Override
	public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {

		Random random = user.getRNG();
		ItemStack itemstack = EnchantmentHelper.getEnchantedItem(this, user);
		if (EnchantmentThorns.shouldHit(level, random)) {
			if (attacker != null) {
				attacker.attackEntityFrom(DamageSource.causeThornsDamage(user), (float) EnchantmentThorns.getDamage(level, random));
			}

			if (!itemstack.isEmpty()) {
				damageArmor(itemstack, 3, user);
			}
		} else if (!itemstack.isEmpty()) {
			damageArmor(itemstack, 1, user);
		}
	}

	private void damageArmor(ItemStack stack, int amount, EntityLivingBase entity) {

		int slot = -1;
		int x = 0;
		for (ItemStack i : entity.getArmorInventoryList()) {
			if (i == stack) {
				slot = x;
				break;
			}
			x++;
		}
		if (slot == -1 || !(stack.getItem() instanceof ISpecialArmor)) {
			stack.damageItem(1, entity);
			return;
		}
		ISpecialArmor armor = (ISpecialArmor) stack.getItem();
		armor.damageArmor(entity, stack, DamageSource.causeThornsDamage(entity), amount, slot);
	}
	// endregion
}
