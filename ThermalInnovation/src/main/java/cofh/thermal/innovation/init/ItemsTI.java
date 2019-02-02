package cofh.thermal.innovation.init;

import cofh.thermal.core.init.CreativeTabsTSeries;
import cofh.thermal.innovation.item.*;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.ThermalSeries.registerItem;

public class ItemsTI {

	private ItemsTI() {

	}

	// region REGISTRATION
	public static void registerItems() {

		itemDrillBasic = new ItemRFDrill(20000, 1000, 2, 6.0F);
		itemSawBasic = new ItemRFSaw(20000, 1000, 2, 6.0F);
		itemCapacitorBasic = new ItemRFCapacitor(40000, 2000, 1000);
		itemMagnetBasic = new ItemRFMagnet(20000, 1000, 4, 3);
		itemInjectorBasic = new ItemPotionInjector(2000);
		itemQuiverBasic = new ItemPotionQuiver(2000, 40);

		toolDrillBasic = registerItem("rf_drill_basic", itemDrillBasic, CreativeTabsTSeries.tabTools);
		toolSawBasic = registerItem("rf_saw_basic", itemSawBasic, CreativeTabsTSeries.tabTools);
		utilCapacitorBasic = registerItem("rf_capacitor_basic", itemCapacitorBasic, CreativeTabsTSeries.tabTools);
		utilMagnetBasic = registerItem("rf_magnet_basic", itemMagnetBasic, CreativeTabsTSeries.tabTools);
		utilInjectorBasic = registerItem("potion_injector_basic", itemInjectorBasic, CreativeTabsTSeries.tabTools);
		utilQuiverBasic = registerItem("potion_quiver_basic", itemQuiverBasic, CreativeTabsTSeries.tabTools);
	}
	// endregion

	// region REFERENCES
	public static ItemRFDrill itemDrillBasic;
	public static ItemRFSaw itemSawBasic;
	public static ItemRFCapacitor itemCapacitorBasic;
	public static ItemRFMagnet itemMagnetBasic;
	public static ItemPotionInjector itemInjectorBasic;
	public static ItemPotionQuiver itemQuiverBasic;

	public static ItemStack toolDrillBasic;
	public static ItemStack toolSawBasic;
	public static ItemStack utilCapacitorBasic;
	public static ItemStack utilMagnetBasic;
	public static ItemStack utilInjectorBasic;
	public static ItemStack utilQuiverBasic;
	// endregion
}
