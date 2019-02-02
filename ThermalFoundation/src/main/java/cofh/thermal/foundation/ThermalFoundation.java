package cofh.thermal.foundation;

import cofh.lib.util.IModule;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.foundation.init.BlocksTF;
import cofh.thermal.foundation.init.EntitiesTF;
import cofh.thermal.foundation.init.ItemsTF;
import cofh.thermal.foundation.init.RecipesTF;
import cofh.thermal.foundation.proxy.ProxyCommon;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

import static cofh.lib.util.Constants.*;

@Mod (modid = ThermalFoundation.MOD_ID, name = ThermalFoundation.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalFoundation.DEPENDENCIES)
public class ThermalFoundation implements IModule {

	public static final String MOD_ID = ID_THERMAL_FOUNDATION;
	public static final String MOD_NAME = "Thermal Foundation";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";required-before:" + ID_THERMAL_SERIES;

	@Instance (MOD_ID)
	public static ThermalFoundation instance;

	@SidedProxy (clientSide = "cofh.thermal.foundation.proxy.ProxyClient", serverSide = "cofh.thermal.foundation.proxy.ProxyCommon")
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

		BlocksTF.registerBlocks();
	}

	@Override
	public void registerItems(RegistryEvent.Register<Item> event) {

		ItemsTF.registerItems();
	}

	@Override
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {

		EntitiesTF.registerEntities();
	}

	// TODO: Placeholder for 1.13.
	@Override
	public void registerFluids() {

	}

	@Override
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		RecipesTF.registerRecipes();
	}
	// endregion
}
