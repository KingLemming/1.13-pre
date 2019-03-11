package cofh.orbulation;

import cofh.core.item.ItemCoFH;
import cofh.orbulation.entity.projectile.EntityFlorb;
import cofh.orbulation.entity.projectile.EntityMorb;
import cofh.orbulation.item.ItemFlorb;
import cofh.orbulation.item.ItemMorb;
import cofh.orbulation.proxy.ProxyCommon;
import cofh.orbulation.util.BehaviorFlorbDispense;
import cofh.orbulation.util.BehaviorMorbDispense;
import cofh.orbulation.util.FlorbUtils;
import cofh.orbulation.util.MorbUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static cofh.lib.util.Constants.ID_COFH_CORE;
import static cofh.lib.util.Constants.ID_ORBULATION;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.RecipeHelper.addShapelessRecipe;

// TODO: DEACTIVATED
// @Mod (modid = Orbulation.MOD_ID, name = Orbulation.MOD_NAME, version = Orbulation.VERSION, dependencies = Orbulation.DEPENDENCIES)
public class Orbulation {

	public static final String MOD_ID = ID_ORBULATION;
	public static final String MOD_NAME = "Orbulation";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:" + ID_COFH_CORE;

	@Instance (MOD_ID)
	public static Orbulation instance;

	@SidedProxy (clientSide = "cofh.orbulation.proxy.ProxyClient", serverSide = "cofh.orbulation.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static Configuration config;

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		File configDir = event.getModConfigurationDirectory();

		config = new Configuration(new File(configDir, "/cofh/" + MOD_ID + "/config.cfg"), VERSION, true);
		config.load();

		config();
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

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {

		config.save();
	}
	// endregion

	// region REGISTRATION
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {

		itemFlorb = new ItemFlorb(false);
		itemFlorbMagmatic = new ItemFlorb(true);

		itemMorb = new ItemMorb(false);
		itemMorbReusable = new ItemMorb(true);

		registerItem("florb", itemFlorb, CreativeTabs.MISC);
		registerItem("florb_magmatic", itemFlorbMagmatic, CreativeTabs.MISC);

		registerItem("morb", itemMorb, CreativeTabs.MISC);
		registerItem("morb_reusable", itemMorbReusable, CreativeTabs.MISC);

		FlorbUtils.config();
		MorbUtils.config();

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(itemFlorb, new BehaviorFlorbDispense());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(itemFlorbMagmatic, new BehaviorFlorbDispense());

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(itemMorb, new BehaviorMorbDispense(false));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(itemMorbReusable, new BehaviorMorbDispense(true));

		proxy.registerItemModels();
	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {

		EntityFlorb.initialize(0);
		EntityMorb.initialize(1);
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		if (disableAllRecipes) {
			return;
		}
		addShapelessRecipe(cloneStack(itemFlorb, 4), "blockGlass", "slimeball");
		addShapelessRecipe(cloneStack(itemFlorbMagmatic, 4), "blockGlass", "slimeball", Items.BLAZE_POWDER);
		addShapelessRecipe(cloneStack(itemFlorbMagmatic, 4), "blockGlass", Items.MAGMA_CREAM);

		addShapelessRecipe(cloneStack(itemMorb, 4), Blocks.SOUL_SAND, "slimeball");
		addShapelessRecipe(cloneStack(itemMorbReusable), itemMorb, "gemLapis", "enderpearl");
	}
	// endregion

	// region HELPERS
	public static boolean disableAllRecipes = false;

	public static void config() {

		String category = "~MODPACK OPTIONS~";
		String comment = "If TRUE, then ALL RECIPES from this mod will be DISABLED. Everything will still be registered - this is a convenience option for pack makers who are completely overhauling recipes.";
		disableAllRecipes = config.getBoolean("Disable ALL Recipes", category, false, comment);
	}

	private static void registerHandlers() {

		MinecraftForge.EVENT_BUS.register(instance);
	}

	public static ItemStack registerItem(String itemName, ItemCoFH item, CreativeTabs tab) {

		item.setCreativeTab(tab);
		item.setRegistryName(MOD_ID, itemName);
		item.setUnlocalizedName(MOD_ID + "." + itemName);
		ForgeRegistries.ITEMS.register(item);

		return new ItemStack(item);
	}
	// endregion

	// region REFERENCES
	public static ItemFlorb itemFlorb;
	public static ItemFlorb itemFlorbMagmatic;

	public static ItemMorb itemMorb;
	public static ItemMorb itemMorbReusable;
	// endregion
}
