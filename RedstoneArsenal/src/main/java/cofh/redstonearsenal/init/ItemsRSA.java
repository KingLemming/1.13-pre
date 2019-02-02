package cofh.redstonearsenal.init;

import cofh.core.item.ItemCoFH;
import cofh.redstonearsenal.item.ItemQuiverFlux;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.redstonearsenal.RedstoneArsenal.registerItem;

public class ItemsRSA {

	private ItemsRSA() {

	}

	// region REGISTRATION
	public static void registerItems() {

		ingotFluxInfused = registerItem("ingot_flux_infused", "ingotFluxInfused", new ItemCoFH(GROUP_INGOTS).setRarity(EnumRarity.UNCOMMON));
		nuggetFluxInfused = registerItem("nugget_flux_infused", "nuggetFluxInfused", new ItemCoFH(GROUP_NUGGETS).setRarity(EnumRarity.UNCOMMON));
		dustFluxInfused = registerItem("dust_flux_infused", "dustFluxInfused", new ItemCoFH(GROUP_DUSTS).setRarity(EnumRarity.UNCOMMON));
		gearFluxInfused = registerItem("gear_flux_infused", "gearFluxInfused", new ItemCoFH(GROUP_GEARS).setRarity(EnumRarity.UNCOMMON));
		plateFluxInfused = registerItem("plate_flux_infused", "plateFluxInfused", new ItemCoFH(GROUP_PLATES).setRarity(EnumRarity.UNCOMMON));
		gemFluxCrystal = registerItem("gem_flux_crystal", "gemFluxCrystal", new ItemCoFH(GROUP_GEMS).setRarity(EnumRarity.UNCOMMON));

		rodObsidian = registerItem("rod_obsidian", new ItemCoFH(GROUP_RODS).setRarity(EnumRarity.UNCOMMON));
		rodObsidianFlux = registerItem("rod_obsidian_flux", new ItemCoFH(GROUP_RODS).setRarity(EnumRarity.UNCOMMON));
		plateArmorFlux = registerItem("plate_armor_flux", new ItemCoFH().setRarity(EnumRarity.UNCOMMON));

		quiverFlux = registerItem("quiver_flux_infused", new ItemQuiverFlux());
	}
	// endregion

	// region REFERENCES
	public static ItemStack ingotFluxInfused;
	public static ItemStack nuggetFluxInfused;
	public static ItemStack dustFluxInfused;
	public static ItemStack gearFluxInfused;
	public static ItemStack plateFluxInfused;
	public static ItemStack gemFluxCrystal;

	public static ItemStack rodObsidian;
	public static ItemStack rodObsidianFlux;
	public static ItemStack plateArmorFlux;

	public static ItemStack quiverFlux;
	// endregion
}
