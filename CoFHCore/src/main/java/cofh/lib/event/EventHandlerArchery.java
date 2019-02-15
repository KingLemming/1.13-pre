package cofh.lib.event;

import cofh.lib.item.IToolBow;
import cofh.lib.item.IToolQuiver;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static cofh.lib.util.Constants.DAMAGE_ARROW;
import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;
import static cofh.lib.util.helpers.ArcheryHelper.*;
import static cofh.lib.util.modhelpers.EnsorcellmentHelper.*;
import static net.minecraft.init.Enchantments.*;

public class EventHandlerArchery {

	private static final EventHandlerArchery INSTANCE = new EventHandlerArchery();
	private static boolean registered = false;

	public static void register() {

		if (registered) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registered = true;
	}

	private EventHandlerArchery() {

	}

	@SubscribeEvent (priority = EventPriority.HIGHEST)
	public void handleArrowLooseEvent(ArrowLooseEvent event) {

		// TODO: Adjust to work with other bows - capability? :/
		if (!(event.getBow().getItem() == Items.BOW) && !(event.getBow().getItem() instanceof IToolBow)) {
			return;
		}
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack bowStack = event.getBow();
		ItemStack arrowStack = findAmmo(player);

		boolean customArrow = false;
		boolean flag = player.capabilities.isCreativeMode || (arrowStack.getItem() instanceof ItemArrow && ((ItemArrow) arrowStack.getItem()).isInfinite(arrowStack, bowStack, player));

		if (arrowStack.isEmpty() && EnchantmentHelper.getEnchantmentLevel(INFINITY, bowStack) > 0) {
			flag = true;
		}
		if (!arrowStack.isEmpty() || flag) {
			if (arrowStack.isEmpty()) {
				arrowStack = new ItemStack(Items.ARROW);
			}
			float arrowVelocity = ItemBow.getArrowVelocity(event.getCharge());

			IToolBow bow = null;
			float speedMod = 1.0F;
			float damageMod = 1.0F;
			float accuracyMod = 1.0F;
			if (bowStack.getItem() instanceof IToolBow) {
				bow = (IToolBow) bowStack.getItem();
				speedMod += bow.getArrowSpeedMultiplier(bowStack);
				damageMod += bow.getArrowDamageMultiplier(bowStack);
			}
			if (arrowVelocity >= 0.1F) {
				if (Utils.isServerWorld(world)) {
					int encVolley = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(VOLLEY, bowStack), 0, MAX_ENCHANT_LEVEL);
					int encTrueshot = EnchantmentHelper.getEnchantmentLevel(TRUESHOT, bowStack);
					int encPunch = EnchantmentHelper.getEnchantmentLevel(PUNCH, bowStack);
					int encPower = EnchantmentHelper.getEnchantmentLevel(POWER, bowStack);
					int encFlame = EnchantmentHelper.getEnchantmentLevel(FLAME, bowStack);

					if (bow != null) {
						bow.onFired(bowStack, player);
						if (isQuiver(arrowStack) && !((IToolQuiver) arrowStack.getItem()).allowCustomArrowOverride(arrowStack)) {
							customArrow = false;
						} else {
							customArrow = bow.hasCustomArrow(bowStack);
						}
					}
					if (encTrueshot > 0) {
						accuracyMod = 0.25F;
						arrowVelocity = MathHelper.clamp(0.1F, arrowVelocity + 0.25F, 1.5F);
					}
					for (int shot = 0; shot <= encVolley; shot++) {
						EntityArrow arrow = createArrow(world, arrowStack, bowStack, customArrow, player);
						arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, arrowVelocity * 3.0F * speedMod, accuracyMod * (1.5F - arrowVelocity) * (shot * 2));
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
						if (flag || shot > 0) {
							arrow.pickupStatus = PickupStatus.CREATIVE_ONLY;
						}
						world.spawnEntity(arrow);
					}
					bowStack.damageItem(1 + (encVolley > 0 ? 1 : 0), player);
				}
				world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

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
				player.addStat(StatList.getObjectUseStats(bowStack.getItem()));
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent (priority = EventPriority.HIGHEST)
	public void handleArrowNockEvent(ArrowNockEvent event) {

		if (!(event.getBow().getItem() == Items.BOW) && !(event.getBow().getItem() instanceof IToolBow)) {
			return;
		}
		ItemStack stack = event.getBow();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack arrowStack = findAmmo(player);

		if (arrowStack.isEmpty() && EnchantmentHelper.getEnchantmentLevel(INFINITY, stack) > 0) {
			arrowStack = new ItemStack(Items.ARROW);
		}
		if (!arrowStack.isEmpty()) {
			player.setActiveHand(event.getHand());
			event.setAction(new ActionResult<>(EnumActionResult.SUCCESS, stack));
		} else if (!player.capabilities.isCreativeMode) {
			event.setAction(new ActionResult<>(EnumActionResult.FAIL, stack));
		}
	}

	@SubscribeEvent
	public void handleItemUseTickEvent(LivingEntityUseItemEvent.Tick event) {

		int encQuickDraw = EnchantmentHelper.getEnchantmentLevel(QUICKDRAW, event.getItem());
		if (encQuickDraw > 0 && event.getDuration() > event.getItem().getMaxItemUseDuration() - 20) {
			event.setDuration(event.getDuration() - 1);
		}
	}

	@SubscribeEvent (priority = EventPriority.HIGH)
	public void handleLivingHurtEvent(LivingHurtEvent event) {

		Entity entity = event.getEntity();
		DamageSource source = event.getSource();
		Entity attacker = event.getSource().getTrueSource();

		if (entity instanceof IProjectile) {
			return;
		}
		if (!(attacker instanceof EntityLivingBase)) {
			return;
		}
		if (source.damageType.equals(DAMAGE_ARROW)) {
			int encVolley = getHeldEnchantmentLevel((EntityLivingBase) attacker, VOLLEY);
			if (encVolley > 0) {
				entity.hurtResistantTime = 0;
			}
		}
	}

	// region HELPERS
	public static int getHeldEnchantmentLevel(EntityLivingBase player, Enchantment enc) {

		return Math.max(EnchantmentHelper.getEnchantmentLevel(enc, player.getHeldItemMainhand()), EnchantmentHelper.getEnchantmentLevel(enc, player.getHeldItemOffhand()));
	}

	public static int getEnchantmentLevel(ItemStack item, Enchantment enc) {

		return EnchantmentHelper.getEnchantmentLevel(enc, item);
	}

	public static void addEnchantment(ItemStack stack, Enchantment enc, int level) {

		stack.addEnchantment(enc, level);
	}
	// endregion
}
