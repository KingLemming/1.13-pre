package cofh.thermal.cultivation.init;

import cofh.core.block.BlockCrop;
import net.minecraft.block.Block;

import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.cultivation.init.ItemsTC.cropOnion;
import static cofh.thermal.cultivation.init.ItemsTC.seedOnion;

public class BlocksTC {

	private BlocksTC() {

	}

	public static void initialize() {

		((BlockCrop) plantOnion).setCrop(cropOnion).setSeed(seedOnion);
	}

	// region REGISTRATION
	public static void registerBlocks() {

		plantOnion = registerBlock("plant_onion", new BlockCrop());
	}
	// endregion

	// region CROPS
	public static Block plantOnion;

	public static Block plantBellPepper;
	public static Block plantGreenBean;
	public static Block plantSpinach;
	public static Block plantStrawberry;
	public static Block plantTomato;
	// endregion
}
