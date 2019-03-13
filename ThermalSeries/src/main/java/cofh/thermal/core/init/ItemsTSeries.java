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
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerItem;
import static cofh.thermal.core.init.ConfigTSeries.*;

public class ItemsTSeries {

	private ItemsTSeries() {

	}

	// region REGISTRATION
	public static void registerItems() {

		utilWrench = registerItem("util_wrench", new ItemWrench(), CreativeTabsTSeries.tabTools);
		utilRedprint = registerItem("util_redprint", new ItemRedprint(), CreativeTabsTSeries.tabTools);
		utilTomeLexicon = registerItem("tome_lexicon", new ItemTomeLexicon(), CreativeTabsTSeries.tabTools);
		utilTomeExperience = registerItem("tome_experience", new ItemTomeExperience(), CreativeTabsTSeries.tabTools);

		dustCoal = registerItem("dust_coal", "dustCoal", new ItemFuel(fuelCoal, GROUP_DUSTS));
		dustCharcoal = registerItem("dust_charcoal", "dustCharcoal", new ItemFuel(fuelCoal, GROUP_DUSTS));
		dustObsidian = registerItem("dust_obsidian", "dustObsidian", new ItemCoFH(GROUP_DUSTS));

		coalCoke = registerItem("coal_coke", "fuelCoke", new ItemFuel(fuelCoalCoke, GROUP_RESOURCES));
		dustSulfur = registerItem("dust_sulfur", "dustSulfur", new ItemCoFH(GROUP_RESOURCES));
		dustNiter = registerItem("dust_niter", "dustNiter", new ItemCoFH(GROUP_RESOURCES));
		dustWood = registerItem("dust_wood", "dustWood", new ItemCoFH(GROUP_RESOURCES));
		//		dustWoodCompressed = registerItem("dust_wood_compressed", "dustWoodCompressed", new ItemCore());

		itemPhytoGro = registerItem("phytogro", new ItemFertilizer(1));
		itemPhytoGroRich = registerItem("phytogro_rich", new ItemFertilizer(2));
		itemPhytoGroFlux = registerItem("phytogro_flux", new ItemFertilizer(3));
	}

	private static void registerVanillaComponents() {

		ingotIron = new ItemStack(Items.IRON_INGOT);
		ingotGold = new ItemStack(Items.GOLD_INGOT);
		gemDiamond = new ItemStack(Items.DIAMOND);
		gemEmerald = new ItemStack(Items.EMERALD);

		nuggetIron = new ItemStack(Items.IRON_NUGGET);
		nuggetGold = new ItemStack(Items.GOLD_NUGGET);
		nuggetDiamond = registerItem("nugget_diamond", "nuggetDiamond", new ItemCoFH(GROUP_NUGGETS));
		nuggetEmerald = registerItem("nugget_emerald", "nuggetEmerald", new ItemCoFH(GROUP_NUGGETS));

		dustIron = registerItem("dust_iron", "dustIron", new ItemCoFH(GROUP_DUSTS));
		dustGold = registerItem("dust_gold", "dustGold", new ItemCoFH(GROUP_DUSTS));
		dustDiamond = registerItem("dust_diamond", "dustDiamond", new ItemCoFH(GROUP_DUSTS));
		dustEmerald = registerItem("dust_emerald", "dustEmerald", new ItemCoFH(GROUP_DUSTS));

		gearWood = registerItem("gear_wood", "gearWood", new ItemCoFH(GROUP_GEARS).showInCreativeTab(enableBasicGears));
		gearStone = registerItem("gear_stone", "gearStone", new ItemCoFH(GROUP_GEARS).showInCreativeTab(enableBasicGears));

		gearIron = registerItem("gear_iron", "gearIron", new ItemCoFH(GROUP_GEARS));
		gearGold = registerItem("gear_gold", "gearGold", new ItemCoFH(GROUP_GEARS));
		gearDiamond = registerItem("gear_diamond", "gearDiamond", new ItemCoFH(GROUP_GEARS));
		gearEmerald = registerItem("gear_emerald", "gearEmerald", new ItemCoFH(GROUP_GEARS));

		plateIron = registerItem("plate_iron", "plateIron", new ItemCoFH(GROUP_PLATES));
		plateGold = registerItem("plate_gold", "plateGold", new ItemCoFH(GROUP_PLATES));
		plateDiamond = registerItem("plate_diamond", "plateDiamond", new ItemCoFH(GROUP_PLATES));
		plateEmerald = registerItem("plate_emerald", "plateEmerald", new ItemCoFH(GROUP_PLATES));

		coinIron = registerItem("coin_iron", "coinIron", new ItemCoin());
		coinGold = registerItem("coin_gold", "coinGold", new ItemCoin());
	}

	private static void registerMetalComponents() {

		ingotCopper = registerItem("ingot_copper", "ingotCopper", new ItemCoFH(GROUP_INGOTS));
		ingotSilver = registerItem("ingot_silver", "ingotSilver", new ItemCoFH(GROUP_INGOTS));
		ingotNickel = registerItem("ingot_nickel", "ingotNickel", new ItemCoFH(GROUP_INGOTS));

		ingotInvar = registerItem("ingot_invar", "ingotInvar", new ItemCoFH(GROUP_INGOTS));
		ingotElectrum = registerItem("ingot_electrum", "ingotElectrum", new ItemCoFH(GROUP_INGOTS));
		ingotConstantan = registerItem("ingot_constantan", "ingotConstantan", new ItemCoFH(GROUP_INGOTS));

		ingotSignalum = registerItem("ingot_signalum", "ingotSignalum", new ItemCoFH(GROUP_INGOTS).setRarity(EnumRarity.UNCOMMON));
		ingotLumium = registerItem("ingot_lumium", "ingotLumium", new ItemCoFH(GROUP_INGOTS).setRarity(EnumRarity.UNCOMMON));
		ingotEnderium = registerItem("ingot_enderium", "ingotEnderium", new ItemCoFH(GROUP_INGOTS).setRarity(EnumRarity.RARE));

		nuggetCopper = registerItem("nugget_copper", "nuggetCopper", new ItemCoFH(GROUP_NUGGETS));
		nuggetSilver = registerItem("nugget_silver", "nuggetSilver", new ItemCoFH(GROUP_NUGGETS));
		nuggetNickel = registerItem("nugget_nickel", "nuggetNickel", new ItemCoFH(GROUP_NUGGETS));

		nuggetInvar = registerItem("nugget_invar", "nuggetInvar", new ItemCoFH(GROUP_NUGGETS));
		nuggetElectrum = registerItem("nugget_electrum", "nuggetElectrum", new ItemCoFH(GROUP_NUGGETS));
		nuggetConstantan = registerItem("nugget_constantan", "nuggetConstantan", new ItemCoFH(GROUP_NUGGETS));

		nuggetSignalum = registerItem("nugget_signalum", "nuggetSignalum", new ItemCoFH(GROUP_NUGGETS).setRarity(EnumRarity.UNCOMMON));
		nuggetLumium = registerItem("nugget_lumium", "nuggetLumium", new ItemCoFH(GROUP_NUGGETS).setRarity(EnumRarity.UNCOMMON));
		nuggetEnderium = registerItem("nugget_enderium", "nuggetEnderium", new ItemCoFH(GROUP_NUGGETS).setRarity(EnumRarity.RARE));

		dustCopper = registerItem("dust_copper", "dustCopper", new ItemCoFH(GROUP_DUSTS));
		dustSilver = registerItem("dust_silver", "dustSilver", new ItemCoFH(GROUP_DUSTS));
		dustNickel = registerItem("dust_nickel", "dustNickel", new ItemCoFH(GROUP_DUSTS));

		dustInvar = registerItem("dust_invar", "dustInvar", new ItemCoFH(GROUP_DUSTS));
		dustElectrum = registerItem("dust_electrum", "dustElectrum", new ItemCoFH(GROUP_DUSTS));
		dustConstantan = registerItem("dust_constantan", "dustConstantan", new ItemCoFH(GROUP_DUSTS));

		dustSignalum = registerItem("dust_signalum", "dustSignalum", new ItemCoFH(GROUP_DUSTS).setRarity(EnumRarity.UNCOMMON));
		dustLumium = registerItem("dust_lumium", "dustLumium", new ItemCoFH(GROUP_DUSTS).setRarity(EnumRarity.UNCOMMON));
		dustEnderium = registerItem("dust_enderium", "dustEnderium", new ItemCoFH(GROUP_DUSTS).setRarity(EnumRarity.UNCOMMON));

		gearCopper = registerItem("gear_copper", "gearCopper", new ItemCoFH(GROUP_GEARS));
		gearSilver = registerItem("gear_silver", "gearSilver", new ItemCoFH(GROUP_GEARS));
		gearNickel = registerItem("gear_nickel", "gearNickel", new ItemCoFH(GROUP_GEARS));

		gearInvar = registerItem("gear_invar", "gearInvar", new ItemCoFH(GROUP_GEARS));
		gearElectrum = registerItem("gear_electrum", "gearElectrum", new ItemCoFH(GROUP_GEARS));
		gearConstantan = registerItem("gear_constantan", "gearConstantan", new ItemCoFH(GROUP_GEARS));

		gearSignalum = registerItem("gear_signalum", "gearSignalum", new ItemCoFH(GROUP_GEARS).setRarity(EnumRarity.UNCOMMON));
		gearLumium = registerItem("gear_lumium", "gearLumium", new ItemCoFH(GROUP_GEARS).setRarity(EnumRarity.UNCOMMON));
		gearEnderium = registerItem("gear_enderium", "gearEnderium", new ItemCoFH(GROUP_GEARS).setRarity(EnumRarity.RARE));

		plateCopper = registerItem("plate_copper", "plateCopper", new ItemCoFH(GROUP_PLATES));
		plateSilver = registerItem("plate_silver", "plateSilver", new ItemCoFH(GROUP_PLATES));
		plateNickel = registerItem("plate_nickel", "plateNickel", new ItemCoFH(GROUP_PLATES));

		plateInvar = registerItem("plate_invar", "plateInvar", new ItemCoFH(GROUP_PLATES));
		plateElectrum = registerItem("plate_electrum", "plateElectrum", new ItemCoFH(GROUP_PLATES));
		plateConstantan = registerItem("plate_constantan", "plateConstantan", new ItemCoFH(GROUP_PLATES));

		plateSignalum = registerItem("plate_signalum", "plateSignalum", new ItemCoFH(GROUP_PLATES).setRarity(EnumRarity.UNCOMMON));
		plateLumium = registerItem("plate_lumium", "plateLumium", new ItemCoFH(GROUP_PLATES).setRarity(EnumRarity.UNCOMMON));
		plateEnderium = registerItem("plate_enderium", "plateEnderium", new ItemCoFH(GROUP_PLATES).setRarity(EnumRarity.RARE));

		coinCopper = registerItem("coin_copper", "coinCopper", new ItemCoin());
		coinSilver = registerItem("coin_silver", "coinSilver", new ItemCoin());
		coinNickel = registerItem("coin_nickel", "coinNickel", new ItemCoin());

		coinInvar = registerItem("coin_invar", "coinInvar", new ItemCoin());
		coinElectrum = registerItem("coin_electrum", "coinElectrum", new ItemCoin());
		coinConstantan = registerItem("coin_constantan", "coinConstantan", new ItemCoin());

		coinSignalum = registerItem("coin_signalum", "coinSignalum", new ItemCoin().setRarity(EnumRarity.UNCOMMON));
		coinLumium = registerItem("coin_lumium", "coinLumium", new ItemCoin().setRarity(EnumRarity.UNCOMMON));
		coinEnderium = registerItem("coin_enderium", "coinEnderium", new ItemCoin().setRarity(EnumRarity.RARE));
	}

	private static void registerExtraMetalComponents() {

		ingotSteel = registerItem("ingot_steel", "ingotSteel", new ItemCoFH(GROUP_INGOTS));
		nuggetSteel = registerItem("nugget_steel", "nuggetSteel", new ItemCoFH(GROUP_NUGGETS));
		dustSteel = registerItem("dust_steel", "dustSteel", new ItemCoFH(GROUP_DUSTS));
		gearSteel = registerItem("gear_steel", "gearSteel", new ItemCoFH(GROUP_GEARS));
		plateSteel = registerItem("plate_steel", "plateSteel", new ItemCoFH(GROUP_PLATES));
		coinSteel = registerItem("coin_steel", "coinSteel", new ItemCoin());
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

	public static ItemStack coalCoke;
	public static ItemStack dustSulfur;
	public static ItemStack dustNiter;

	public static ItemStack dustWood;
	public static ItemStack dustWoodCompressed;

	public static ItemStack itemPhytoGro;
	public static ItemStack itemPhytoGroRich;
	public static ItemStack itemPhytoGroFlux;
	// endregion

	// region INGOTS
	public static ItemStack ingotIron;
	public static ItemStack ingotGold;

	public static ItemStack ingotCopper;
	public static ItemStack ingotSilver;
	public static ItemStack ingotNickel;

	public static ItemStack ingotInvar;
	public static ItemStack ingotElectrum;
	public static ItemStack ingotConstantan;

	public static ItemStack ingotSteel;
	public static ItemStack ingotSignalum;
	public static ItemStack ingotLumium;
	public static ItemStack ingotEnderium;
	// endregion

	// region GEMS
	public static ItemStack gemDiamond;
	public static ItemStack gemEmerald;
	// endregion

	// region NUGGETS
	public static ItemStack nuggetIron;
	public static ItemStack nuggetGold;
	public static ItemStack nuggetDiamond;
	public static ItemStack nuggetEmerald;

	public static ItemStack nuggetCopper;
	public static ItemStack nuggetSilver;
	public static ItemStack nuggetNickel;

	public static ItemStack nuggetInvar;
	public static ItemStack nuggetElectrum;
	public static ItemStack nuggetConstantan;

	public static ItemStack nuggetSteel;
	public static ItemStack nuggetSignalum;
	public static ItemStack nuggetLumium;
	public static ItemStack nuggetEnderium;
	// endregion

	// region DUSTS
	public static ItemStack dustIron;
	public static ItemStack dustGold;
	public static ItemStack dustDiamond;
	public static ItemStack dustEmerald;

	public static ItemStack dustCopper;
	public static ItemStack dustSilver;
	public static ItemStack dustNickel;

	public static ItemStack dustInvar;
	public static ItemStack dustElectrum;
	public static ItemStack dustConstantan;

	public static ItemStack dustSteel;
	public static ItemStack dustSignalum;
	public static ItemStack dustLumium;
	public static ItemStack dustEnderium;

	// endregion

	// region GEARS
	public static ItemStack gearWood;
	public static ItemStack gearStone;

	public static ItemStack gearIron;
	public static ItemStack gearGold;
	public static ItemStack gearDiamond;
	public static ItemStack gearEmerald;

	public static ItemStack gearCopper;
	public static ItemStack gearSilver;
	public static ItemStack gearNickel;

	public static ItemStack gearInvar;
	public static ItemStack gearElectrum;
	public static ItemStack gearConstantan;

	public static ItemStack gearSteel;
	public static ItemStack gearSignalum;
	public static ItemStack gearLumium;
	public static ItemStack gearEnderium;
	// endregion

	// region PLATES
	public static ItemStack plateIron;
	public static ItemStack plateGold;
	public static ItemStack plateDiamond;
	public static ItemStack plateEmerald;

	public static ItemStack plateCopper;
	public static ItemStack plateSilver;
	public static ItemStack plateNickel;

	public static ItemStack plateInvar;
	public static ItemStack plateElectrum;
	public static ItemStack plateConstantan;

	public static ItemStack plateSteel;
	public static ItemStack plateSignalum;
	public static ItemStack plateLumium;
	public static ItemStack plateEnderium;
	// endregion

	// region COINS
	public static ItemStack coinIron;
	public static ItemStack coinGold;

	public static ItemStack coinCopper;
	public static ItemStack coinSilver;
	public static ItemStack coinNickel;

	public static ItemStack coinInvar;
	public static ItemStack coinElectrum;
	public static ItemStack coinConstantan;

	public static ItemStack coinSteel;
	public static ItemStack coinSignalum;
	public static ItemStack coinLumium;
	public static ItemStack coinEnderium;
	// endregion
}
