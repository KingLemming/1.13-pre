package cofh.ensorcellment.enchantment;

import cofh.ensorcellment.enchantment.armor.EnchantmentAirWorker;
import cofh.ensorcellment.enchantment.armor.EnchantmentDisplacement;
import cofh.ensorcellment.enchantment.armor.EnchantmentGourmand;
import cofh.ensorcellment.enchantment.bow.EnchantmentQuickdraw;
import cofh.ensorcellment.enchantment.bow.EnchantmentTrueshot;
import cofh.ensorcellment.enchantment.bow.EnchantmentVolley;
import cofh.ensorcellment.enchantment.digger.EnchantmentInsight;
import cofh.ensorcellment.enchantment.digger.EnchantmentSmashing;
import cofh.ensorcellment.enchantment.digger.EnchantmentSmelting;
import cofh.ensorcellment.enchantment.looting.EnchantmentAngler;
import cofh.ensorcellment.enchantment.looting.EnchantmentFarmer;
import cofh.ensorcellment.enchantment.looting.EnchantmentHunter;
import cofh.ensorcellment.enchantment.misc.EnchantmentHolding;
import cofh.ensorcellment.enchantment.misc.EnchantmentSoulbound;
import cofh.ensorcellment.enchantment.override.*;
import cofh.ensorcellment.enchantment.shield.EnchantmentBulwark;
import cofh.ensorcellment.enchantment.shield.EnchantmentPhalanx;
import cofh.ensorcellment.enchantment.weapon.EnchantmentLeech;
import cofh.ensorcellment.enchantment.weapon.EnchantmentVorpal;
import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentProtection.Type;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static cofh.ensorcellment.Ensorcellment.hardDisable;
import static cofh.lib.util.modhelpers.EnsorcellmentHelper.*;

public class EnchantmentsEnsorc {

	private EnchantmentsEnsorc() {

	}

	// region REGISTRATION
	public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {

		IForgeRegistry<Enchantment> registry = event.getRegistry();

		EntityEquipmentSlot[] armorSlots = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
		overrideProtection = new EnchantmentProtectionImp(ID_PROTECTION, Rarity.COMMON, Type.ALL, armorSlots, "Protection", "Protection");
		overrideProtectionFire = new EnchantmentProtectionImp(ID_PROTECTION_FIRE, Rarity.UNCOMMON, Type.FIRE, armorSlots, "FireProtection", "Fire Protection");
		overrideProtectionFall = new EnchantmentProtectionImp(ID_PROTECTION_FALL, Rarity.UNCOMMON, Type.FALL, armorSlots, "FeatherFalling", "Feather Falling");
		overrideProtectionBlast = new EnchantmentProtectionImp(ID_PROTECTION_BLAST, Rarity.RARE, Type.EXPLOSION, armorSlots, "BlastProtection", "Blast Protection");
		overrideProtectionProjectile = new EnchantmentProtectionImp(ID_PROTECTION_PROJECTILE, Rarity.UNCOMMON, Type.PROJECTILE, armorSlots, "ProjectileProtection", "Projectile Protection");

		overrideThorns = new EnchantmentThornsImp(ID_THORNS);
		overrideFrostWalker = new EnchantmentFrostWalkerImp(ID_FROST_WALKER);
		overrideKnockback = new EnchantmentKnockbackImp(ID_KNOCKBACK);
		overrideFireAspect = new EnchantmentFireAspectImp(ID_FIRE_ASPECT);
		overrideLooting = new EnchantmentLootingImp(ID_LOOTING);
		overrideMending = new EnchantmentMendingAlt(ID_MENDING);

		miscHolding = new EnchantmentHolding(ID_HOLDING);
		miscSoulbound = new EnchantmentSoulbound(ID_SOULBOUND);

		armorAirWorker = new EnchantmentAirWorker(ID_AIR_WORKER);
		armorDisplacement = new EnchantmentDisplacement(ID_DISPLACEMENT);
		armorGourmand = new EnchantmentGourmand(ID_GOURMAND);

		weaponLeech = new EnchantmentLeech(ID_LEECH);
		weaponVorpal = new EnchantmentVorpal(ID_VORPAL);

		diggerInsight = new EnchantmentInsight(ID_INSIGHT);
		diggerSmashing = new EnchantmentSmashing(ID_SMASHING);
		diggerSmelting = new EnchantmentSmelting(ID_SMELTING);

		bowQuickdraw = new EnchantmentQuickdraw(ID_QUICKDRAW);
		bowTrueshot = new EnchantmentTrueshot(ID_TRUESHOT);
		bowVolley = new EnchantmentVolley(ID_VOLLEY);

		shieldBulwark = new EnchantmentBulwark(ID_BULWARK);
		shieldPhalanx = new EnchantmentPhalanx(ID_PHALANX);

		boostAngler = new EnchantmentAngler(ID_ANGLER);
		boostFarmer = new EnchantmentFarmer(ID_FARMER);
		boostHunter = new EnchantmentHunter(ID_HUNTER);

		registerEnchantmentOverride(registry, overrideProtection);
		registerEnchantmentOverride(registry, overrideProtectionFire);
		registerEnchantmentOverride(registry, overrideProtectionFall);
		registerEnchantmentOverride(registry, overrideProtectionBlast);
		registerEnchantmentOverride(registry, overrideProtectionProjectile);

		registerEnchantmentOverride(registry, overrideThorns);
		registerEnchantmentOverride(registry, overrideFrostWalker);
		registerEnchantmentOverride(registry, overrideKnockback);
		registerEnchantmentOverride(registry, overrideFireAspect);
		registerEnchantmentOverride(registry, overrideLooting);
		registerEnchantmentOverride(registry, overrideMending);

		registerEnchantment(registry, miscHolding);
		registerEnchantment(registry, miscSoulbound);

		registerEnchantment(registry, armorAirWorker);
		registerEnchantment(registry, armorDisplacement);
		registerEnchantment(registry, armorGourmand);

		registerEnchantment(registry, weaponLeech);
		registerEnchantment(registry, weaponVorpal);

		registerEnchantment(registry, diggerInsight);
		registerEnchantment(registry, diggerSmashing);
		registerEnchantment(registry, diggerSmelting);

		registerEnchantment(registry, bowQuickdraw);
		registerEnchantment(registry, bowTrueshot);
		registerEnchantment(registry, bowVolley);

		registerEnchantment(registry, shieldBulwark);
		registerEnchantment(registry, shieldPhalanx);

		registerEnchantment(registry, boostAngler);
		registerEnchantment(registry, boostFarmer);
		registerEnchantment(registry, boostHunter);
	}

	private static void registerEnchantment(IForgeRegistry<Enchantment> registry, EnchantmentCoFH enchantment) {

		if (hardDisable && !enchantment.enable) {
			return;
		}
		registry.register(enchantment);
	}

	private static void registerEnchantmentOverride(IForgeRegistry<Enchantment> registry, EnchantmentCoFH enchantment) {

		if (!enchantment.enable) {
			return;
		}
		registry.register(enchantment);
	}
	// endregion

	// region REFERENCES
	public static EnchantmentCoFH overrideProtection;
	public static EnchantmentCoFH overrideProtectionFire;
	public static EnchantmentCoFH overrideProtectionFall;
	public static EnchantmentCoFH overrideProtectionBlast;
	public static EnchantmentCoFH overrideProtectionProjectile;

	public static EnchantmentCoFH overrideThorns;
	public static EnchantmentCoFH overrideFrostWalker;
	public static EnchantmentCoFH overrideKnockback;
	public static EnchantmentCoFH overrideFireAspect;
	public static EnchantmentCoFH overrideLooting;
	public static EnchantmentCoFH overrideMending;

	public static EnchantmentCoFH miscHolding;
	public static EnchantmentCoFH miscSoulbound;

	public static EnchantmentCoFH armorAirWorker;
	public static EnchantmentCoFH armorDisplacement;
	public static EnchantmentCoFH armorGourmand;

	public static EnchantmentCoFH weaponLeech;
	public static EnchantmentCoFH weaponVorpal;

	public static EnchantmentCoFH diggerInsight;
	public static EnchantmentCoFH diggerSmashing;
	public static EnchantmentCoFH diggerSmelting;

	public static EnchantmentCoFH bowVolley;
	public static EnchantmentCoFH bowQuickdraw;
	public static EnchantmentCoFH bowTrueshot;

	public static EnchantmentCoFH shieldBulwark;
	public static EnchantmentCoFH shieldPhalanx;

	public static EnchantmentCoFH boostAngler;
	public static EnchantmentCoFH boostFarmer;
	public static EnchantmentCoFH boostHunter;
	// endregion
}
