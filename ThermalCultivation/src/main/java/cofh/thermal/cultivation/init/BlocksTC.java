package cofh.thermal.cultivation.init;

import cofh.core.block.BlockCrop;
import cofh.core.block.BlockCropPerennial;
import net.minecraft.block.Block;

import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.cultivation.init.ItemsTC.*;

public class BlocksTC {

	private BlocksTC() {

	}

	public static void initialize() {

		((BlockCrop) plantOnion).setCrop(cropOnion).setSeed(seedOnion);
		((BlockCrop) plantSadiroot).setCrop(cropSadiroot).setSeed(seedSadiroot);
		((BlockCrop) plantSpinach).setCrop(cropSpinach).setSeed(seedSpinach);

		((BlockCrop) plantBellPepper).setCrop(cropBellPepper).setSeed(seedBellPepper);
		((BlockCrop) plantGreenBean).setCrop(cropGreenBean).setSeed(seedGreenBean);
		((BlockCrop) plantPeanut).setCrop(cropPeanut).setSeed(seedPeanut);
		((BlockCrop) plantStrawberry).setCrop(cropStrawberry).setSeed(seedStrawberry);
		((BlockCrop) plantTomato).setCrop(cropTomato).setSeed(seedTomato);
	}

	// region REGISTRATION
	public static void registerBlocks() {

		plantOnion = registerBlock("plant_onion", new BlockCrop());
		plantSadiroot = registerBlock("plant_sadiroot", new BlockCrop());
		plantSpinach = registerBlock("plant_spinach", new BlockCrop());

		plantBellPepper = registerBlock("plant_bell_pepper", new BlockCropPerennial());
		plantGreenBean = registerBlock("plant_green_bean", new BlockCropPerennial());
		plantPeanut = registerBlock("plant_peanut", new BlockCropPerennial());
		plantStrawberry = registerBlock("plant_strawberry", new BlockCropPerennial());
		plantTomato = registerBlock("plant_tomato", new BlockCropPerennial());
	}
	// endregion

	// region CROPS
	public static Block plantOnion;
	public static Block plantSadiroot;
	public static Block plantSpinach;

	public static Block plantBellPepper;
	public static Block plantGreenBean;
	public static Block plantPeanut;
	public static Block plantStrawberry;
	public static Block plantTomato;
	// endregion
}
