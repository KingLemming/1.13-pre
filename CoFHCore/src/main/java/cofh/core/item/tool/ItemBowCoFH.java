package cofh.core.item.tool;

import cofh.lib.item.IFOVChangeItem;
import cofh.lib.item.IToolBow;
import cofh.lib.item.IToolQuiver;
import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ArcheryHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.helpers.ArcheryHelper.createArrow;
import static cofh.lib.util.helpers.ArcheryHelper.isQuiver;

public class ItemBowCoFH extends ItemBow implements IFOVChangeItem, IToolBow, IModelRegister {

	protected String repairItem = "";
	protected int enchantability;
	protected float damageMultiplier;
	protected float speedMultiplier;
	protected float zoomMultiplier;
	protected boolean showInCreativeTab = true;

	public ItemBowCoFH(ToolMaterial toolMaterial) {

		setMaxStackSize(1);
		setMaxDamage(toolMaterial.getMaxUses() + 325);
		setCreativeTab(CreativeTabs.COMBAT);

		this.enchantability = toolMaterial.getEnchantability();
		this.damageMultiplier = toolMaterial.getAttackDamage() / 4;
		this.speedMultiplier = toolMaterial.getEfficiency() / 20;
		this.zoomMultiplier = MathHelper.clamp(0.15F + toolMaterial.getHarvestLevel() * 0.05F, 0.15F, 0.30F);

		addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@SideOnly (Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {

				if (entityIn == null) {
					return 0.0F;
				} else {
					ItemStack itemstack = entityIn.getActiveItemStack();
					return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemBowCoFH ? (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
				}
			}
		});
		addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
			@SideOnly (Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {

				return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
	}

	public ItemBowCoFH setRepairItem(String repairItem) {

		this.repairItem = repairItem;
		return this;
	}

	public ItemBowCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (showInCreativeTab) {
			super.getSubItems(tab, items);
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

		if (!(entityLiving instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) entityLiving;
		ItemStack arrowStack = ArcheryHelper.findAmmo(player);

		boolean customArrow;
		boolean flag = player.capabilities.isCreativeMode || (arrowStack.getItem() instanceof ItemArrow && ((ItemArrow) arrowStack.getItem()).isInfinite(arrowStack, stack, player));

		int charge = this.getMaxItemUseDuration(stack) - timeLeft;
		charge = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, charge, !arrowStack.isEmpty() || flag);
		if (charge < 0) {
			return;
		}
		if (arrowStack.isEmpty() && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0) {
			flag = true;
		}
		if (!arrowStack.isEmpty() || flag) {
			if (arrowStack.isEmpty()) {
				arrowStack = new ItemStack(Items.ARROW);
			}
			float arrowVelocity = getArrowVelocity(charge);
			float speedMod = 1.0F + getArrowSpeedMultiplier(stack);
			float damageMod = 1.0F + getArrowDamageMultiplier(stack);

			if (arrowVelocity >= 0.1F) {
				if (Utils.isServerWorld(worldIn)) {
					int encPunch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
					int encPower = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
					int encFlame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);

					onFired(stack, player);
					if (isQuiver(arrowStack) && !((IToolQuiver) arrowStack.getItem()).allowCustomArrowOverride(arrowStack)) {
						customArrow = false;
					} else {
						customArrow = hasCustomArrow(stack);
					}
					EntityArrow arrow = createArrow(worldIn, arrowStack, stack, customArrow, player);
					arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, arrowVelocity * 3.0F * speedMod, 1.0F);
					arrow.setDamage(arrow.getDamage() * damageMod);

					if (arrowVelocity >= 1.0F) {
						arrow.setIsCritical(true);
					}
					if (encPower > 0) {
						arrow.setDamage(arrow.getDamage() + (double) encPower * 0.5D + 0.5D);
					}
					if (encPunch > 0) {
						arrow.setKnockbackStrength(encPunch);
					}
					if (encFlame > 0) {
						arrow.setFire(100);
					}
					if (flag) {
						arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
					}
					worldIn.spawnEntity(arrow);
					stack.damageItem(1, player);
				}
				worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

				if (!flag && !player.capabilities.isCreativeMode) {
					if (isQuiver(arrowStack)) {
						((IToolQuiver) arrowStack.getItem()).onArrowFired(arrowStack, player);
					} else {
						arrowStack.shrink(1);
						if (arrowStack.isEmpty()) {
							player.inventory.deleteStack(arrowStack);
						}
					}
				}
				player.addStat(StatList.getObjectUseStats(this));
			}
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {

		return OreDictHelper.isOreNameEqual(stack, repairItem);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {

		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return enchantability;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		boolean flag = !this.findAmmo(player).isEmpty();

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(stack, world, player, hand, flag);
		if (ret != null) {
			return ret;
		}
		if (!player.capabilities.isCreativeMode && !flag) {
			return !flag ? new ActionResult<>(EnumActionResult.FAIL, stack) : new ActionResult<>(EnumActionResult.PASS, stack);
		} else {
			player.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
	}

	// region IFOVChangeItem
	@Override
	public float getFOVMod(ItemStack stack, EntityPlayer player) {

		float progress = MathHelper.clamp((stack.getMaxItemUseDuration() - player.getItemInUseCount()) / 20.0F, 0, 1.0F);
		return progress * progress * zoomMultiplier;
	}
	// endregion

	// region IToolBow
	@Override
	public void onFired(ItemStack item, EntityPlayer player) {

	}

	@Override
	public float getArrowDamageMultiplier(ItemStack item) {

		return damageMultiplier;
	}

	@Override
	public float getArrowSpeedMultiplier(ItemStack item) {

		return speedMultiplier;
	}
	// endregion

	// region IModelRegister
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().getResourceDomain() + ":tools/" + getRegistryName().getResourcePath(), "inventory"));
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();

		String modelBase = "{\"parent\":\"" + domain + ":item/tools/bow\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/" + path + "\"},\"overrides\":[{\"predicate\":{\"pulling\":1},\"model\":\"" + domain + ":item/tools/" + path + "_0\"},{\"predicate\":{\"pulling\":1,\"pull\":0.65},\"model\":\"" + domain + ":item/tools/" + path + "_1\"},{\"predicate\":{\"pulling\":1,\"pull\":0.9},\"model\":\"" + domain + ":item/tools/" + path + "_2\"}]}";
		try {
			File itemModel = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + ".json");
			FileUtils.writeStringToFile(itemModel, Utils.createPrettyJSON(modelBase), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
		for (int i = 0; i < 3; i++) {
			String modelSub = "{\"parent\":\"" + domain + ":item/tools/" + path + "\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/" + path + "_" + i + "\"}}";
			try {
				File itemModelSub = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_" + i + ".json");
				FileUtils.writeStringToFile(itemModelSub, Utils.createPrettyJSON(modelSub), Charset.forName("UTF-8"));
			} catch (Throwable t) {
				// pokemon!
			}
		}
	}
	// endregion
}
