package cofh.thermal.cultivation;

import cofh.lib.util.IModule;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.cultivation.init.BlocksTC;
import cofh.thermal.cultivation.init.ItemsTC;
import cofh.thermal.cultivation.init.RecipesTC;
import cofh.thermal.cultivation.proxy.ProxyCommon;
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
import net.minecraftforge.fml.common.registry.EntityEntry;

import static cofh.lib.util.Constants.*;

@Mod (modid = ThermalCultivation.MOD_ID, name = ThermalCultivation.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalCultivation.DEPENDENCIES)
public class ThermalCultivation implements IModule {

	public static final String MOD_ID = ID_THERMAL_CULTIVATION;
	public static final String MOD_NAME = "Thermal Cultivation";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";required-before:" + ID_THERMAL_SERIES;

	@Instance (MOD_ID)
	public static ThermalCultivation instance;

	@SidedProxy (clientSide = "cofh.thermal.cultivation.proxy.ProxyClient", serverSide = "cofh.thermal.cultivation.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		ThermalSeries.addModule(instance);

		proxy.preInit(event);
	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {

		BlocksTC.initialize();

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

		BlocksTC.registerBlocks();
	}

	@Override
	public void registerItems(RegistryEvent.Register<Item> event) {

		ItemsTC.registerItems();
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

		RecipesTC.registerRecipes();
	}

	@Override
	public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {

	}
	// endregion
}
