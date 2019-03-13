package cofh.thermal.foundation.init;

import cofh.core.item.ItemCoFH;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.GROUP_DUSTS;
import static cofh.lib.util.Constants.GROUP_RODS;
import static cofh.thermal.core.ThermalSeries.registerItem;

public class ItemsTF {

	private ItemsTF() {

	}

	// region REGISTRATION
	public static void registerItems() {

		dustBlizz = registerItem("dust_blizz", "dustBlizz", new ItemCoFH(GROUP_DUSTS));
		dustBlitz = registerItem("dust_blitz", "dustBlitz", new ItemCoFH(GROUP_DUSTS));
		dustBasalz = registerItem("dust_basalz", "dustBasalz", new ItemCoFH(GROUP_DUSTS));

		rodBlizz = registerItem("rod_blizz", "rodBlizz", new ItemCoFH(GROUP_RODS));
		rodBlitz = registerItem("rod_blitz", "rodBlitz", new ItemCoFH(GROUP_RODS));
		rodBasalz = registerItem("rod_basalz", "rodBasalz", new ItemCoFH(GROUP_RODS));
	}
	// endregion

	// region MOB DROPS
	public static ItemStack dustBlizz;
	public static ItemStack dustBlitz;
	public static ItemStack dustBasalz;

	public static ItemStack rodBlizz;
	public static ItemStack rodBlitz;
	public static ItemStack rodBasalz;
	// endregion

	public static ItemStack redstoneServo;
	public static ItemStack powerCoilGold;
	public static ItemStack powerCoilSilver;
	public static ItemStack powerCoilElectrum;

	public static ItemStack partToolCasing;
	public static ItemStack partDrillHead;
	public static ItemStack partSawBlade;

	public static ItemStack dustWood;
	public static ItemStack dustWoodCompressed;
	public static ItemStack fuelCoke;

	public static ItemStack dustBiomass;
	public static ItemStack dustBiomassRich;
	public static ItemStack dustBioblend;
	public static ItemStack dustBioblendRich;

	public static ItemStack globRosin;
	public static ItemStack globTar;

	public static ItemStack crystalSlag;
	public static ItemStack crystalSlagRich;
	public static ItemStack crystalCinnabar;

	public static ItemStack crystalCrudeOil;
	public static ItemStack crystalRedstone;
	public static ItemStack crystalGlowstone;
	public static ItemStack crystalEnder;

	public static ItemStack dustPyrotheum;
	public static ItemStack dustCryotheum;
	public static ItemStack dustAerotheum;
	public static ItemStack dustPetrotheum;
	public static ItemStack dustMana;
}
