package cofh.ensorcellment.proxy;

import cofh.ensorcellment.enchantment.EnchantmentsEnsorc;
import cofh.ensorcellment.enchantment.digger.EnchantmentInsight;
import cofh.ensorcellment.enchantment.digger.EnchantmentSmashing;
import cofh.ensorcellment.enchantment.digger.EnchantmentSmelting;
import cofh.ensorcellment.enchantment.misc.EnchantmentSoulbound;
import cofh.ensorcellment.enchantment.override.EnchantmentMendingAlt;
import cofh.ensorcellment.enchantment.override.EnchantmentProtectionImp;
import cofh.ensorcellment.enchantment.shield.EnchantmentPhalanx;
import cofh.ensorcellment.enchantment.weapon.EnchantmentVorpal;
import cofh.lib.util.helpers.MathHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ListIterator;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.modhelpers.EnsorcellmentHelper.*;
import static net.minecraft.init.Enchantments.*;

public class EventHandler {

	private static final EventHandler INSTANCE = new EventHandler();
	private static boolean registered = false;

	public static void register() {

		if (registered) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registered = true;
	}

	private EventHandler() {

	}

	// region LIVING EVENTS
	@SubscribeEvent
	public void handleLivingAttackEvent(LivingAttackEvent event) {

		Entity entity = event.getEntity();
		DamageSource source = event.getSource();
		Entity attacker = event.getSource().getTrueSource();

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack stack = player.getActiveItemStack();

			if (stack.getItem().isShield(stack, player)) {
				int encThorns = EnchantmentHelper.getEnchantmentLevel(THORNS, stack);
				if (EnchantmentThorns.shouldHit(encThorns, MathHelper.RANDOM) && attacker != null) {
					attacker.attackEntityFrom(DamageSource.causeThornsDamage(entity), EnchantmentThorns.getDamage(encThorns, MathHelper.RANDOM));
					if (MathHelper.RANDOM.nextInt(1 + encThorns) == 0) {
						player.getCooldownTracker().setCooldown(stack.getItem(), 40);
						player.resetActiveHand();
						player.world.setEntityState(player, (byte) 30);
					}
				}
			}
		} else if (entity instanceof EntityHorse) {
			ItemStack armor = ((EntityHorse) entity).horseChest.getStackInSlot(1);
			if (!armor.isEmpty()) {
				int encFrostWalker = EnchantmentHelper.getEnchantmentLevel(FROST_WALKER, armor);
				if (source.equals(DamageSource.HOT_FLOOR) && encFrostWalker > 0) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void handleLivingDeathEvent(LivingDeathEvent event) {

		Entity entity = event.getEntity();
		Entity attacker = event.getSource().getTrueSource();

		if (attacker instanceof EntityPlayer) {
			int encLeech = getHeldEnchantmentLevel((EntityPlayer) attacker, LEECH);
			int encInsight = getHeldEnchantmentLevel((EntityPlayer) attacker, INSIGHT);
			if (encLeech > 0) {
				((EntityPlayer) attacker).heal(encLeech);
			}
			if (encInsight > 0) {
				entity.world.spawnEntity(new EntityXPOrb(entity.world, entity.posX, entity.posY + 0.5D, entity.posZ, encInsight + entity.world.rand.nextInt(1 + encInsight * EnchantmentInsight.experience)));
			}
		}
	}

	@SubscribeEvent
	public void handleLivingHurtEvent(LivingHurtEvent event) {

		Entity entity = event.getEntity();
		DamageSource source = event.getSource();
		Entity attacker = event.getSource().getTrueSource();

		// region HORSE ARMOR
		if (entity instanceof EntityHorse) {
			ItemStack armor = ((EntityHorse) entity).horseChest.getStackInSlot(1);

			if (!armor.isEmpty()) {
				int encProtection = EnchantmentHelper.getEnchantmentLevel(PROTECTION, armor);
				int encThorns = EnchantmentHelper.getEnchantmentLevel(THORNS, armor);
				if (encProtection > 0) {
					float damageReduction = Math.min(encProtection * EnchantmentProtectionImp.protection, 20) / 25F;
					event.setAmount(event.getAmount() * damageReduction);
				}
				if (EnchantmentThorns.shouldHit(encThorns, MathHelper.RANDOM) && attacker != null) {
					attacker.attackEntityFrom(DamageSource.causeThornsDamage(entity), EnchantmentThorns.getDamage(encThorns, MathHelper.RANDOM));
				}
			}
		}
		// endregion

		// region DAMAGE
		if (entity instanceof IProjectile) {
			return;
		}
		if (!(attacker instanceof EntityLivingBase)) {
			return;
		}
		if (source.damageType.equals(DAMAGE_PLAYER)) {
			int encVorpal = getHeldEnchantmentLevel((EntityLivingBase) attacker, VORPAL);
			if (encVorpal > 0 && entity.world.rand.nextInt(100) < EnchantmentVorpal.critChance * encVorpal) {
				event.setAmount(event.getAmount() * EnchantmentVorpal.critDamage);
				attacker.world.playSound(null, attacker.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1.0F, 1.0F);
				for (int i = 0; i < encVorpal * 2; i++) {
					((WorldServer) entity.world).spawnParticle(EnumParticleTypes.CRIT, entity.posX + entity.world.rand.nextDouble(), entity.posY + 1.5D, entity.posZ + entity.world.rand.nextDouble(), 1, 0, 0, 0, 0.0, 0);
					((WorldServer) entity.world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, entity.posX + entity.world.rand.nextDouble(), entity.posY + 1.5D, entity.posZ + entity.world.rand.nextDouble(), 1, 0, 0, 0, 0.0, 0);
				}
			}
		}
		// endregion
	}

	@SubscribeEvent
	public void handleLivingUpdateEvent(LivingUpdateEvent event) {

		Entity entity = event.getEntity();

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			ItemStack stack = player.getActiveItemStack();
			if (stack.getItem().isShield(stack, player)) {
				int encBulwark = EnchantmentHelper.getEnchantmentLevel(BULWARK, stack);
				int encPhalanx = EnchantmentHelper.getEnchantmentLevel(PHALANX, stack);

				Multimap<String, AttributeModifier> attributes = HashMultimap.create();
				if (encBulwark > 0) {
					attributes.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(UUID_KNOCKBACK_RESISTANCE, "Bulwark Enchant", encBulwark * 1.0D, 0).setSaved(false));
				}
				if (encPhalanx > 0) {
					attributes.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID_MOVEMENT_SPEED, "Phalanx Enchant", EnchantmentPhalanx.SPEED * encPhalanx, 2).setSaved(false));
				}
				if (!attributes.isEmpty()) {
					player.getAttributeMap().applyAttributeModifiers(attributes);
				}
			} else {
				Multimap<String, AttributeModifier> attributes = HashMultimap.create();
				attributes.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(UUID_KNOCKBACK_RESISTANCE, "Bulwark Enchant", 1.0D, 0).setSaved(false));
				attributes.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID_MOVEMENT_SPEED, "Phalanx Enchant", EnchantmentPhalanx.SPEED, 2).setSaved(false));
				player.getAttributeMap().removeAttributeModifiers(attributes);
			}
		} else if (entity instanceof EntityHorse) {
			ItemStack armor = ((EntityHorse) entity).horseChest.getStackInSlot(1);
			if (!armor.isEmpty()) {
				int encFrostWalker = EnchantmentHelper.getEnchantmentLevel(FROST_WALKER, armor);
				if (encFrostWalker > 0) {
					EnchantmentFrostWalker.freezeNearby((EntityHorse) entity, entity.world, new BlockPos(entity), encFrostWalker);
				}
			}
		}
	}
	// endregion

	// region BLOCK BREAKING
	@SubscribeEvent
	public void handleBlockBreakEvent(BlockEvent.BreakEvent event) {

		EntityPlayer player = event.getPlayer();

		if (player == null) {
			return;
		}
		if (event.getExpToDrop() > 0) {
			int encInsight = EnchantmentHelper.getEnchantmentLevel(INSIGHT, player.getHeldItemMainhand());
			if (encInsight > 0) {
				event.setExpToDrop(event.getExpToDrop() + encInsight + player.world.rand.nextInt(1 + encInsight * EnchantmentInsight.experience));
			}
		}
	}

	@SubscribeEvent (priority = EventPriority.LOW)
	public void handleHarvestDropsEvent(final BlockEvent.HarvestDropsEvent event) {

		final EntityPlayer player = event.getHarvester();

		if (player == null || event.isSilkTouching()) {
			return;
		}
		final ItemStack tool = player.getHeldItemMainhand();
		final int encSmashing = EnchantmentHelper.getEnchantmentLevel(SMASHING, tool);
		final int encSmelting = EnchantmentHelper.getEnchantmentLevel(SMELTING, tool);

		event.getDrops().replaceAll(stack -> {
			if (stack.isEmpty()) {
				return stack; // Nope, processing on this sometimes results in...results.
			}
			ItemStack result = stack;
			if (encSmashing > 0) {
				ItemStack smashed = EnchantmentSmashing.getItemStack(result);
				if (!smashed.isEmpty()) {
					result = smashed;
					tool.damageItem(1, player);
				}
			}
			if (encSmelting > 0) {
				ItemStack smelted = EnchantmentSmelting.getItemStack(result);
				if (!smelted.isEmpty()) {
					result = smelted;
					tool.damageItem(1, player);
				}
			}
			return result;
		});
	}
	// endregion

	// region PRESERVATION
	@SubscribeEvent (priority = EventPriority.HIGH)
	public void handleAnvilRepairEvent(AnvilRepairEvent event) {

		if (!EnchantmentsEnsorc.overrideMending.enable) {
			return;
		}
		ItemStack left = event.getItemInput();
		if (getEnchantmentLevel(left, Enchantments.MENDING) < 1) {
			return;
		}
		ItemStack output = event.getItemResult();

		if (output.getItemDamage() < left.getItemDamage()) {
			event.setBreakChance(EnchantmentMendingAlt.anvilDamage);
		}
	}

	@SubscribeEvent (priority = EventPriority.NORMAL)
	public void handleAnvilUpdateEvent(AnvilUpdateEvent event) {

		if (!EnchantmentsEnsorc.overrideMending.enable) {
			return;
		}
		ItemStack left = event.getLeft();
		if (getEnchantmentLevel(left, Enchantments.MENDING) < 1) {
			return;
		}
		ItemStack right = event.getRight();
		ItemStack output = left.copy();

		if (output.isItemStackDamageable() && output.getItem().getIsRepairable(left, right)) {
			int damageLeft = Math.min(output.getItemDamage(), output.getMaxDamage() / 4);
			if (damageLeft <= 0) {
				return;
			}
			int matCost;
			for (matCost = 0; damageLeft > 0 && matCost < right.getCount(); ++matCost) {
				int j3 = output.getItemDamage() - damageLeft;
				output.setItemDamage(j3);
				damageLeft = Math.min(output.getItemDamage(), output.getMaxDamage() / 4);
			}
			event.setMaterialCost(matCost);
			//event.setCost(0);
			event.setOutput(output);
		} else if (output.isItemStackDamageable()) {
			int damageLeft = left.getMaxDamage() - left.getItemDamage();
			int damageRight = right.getMaxDamage() - right.getItemDamage();
			int damageRepair = damageLeft + damageRight + output.getMaxDamage() * 12 / 100;
			int damageOutput = output.getMaxDamage() - damageRepair;

			if (damageOutput < 0) {
				damageOutput = 0;
			}
			if (damageOutput < output.getItemDamage()) { // vanilla uses metadata here instead of damage.
				output.setItemDamage(damageOutput);
			}
			//event.setCost(0);
			event.setOutput(output);
		}
	}

	@SubscribeEvent (priority = EventPriority.LOWEST)
	public void handlePlayerPickupXpEvent(PlayerPickupXpEvent event) {

		if (!EnchantmentsEnsorc.overrideMending.enable) {
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		EntityXPOrb orb = event.getOrb();

		player.xpCooldown = 2;
		player.onItemPickup(orb, 1);
		if (orb.xpValue > 0) {
			player.addExperience(orb.xpValue);
		}
		orb.setDead();
		event.setCanceled(true);
	}
	// endregion

	// region SOULBOUND
	@SubscribeEvent (priority = EventPriority.HIGHEST)
	public void handleLivingDropsEvent(LivingDropsEvent event) {

		Entity source = event.getSource().getTrueSource();

		if (!(source instanceof EntityPlayer) || !event.isRecentlyHit()) {
			return;
		}
		EntityPlayer player = (EntityPlayer) source;
		int encVorpal = EnchantmentHelper.getEnchantmentLevel(VORPAL, player.getHeldItemMainhand());

		if (encVorpal > 0) {
			Entity entity = event.getEntity();
			ItemStack itemSkull = ItemStack.EMPTY;

			if (entity.world.rand.nextInt(100) < EnchantmentVorpal.headChance * encVorpal) {
				if (entity instanceof EntityPlayerMP) {
					EntityPlayer target = (EntityPlayerMP) event.getEntity();
					itemSkull = new ItemStack(Items.SKULL, 1, 3);
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("SkullOwner", target.getName());
					itemSkull.setTagCompound(tag);
				} else if (entity instanceof EntitySkeleton) {
					itemSkull = new ItemStack(Items.SKULL, 1, 0);
				} else if (entity instanceof EntityWitherSkeleton) {
					itemSkull = new ItemStack(Items.SKULL, 1, 1);
				} else if (entity instanceof EntityZombie) {
					itemSkull = new ItemStack(Items.SKULL, 1, 2);
				} else if (entity instanceof EntityCreeper) {
					itemSkull = new ItemStack(Items.SKULL, 1, 4);
				}
			}
			if (itemSkull.isEmpty()) {
				return;
			}
			EntityItem drop = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, itemSkull);
			drop.setPickupDelay(10);
			event.getDrops().add(drop);
		}
	}

	@SubscribeEvent (priority = EventPriority.HIGH)
	public void handlePlayerDropsEvent(PlayerDropsEvent event) {

		EntityPlayer player = event.getEntityPlayer();

		if (player instanceof FakePlayer) {
			return;
		}
		if (player.world.getGameRules().getBoolean("keepInventory")) {
			return;
		}
		ListIterator<EntityItem> iter = event.getDrops().listIterator();
		while (iter.hasNext()) {
			EntityItem drop = iter.next();
			ItemStack stack = drop.getItem();
			if (isSoulbound(stack)) {
				if (addToPlayerInventory(player, stack)) {
					iter.remove();
				}
			}
		}
	}

	@SubscribeEvent (priority = EventPriority.HIGH)
	public void handlePlayerCloneEvent(PlayerEvent.Clone event) {

		if (!event.isWasDeath()) {
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		EntityPlayer oldPlayer = event.getOriginal();

		if (player instanceof FakePlayer) {
			return;
		}
		if (player.world.getGameRules().getBoolean("keepInventory")) {
			return;
		}
		for (int i = 0; i < oldPlayer.inventory.armorInventory.size(); i++) {
			ItemStack stack = oldPlayer.inventory.armorInventory.get(i);
			int encSoulbound = EnchantmentHelper.getEnchantmentLevel(SOULBOUND, stack);
			if (encSoulbound > 0) {
				if (EnchantmentSoulbound.permanent) {
					if (encSoulbound > 1) {
						removeEnchantment(stack, SOULBOUND);
						addEnchantment(stack, SOULBOUND, 1);
					}
				} else if (player.world.rand.nextInt(1 + encSoulbound) == 0) {
					removeEnchantment(stack, SOULBOUND);
					if (encSoulbound > 1) {
						addEnchantment(stack, SOULBOUND, encSoulbound - 1);
					}
				}
				if (addToPlayerInventory(player, stack)) {
					oldPlayer.inventory.armorInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
		for (int i = 0; i < oldPlayer.inventory.mainInventory.size(); i++) {
			ItemStack stack = oldPlayer.inventory.mainInventory.get(i);
			int encSoulbound = EnchantmentHelper.getEnchantmentLevel(SOULBOUND, stack);
			if (encSoulbound > 0) {
				if (player.world.rand.nextInt(1 + encSoulbound) == 0) {
					removeEnchantment(stack, SOULBOUND);
					if (encSoulbound > 1) {
						addEnchantment(stack, SOULBOUND, encSoulbound - 1);
					}
				}
				if (addToPlayerInventory(player, stack)) {
					oldPlayer.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
	}
	// endregion

	// region HELPERS
	public static boolean addToPlayerInventory(EntityPlayer player, ItemStack stack) {

		if (stack.isEmpty() || player == null) {
			return false;
		}
		if (stack.getItem() instanceof ItemArmor) {
			ItemArmor arm = (ItemArmor) stack.getItem();
			int index = arm.armorType.getIndex();
			if (player.inventory.armorInventory.get(index).isEmpty()) {
				player.inventory.armorInventory.set(index, stack);
				return true;
			}
		}
		InventoryPlayer inv = player.inventory;
		for (int i = 0; i < inv.mainInventory.size(); i++) {
			if (inv.mainInventory.get(i).isEmpty()) {
				inv.mainInventory.set(i, stack.copy());
				return true;
			}
		}
		return false;
	}

	public static boolean isSoulbound(ItemStack stack) {

		return getEnchantmentLevel(stack, SOULBOUND) > 0;
	}

	public static int getHeldEnchantmentLevel(EntityLivingBase player, Enchantment enc) {

		return Math.max(EnchantmentHelper.getEnchantmentLevel(enc, player.getHeldItemMainhand()), EnchantmentHelper.getEnchantmentLevel(enc, player.getHeldItemOffhand()));
	}

	public static int getEnchantmentLevel(ItemStack item, Enchantment enc) {

		return EnchantmentHelper.getEnchantmentLevel(enc, item);
	}

	public static void addEnchantment(ItemStack stack, Enchantment enc, int level) {

		stack.addEnchantment(enc, level);
	}

	public static void removeEnchantment(ItemStack stack, Enchantment enc) {

		if (stack.getTagCompound() == null) {
			return;
		}
		if (!stack.getTagCompound().hasKey("ench", 9)) {
			return;
		}
		NBTTagList list = stack.getTagCompound().getTagList("ench", 10);
		int encId = Enchantment.getEnchantmentID(enc);

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			int id = tag.getInteger("id");
			if (encId == id) {
				list.removeTag(i);
				break;
			}
		}
	}
	// endregion
}
