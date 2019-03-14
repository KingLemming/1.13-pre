package cofh.thermal.cultivation.init;

import cofh.core.block.BlockCrop;
import cofh.core.block.BlockCropPerennial;
import cofh.core.block.BlockCropTall;
import cofh.core.block.BlockCropTallPerennial;
import net.minecraft.block.Block;

import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.cultivation.init.ItemsTC.*;

public class BlocksTC {

	private BlocksTC() {

	}

	public static void initialize() {

		((BlockCrop) plantBarley).setCrop(cropBarley).setSeed(seedBarley);
		((BlockCrop) plantOnion).setCrop(cropOnion).setSeed(seedOnion);
		((BlockCrop) plantSadiroot).setCrop(cropSadiroot).setSeed(seedSadiroot);
		((BlockCrop) plantSpinach).setCrop(cropSpinach).setSeed(seedSpinach);
		((BlockCrop) plantRice).setCrop(cropRice).setSeed(seedRice);

		((BlockCrop) plantCorn).setCrop(cropCorn); //.setSeed(seedCorn);

		((BlockCrop) plantBellPepper).setCrop(cropBellPepper); //.setSeed(seedBellPepper);
		((BlockCrop) plantGreenBean).setCrop(cropGreenBean); //.setSeed(seedGreenBean);
		((BlockCrop) plantPeanut).setCrop(cropPeanut); //.setSeed(seedPeanut);
		((BlockCrop) plantStrawberry).setCrop(cropStrawberry); //.setSeed(seedStrawberry);
		((BlockCrop) plantTomato).setCrop(cropTomato); //.setSeed(seedTomato);

		((BlockCrop) plantCoffee).setCrop(cropCoffee); //.setSeed(seedCoffee);
		((BlockCrop) plantTea).setCrop(cropTea); //.setSeed(seedTea);
		((BlockCrop) plantHops).setCrop(cropHops); //.setSeed(seedHops);
	}

	// region REGISTRATION
	public static void registerBlocks() {

		plantBarley = registerBlock("plant_barley", new BlockCrop());
		plantOnion = registerBlock("plant_onion", new BlockCrop());
		plantSadiroot = registerBlock("plant_sadiroot", new BlockCrop());
		plantSpinach = registerBlock("plant_spinach", new BlockCrop());
		plantRice = registerBlock("plant_rice", new BlockCrop());

		plantCorn = registerBlock("plant_corn", new BlockCropTall());

		plantBellPepper = registerBlock("plant_bell_pepper", new BlockCropPerennial());
		plantGreenBean = registerBlock("plant_green_bean", new BlockCropPerennial());
		plantPeanut = registerBlock("plant_peanut", new BlockCropPerennial());
		plantStrawberry = registerBlock("plant_strawberry", new BlockCropPerennial());
		plantTomato = registerBlock("plant_tomato", new BlockCropPerennial());

		plantCoffee = registerBlock("plant_coffee", new BlockCropPerennial());
		plantTea = registerBlock("plant_tea", new BlockCropPerennial());
		plantHops = registerBlock("plant_hops", new BlockCropTallPerennial());
	}
	// endregion

	// region CROPS
	public static Block plantBarley;
	public static Block plantCorn;
	public static Block plantOnion;
	public static Block plantSadiroot;
	public static Block plantSpinach;
	public static Block plantRice;

	public static Block plantBellPepper;
	public static Block plantGreenBean;
	public static Block plantPeanut;
	public static Block plantStrawberry;
	public static Block plantTomato;

	public static Block plantCoffee;
	public static Block plantTea;
	public static Block plantHops;
	// endregion
}
