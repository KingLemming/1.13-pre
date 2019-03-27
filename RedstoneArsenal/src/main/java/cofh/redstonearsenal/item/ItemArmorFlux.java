package cofh.redstonearsenal.item;

import cofh.core.item.armor.ItemArmorCoFH;
import cofh.lib.energy.EnergyEnchantableItemWrapper;
import cofh.lib.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.redstonearsenal.init.ConfigRSA;
import cofh.redstonearsenal.init.CreativeTabsRSA;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.RGB_DURABILITY_FLUX;
import static cofh.lib.util.Constants.TAG_ENERGY;
import static cofh.lib.util.helpers.StringHelper.*;
import static cofh.lib.util.modhelpers.EnsorcellmentHelper.HOLDING;

public class ItemArmorFlux extends ItemArmorCoFH implements ISpecialArmor, IEnergyContainerItem {

	private static final ArmorProperties FLUX = new ArmorProperties(0, 0.20D, Integer.MAX_VALUE);
	private static final ArmorProperties FALL = new ArmorProperties(0, 0.00D, 0);

	protected int maxEnergy = 320000;
	protected int maxTransfer = 4000;

	protected double absorbRatio = 0.9D;
	protected int energyPerDamage = 200;

	public ItemArmorFlux(ArmorMaterial material, EntityEquipmentSlot type) {

		super(material, type);

		setCreativeTab(CreativeTabsRSA.tabBasicArmor);
		setMaxDamage(0);
		setNoRepair();
	}

	public ItemArmorFlux setEnergyParams(int maxEnergy, int maxTransfer) {

		this.maxEnergy = maxEnergy;
		this.maxTransfer = maxTransfer;

		return this;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

		return HashMultimap.create();
	}

	// region FLUX BOILERPLATE
	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		if (stack.getTagCompound() == null) {
			EnergyHelper.setDefaultEnergyTag(stack, 0);
		}
		tooltip.add(localize("info.cofh.charge") + ": " + formatNumber(stack.getTagCompound().getInteger(TAG_ENERGY)) + " / " + formatNumber(getMaxEnergyStored(stack)) + " RF");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (isInCreativeTab(tab) && showInCreativeTab) {
			items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), 0));
			items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), maxEnergy));
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		if (EnumEnchantmentType.BREAKABLE.equals(enchantment.type)) {
			return enchantment.equals(Enchantments.UNBREAKING);
		}
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {

		return false;
	}

	@Override
	public boolean isDamageable() {

		return true;
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {

		return !(newStack.getItem() == oldStack.getItem()) || (getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {

		return ConfigRSA.showArmorCharge && getEnergyStored(stack) > 0;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {

		return 0;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {

		return RGB_DURABILITY_FLUX;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {

		if (stack.getTagCompound() == null) {
			EnergyHelper.setDefaultEnergyTag(stack, 0);
		}
		return MathHelper.clamp(1.0D - ((double) stack.getTagCompound().getInteger(TAG_ENERGY) / (double) getMaxEnergyStored(stack)), 0.0D, 1.0D);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return EnumRarity.UNCOMMON;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		ArrayList<ResourceLocation> enchants = new ArrayList<>();

		if (HOLDING != null) {
			enchants.add(HOLDING.getRegistryName());
		}
		return new EnergyEnchantableItemWrapper(stack, this, enchants);
	}
	// endregion

	// region IEnergyContainerItem
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

		if (container.getTagCompound() == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
		}
		int stored = Math.min(container.getTagCompound().getInteger(TAG_ENERGY), getMaxEnergyStored(container));
		int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(container) - stored, maxTransfer));

		if (!simulate) {
			stored += receive;
			container.getTagCompound().setInteger(TAG_ENERGY, stored);
		}
		return receive;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

		if (container.getTagCompound() == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
		}
		int stored = Math.min(container.getTagCompound().getInteger(TAG_ENERGY), getMaxEnergyStored(container));
		int extract = Math.min(maxExtract, stored);

		if (!simulate) {
			stored -= extract;
			container.getTagCompound().setInteger(TAG_ENERGY, stored);
		}
		return extract;
	}

	@Override
	public int getEnergyStored(ItemStack container) {

		if (container.getTagCompound() == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
		}
		return Math.min(container.getTagCompound().getInteger(TAG_ENERGY), getMaxEnergyStored(container));
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {

		int enchant = EnchantmentHelper.getEnchantmentLevel(HOLDING, container);
		return maxEnergy + maxEnergy * enchant / 2;
	}
	// endregion

	// region ISpecialArmor
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {

		if (source.damageType.equals("flux")) {
			return FLUX;
		} else if (source.damageType.equals("fall")) {
			if (slot == 0) {
				int absorbMax = energyPerDamage > 0 ? 25 * getEnergyStored(armor) / energyPerDamage : 0;
				return new ArmorProperties(0, absorbRatio * getArmorMaterial().getDamageReductionAmount(armorType) * 0.25, absorbMax);
			}
			return FALL;
		} else if (source.isUnblockable()) {
			int absorbMax = energyPerDamage > 0 ? 25 * getEnergyStored(armor) / energyPerDamage : 0;
			return new ArmorProperties(0, absorbRatio * getArmorMaterial().getDamageReductionAmount(armorType) * 0.025, absorbMax);
		}
		int absorbMax = energyPerDamage > 0 ? 25 * getEnergyStored(armor) / energyPerDamage : 0;
		return new ArmorProperties(0, absorbRatio * getArmorMaterial().getDamageReductionAmount(armorType) * 0.05, absorbMax);
		// 0.05 = 1 / 20 (max armor)
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {

		if (getEnergyStored(armor) >= energyPerDamage) {
			return getArmorMaterial().getDamageReductionAmount(armorType);
		}
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack armor, DamageSource source, int damage, int slot) {

		if (source.damageType.equals("flux")) {
			receiveEnergy(armor, damage * energyPerDamage, false);
		} else {
			int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, armor), 0, 10);
			if (MathHelper.RANDOM.nextInt(3 + unbreakingLevel) >= 3) {
				return;
			}
			extractEnergy(armor, damage * energyPerDamage, false);
		}
	}

	@Override
	public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {

		return true;
	}
	// endregion
}
