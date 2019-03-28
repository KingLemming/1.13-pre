package cofh.thermal.foundation.init;

import cofh.core.item.ItemCoFH;
import cofh.thermal.core.item.ItemCoin;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerItem;
import static net.minecraft.item.EnumRarity.UNCOMMON;

public class ItemsTF {

	private ItemsTF() {

	}

	// region REGISTRATION
	public static void registerItems() {

		registerThermalMetals();
		registerExtraMetals();

		dustBlizz = registerItem("dust_blizz", "dustBlizz", new ItemCoFH(GROUP_DUSTS));
		dustBlitz = registerItem("dust_blitz", "dustBlitz", new ItemCoFH(GROUP_DUSTS));
		dustBasalz = registerItem("dust_basalz", "dustBasalz", new ItemCoFH(GROUP_DUSTS));

		rodBlizz = registerItem("rod_blizz", "rodBlizz", new ItemCoFH(GROUP_RODS));
		rodBlitz = registerItem("rod_blitz", "rodBlitz", new ItemCoFH(GROUP_RODS));
		rodBasalz = registerItem("rod_basalz", "rodBasalz", new ItemCoFH(GROUP_RODS));
	}

	private static void registerThermalMetals() {

		ingotCopper = registerItem("ingot_copper", "ingotCopper", new ItemCoFH(GROUP_INGOTS));
		nuggetCopper = registerItem("nugget_copper", "nuggetCopper", new ItemCoFH(GROUP_NUGGETS));
		dustCopper = registerItem("dust_copper", "dustCopper", new ItemCoFH(GROUP_DUSTS));
		gearCopper = registerItem("gear_copper", "gearCopper", new ItemCoFH(GROUP_GEARS));
		plateCopper = registerItem("plate_copper", "plateCopper", new ItemCoFH(GROUP_PLATES));
		coinCopper = registerItem("coin_copper", "coinCopper", new ItemCoin());

		ingotSilver = registerItem("ingot_silver", "ingotSilver", new ItemCoFH(GROUP_INGOTS));
		nuggetSilver = registerItem("nugget_silver", "nuggetSilver", new ItemCoFH(GROUP_NUGGETS));
		dustSilver = registerItem("dust_silver", "dustSilver", new ItemCoFH(GROUP_DUSTS));
		gearSilver = registerItem("gear_silver", "gearSilver", new ItemCoFH(GROUP_GEARS));
		plateSilver = registerItem("plate_silver", "plateSilver", new ItemCoFH(GROUP_PLATES));
		coinSilver = registerItem("coin_silver", "coinSilver", new ItemCoin());

		ingotNickel = registerItem("ingot_nickel", "ingotNickel", new ItemCoFH(GROUP_INGOTS));
		nuggetNickel = registerItem("nugget_nickel", "nuggetNickel", new ItemCoFH(GROUP_NUGGETS));
		dustNickel = registerItem("dust_nickel", "dustNickel", new ItemCoFH(GROUP_DUSTS));
		gearNickel = registerItem("gear_nickel", "gearNickel", new ItemCoFH(GROUP_GEARS));
		plateNickel = registerItem("plate_nickel", "plateNickel", new ItemCoFH(GROUP_PLATES));
		coinNickel = registerItem("coin_nickel", "coinNickel", new ItemCoin());

		ingotInvar = registerItem("ingot_invar", "ingotInvar", new ItemCoFH(GROUP_INGOTS));
		nuggetInvar = registerItem("nugget_invar", "nuggetInvar", new ItemCoFH(GROUP_NUGGETS));
		dustInvar = registerItem("dust_invar", "dustInvar", new ItemCoFH(GROUP_DUSTS));
		gearInvar = registerItem("gear_invar", "gearInvar", new ItemCoFH(GROUP_GEARS));
		plateInvar = registerItem("plate_invar", "plateInvar", new ItemCoFH(GROUP_PLATES));
		coinInvar = registerItem("coin_invar", "coinInvar", new ItemCoin());

		ingotConstantan = registerItem("ingot_constantan", "ingotConstantan", new ItemCoFH(GROUP_INGOTS));
		nuggetConstantan = registerItem("nugget_constantan", "nuggetConstantan", new ItemCoFH(GROUP_NUGGETS));
		dustConstantan = registerItem("dust_constantan", "dustConstantan", new ItemCoFH(GROUP_DUSTS));
		gearConstantan = registerItem("gear_constantan", "gearConstantan", new ItemCoFH(GROUP_GEARS));
		plateConstantan = registerItem("plate_constantan", "plateConstantan", new ItemCoFH(GROUP_PLATES));
		coinConstantan = registerItem("coin_constantan", "coinConstantan", new ItemCoin());

		ingotElectrum = registerItem("ingot_electrum", "ingotElectrum", new ItemCoFH(GROUP_INGOTS));
		nuggetElectrum = registerItem("nugget_electrum", "nuggetElectrum", new ItemCoFH(GROUP_NUGGETS));
		dustElectrum = registerItem("dust_electrum", "dustElectrum", new ItemCoFH(GROUP_DUSTS));
		gearElectrum = registerItem("gear_electrum", "gearElectrum", new ItemCoFH(GROUP_GEARS));
		plateElectrum = registerItem("plate_electrum", "plateElectrum", new ItemCoFH(GROUP_PLATES));
		coinElectrum = registerItem("coin_electrum", "coinElectrum", new ItemCoin());
	}

	private static void registerExtraMetals() {

		ingotTin = registerItem("ingot_tin", "ingotTin", new ItemCoFH(GROUP_INGOTS));
		nuggetTin = registerItem("nugget_tin", "nuggetTin", new ItemCoFH(GROUP_NUGGETS));
		dustTin = registerItem("dust_tin", "dustTin", new ItemCoFH(GROUP_DUSTS));
		gearTin = registerItem("gear_tin", "gearTin", new ItemCoFH(GROUP_GEARS));
		plateTin = registerItem("plate_tin", "plateTin", new ItemCoFH(GROUP_PLATES));
		coinTin = registerItem("coin_tin", "coinTin", new ItemCoin());

		ingotAluminum = registerItem("ingot_aluminum", "ingotAluminum", new ItemCoFH(GROUP_INGOTS));
		nuggetAluminum = registerItem("nugget_aluminum", "nuggetAluminum", new ItemCoFH(GROUP_NUGGETS));
		dustAluminum = registerItem("dust_aluminum", "dustAluminum", new ItemCoFH(GROUP_DUSTS));
		gearAluminum = registerItem("gear_aluminum", "gearAluminum", new ItemCoFH(GROUP_GEARS));
		plateAluminum = registerItem("plate_aluminum", "plateAluminum", new ItemCoFH(GROUP_PLATES));
		coinAluminum = registerItem("coin_aluminum", "coinAluminum", new ItemCoin());

		ingotLead = registerItem("ingot_lead", "ingotLead", new ItemCoFH(GROUP_INGOTS));
		nuggetLead = registerItem("nugget_lead", "nuggetLead", new ItemCoFH(GROUP_NUGGETS));
		dustLead = registerItem("dust_lead", "dustLead", new ItemCoFH(GROUP_DUSTS));
		gearLead = registerItem("gear_lead", "gearLead", new ItemCoFH(GROUP_GEARS));
		plateLead = registerItem("plate_lead", "plateLead", new ItemCoFH(GROUP_PLATES));
		coinLead = registerItem("coin_lead", "coinLead", new ItemCoin());

		ingotPlatinum = registerItem("ingot_platinum", "ingotPlatinum", new ItemCoFH(GROUP_INGOTS).setRarity(UNCOMMON));
		nuggetPlatinum = registerItem("nugget_platinum", "nuggetPlatinum", new ItemCoFH(GROUP_NUGGETS).setRarity(UNCOMMON));
		dustPlatinum = registerItem("dust_platinum", "dustPlatinum", new ItemCoFH(GROUP_DUSTS).setRarity(UNCOMMON));
		gearPlatinum = registerItem("gear_platinum", "gearPlatinum", new ItemCoFH(GROUP_GEARS).setRarity(UNCOMMON));
		platePlatinum = registerItem("plate_platinum", "platePlatinum", new ItemCoFH(GROUP_PLATES).setRarity(UNCOMMON));
		coinPlatinum = registerItem("coin_platinum", "coinPlatinum", new ItemCoin().setRarity(UNCOMMON));

		ingotIridium = registerItem("ingot_iridium", "ingotIridium", new ItemCoFH(GROUP_INGOTS).setRarity(UNCOMMON));
		nuggetIridium = registerItem("nugget_iridium", "nuggetIridium", new ItemCoFH(GROUP_NUGGETS).setRarity(UNCOMMON));
		dustIridium = registerItem("dust_iridium", "dustIridium", new ItemCoFH(GROUP_DUSTS).setRarity(UNCOMMON));
		gearIridium = registerItem("gear_iridium", "gearIridium", new ItemCoFH(GROUP_GEARS).setRarity(UNCOMMON));
		plateIridium = registerItem("plate_iridium", "plateIridium", new ItemCoFH(GROUP_PLATES).setRarity(UNCOMMON));
		coinIridium = registerItem("coin_iridium", "coinIridium", new ItemCoin().setRarity(UNCOMMON));

		ingotSteel = registerItem("ingot_steel", "ingotSteel", new ItemCoFH(GROUP_INGOTS));
		nuggetSteel = registerItem("nugget_steel", "nuggetSteel", new ItemCoFH(GROUP_NUGGETS));
		dustSteel = registerItem("dust_steel", "dustSteel", new ItemCoFH(GROUP_DUSTS));
		gearSteel = registerItem("gear_steel", "gearSteel", new ItemCoFH(GROUP_GEARS));
		plateSteel = registerItem("plate_steel", "plateSteel", new ItemCoFH(GROUP_PLATES));
		coinSteel = registerItem("coin_steel", "coinSteel", new ItemCoin());

		ingotBronze = registerItem("ingot_bronze", "ingotBronze", new ItemCoFH(GROUP_INGOTS));
		nuggetBronze = registerItem("nugget_bronze", "nuggetBronze", new ItemCoFH(GROUP_NUGGETS));
		dustBronze = registerItem("dust_bronze", "dustBronze", new ItemCoFH(GROUP_DUSTS));
		gearBronze = registerItem("gear_bronze", "gearBronze", new ItemCoFH(GROUP_GEARS));
		plateBronze = registerItem("plate_bronze", "plateBronze", new ItemCoFH(GROUP_PLATES));
		coinBronze = registerItem("coin_bronze", "coinBronze", new ItemCoin());
	}
	// endregion

	// region THERMAL METALS
	public static ItemStack ingotCopper;
	public static ItemStack nuggetCopper;
	public static ItemStack dustCopper;
	public static ItemStack gearCopper;
	public static ItemStack plateCopper;
	public static ItemStack coinCopper;

	public static ItemStack ingotSilver;
	public static ItemStack nuggetSilver;
	public static ItemStack dustSilver;
	public static ItemStack gearSilver;
	public static ItemStack plateSilver;
	public static ItemStack coinSilver;

	public static ItemStack ingotNickel;
	public static ItemStack nuggetNickel;
	public static ItemStack dustNickel;
	public static ItemStack gearNickel;
	public static ItemStack plateNickel;
	public static ItemStack coinNickel;

	public static ItemStack ingotInvar;
	public static ItemStack nuggetInvar;
	public static ItemStack dustInvar;
	public static ItemStack gearInvar;
	public static ItemStack plateInvar;
	public static ItemStack coinInvar;

	public static ItemStack ingotConstantan;
	public static ItemStack nuggetConstantan;
	public static ItemStack dustConstantan;
	public static ItemStack gearConstantan;
	public static ItemStack plateConstantan;
	public static ItemStack coinConstantan;

	public static ItemStack ingotElectrum;
	public static ItemStack nuggetElectrum;
	public static ItemStack dustElectrum;
	public static ItemStack gearElectrum;
	public static ItemStack plateElectrum;
	public static ItemStack coinElectrum;
	// endregion

	// region EXTRA METALS
	public static ItemStack ingotTin;
	public static ItemStack nuggetTin;
	public static ItemStack dustTin;
	public static ItemStack gearTin;
	public static ItemStack plateTin;
	public static ItemStack coinTin;

	public static ItemStack ingotAluminum;
	public static ItemStack nuggetAluminum;
	public static ItemStack dustAluminum;
	public static ItemStack gearAluminum;
	public static ItemStack plateAluminum;
	public static ItemStack coinAluminum;

	public static ItemStack ingotLead;
	public static ItemStack nuggetLead;
	public static ItemStack dustLead;
	public static ItemStack gearLead;
	public static ItemStack plateLead;
	public static ItemStack coinLead;

	public static ItemStack ingotPlatinum;
	public static ItemStack nuggetPlatinum;
	public static ItemStack dustPlatinum;
	public static ItemStack gearPlatinum;
	public static ItemStack platePlatinum;
	public static ItemStack coinPlatinum;

	public static ItemStack ingotIridium;
	public static ItemStack nuggetIridium;
	public static ItemStack dustIridium;
	public static ItemStack gearIridium;
	public static ItemStack plateIridium;
	public static ItemStack coinIridium;

	public static ItemStack ingotSteel;
	public static ItemStack nuggetSteel;
	public static ItemStack dustSteel;
	public static ItemStack gearSteel;
	public static ItemStack plateSteel;
	public static ItemStack coinSteel;

	public static ItemStack ingotBronze;
	public static ItemStack nuggetBronze;
	public static ItemStack dustBronze;
	public static ItemStack gearBronze;
	public static ItemStack plateBronze;
	public static ItemStack coinBronze;
	// endregion

	// region MOB DROPS
	public static ItemStack dustBlizz;
	public static ItemStack dustBlitz;
	public static ItemStack dustBasalz;

	public static ItemStack rodBlizz;
	public static ItemStack rodBlitz;
	public static ItemStack rodBasalz;
	// endregion

	public static ItemStack redstoneServo;
	public static ItemStack powerCoilGold;
	public static ItemStack powerCoilSilver;
	public static ItemStack powerCoilElectrum;

	public static ItemStack partToolCasing;
	public static ItemStack partDrillHead;
	public static ItemStack partSawBlade;

	public static ItemStack dustWood;
	public static ItemStack dustWoodCompressed;
	public static ItemStack fuelCoke;

	public static ItemStack dustBiomass;
	public static ItemStack dustBiomassRich;
	public static ItemStack dustBioblend;
	public static ItemStack dustBioblendRich;

	public static ItemStack globRosin;
	public static ItemStack globTar;

	public static ItemStack crystalSlag;
	public static ItemStack crystalSlagRich;
	public static ItemStack crystalCinnabar;

	public static ItemStack crystalCrudeOil;
	public static ItemStack crystalRedstone;
	public static ItemStack crystalGlowstone;
	public static ItemStack crystalEnder;

	public static ItemStack dustPyrotheum;
	public static ItemStack dustCryotheum;
	public static ItemStack dustAerotheum;
	public static ItemStack dustPetrotheum;
	public static ItemStack dustMana;
}
