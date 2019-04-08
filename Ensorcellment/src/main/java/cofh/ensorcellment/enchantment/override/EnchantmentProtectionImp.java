package cofh.ensorcellment.enchantment.override;

import cofh.ensorcellment.Ensorcellment;
import cofh.ensorcellment.enchantment.EnchantmentOverride;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentProtectionImp extends EnchantmentOverride {

	public static final int HORSE_MODIFIER = 3;

	public EnchantmentProtection.Type protectionType;

	public EnchantmentProtectionImp(String id, Rarity rarityIn, EnchantmentProtection.Type protectionTypeIn, EntityEquipmentSlot[] slots, String categoryName, String name) {

		super(id, rarityIn, EnumEnchantmentType.ARMOR, slots);
		this.protectionType = protectionTypeIn;

		if (protectionTypeIn == EnchantmentProtection.Type.FALL) {
			this.type = EnumEnchantmentType.ARMOR_FEET;
		}
		config(categoryName, name);
	}

	@Override
	protected void config() {

	}

	protected void config(String categoryName, String name) {

		String category = "Override." + categoryName;
		String comment = "If TRUE, the " + name + " Enchantment is replaced with a more configurable version which works on more items, such as Horse Armor.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.get("Max Level", category, 4, 1, MAX_ENCHANT_LEVEL, comment);
	}

	@Override
	public int calcModifierDamage(int level, DamageSource source) {

		if (level <= 0 || source.canHarmInCreative()) {
			return 0;
		} else if (this.protectionType == EnchantmentProtection.Type.ALL) {
			return level;
		} else if (this.protectionType == EnchantmentProtection.Type.FIRE && source.isFireDamage()) {
			return level * 2;
		} else if (this.protectionType == EnchantmentProtection.Type.FALL && source == DamageSource.FALL) {
			return level * 3;
		} else if (this.protectionType == EnchantmentProtection.Type.EXPLOSION && source.isExplosion()) {
			return level * 2;
		} else {
			return this.protectionType == EnchantmentProtection.Type.PROJECTILE && source.isProjectile() ? level * 2 : 0;
		}
	}

	@Override
	public int getMinEnchantability(int level) {

		return 1 + (level - 1) * 5;
	}

	@Override
	public int getMaxEnchantability(int level) {

		return getMinEnchantability(level) + 50;
	}

	@Override
	public String getName() {

		return "enchantment.protect." + this.protectionType.getTypeName();
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (type != null && type.canEnchantItem(item) || item.getHorseArmorType(stack) != HorseArmorType.NONE || capabilityCompatible(stack));
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {

		if (ench instanceof EnchantmentProtectionImp) {
			EnchantmentProtectionImp enchProtection = (EnchantmentProtectionImp) ench;
			if (this.protectionType == enchProtection.protectionType) {
				return false;
			} else {
				return this.protectionType == EnchantmentProtection.Type.FALL || enchProtection.protectionType == EnchantmentProtection.Type.FALL;
			}
		} else {
			return super.canApplyTogether(ench);
		}
	}

}
