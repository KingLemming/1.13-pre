package cofh.thermal.core.init;

import cofh.core.item.ItemCoFH;
import cofh.core.item.ItemFuel;
import cofh.thermal.core.item.ItemCoin;
import cofh.thermal.core.item.ItemFertilizer;
import cofh.thermal.core.item.ItemRedprint;
import cofh.thermal.core.item.ItemWrench;
import cofh.thermal.core.item.tome.ItemTomeExperience;
import cofh.thermal.core.item.tome.ItemTomeLexicon;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerItem;
import static cofh.thermal.core.init.ConfigTSeries.*;
import static net.minecraft.item.EnumRarity.RARE;
import static net.minecraft.item.EnumRarity.UNCOMMON;

public class ItemsTSeries {

	private ItemsTSeries() {

	}

	// region REGISTRATION
	public static void registerItems() {

		registerTools();
		registerResources();
		registerVanillaComponents();
		registerThermalMetals();
		// registerExtraMetals();
	}

	private static void registerTools() {

		utilWrench = registerItem("util_wrench", new ItemWrench(), CreativeTabsTSeries.tabTools);
		utilRedprint = registerItem("util_redprint", new ItemRedprint(), CreativeTabsTSeries.tabTools);
		utilTomeLexicon = registerItem("tome_lexicon", new ItemTomeLexicon(), CreativeTabsTSeries.tabTools);
		utilTomeExperience = registerItem("tome_experience", new ItemTomeExperience(), CreativeTabsTSeries.tabTools);
	}

	private static void registerResources() {

		dustCoal = registerItem("dust_coal", "dustCoal", new ItemFuel(fuelCoal, GROUP_DUSTS));
		dustCharcoal = registerItem("dust_charcoal", "dustCharcoal", new ItemFuel(fuelCoal, GROUP_DUSTS));
		dustObsidian = registerItem("dust_obsidian", "dustObsidian", new ItemCoFH(GROUP_DUSTS));

		dustWood = registerItem("dust_wood", "dustWood", new ItemCoFH(GROUP_RESOURCES));
		ingotWood = registerItem("ingot_wood", "ingotWood", new ItemCoFH(GROUP_RESOURCES));
		fuelCoke = registerItem("fuel_coke", "fuelCoke", new ItemFuel(fuelCoalCoke, GROUP_RESOURCES));

		dustSulfur = registerItem("dust_sulfur", "dustSulfur", new ItemCoFH(GROUP_RESOURCES));
		dustNiter = registerItem("dust_niter", "dustNiter", new ItemCoFH(GROUP_RESOURCES));
		mineralCinnabar = registerItem("mineral_cinnabar", "itemCinnabar", new ItemCoFH(GROUP_RESOURCES));

		mineralSlag = registerItem("mineral_slag", "itemSlag", new ItemCoFH(GROUP_RESOURCES));
		mineralSlagRich = registerItem("mineral_slag_rich", "itemSlagRich", new ItemCoFH(GROUP_RESOURCES));
		//
		//		dustBiomass = registerItem("biomass", new ItemCoFH(GROUP_RESOURCES));
		//		dustBiomassRich = registerItem("biomass_rich", new ItemCoFH(GROUP_RESOURCES));
		//		dustBioblend = registerItem("bioblend", new ItemCoFH(GROUP_RESOURCES));
		//		dustBioblendRich = registerItem("bioblend_rich", new ItemCoFH(GROUP_RESOURCES));

		globRosin = registerItem("glob_rosin", new ItemCoFH(GROUP_RESOURCES));
		globTar = registerItem("glob_tar", new ItemCoFH(GROUP_RESOURCES));

		itemPhytoGro = registerItem("phytogro", new ItemFertilizer(1));
		itemPhytoGroRich = registerItem("phytogro_rich", new ItemFertilizer(2));
		itemPhytoGroFlux = registerItem("phytogro_flux", new ItemFertilizer(3));
	}

	private static void registerVanillaComponents() {

		gearWood = registerItem("gear_wood", "gearWood", new ItemCoFH(GROUP_GEARS).showInCreativeTab(enableBasicGears));
		gearStone = registerItem("gear_stone", "gearStone", new ItemCoFH(GROUP_GEARS).showInCreativeTab(enableBasicGears));

		ingotIron = new ItemStack(Items.IRON_INGOT);
		nuggetIron = new ItemStack(Items.IRON_NUGGET);
		dustIron = registerItem("dust_iron", "dustIron", new ItemCoFH(GROUP_DUSTS));
		gearIron = registerItem("gear_iron", "gearIron", new ItemCoFH(GROUP_GEARS));
		plateIron = registerItem("plate_iron", "plateIron", new ItemCoFH(GROUP_PLATES));
		coinIron = registerItem("coin_iron", "coinIron", new ItemCoin());

		ingotGold = new ItemStack(Items.GOLD_INGOT);
		nuggetGold = new ItemStack(Items.GOLD_NUGGET);
		dustGold = registerItem("dust_gold", "dustGold", new ItemCoFH(GROUP_DUSTS));
		gearGold = registerItem("gear_gold", "gearGold", new ItemCoFH(GROUP_GEARS));
		plateGold = registerItem("plate_gold", "plateGold", new ItemCoFH(GROUP_PLATES));
		coinGold = registerItem("coin_gold", "coinGold", new ItemCoin());

		gemDiamond = new ItemStack(Items.DIAMOND);
		nuggetDiamond = registerItem("nugget_diamond", "nuggetDiamond", new ItemCoFH(GROUP_NUGGETS));
		dustDiamond = registerItem("dust_diamond", "dustDiamond", new ItemCoFH(GROUP_DUSTS));
		gearDiamond = registerItem("gear_diamond", "gearDiamond", new ItemCoFH(GROUP_GEARS));
		plateDiamond = registerItem("plate_diamond", "plateDiamond", new ItemCoFH(GROUP_PLATES));

		gemEmerald = new ItemStack(Items.EMERALD);
		nuggetEmerald = registerItem("nugget_emerald", "nuggetEmerald", new ItemCoFH(GROUP_NUGGETS));
		dustEmerald = registerItem("dust_emerald", "dustEmerald", new ItemCoFH(GROUP_DUSTS));
		gearEmerald = registerItem("gear_emerald", "gearEmerald", new ItemCoFH(GROUP_GEARS));
		plateEmerald = registerItem("plate_emerald", "plateEmerald", new ItemCoFH(GROUP_PLATES));
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

		ingotSignalum = registerItem("ingot_signalum", "ingotSignalum", new ItemCoFH(GROUP_INGOTS).setRarity(UNCOMMON));
		nuggetSignalum = registerItem("nugget_signalum", "nuggetSignalum", new ItemCoFH(GROUP_NUGGETS).setRarity(UNCOMMON));
		dustSignalum = registerItem("dust_signalum", "dustSignalum", new ItemCoFH(GROUP_DUSTS).setRarity(UNCOMMON));
		gearSignalum = registerItem("gear_signalum", "gearSignalum", new ItemCoFH(GROUP_GEARS).setRarity(UNCOMMON));
		plateSignalum = registerItem("plate_signalum", "plateSignalum", new ItemCoFH(GROUP_PLATES).setRarity(UNCOMMON));
		coinSignalum = registerItem("coin_signalum", "coinSignalum", new ItemCoin().setRarity(UNCOMMON));

		ingotLumium = registerItem("ingot_lumium", "ingotLumium", new ItemCoFH(GROUP_INGOTS).setRarity(UNCOMMON));
		nuggetLumium = registerItem("nugget_lumium", "nuggetLumium", new ItemCoFH(GROUP_NUGGETS).setRarity(UNCOMMON));
		dustLumium = registerItem("dust_lumium", "dustLumium", new ItemCoFH(GROUP_DUSTS).setRarity(UNCOMMON));
		gearLumium = registerItem("gear_lumium", "gearLumium", new ItemCoFH(GROUP_GEARS).setRarity(UNCOMMON));
		plateLumium = registerItem("plate_lumium", "plateLumium", new ItemCoFH(GROUP_PLATES).setRarity(UNCOMMON));
		coinLumium = registerItem("coin_lumium", "coinLumium", new ItemCoin().setRarity(UNCOMMON));

		ingotEnderium = registerItem("ingot_enderium", "ingotEnderium", new ItemCoFH(GROUP_INGOTS).setRarity(RARE));
		nuggetEnderium = registerItem("nugget_enderium", "nuggetEnderium", new ItemCoFH(GROUP_NUGGETS).setRarity(RARE));
		dustEnderium = registerItem("dust_enderium", "dustEnderium", new ItemCoFH(GROUP_DUSTS).setRarity(RARE));
		gearEnderium = registerItem("gear_enderium", "gearEnderium", new ItemCoFH(GROUP_GEARS).setRarity(RARE));
		plateEnderium = registerItem("plate_enderium", "plateEnderium", new ItemCoFH(GROUP_PLATES).setRarity(RARE));
		coinEnderium = registerItem("coin_enderium", "coinEnderium", new ItemCoin().setRarity(RARE));
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

	// region TOOLS
	public static ItemStack utilWrench;
	public static ItemStack utilRedprint;
	public static ItemStack utilTomeLexicon;
	public static ItemStack utilTomeExperience;
	// endregion

	// region RESOURCES
	public static ItemStack itemCoal = new ItemStack(Items.COAL);
	public static ItemStack itemCharcoal = new ItemStack(Items.COAL, 1, 1);

	public static ItemStack dustCoal;
	public static ItemStack dustCharcoal;
	public static ItemStack dustObsidian;

	public static ItemStack dustWood;
	public static ItemStack ingotWood;

	public static ItemStack fuelCoke;
	public static ItemStack dustSulfur;
	public static ItemStack dustNiter;
	public static ItemStack mineralCinnabar;

	public static ItemStack mineralSlag;
	public static ItemStack mineralSlagRich;

	public static ItemStack dustBiomass;
	public static ItemStack dustBiomassRich;
	public static ItemStack dustBioblend;
	public static ItemStack dustBioblendRich;

	public static ItemStack globRosin;
	public static ItemStack globTar;

	public static ItemStack itemPhytoGro;
	public static ItemStack itemPhytoGroRich;
	public static ItemStack itemPhytoGroFlux;
	// endregion

	// region VANILLA
	public static ItemStack gearWood;
	public static ItemStack gearStone;

	public static ItemStack ingotIron;
	public static ItemStack nuggetIron;
	public static ItemStack dustIron;
	public static ItemStack gearIron;
	public static ItemStack plateIron;
	public static ItemStack coinIron;

	public static ItemStack ingotGold;
	public static ItemStack nuggetGold;
	public static ItemStack dustGold;
	public static ItemStack gearGold;
	public static ItemStack plateGold;
	public static ItemStack coinGold;

	public static ItemStack gemDiamond;
	public static ItemStack nuggetDiamond;
	public static ItemStack dustDiamond;
	public static ItemStack gearDiamond;
	public static ItemStack plateDiamond;

	public static ItemStack gemEmerald;
	public static ItemStack nuggetEmerald;
	public static ItemStack dustEmerald;
	public static ItemStack gearEmerald;
	public static ItemStack plateEmerald;
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

	public static ItemStack ingotSignalum;
	public static ItemStack nuggetSignalum;
	public static ItemStack dustSignalum;
	public static ItemStack gearSignalum;
	public static ItemStack plateSignalum;
	public static ItemStack coinSignalum;

	public static ItemStack ingotLumium;
	public static ItemStack nuggetLumium;
	public static ItemStack dustLumium;
	public static ItemStack gearLumium;
	public static ItemStack plateLumium;
	public static ItemStack coinLumium;

	public static ItemStack ingotEnderium;
	public static ItemStack nuggetEnderium;
	public static ItemStack dustEnderium;
	public static ItemStack gearEnderium;
	public static ItemStack plateEnderium;
	public static ItemStack coinEnderium;
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
}
