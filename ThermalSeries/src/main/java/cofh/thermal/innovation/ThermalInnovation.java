package cofh.thermal.innovation;

import cofh.lib.util.IModule;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.innovation.gui.GuiHandler;
import cofh.thermal.innovation.init.ItemsTI;
import cofh.thermal.innovation.proxy.ProxyCommon;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static cofh.lib.util.Constants.*;

@Mod (modid = ThermalInnovation.MOD_ID, name = ThermalInnovation.MOD_NAME, version = ThermalSeries.VERSION, dependencies = ThermalInnovation.DEPENDENCIES)
public class ThermalInnovation implements IModule {

	public static final String MOD_ID = ID_THERMAL_INNOVATION;
	public static final String MOD_NAME = "Thermal Innovation";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE + ";required-before:" + ID_THERMAL_SERIES + ";after:" + ID_THERMAL_EXPANSION;

	@Instance (MOD_ID)
	public static ThermalInnovation instance;

	@SidedProxy (clientSide = "cofh.thermal.innovation.proxy.ProxyClient", serverSide = "cofh.thermal.innovation.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		ThermalSeries.addModule(instance);

		registerHandlers();

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
	public void registerItems(RegistryEvent.Register<Item> event) {

		ItemsTI.registerItems();
	}

	@Override
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

	}
	// endregion

	// region HELPERS
	private static void registerHandlers() {

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, GuiHandler.INSTANCE);
	}
	// endregion
}
