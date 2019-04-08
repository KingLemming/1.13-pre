package cofh.thermal.dynamics;

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

@Mod (modid = ThermalDynamics.MOD_ID, name = ThermalDynamics.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalDynamics.DEPENDENCIES)
public class ThermalDynamics implements IModule {

	public static final String MOD_ID = ID_THERMAL_DYNAMICS;
	public static final String MOD_NAME = "Thermal Dynamics";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";required-before:" + ID_THERMAL_SERIES + ";after:" + ID_THERMAL_EXPANSION;

	@Instance (MOD_ID)
	public static ThermalDynamics instance;

	//	@SidedProxy (clientSide = "cofh.thermal.dynamics.proxy.ProxyClient", serverSide = "cofh.thermal.dynamics.proxy.ProxyCommon")
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

	@Override
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

	}

	@Override
	public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {

	}
	// endregion
}
