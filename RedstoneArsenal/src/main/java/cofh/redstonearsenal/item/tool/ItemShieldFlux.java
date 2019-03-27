package cofh.redstonearsenal.item.tool;

import cofh.core.item.tool.ItemShieldCoFH;
import cofh.lib.energy.EnergyEnchantableItemWrapper;
import cofh.lib.energy.IEnergyContainerItem;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.DamageHelper;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.redstonearsenal.init.ConfigRSA;
import cofh.redstonearsenal.init.CreativeTabsRSA;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.RGB_DURABILITY_FLUX;
import static cofh.lib.util.Constants.TAG_ENERGY;
import static cofh.lib.util.helpers.StringHelper.*;
import static cofh.lib.util.modhelpers.EnsorcellmentHelper.HOLDING;

public class ItemShieldFlux extends ItemShieldCoFH implements IEnergyContainerItem, IMultiModeItem {

	protected int maxEnergy = 160000;
	protected int maxTransfer = 4000;

	protected int energyPerUse = 200;
	protected int energyPerUseEmpowered = 800;

	protected int damageEmpowered = 6;

	public ItemShieldFlux(ToolMaterial toolMaterial) {

		super(toolMaterial);

		setCreativeTab(CreativeTabsRSA.tabBasicCombat);
		setMaxDamage(0);
		setNoRepair();

		addPropertyOverride(new ResourceLocation("charged"), (stack, world, entity) -> ItemShieldFlux.this.getEnergyStored(stack) > 0 && !ItemShieldFlux.this.isEmpowered(stack) ? 1F : 0F);
		addPropertyOverride(new ResourceLocation("empowered"), (stack, world, entity) -> ItemShieldFlux.this.isEmpowered(stack) ? 1F : 0F);
	}

	public ItemShieldFlux setEnergyParams(int maxEnergy, int maxTransfer, int energyPerUse, int energyPerUseEmpowered) {

		this.maxEnergy = maxEnergy;
		this.maxTransfer = maxTransfer;
		this.energyPerUse = energyPerUse;
		this.energyPerUseEmpowered = energyPerUseEmpowered;

		return this;
	}

	// region FLUX BOILERPLATE
	protected boolean isEmpowered(ItemStack stack) {

		return getMode(stack) == 1 && getEnergyStored(stack) >= energyPerUseEmpowered;
	}

	protected int getEnergyPerUse(ItemStack stack) {

		return isEmpowered(stack) ? energyPerUseEmpowered : energyPerUse;
	}

	protected int useEnergy(ItemStack stack, boolean simulate) {

		int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 10);
		if (MathHelper.RANDOM.nextInt(2 + unbreakingLevel) >= 2) {
			return 0;
		}
		return extractEnergy(stack, isEmpowered(stack) ? energyPerUseEmpowered : energyPerUse, simulate);
	}

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
		tooltip.add(localize("info.cofh.charge") + ": " + getScaledNumber(getEnergyStored(stack)) + " / " + getScaledNumber(getMaxEnergyStored(stack)) + " RF");
		tooltip.add(ORANGE + getEnergyPerUse(stack) + " " + localize("info.redstonearsenal.tool.energyPerUse") + END);
		ConfigRSA.addEmpoweredTip(this, stack, tooltip);

		if (getEnergyStored(stack) >= getEnergyPerUse(stack)) {
			tooltip.add("");
			tooltip.add(BRIGHT_GREEN + (isEmpowered(stack) ? damageEmpowered : 1) + " " + localize("info.cofh.damage_flux") + END);
		}
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

		return !oldStack.equals(newStack) && (getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {

		return ConfigRSA.showToolCharge && getEnergyStored(stack) > 0;
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

		return isEmpowered(stack) ? EnumRarity.RARE : EnumRarity.UNCOMMON;
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

			if (stored == 0) {
				setMode(container, 0);
			}
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

	// region IMultiModeItem
	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		if (isEmpowered(stack)) {
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.PLAYERS, 0.4F, 1.0F);
		} else {
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.6F);
		}
	}
	// endregion

	// region IToolShield
	@Override
	public void onBlock(ItemStack stack, EntityPlayer player, Entity source) {

		if (source != null && player != source) {
			float potionDamage = 1.0F;
			if (player.isPotionActive(MobEffects.STRENGTH)) {
				potionDamage += player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier() * 1.3F;
			}
			source.attackEntityFrom(DamageHelper.causePlayerFluxDamage(player), (isEmpowered(stack) ? damageEmpowered : 1) * potionDamage);
		}
		if (!player.capabilities.isCreativeMode) {
			useEnergy(stack, false);
		}
	}
	// endregion

	// region IModelRegister
	@Override
	@SideOnly (Side.CLIENT)
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();

		//String modelBase = "{\"parent\":\"" + domain + ":item/tools/shield\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "\"},\"overrides\":[{\"predicate\":{\"blocking\":1},\"model\":\"" + domain + ":item/tools/" + path + "_blocking\"}]}";
		String modelBase = "{\"parent\":\"" + domain + ":item/tools/shield\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "\"},\"overrides\":[{\"predicate\":{\"charged\":1},\"model\":\"" + domain + ":item/tools/" + path + "_charged\"},{\"predicate\":{\"blocking\":1,\"charged\":1},\"model\":\"" + domain + ":item/tools/" + path + "_charged_blocking\"},{\"predicate\":{\"empowered\":1},\"model\":\"" + domain + ":item/tools/" + path + "_empowered\"},{\"predicate\":{\"blocking\":1,\"empowered\":1},\"model\":\"" + domain + ":item/tools/" + path + "_empowered_blocking\"}]}";
		String modelCharged = "{\"parent\":\"" + domain + ":item/tools/" + path + "\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "_charged\"}}";
		String modelEmpowered = "{\"parent\":\"" + domain + ":item/tools/" + path + "\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "_empowered\"}}";

		String modelBlock = "{\"parent\":\"" + domain + ":item/tools/shield_blocking\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "\"}}";
		String modelChargedBlock = "{\"parent\":\"" + domain + ":item/tools/shield_blocking\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "_charged\"}}";
		String modelEmpoweredBlock = "{\"parent\":\"" + domain + ":item/tools/shield_blocking\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "_empowered\"}}";

		try {
			File itemModelBase = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + ".json");
			FileUtils.writeStringToFile(itemModelBase, Utils.createPrettyJSON(modelBase), Charset.forName("UTF-8"));

			File itemModelCharged = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_charged.json");
			FileUtils.writeStringToFile(itemModelCharged, Utils.createPrettyJSON(modelCharged), Charset.forName("UTF-8"));

			File itemModelEmpowered = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_empowered.json");
			FileUtils.writeStringToFile(itemModelEmpowered, Utils.createPrettyJSON(modelEmpowered), Charset.forName("UTF-8"));

			File itemModelBlock = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_blocking.json");
			FileUtils.writeStringToFile(itemModelBlock, Utils.createPrettyJSON(modelBlock), Charset.forName("UTF-8"));

			File itemModelChargedBlock = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_charged_blocking.json");
			FileUtils.writeStringToFile(itemModelChargedBlock, Utils.createPrettyJSON(modelChargedBlock), Charset.forName("UTF-8"));

			File itemModelEmpoweredBlock = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_empowered_blocking.json");
			FileUtils.writeStringToFile(itemModelEmpoweredBlock, Utils.createPrettyJSON(modelEmpoweredBlock), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
