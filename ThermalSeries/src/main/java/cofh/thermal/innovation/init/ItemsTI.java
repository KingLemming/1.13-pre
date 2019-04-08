package cofh.thermal.innovation.init;

import cofh.thermal.core.init.CreativeTabsTSeries;
import cofh.thermal.core.item.ItemRFContainer;
import cofh.thermal.innovation.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.thermal.core.ThermalSeries.config;
import static cofh.thermal.core.ThermalSeries.registerItem;
import static net.minecraft.item.EnumRarity.EPIC;
import static net.minecraft.item.EnumRarity.RARE;

public class ItemsTI {

	private ItemsTI() {

	}

	// region REGISTRATION
	public static void registerItems() {

		CreativeTabs tab = CreativeTabsTSeries.tabTools;

		itemDrillBasic = createDrill("basic", 20000, 2, 6.0F, 3.0F, 2);
		itemDrillResonant = (ItemRFDrill) createDrill("resonant", 200000, 4, 12.0F, 5.0F, 5).setRarity(RARE);
		itemDrillCreative = (ItemRFDrill) createDrill("creative", 1000, 8, 32.0F, 5.0F, 5).setRarity(EPIC).setCreative(true);

		itemSawBasic = createSaw("basic", 20000, 2, 6.0F, 4.0F, 2);
		itemSawResonant = (ItemRFSaw) createSaw("resonant", 200000, 4, 12.0F, 6.0F, 5).setRarity(RARE);
		itemSawCreative = (ItemRFSaw) createSaw("creative", 1000, 8, 32.0F, 6.0F, 5).setRarity(EPIC).setCreative(true);

		itemCapacitorBasic = createCapacitor("basic", 40000, 4000);
		itemCapacitorResonant = (ItemRFCapacitor) createCapacitor("resonant", 400000, 40000).setRarity(RARE);
		itemCapacitorCreative = (ItemRFContainer) createCapacitor("creative", 1000, 1000000000).setRarity(EPIC).setCreative(true);

		itemMagnetBasic = createMagnet("basic", 20000, 4, 3);
		itemMagnetResonant = (ItemRFMagnet) createMagnet("resonant", 200000, 8, 15).setRarity(RARE);
		itemMagnetCreative = (ItemRFMagnet) createMagnet("creative", 1000, 8, 15).setRarity(EPIC).setCreative(true);

		itemInjectorBasic = createInjector("basic", 2000);
		itemInjectorResonant = (ItemPotionInjector) createInjector("resonant", 20000).setRarity(RARE);
		itemInjectorCreative = (ItemPotionInjector) createInjector("creative", 2000).setRarity(EPIC).setCreative(true);

		itemQuiverBasic = createQuiver("basic", 2000, 40);
		itemQuiverResonant = (ItemPotionQuiver) createQuiver("resonant", 20000, 400).setRarity(RARE);
		itemQuiverCreative = (ItemPotionQuiver) createQuiver("creative", 2000, 64).setRarity(EPIC).setCreative(true);

		registerItem("rf_drill_basic", itemDrillBasic, tab);
		registerItem("rf_drill_resonant", itemDrillResonant, tab);
		registerItem("rf_drill_creative", itemDrillCreative, tab);

		registerItem("rf_saw_basic", itemSawBasic, tab);
		registerItem("rf_saw_resonant", itemSawResonant, tab);
		registerItem("rf_saw_creative", itemSawCreative, tab);

		registerItem("rf_capacitor_basic", itemCapacitorBasic, tab);
		registerItem("rf_capacitor_resonant", itemCapacitorResonant, tab);
		registerItem("rf_capacitor_creative", itemCapacitorCreative, tab);

		registerItem("rf_magnet_basic", itemMagnetBasic, tab);
		registerItem("rf_magnet_resonant", itemMagnetResonant, tab);
		registerItem("rf_magnet_creative", itemMagnetCreative, tab);

		registerItem("potion_injector_basic", itemInjectorBasic, tab);
		registerItem("potion_injector_resonant", itemInjectorResonant, tab);
		registerItem("potion_injector_creative", itemInjectorCreative, tab);

		registerItem("potion_quiver_basic", itemQuiverBasic, tab);
		registerItem("potion_quiver_resonant", itemQuiverResonant, tab);
		registerItem("potion_quiver_creative", itemQuiverCreative, tab);
	}
	// endregion

	// region HELPERS
	private static ItemRFDrill createDrill(String id, int maxEnergy, int harvestLevel, float efficiency, float damage, int numModes) {

		String category = "Tools.Drill." + titleCase(id);
		String comment = "Adjust this value to set how much energy (RF) this Drill holds.";
		maxEnergy = config.getInt("Energy", category, maxEnergy, 1000, 1000000000, comment);

		int maxReceive = maxEnergy / 20;

		comment = "Adjust this value to set the harvest level for this Drill.";
		harvestLevel = config.getInt("Harvest Level", category, harvestLevel, 0, 100, comment);

		comment = "Adjust this value to set the base harvest speed for this Drill.";
		efficiency = config.getFloat("Efficiency", category, efficiency, 2.0F, 100.0F, comment);

		comment = "Adjust this value to set the base attack damage for this Drill.";
		damage = config.getFloat("Attack Damage", category, damage, 1.0F, 10.0F, comment);

		return new ItemRFDrill(maxEnergy, maxReceive, harvestLevel, efficiency, damage, numModes);
	}

	private static ItemRFSaw createSaw(String id, int maxEnergy, int harvestLevel, float efficiency, float damage, int numModes) {

		String category = "Tools.Saw." + titleCase(id);
		String comment = "Adjust this value to set how much energy (RF) this Saw holds.";
		maxEnergy = config.getInt("Energy", category, maxEnergy, 1000, 1000000000, comment);

		int maxReceive = maxEnergy / 20;

		comment = "Adjust this value to set the harvest level for this Saw.";
		harvestLevel = config.getInt("Harvest Level", category, harvestLevel, 0, 100, comment);

		comment = "Adjust this value to set the base harvest speed for this Saw.";
		efficiency = config.getFloat("Efficiency", category, efficiency, 2.0F, 100.0F, comment);

		comment = "Adjust this value to set the base attack damage for this Saw.";
		damage = config.getFloat("Attack Damage", category, damage, 1.0F, 10.0F, comment);

		return new ItemRFSaw(maxEnergy, maxReceive, harvestLevel, efficiency, damage, numModes);
	}

	private static ItemRFCapacitor createCapacitor(String id, int maxEnergy, int maxSend) {

		String category = "Tools.Capacitor." + titleCase(id);
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

		comment = "Adjust this value to set how many arrows this Quiver holds.";
		arrowCapacity = config.getInt("Arrows", category, arrowCapacity, 16, 32000, comment);

		return new ItemPotionQuiver(fluidCapacity, arrowCapacity);
	}
	// endregion

	// region REFERENCES
	public static ItemRFDrill itemDrillBasic;
	public static ItemRFDrill itemDrillResonant;
	public static ItemRFDrill itemDrillCreative;

	public static ItemRFSaw itemSawBasic;
	public static ItemRFSaw itemSawResonant;
	public static ItemRFSaw itemSawCreative;

	public static ItemRFCapacitor itemCapacitorBasic;
	public static ItemRFCapacitor itemCapacitorResonant;
	public static ItemRFContainer itemCapacitorCreative;

	public static ItemRFMagnet itemMagnetBasic;
	public static ItemRFMagnet itemMagnetResonant;
	public static ItemRFMagnet itemMagnetCreative;

	public static ItemPotionInjector itemInjectorBasic;
	public static ItemPotionInjector itemInjectorResonant;
	public static ItemPotionInjector itemInjectorCreative;

	public static ItemPotionQuiver itemQuiverBasic;
	public static ItemPotionQuiver itemQuiverResonant;
	public static ItemPotionQuiver itemQuiverCreative;
	// endregion
}
