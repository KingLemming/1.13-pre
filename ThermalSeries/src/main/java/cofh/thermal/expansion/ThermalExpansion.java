package cofh.thermal.expansion;

import cofh.lib.util.IModule;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.init.ParsersTSeries;
import cofh.thermal.core.init.RecipesTSeries;
import cofh.thermal.expansion.init.BlocksTE;
import cofh.thermal.expansion.init.ItemsTE;
import cofh.thermal.expansion.proxy.ProxyCommon;
import cofh.thermal.expansion.util.managers.FactorizerManager;
import cofh.thermal.expansion.util.managers.TapperManager;
import cofh.thermal.expansion.util.managers.dynamo.*;
import cofh.thermal.expansion.util.managers.machine.*;
import cofh.thermal.expansion.util.parsers.dynamo.*;
import cofh.thermal.expansion.util.parsers.machine.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static cofh.lib.util.Constants.*;

@Mod (modid = ThermalExpansion.MOD_ID, name = ThermalExpansion.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalExpansion.DEPENDENCIES)
public class ThermalExpansion implements IModule {

	public static final String MOD_ID = ID_THERMAL_EXPANSION;
	public static final String MOD_NAME = "Thermal Expansion";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";required-before:" + ID_THERMAL_SERIES + ";after:" + ID_THERMAL_FOUNDATION;

	@Instance (MOD_ID)
	public static ThermalExpansion instance;

	@SidedProxy (clientSide = "cofh.thermal.expansion.proxy.ProxyClient", serverSide = "cofh.thermal.expansion.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		ThermalSeries.addModule(instance);

		proxy.preInit(event);
	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {

		proxy.initialize(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);
	}
	// endregion

	// region REGISTRATION
	@Override
	public void registerBlocks(RegistryEvent.Register<Block> event) {

		BlocksTE.registerBlocks();
	}

	@Override
	public void registerItems(RegistryEvent.Register<Item> event) {

		ItemsTE.registerItems();
	}

	@Override
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		RecipesTSeries.registerManager(FurnaceRecipeManager.instance());
		RecipesTSeries.registerManager(PulverizerRecipeManager.instance());
		RecipesTSeries.registerManager(SawmillRecipeManager.instance());
		RecipesTSeries.registerManager(InsolatorRecipeManager.instance());
		RecipesTSeries.registerManager(CentrifugeRecipeManager.instance());
		RecipesTSeries.registerManager(CrucibleRecipeManager.instance());
		RecipesTSeries.registerManager(RefineryRecipeManager.instance());
		RecipesTSeries.registerManager(BrewerRecipeManager.instance());

		RecipesTSeries.registerManager(FactorizerManager.instance());

		RecipesTSeries.registerManager(TapperManager.instance());

		RecipesTSeries.registerManager(StirlingFuelManager.instance());
		RecipesTSeries.registerManager(MagmaticFuelManager.instance());
		RecipesTSeries.registerManager(CompressionFuelManager.instance());
		RecipesTSeries.registerManager(NumismaticFuelManager.instance());
		RecipesTSeries.registerManager(LapidaryFuelManager.instance());

		ParsersTSeries.registerParser("furnace", FurnaceRecipeParser.instance());
		ParsersTSeries.registerParser("pulverizer", PulverizerRecipeParser.instance());
		ParsersTSeries.registerParser("sawmill", SawmillRecipeParser.instance());
		ParsersTSeries.registerParser("insolator", InsolatorRecipeParser.instance());
		ParsersTSeries.registerParser("centrifuge", CentrifugeRecipeParser.instance());
		ParsersTSeries.registerParser("crucible", CrucibleRecipeParser.instance());
		ParsersTSeries.registerParser("refinery", RefineryRecipeParser.instance());
		ParsersTSeries.registerParser("brewer", BrewerRecipeParser.instance());

		ParsersTSeries.registerParser("catalyst_pulverizer", PulverizerCatalystParser.instance());
		ParsersTSeries.registerParser("catalyst_insolator", InsolatorCatalystParser.instance());

		ParsersTSeries.registerParser("fuel_stirling", StirlingFuelParser.instance());
		ParsersTSeries.registerParser("fuel_magmatic", MagmaticFuelParser.instance());
		ParsersTSeries.registerParser("fuel_compression", CompressionFuelParser.instance());
		ParsersTSeries.registerParser("fuel_numismatic", NumismaticFuelParser.instance());
		ParsersTSeries.registerParser("fuel_lapidary", LapidaryFuelParser.instance());
	}

	@Override
	public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {

	}
	// endregion
}
