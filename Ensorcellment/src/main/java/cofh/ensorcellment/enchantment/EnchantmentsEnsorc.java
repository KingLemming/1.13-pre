package cofh.ensorcellment.enchantment;

import cofh.ensorcellment.enchantment.bow.EnchantmentMultishot;
import cofh.ensorcellment.enchantment.bow.EnchantmentQuickDraw;
import cofh.ensorcellment.enchantment.bow.EnchantmentTrueshot;
import cofh.ensorcellment.enchantment.digger.EnchantmentInsight;
import cofh.ensorcellment.enchantment.digger.EnchantmentSmashing;
import cofh.ensorcellment.enchantment.digger.EnchantmentSmelting;
import cofh.ensorcellment.enchantment.misc.EnchantmentHolding;
import cofh.ensorcellment.enchantment.misc.EnchantmentSoulbound;
import cofh.ensorcellment.enchantment.override.*;
import cofh.ensorcellment.enchantment.shield.EnchantmentBulwark;
import cofh.ensorcellment.enchantment.shield.EnchantmentPhalanx;
import cofh.ensorcellment.enchantment.weapon.EnchantmentLeech;
import cofh.ensorcellment.enchantment.weapon.EnchantmentVorpal;
import net.minecraft.enchantment.Enchantment;
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

		overrideThorns = new EnchantmentThornsImp(ID_THORNS);
		overrideFrostWalker = new EnchantmentFrostWalkerImp(ID_FROST_WALKER);
		overrideFireAspect = new EnchantmentFireAspectImp(ID_FIRE_ASPECT);
		overrideLooting = new EnchantmentLootingImp(ID_LOOTING);
		overrideMending = new EnchantmentMendingAlt(ID_MENDING);

		miscHolding = new EnchantmentHolding(ID_HOLDING);
		miscSoulbound = new EnchantmentSoulbound(ID_SOULBOUND);

		weaponLeech = new EnchantmentLeech(ID_LEECH);
		weaponVorpal = new EnchantmentVorpal(ID_VORPAL);

		diggerInsight = new EnchantmentInsight(ID_INSIGHT);
		diggerSmashing = new EnchantmentSmashing(ID_SMASHING);
		diggerSmelting = new EnchantmentSmelting(ID_SMELTING);

		bowMultishot = new EnchantmentMultishot(ID_MULTISHOT);
		bowQuickdraw = new EnchantmentQuickDraw(ID_QUICKDRAW);
		bowTrueshot = new EnchantmentTrueshot(ID_TRUESHOT);

		shieldBulwark = new EnchantmentBulwark(ID_BULWARK);
		shieldPhalanx = new EnchantmentPhalanx(ID_PHALANX);

		registerEnchantmentOverride(registry, overrideThorns);
		registerEnchantmentOverride(registry, overrideFrostWalker);
		registerEnchantmentOverride(registry, overrideFireAspect);
		registerEnchantmentOverride(registry, overrideLooting);
		registerEnchantmentOverride(registry, overrideMending);

		registerEnchantment(registry, miscHolding);
		registerEnchantment(registry, miscSoulbound);

		registerEnchantment(registry, weaponLeech);
		registerEnchantment(registry, weaponVorpal);

		registerEnchantment(registry, diggerInsight);
		registerEnchantment(registry, diggerSmashing);
		registerEnchantment(registry, diggerSmelting);

		registerEnchantment(registry, bowMultishot);
		registerEnchantment(registry, bowQuickdraw);
		registerEnchantment(registry, bowTrueshot);

		registerEnchantment(registry, shieldBulwark);
		registerEnchantment(registry, shieldPhalanx);
	}

	private static void registerEnchantment(IForgeRegistry<Enchantment> registry, EnchantmentBase enchantment) {

		if (hardDisable && !enchantment.enable) {
			return;
		}
		registry.register(enchantment);
	}

	private static void registerEnchantmentOverride(IForgeRegistry<Enchantment> registry, EnchantmentBase enchantment) {

		if (!enchantment.enable) {
			return;
		}
		registry.register(enchantment);
	}
	// endregion

	// region REFERENCES
	public static EnchantmentBase overrideProtection;
	public static EnchantmentBase overrideThorns;
	public static EnchantmentBase overrideFrostWalker;
	public static EnchantmentBase overrideFireAspect;
	public static EnchantmentBase overrideLooting;
	public static EnchantmentBase overrideMending;

	public static EnchantmentBase miscHolding;
	public static EnchantmentBase miscSoulbound;

	public static EnchantmentBase weaponLeech;
	public static EnchantmentBase weaponVorpal;

	public static EnchantmentBase diggerInsight;
	public static EnchantmentBase diggerSmashing;
	public static EnchantmentBase diggerSmelting;

	public static EnchantmentBase bowMultishot;
	public static EnchantmentBase bowQuickdraw;
	public static EnchantmentBase bowTrueshot;

	public static EnchantmentBase shieldBulwark;
	public static EnchantmentBase shieldPhalanx;
	// endregion
}
