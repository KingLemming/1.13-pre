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
}
