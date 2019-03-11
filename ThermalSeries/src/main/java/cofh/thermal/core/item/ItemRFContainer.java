package cofh.thermal.core.item;

import cofh.core.item.ItemCoFH;
import cofh.lib.energy.EnergyEnchantableItemWrapper;
import cofh.lib.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.modhelpers.EnsorcellmentHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.StringHelper.getScaledNumber;
import static cofh.lib.util.helpers.StringHelper.localize;

public abstract class ItemRFContainer extends ItemCoFH implements IEnergyContainerItem {

	protected int maxEnergy;
	protected int maxReceive;

	public ItemRFContainer(int maxEnergy, int maxReceive) {

		super(GROUP_UTILS);

		setMaxStackSize(1);
		setNoRepair();

		this.maxEnergy = maxEnergy;
		this.maxReceive = maxReceive;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (isCreative()) {
			tooltip.add(localize("info.cofh.infinite_energy"));
		} else {
			tooltip.add(localize("info.cofh.charge") + ": " + getScaledNumber(getEnergyStored(stack)) + " / " + getScaledNumber(getMaxEnergyStored(stack)) + " RF");
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (isInCreativeTab(tab) && showInCreativeTab) {
			if (!isCreative()) {
				items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), 0));
			}
			items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), maxEnergy));
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		return !EnumEnchantmentType.BREAKABLE.equals(enchantment.type) && super.canApplyAtEnchantingTable(stack, enchantment);
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
	public boolean isEnchantable(ItemStack stack) {

		return true;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean isFull3D() {

		return true;
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {

		return !oldStack.equals(newStack) && (getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {

		return !isCreative() && getEnergyStored(stack) > 0;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return 10;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {

		return RGB_DURABILITY_FLUX;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {

		if (stack.getTagCompound() == null) {
			setDefaultTag(stack, 0);
		}
		return MathHelper.clamp(1.0D - ((double) stack.getTagCompound().getInteger(TAG_ENERGY) / (double) getMaxEnergyStored(stack)), 0.0D, 1.0D);
	}

	// region HELPERS
	public static ItemStack setDefaultTag(ItemStack stack, int energy) {

		return EnergyHelper.setDefaultEnergyTag(stack, energy);
	}
	// endregion

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		ArrayList<ResourceLocation> enchants = new ArrayList<>();

		if (EnsorcellmentHelper.HOLDING != null) {
			enchants.add(EnsorcellmentHelper.HOLDING.getRegistryName());
		}
		return new EnergyEnchantableItemWrapper(stack, this, true, true, enchants);
	}

	// region IEnergyContainerItem
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

		if (container.getTagCompound() == null) {
			setDefaultTag(container, 0);
		}
		int stored = Math.min(container.getTagCompound().getInteger(TAG_ENERGY), getMaxEnergyStored(container));
		int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(container) - stored, maxReceive));

		if (!simulate && !isCreative(container)) {
			stored += receive;
			container.getTagCompound().setInteger(TAG_ENERGY, stored);
		}
		return receive;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

		if (container.getTagCompound() == null) {
			setDefaultTag(container, 0);
		}
		if (isCreative(container)) {
			return maxExtract;
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
			setDefaultTag(container, 0);
		}
		return Math.min(container.getTagCompound().getInteger(TAG_ENERGY), getMaxEnergyStored(container));
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {

		return maxEnergy;
	}
	// endregion
}
