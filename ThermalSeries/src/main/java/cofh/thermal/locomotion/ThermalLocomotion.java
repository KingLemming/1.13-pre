package cofh.thermal.locomotion;

import cofh.lib.util.IModule;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.locomotion.init.BlocksTL;
import cofh.thermal.locomotion.init.RecipesTL;
import cofh.thermal.locomotion.proxy.ProxyCommon;
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

@Mod (modid = ThermalLocomotion.MOD_ID, name = ThermalLocomotion.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalLocomotion.DEPENDENCIES)
public class ThermalLocomotion implements IModule {

	public static final String MOD_ID = ID_THERMAL_LOCOMOTION;
	public static final String MOD_NAME = "Thermal Locomotion";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";required-before:" + ID_THERMAL_SERIES + ";after:" + ID_THERMAL_EXPANSION;

	@Instance (MOD_ID)
	public static ThermalLocomotion instance;

	@SidedProxy (clientSide = "cofh.thermal.locomotion.proxy.ProxyClient", serverSide = "cofh.thermal.locomotion.proxy.ProxyCommon")
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

		BlocksTL.registerBlocks();
	}

	@Override
	public void registerItems(RegistryEvent.Register<Item> event) {

	}

	@Override
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {

	}

	@Override
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		RecipesTL.registerRecipes();
	}
	// endregion
}
