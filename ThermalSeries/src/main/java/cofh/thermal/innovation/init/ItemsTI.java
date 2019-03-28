package cofh.thermal.innovation.init;

import cofh.thermal.core.init.CreativeTabsTSeries;
import cofh.thermal.core.item.ItemRFContainer;
import cofh.thermal.innovation.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.thermal.core.ThermalSeries.config;
import static cofh.thermal.core.ThermalSeries.registerItem;

public class ItemsTI {

	private ItemsTI() {

	}

	// region REGISTRATION
	public static void registerItems() {

		CreativeTabs tab = CreativeTabsTSeries.tabTools;

		itemDrillBasic = new ItemRFDrill(20000, 1000, 2, 6.0F);

		itemSawBasic = new ItemRFSaw(20000, 1000, 2, 6.0F);

		itemCapacitorBasic = createCapacitor("basic", 40000, 1000);
		itemCapacitorCreative = (ItemRFContainer) createCapacitor("creative", 1000, 1000000000).setRarity(EnumRarity.EPIC).setCreative(true);

		itemMagnetBasic = createMagnet("basic", 20000, 4, 3);
		itemMagnetCreative = (ItemRFMagnet) createMagnet("creative", 1000, 8, 15).setRarity(EnumRarity.EPIC).setCreative(true);

		itemInjectorBasic = createInjector("basic", 2000);
		itemInjectorCreative = (ItemPotionInjector) createInjector("creative", 2000).setRarity(EnumRarity.EPIC).setCreative(true);

		itemQuiverBasic = createQuiver("basic", 2000, 40);
		itemQuiverCreative = (ItemPotionQuiver) createQuiver("creative", 2000, 64).setRarity(EnumRarity.EPIC).setCreative(true);

		registerItem("rf_drill_basic", itemDrillBasic, tab);

		registerItem("rf_saw_basic", itemSawBasic, tab);

		registerItem("rf_capacitor_basic", itemCapacitorBasic, tab);
		registerItem("rf_capacitor_creative", itemCapacitorCreative, tab);

		registerItem("rf_magnet_basic", itemMagnetBasic, tab);
		registerItem("rf_magnet_creative", itemMagnetCreative, tab);

		registerItem("potion_injector_basic", itemInjectorBasic, tab);
		registerItem("potion_injector_creative", itemInjectorCreative, tab);

		registerItem("potion_quiver_basic", itemQuiverBasic, tab);
		registerItem("potion_quiver_creative", itemQuiverCreative, tab);
	}
	// endregion

	// region HELPERS
	private static ItemRFCapacitor createCapacitor(String id, int maxEnergy, int maxSend) {

		String category = "Tools.Magnet." + titleCase(id);
		String comment = "Adjust this value to set how much energy (RF) this Capacitor holds.";
		maxEnergy = config.getInt("Energy", category, maxEnergy, 1000, 1000000000, comment);

		int maxReceive = maxEnergy / 20;

		comment = "Adjust this value to set how much Energy (RF/t) that can be sent by this Capacitor.";
		maxSend = config.getInt("Send", category, maxSend, 1000, 1000000000, comment);

		return new ItemRFCapacitor(maxEnergy, maxReceive, maxSend);
	}

	private static ItemRFMagnet createMagnet(String id, int maxEnergy, int radius, int filterSize) {

		String category = "Tools.Magnet." + titleCase(id);
		String comment = "Adjust this value to set how much energy (RF) this Magnet holds.";
		maxEnergy = config.getInt("Energy", category, maxEnergy, 1000, 1000000000, comment);

		int maxReceive = maxEnergy / 20;

		comment = "Adjust this value to set the maximum radius for this Magnet.";
		radius = config.getInt("Radius", category, radius, 2, 16, comment);

		return new ItemRFMagnet(maxEnergy, maxReceive, radius, filterSize);
	}

	private static ItemPotionInjector createInjector(String id, int fluidCapacity) {

		String category = "Tools.PotionInjector." + titleCase(id);
		String comment = "Adjust this value to set how much Potion (mB) this Injector holds.";
		fluidCapacity = config.getInt("Potion", category, fluidCapacity, Fluid.BUCKET_VOLUME * 2, Fluid.BUCKET_VOLUME * 1000, comment);

		return new ItemPotionInjector(fluidCapacity);
	}

	private static ItemPotionQuiver createQuiver(String id, int fluidCapacity, int arrowCapacity) {

		String category = "Tools.PotionQuiver." + titleCase(id);
		String comment = "Adjust this value to set how much Potion (mB) this Quiver holds.";
		fluidCapacity = config.getInt("Potion", category, fluidCapacity, Fluid.BUCKET_VOLUME * 2, Fluid.BUCKET_VOLUME * 1000, comment);

		comment = "Adjust this value to set how many arrows this quiver holds.";
		arrowCapacity = config.getInt("Arrows", category, arrowCapacity, 16, 32000, comment);

		return new ItemPotionQuiver(fluidCapacity, arrowCapacity);
	}
	// endregion

	// region REFERENCES
	public static ItemRFDrill itemDrillBasic;
	public static ItemRFDrill itemDrillCreative;

	public static ItemRFSaw itemSawBasic;
	public static ItemRFSaw itemSawCreative;

	public static ItemRFCapacitor itemCapacitorBasic;
	public static ItemRFContainer itemCapacitorCreative;

	public static ItemRFMagnet itemMagnetBasic;
	public static ItemRFMagnet itemMagnetCreative;

	public static ItemPotionInjector itemInjectorBasic;
	public static ItemPotionInjector itemInjectorCreative;

	public static ItemPotionQuiver itemQuiverBasic;
	public static ItemPotionQuiver itemQuiverCreative;
	// endregion
}
