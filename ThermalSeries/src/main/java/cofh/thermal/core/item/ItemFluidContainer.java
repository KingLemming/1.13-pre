package cofh.thermal.core.item;

import cofh.core.item.ItemCoFH;
import cofh.lib.fluid.FluidEnchantableItemWrapper;
import cofh.lib.fluid.IFluidContainerItem;
import cofh.lib.item.IColorableItem;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.modhelpers.EnsorcellmentHelper;
import cofh.thermal.core.init.FluidsTSeries;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.ItemHelper.areItemStacksEqualIgnoreTags;
import static cofh.lib.util.helpers.StringHelper.*;

public abstract class ItemFluidContainer extends ItemCoFH implements IColorableItem, IFluidContainerItem {

	protected int fluidCapacity;

	public ItemFluidContainer(int fluidCapacity) {

		super(GROUP_UTILS);

		setMaxStackSize(1);
		setNoRepair();

		this.fluidCapacity = fluidCapacity;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		FluidStack fluid = getFluid(stack);
		if (fluid != null) {
			String color = fluid.getFluid().getRarity().rarityColor.toString();
			tooltip.add(localize("info.cofh.fluid") + ": " + color + fluid.getFluid().getLocalizedName(fluid) + LIGHT_GRAY);
			if (isCreative()) {
				tooltip.add(localize("info.cofh.infinite_source"));
			} else {
				tooltip.add(localize("info.cofh.level") + ": " + formatNumber(fluid.amount) + " / " + formatNumber(getCapacity(stack)) + " mB");
			}
			if (FluidsTSeries.isPotion(fluid)) {
				tooltip.add("");
				tooltip.add(localize("info.cofh.effects.") + ":");
				addPotionTooltip(fluid.tag, tooltip);
			}
		} else {
			tooltip.add(localize("info.cofh.fluid") + ": " + localize("info.cofh.empty"));
			if (isCreative()) {
				tooltip.add(localize("info.cofh.infinite_source"));
			} else {
				tooltip.add(localize("info.cofh.level") + ": 0 / " + formatNumber(getCapacity(stack)) + " mB");
			}
		}
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {

		return !isCreative();
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean isFull3D() {

		return true;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || !areItemStacksEqualIgnoreTags(oldStack, newStack, TAG_FLUID));
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return 10;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {

		return MathHelper.clamp(1.0D - ((double) getFluidAmount(stack) / (double) getCapacity(stack)), 0.0D, 1.0D);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {

		return !isCreative() && getFluidAmount(stack) > 0;
	}

	// region HELPERS
	@SideOnly (Side.CLIENT)
	protected static void addPotionTooltip(NBTTagCompound potionTag, List<String> lores) {

		List<PotionEffect> list = PotionUtils.getEffectsFromTag(potionTag);
		if (list.isEmpty()) {
			String s = localize("effect.none").trim();
			lores.add(TextFormatting.GRAY + s);
		} else {
			for (PotionEffect potioneffect : list) {
				String s1 = localize(potioneffect.getEffectName()).trim();
				Potion potion = potioneffect.getPotion();
				if (potioneffect.getAmplifier() > 0) {
					s1 = s1 + " " + localize("potion.potency." + potioneffect.getAmplifier()).trim();
				}
				if (potion.isBadEffect()) {
					lores.add(TextFormatting.RED + s1);
				} else {
					lores.add(TextFormatting.BLUE + s1);
				}
			}
		}
	}
	// endregion

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		ArrayList<ResourceLocation> enchants = new ArrayList<>();

		if (EnsorcellmentHelper.HOLDING != null) {
			enchants.add(EnsorcellmentHelper.HOLDING.getRegistryName());
		}
		return new FluidEnchantableItemWrapper(stack, this, true, true, enchants);
	}

	// region IFluidContainerItem
	@Override
	public int getCapacity(ItemStack container) {

		int enchant = EnchantmentHelper.getEnchantmentLevel(EnsorcellmentHelper.HOLDING, container);
		return fluidCapacity + fluidCapacity * enchant / 2;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {

		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}
		if (resource == null || resource.amount <= 0) {
			return 0;
		}
		int capacity = getCapacity(container);

		if (isCreative(container)) {
			if (doFill) {
				NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());
				fluidTag.setInteger(TAG_AMOUNT, capacity - Fluid.BUCKET_VOLUME);
				container.getTagCompound().setTag(TAG_FLUID, fluidTag);
			}
			return resource.amount;
		}
		if (!doFill) {
			if (!container.getTagCompound().hasKey(TAG_FLUID)) {
				return Math.min(capacity, resource.amount);
			}
			FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag(TAG_FLUID));

			if (stack == null) {
				return Math.min(capacity, resource.amount);
			}
			if (!stack.isFluidEqual(resource)) {
				return 0;
			}
			return Math.min(capacity - stack.amount, resource.amount);
		}
		if (!container.getTagCompound().hasKey(TAG_FLUID)) {
			NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());
			if (capacity < resource.amount) {
				fluidTag.setInteger(TAG_AMOUNT, capacity);
				container.getTagCompound().setTag(TAG_FLUID, fluidTag);
				return capacity;
			}
			fluidTag.setInteger(TAG_AMOUNT, resource.amount);
			container.getTagCompound().setTag(TAG_FLUID, fluidTag);
			return resource.amount;
		}
		NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag(TAG_FLUID);
		FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);
		if (stack == null || !stack.isFluidEqual(resource)) {
			return 0;
		}
		int filled = capacity - stack.amount;
		if (resource.amount < filled) {
			stack.amount += resource.amount;
			filled = resource.amount;
		} else {
			stack.amount = capacity;
		}
		container.getTagCompound().setTag(TAG_FLUID, stack.writeToNBT(fluidTag));
		return filled;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {

		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}
		if (!container.getTagCompound().hasKey(TAG_FLUID) || maxDrain == 0) {
			return null;
		}
		FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag(TAG_FLUID));
		if (stack == null) {
			return null;
		}
		boolean creative = isCreative(container);
		int drained = creative ? maxDrain : Math.min(stack.amount, maxDrain);
		if (doDrain && !creative) {
			if (maxDrain >= stack.amount) {
				container.getTagCompound().removeTag(TAG_FLUID);
				return stack;
			}
			NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag(TAG_FLUID);
			fluidTag.setInteger(TAG_AMOUNT, fluidTag.getInteger(TAG_AMOUNT) - drained);
			container.getTagCompound().setTag(TAG_FLUID, fluidTag);
		}
		stack.amount = drained;
		return stack;
	}
	// endregion
}
