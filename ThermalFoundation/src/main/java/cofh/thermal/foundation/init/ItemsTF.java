package cofh.thermal.foundation.init;

import cofh.core.item.ItemCoFH;
import cofh.thermal.core.item.ItemCoin;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerItem;

public class ItemsTF {

	private ItemsTF() {

	}

	// region REGISTRATION
	public static void registerItems() {

		ingotCopper = registerItem("ingot_copper", "ingotCopper", new ItemCoFH(GROUP_INGOTS));
		ingotTin = registerItem("ingot_tin", "ingotTin", new ItemCoFH(GROUP_INGOTS));
		ingotLead = registerItem("ingot_lead", "ingotLead", new ItemCoFH(GROUP_INGOTS));
		ingotSilver = registerItem("ingot_silver", "ingotSilver", new ItemCoFH(GROUP_INGOTS));
		ingotNickel = registerItem("ingot_nickel", "ingotNickel", new ItemCoFH(GROUP_INGOTS));
		ingotPlatinum = registerItem("ingot_platinum", "ingotPlatinum", new ItemCoFH(GROUP_INGOTS).setRarity(EnumRarity.UNCOMMON));

		//		ingotZinc = registerItem("ingot_zinc", "ingotZinc", new ItemCore(GROUP_INGOTS));
		//		ingotAluminum = registerItem("ingot_aluminum", "ingotAluminum", new ItemCore(GROUP_INGOTS));
		//		ingotTitanium = registerItem("ingot_titanium", "ingotTitanium", new ItemCore(GROUP_INGOTS));
		//		ingotOsmium = registerItem("ingot_osmium", "ingotOsmium", new ItemCore(GROUP_INGOTS));
		//		ingotIridium = registerItem("ingot_iridium", "ingotIridium", new ItemCore(GROUP_INGOTS).setRarity(EnumRarity.UNCOMMON));
		//		ingotTungsten = registerItem("ingot_tungsten", "ingotTungsten", new ItemCore(GROUP_INGOTS).setRarity(EnumRarity.UNCOMMON));

		ingotBronze = registerItem("ingot_bronze", "ingotBronze", new ItemCoFH(GROUP_INGOTS));
		ingotInvar = registerItem("ingot_invar", "ingotInvar", new ItemCoFH(GROUP_INGOTS));
		ingotElectrum = registerItem("ingot_electrum", "ingotElectrum", new ItemCoFH(GROUP_INGOTS));
		ingotConstantan = registerItem("ingot_constantan", "ingotConstantan", new ItemCoFH(GROUP_INGOTS));

		nuggetCopper = registerItem("nugget_copper", "nuggetCopper", new ItemCoFH(GROUP_NUGGETS));
		nuggetTin = registerItem("nugget_tin", "nuggetTin", new ItemCoFH(GROUP_NUGGETS));
		nuggetLead = registerItem("nugget_lead", "nuggetLead", new ItemCoFH(GROUP_NUGGETS));
		nuggetSilver = registerItem("nugget_silver", "nuggetSilver", new ItemCoFH(GROUP_NUGGETS));
		nuggetNickel = registerItem("nugget_nickel", "nuggetNickel", new ItemCoFH(GROUP_NUGGETS));
		nuggetPlatinum = registerItem("nugget_platinum", "nuggetPlatinum", new ItemCoFH(GROUP_NUGGETS).setRarity(EnumRarity.UNCOMMON));

		//		nuggetZinc = registerItem("nugget_zinc", "nuggetZinc", new ItemCore(GROUP_NUGGETS));
		//		nuggetAluminum = registerItem("nugget_aluminum", "nuggetAluminum", new ItemCore(GROUP_NUGGETS));
		//		nuggetTitanium = registerItem("nugget_titanium", "nuggetTitanium", new ItemCore(GROUP_NUGGETS));
		//		nuggetOsmium = registerItem("nugget_osmium", "nuggetOsmium", new ItemCore(GROUP_NUGGETS));
		//		nuggetIridium = registerItem("nugget_iridium", "nuggetIridium", new ItemCore(GROUP_NUGGETS).setRarity(EnumRarity.UNCOMMON));
		//		nuggetTungsten = registerItem("nugget_tungsten", "nuggetTungsten", new ItemCore(GROUP_NUGGETS).setRarity(EnumRarity.UNCOMMON));

		nuggetBronze = registerItem("nugget_bronze", "nuggetBronze", new ItemCoFH(GROUP_NUGGETS));
		nuggetInvar = registerItem("nugget_invar", "nuggetInvar", new ItemCoFH(GROUP_NUGGETS));
		nuggetElectrum = registerItem("nugget_electrum", "nuggetElectrum", new ItemCoFH(GROUP_NUGGETS));
		nuggetConstantan = registerItem("nugget_constantan", "nuggetConstantan", new ItemCoFH(GROUP_NUGGETS));

		dustCopper = registerItem("dust_copper", "dustCopper", new ItemCoFH(GROUP_DUSTS));
		dustTin = registerItem("dust_tin", "dustTin", new ItemCoFH(GROUP_DUSTS));
		dustLead = registerItem("dust_lead", "dustLead", new ItemCoFH(GROUP_DUSTS));
		dustSilver = registerItem("dust_silver", "dustSilver", new ItemCoFH(GROUP_DUSTS));
		dustNickel = registerItem("dust_nickel", "dustNickel", new ItemCoFH(GROUP_DUSTS));
		dustPlatinum = registerItem("dust_platinum", "dustPlatinum", new ItemCoFH(GROUP_DUSTS).setRarity(EnumRarity.UNCOMMON));

		//		dustZinc = registerItem("dust_zinc", "dustZinc", new ItemCore(GROUP_DUSTS));
		//		dustAluminum = registerItem("dust_aluminum", "dustAluminum", new ItemCore(GROUP_DUSTS));
		//		dustTitanium = registerItem("dust_titanium", "dustTitanium", new ItemCore(GROUP_DUSTS));
		//		dustOsmium = registerItem("dust_osmium", "dustOsmium", new ItemCore(GROUP_DUSTS));
		//		dustIridium = registerItem("dust_iridium", "dustIridium", new ItemCore(GROUP_DUSTS).setRarity(EnumRarity.UNCOMMON));
		//		dustTungsten = registerItem("dust_tungsten", "dustTungsten", new ItemCore(GROUP_DUSTS).setRarity(EnumRarity.UNCOMMON));

		dustBronze = registerItem("dust_bronze", "dustBronze", new ItemCoFH(GROUP_DUSTS));
		dustInvar = registerItem("dust_invar", "dustInvar", new ItemCoFH(GROUP_DUSTS));
		dustElectrum = registerItem("dust_electrum", "dustElectrum", new ItemCoFH(GROUP_DUSTS));
		dustConstantan = registerItem("dust_constantan", "dustConstantan", new ItemCoFH(GROUP_DUSTS));

		gearCopper = registerItem("gear_copper", "gearCopper", new ItemCoFH(GROUP_GEARS));
		gearTin = registerItem("gear_tin", "gearTin", new ItemCoFH(GROUP_GEARS));
		gearLead = registerItem("gear_lead", "gearLead", new ItemCoFH(GROUP_GEARS));
		gearSilver = registerItem("gear_silver", "gearSilver", new ItemCoFH(GROUP_GEARS));
		gearNickel = registerItem("gear_nickel", "gearNickel", new ItemCoFH(GROUP_GEARS));
		gearPlatinum = registerItem("gear_platinum", "gearPlatinum", new ItemCoFH(GROUP_GEARS).setRarity(EnumRarity.UNCOMMON));

		//		gearZinc = registerItem("gear_zinc", "gearZinc", new ItemCore(GROUP_GEARS));
		//		gearAluminum = registerItem("gear_aluminum", "gearAluminum", new ItemCore(GROUP_GEARS));
		//		gearTitanium = registerItem("gear_titanium", "gearTitanium", new ItemCore(GROUP_GEARS));
		//		gearOsmium = registerItem("gear_osmium", "gearOsmium", new ItemCore(GROUP_GEARS));
		//		gearIridium = registerItem("gear_iridium", "gearIridium", new ItemCore(GROUP_GEARS).setRarity(EnumRarity.UNCOMMON));
		//		gearTungsten = registerItem("gear_tungsten", "gearTungsten", new ItemCore(GROUP_GEARS).setRarity(EnumRarity.UNCOMMON));

		gearBronze = registerItem("gear_bronze", "gearBronze", new ItemCoFH(GROUP_GEARS));
		gearInvar = registerItem("gear_invar", "gearInvar", new ItemCoFH(GROUP_GEARS));
		gearElectrum = registerItem("gear_electrum", "gearElectrum", new ItemCoFH(GROUP_GEARS));
		gearConstantan = registerItem("gear_constantan", "gearConstantan", new ItemCoFH(GROUP_GEARS));

		plateCopper = registerItem("plate_copper", "plateCopper", new ItemCoFH(GROUP_PLATES));
		plateTin = registerItem("plate_tin", "plateTin", new ItemCoFH(GROUP_PLATES));
		plateLead = registerItem("plate_lead", "plateLead", new ItemCoFH(GROUP_PLATES));
		plateSilver = registerItem("plate_silver", "plateSilver", new ItemCoFH(GROUP_PLATES));
		plateNickel = registerItem("plate_nickel", "plateNickel", new ItemCoFH(GROUP_PLATES));
		platePlatinum = registerItem("plate_platinum", "platePlatinum", new ItemCoFH(GROUP_PLATES).setRarity(EnumRarity.UNCOMMON));

		//		plateZinc = registerItem("plate_zinc", "plateZinc", new ItemCore(GROUP_PLATES));
		//		plateAluminum = registerItem("plate_aluminum", "plateAluminum", new ItemCore(GROUP_PLATES));
		//		plateTitanium = registerItem("plate_titanium", "plateTitanium", new ItemCore(GROUP_PLATES));
		//		plateOsmium = registerItem("plate_osmium", "plateOsmium", new ItemCore(GROUP_PLATES));
		//		plateIridium = registerItem("plate_iridium", "plateIridium", new ItemCore(GROUP_PLATES).setRarity(EnumRarity.UNCOMMON));
		//		plateTungsten = registerItem("plate_tungsten", "plateTungsten", new ItemCore(GROUP_PLATES).setRarity(EnumRarity.UNCOMMON));

		plateBronze = registerItem("plate_bronze", "plateBronze", new ItemCoFH(GROUP_PLATES));
		plateInvar = registerItem("plate_invar", "plateInvar", new ItemCoFH(GROUP_PLATES));
		plateElectrum = registerItem("plate_electrum", "plateElectrum", new ItemCoFH(GROUP_PLATES));
		plateConstantan = registerItem("plate_constantan", "plateConstantan", new ItemCoFH(GROUP_PLATES));

		coinCopper = registerItem("coin_copper", "coinCopper", new ItemCoin());
		coinTin = registerItem("coin_tin", "coinTin", new ItemCoin());
		coinLead = registerItem("coin_lead", "coinLead", new ItemCoin());
		coinSilver = registerItem("coin_silver", "coinSilver", new ItemCoin());
		coinNickel = registerItem("coin_nickel", "coinNickel", new ItemCoin());
		coinPlatinum = registerItem("coin_platinum", "coinPlatinum", new ItemCoin().setRarity(EnumRarity.UNCOMMON));

		//		coinZinc = registerItem("coin_zinc", "coinZinc", new ItemCoin());
		//		coinAluminum = registerItem("coin_aluminum", "coinAluminum", new ItemCoin());
		//		coinTitanium = registerItem("coin_titanium", "coinTitanium", new ItemCoin());
		//		coinOsmium = registerItem("coin_osmium", "coinOsmium", new ItemCoin());
		//		coinIridium = registerItem("coin_iridium", "coinIridium", new ItemCoin().setRarity(EnumRarity.UNCOMMON));
		//		coinTungsten = registerItem("coin_tungsten", "coinTungsten", new ItemCoin().setRarity(EnumRarity.UNCOMMON));

		coinBronze = registerItem("coin_bronze", "coinBronze", new ItemCoin());
		coinInvar = registerItem("coin_invar", "coinInvar", new ItemCoin());
		coinElectrum = registerItem("coin_electrum", "coinElectrum", new ItemCoin());
		coinConstantan = registerItem("coin_constantan", "coinConstantan", new ItemCoin());

		dustBlizz = registerItem("dust_blizz", "dustBlizz", new ItemCoFH(GROUP_DUSTS));
		dustBlitz = registerItem("dust_blitz", "dustBlitz", new ItemCoFH(GROUP_DUSTS));
		dustBasalz = registerItem("dust_basalz", "dustBasalz", new ItemCoFH(GROUP_DUSTS));

		rodBlizz = registerItem("rod_blizz", "rodBlizz", new ItemCoFH(GROUP_RODS));
		rodBlitz = registerItem("rod_blitz", "rodBlitz", new ItemCoFH(GROUP_RODS));
		rodBasalz = registerItem("rod_basalz", "rodBasalz", new ItemCoFH(GROUP_RODS));
	}
	// endregion

	// region INGOTS
	public static ItemStack ingotCopper;
	public static ItemStack ingotTin;
	public static ItemStack ingotLead;
	public static ItemStack ingotSilver;
	public static ItemStack ingotNickel;
	public static ItemStack ingotPlatinum;

	public static ItemStack ingotZinc;
	public static ItemStack ingotAluminum;
	public static ItemStack ingotTitanium;
	public static ItemStack ingotOsmium;
	public static ItemStack ingotIridium;
	public static ItemStack ingotTungsten;

	public static ItemStack ingotBronze;
	public static ItemStack ingotInvar;
	public static ItemStack ingotElectrum;
	public static ItemStack ingotConstantan;
	// endregion

	// region GEMS
	public static ItemStack gemAmethyst;
	public static ItemStack gemRuby;
	public static ItemStack gemSapphire;
	// endregion

	// region NUGGETS
	public static ItemStack nuggetCopper;
	public static ItemStack nuggetTin;
	public static ItemStack nuggetLead;
	public static ItemStack nuggetSilver;
	public static ItemStack nuggetNickel;
	public static ItemStack nuggetPlatinum;

	public static ItemStack nuggetZinc;
	public static ItemStack nuggetAluminum;
	public static ItemStack nuggetTitanium;
	public static ItemStack nuggetOsmium;
	public static ItemStack nuggetIridium;
	public static ItemStack nuggetTungsten;

	public static ItemStack nuggetBronze;
	public static ItemStack nuggetInvar;
	public static ItemStack nuggetElectrum;
	public static ItemStack nuggetConstantan;

	public static ItemStack nuggetAmethyst;
	public static ItemStack nuggetRuby;
	public static ItemStack nuggetSapphire;
	// endregion

	// region DUSTS
	public static ItemStack dustCopper;
	public static ItemStack dustTin;
	public static ItemStack dustLead;
	public static ItemStack dustSilver;
	public static ItemStack dustNickel;
	public static ItemStack dustPlatinum;

	public static ItemStack dustZinc;
	public static ItemStack dustAluminum;
	public static ItemStack dustTitanium;
	public static ItemStack dustOsmium;
	public static ItemStack dustIridium;
	public static ItemStack dustTungsten;

	public static ItemStack dustBronze;
	public static ItemStack dustInvar;
	public static ItemStack dustElectrum;
	public static ItemStack dustConstantan;

	public static ItemStack dustAmethyst;
	public static ItemStack dustRuby;
	public static ItemStack dustSapphire;
	// endregion

	// region GEARS
	public static ItemStack gearCopper;
	public static ItemStack gearTin;
	public static ItemStack gearLead;
	public static ItemStack gearSilver;
	public static ItemStack gearNickel;
	public static ItemStack gearPlatinum;

	public static ItemStack gearZinc;
	public static ItemStack gearAluminum;
	public static ItemStack gearTitanium;
	public static ItemStack gearOsmium;
	public static ItemStack gearIridium;
	public static ItemStack gearTungsten;

	public static ItemStack gearBronze;
	public static ItemStack gearInvar;
	public static ItemStack gearElectrum;
	public static ItemStack gearConstantan;

	public static ItemStack gearAmethyst;
	public static ItemStack gearRuby;
	public static ItemStack gearSapphire;
	// endregion

	// region PLATES
	public static ItemStack plateCopper;
	public static ItemStack plateTin;
	public static ItemStack plateLead;
	public static ItemStack plateSilver;
	public static ItemStack plateNickel;
	public static ItemStack platePlatinum;

	public static ItemStack plateZinc;
	public static ItemStack plateAluminum;
	public static ItemStack plateTitanium;
	public static ItemStack plateOsmium;
	public static ItemStack plateIridium;
	public static ItemStack plateTungsten;

	public static ItemStack plateBronze;
	public static ItemStack plateInvar;
	public static ItemStack plateElectrum;
	public static ItemStack plateConstantan;

	public static ItemStack plateAmethyst;
	public static ItemStack plateRuby;
	public static ItemStack plateSapphire;
	// endregion

	// region COINS
	public static ItemStack coinCopper;
	public static ItemStack coinTin;
	public static ItemStack coinLead;
	public static ItemStack coinSilver;
	public static ItemStack coinNickel;
	public static ItemStack coinPlatinum;

	public static ItemStack coinZinc;
	public static ItemStack coinAluminum;
	public static ItemStack coinTitanium;
	public static ItemStack coinOsmium;
	public static ItemStack coinIridium;
	public static ItemStack coinTungsten;

	public static ItemStack coinBronze;
	public static ItemStack coinInvar;
	public static ItemStack coinElectrum;
	public static ItemStack coinConstantan;
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
