package cofh.ensorcellment.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import static cofh.lib.capabilities.CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY;

public abstract class EnchantmentBase extends Enchantment {

	public boolean enable = true;
	public int maxLevel = 1;

	protected EnchantmentBase(String id, Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {

		super(rarityIn, typeIn, slots);
		setRegistryName(id);
		config();
	}

	protected EnchantmentBase setEnable(boolean enable) {

		this.enable = enable;
		return this;
	}

	protected EnchantmentBase setMaxLevel(int maxLevel) {

		this.maxLevel = maxLevel;
		return this;
	}

	protected abstract void config();

	@Override
	public int getMaxLevel() {

		return maxLevel;
	}

	@Override
	public String getName() {

		return "enchantment." + this.getRegistryName().getResourceDomain() + "." + this.getRegistryName().getResourcePath();
	}

	@Override
	public boolean canApply(ItemStack stack) {

		return enable;
	}

	protected boolean capabilityCompatible(ItemStack stack) {

		return stack.hasCapability(ENCHANTABLE_ITEM_CAPABILITY, null) && stack.getCapability(ENCHANTABLE_ITEM_CAPABILITY, null).canEnchant(stack, this.getRegistryName());
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {

		return canApply(stack);
	}

	@Override
	public boolean isAllowedOnBooks() {

		return enable;
	}

	// region HELPERS

	// endregion
}
