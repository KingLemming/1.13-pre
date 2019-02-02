package cofh.thermal.atomics;

import cofh.lib.util.IModule;
import cofh.thermal.core.ThermalSeries;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

import static cofh.lib.util.Constants.*;

@Mod (modid = ThermalAtomics.MOD_ID, name = ThermalAtomics.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalAtomics.DEPENDENCIES)
public class ThermalAtomics implements IModule {

	public static final String MOD_ID = ID_THERMAL_ATOMICS;
	public static final String MOD_NAME = "Thermal Atomics";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";required-before:" + ID_THERMAL_SERIES;

	@Instance (MOD_ID)
	public static ThermalAtomics instance;

	//	@SidedProxy (clientSide = "cofh.thermal.atomics.proxy.ProxyClient", serverSide = "cofh.thermal.atomics.proxy.ProxyCommon")
	//	public static ProxyCommon proxy;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		ThermalSeries.addModule(instance);

		// proxy.preInit(event);
	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {

		// proxy.initialize(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		// proxy.postInit(event);
	}
	// endregion

	// region REGISTRATION
	@Override
	public void registerBlocks(RegistryEvent.Register<Block> event) {

	}

	@Override
	public void registerItems(RegistryEvent.Register<Item> event) {

	}

	@Override
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {

	}

	// TODO: Placeholder for 1.13.
	@Override
	public void registerFluids() {

	}

	@Override
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

	}

	@Override
	public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {

	}
	// endregion
}
