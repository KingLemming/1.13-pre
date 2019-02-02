package cofh.thermal.core.item.tome;

import cofh.core.key.KeyMultiModeItem;
import cofh.lib.fluid.FluidEnchantableItemWrapper;
import cofh.lib.fluid.IFluidContainerItem;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.modhelpers.EnsorcellmentHelper;
import cofh.thermal.core.init.FluidsTSeries;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.StringHelper.*;

public class ItemTomeExperience extends ItemTome implements IFluidContainerItem {

	public static final int CAPACITY = 10000;
	public static final String EXPERIENCE_TIMER = "thermal.xp_timer";

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(getInfoText("info.thermal.tome_xp.0"));
		tooltip.add(localize("info.thermal.tome_xp.1"));
		tooltip.add(getNoticeText("info.thermal.tome_xp.2"));
		tooltip.add(localizeFormat("info.thermal.tome_xp.a." + (isEmpowered(stack) ? 1 : 0), getKeyName(KeyMultiModeItem.INSTANCE.getKey())));
		tooltip.add(localize("info.experience") + ": " + formatNumber(getExperience(stack)) + " / " + formatNumber(getMaxExperience(stack)));
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {

		if (!isEmpowered(stack)) {
			return;
		}
		NBTTagCompound tag = entity.getEntityData();
		tag.setLong(EXPERIENCE_TIMER, entity.world.getTotalWorldTime());
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {

		return true;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || !ItemHelper.areItemStacksEqualIgnoreTags(oldStack, newStack, TAG_EXP));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {

		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return 10;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {

		return RGB_DURABILITY_EXP;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {

		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return MathHelper.clamp(1.0D - ((double) getExperience(stack) / (double) getMaxExperience(stack)), 0.0D, 1.0D);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (Utils.isFakePlayer(player) || hand != EnumHand.MAIN_HAND || Utils.isClientWorld(world)) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		int exp;
		int curLevel = player.experienceLevel;

		if (player.isSneaking()) {
			if (getExtraPlayerExperience(player) > 0) {
				exp = Math.min(getTotalExpForLevel(player.experienceLevel + 1) - getTotalExpForLevel(player.experienceLevel) - getExtraPlayerExperience(player), getExperience(stack));
				setPlayerExperience(player, getPlayerExperience(player) + exp);
				if (player.experienceLevel < curLevel + 1 && getPlayerExperience(player) >= getTotalExpForLevel(curLevel + 1)) {
					setPlayerLevel(player, curLevel + 1);
				}
				modifyExperience(stack, -exp);
			} else {
				exp = Math.min(getTotalExpForLevel(player.experienceLevel + 1) - getTotalExpForLevel(player.experienceLevel), getExperience(stack));
				setPlayerExperience(player, getPlayerExperience(player) + exp);
				if (player.experienceLevel < curLevel + 1 && getPlayerExperience(player) >= getTotalExpForLevel(curLevel + 1)) {
					setPlayerLevel(player, curLevel + 1);
				}
				modifyExperience(stack, -exp);
			}
		} else {
			if (getExtraPlayerExperience(player) > 0) {
				exp = Math.min(getExtraPlayerExperience(player), getRemainingCapacity(stack));
				setPlayerExperience(player, getPlayerExperience(player) - exp);
				if (player.experienceLevel < curLevel) {
					setPlayerLevel(player, curLevel);
				}
				modifyExperience(stack, exp);
			} else if (player.experienceLevel > 0) {
				exp = Math.min(getTotalExpForLevel(player.experienceLevel) - getTotalExpForLevel(player.experienceLevel - 1), getRemainingCapacity(stack));
				setPlayerExperience(player, getPlayerExperience(player) - exp);
				if (player.experienceLevel < curLevel - 1) {
					setPlayerLevel(player, curLevel - 1);
				}
				modifyExperience(stack, exp);
			}
		}
		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	// region HELPERS
	public static int getExperience(ItemStack stack) {

		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound().getInteger(TAG_EXP);
	}

	public static int getRemainingCapacity(ItemStack stack) {

		return getMaxExperience(stack) - getExperience(stack);
	}

	public static int getMaxExperience(ItemStack stack) {

		return CAPACITY + CAPACITY * EnchantmentHelper.getEnchantmentLevel(EnsorcellmentHelper.HOLDING, stack) / 2;
	}

	public static int modifyExperience(ItemStack stack, int exp) {

		int storedExp = getExperience(stack) + exp;

		if (storedExp > getMaxExperience(stack)) {
			storedExp = getMaxExperience(stack);
		} else if (storedExp < 0) {
			storedExp = 0;
		}
		stack.getTagCompound().setInteger(TAG_EXP, storedExp);
		return storedExp;
	}

	public static int getPlayerExperience(EntityPlayer player) {

		return getLevelPlayerExperience(player) + getExtraPlayerExperience(player);
	}

	public static int getLevelPlayerExperience(EntityPlayer player) {

		return getTotalExpForLevel(player.experienceLevel);
	}

	public static int getExtraPlayerExperience(EntityPlayer player) {

		return Math.round(player.experience * player.xpBarCap());
	}

	public static void setPlayerExperience(EntityPlayer player, int exp) {

		player.experienceLevel = 0;
		player.experience = 0.0F;
		player.experienceTotal = 0;

		addExperienceToPlayer(player, exp);
	}

	public static void setPlayerLevel(EntityPlayer player, int level) {

		player.experienceLevel = level;
		player.experience = 0.0F;
	}

	public static void addExperienceToPlayer(EntityPlayer player, int exp) {

		int i = Integer.MAX_VALUE - player.experienceTotal;

		if (exp > i) {
			exp = i;
		}
		player.experience += (float) exp / (float) player.xpBarCap();
		for (player.experienceTotal += exp; player.experience >= 1.0F; player.experience /= (float) player.xpBarCap()) {
			player.experience = (player.experience - 1.0F) * (float) player.xpBarCap();
			addExperienceLevelToPlayer(player, 1);
		}
	}

	public static void addExperienceLevelToPlayer(EntityPlayer player, int levels) {

		player.experienceLevel += levels;

		if (player.experienceLevel < 0) {
			player.experienceLevel = 0;
			player.experience = 0.0F;
			player.experienceTotal = 0;
		}
	}

	public static int getTotalExpForLevel(int level) {

		return level >= 32 ? (9 * level * level - 325 * level + 4440) / 2 : level >= 17 ? (5 * level * level - 81 * level + 720) / 2 : (level * level + 6 * level);
	}

	public static boolean onXPPickup(PlayerPickupXpEvent event, ItemStack stack) {

		EntityXPOrb orb = event.getOrb();
		int toAdd = Math.min(getRemainingCapacity(stack), orb.xpValue);

		if (toAdd > 0) {
			stack.setAnimationsToGo(5);
			EntityPlayer player = event.getEntityPlayer();
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (MathHelper.RANDOM.nextFloat() - MathHelper.RANDOM.nextFloat()) * 0.35F + 0.9F);

			ItemTomeExperience.modifyExperience(stack, toAdd);
			orb.xpValue -= toAdd;
			if (orb.xpValue <= 0) {
				orb.setDead();
			}
		}
		return orb.isDead;
	}
	// endregion

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		ArrayList<ResourceLocation> enchants = new ArrayList<>();

		if (EnsorcellmentHelper.HOLDING != null) {
			enchants.add(EnsorcellmentHelper.HOLDING.getRegistryName());
		}
		return new FluidEnchantableItemWrapper(stack, this, false, true, enchants);
	}

	// region IFluidContainerItem
	@Override
	public FluidStack getFluid(ItemStack container) {

		int experience = getExperience(container);
		return experience > 0 ? new FluidStack(FluidsTSeries.fluidExperience, experience * MB_PER_XP) : null;
	}

	@Override
	public int getCapacity(ItemStack container) {

		return getMaxExperience(container) * MB_PER_XP;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {

		if (resource == null || (resource.getFluid() != FluidsTSeries.fluidExperience && !resource.getFluid().getName().equals(FLUID_ESSENCE) && !resource.getFluid().getName().equals(FLUID_XPJUICE))) {
			return 0;
		}
		int experience = getExperience(container);
		int filled = Math.min(getMaxExperience(container) - experience, resource.amount / MB_PER_XP);

		if (doFill) {
			modifyExperience(container, filled);
		}
		return filled * MB_PER_XP;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {

		int experience = getExperience(container);
		if (experience <= 0) {
			return null;
		}
		int drained = Math.min(experience, maxDrain / MB_PER_XP);

		if (doDrain) {
			modifyExperience(container, -drained);
		}
		return new FluidStack(FluidsTSeries.fluidExperience, drained * MB_PER_XP);
	}
	// endregion
}
