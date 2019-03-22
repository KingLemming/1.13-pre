package cofh.thermal.foundation.init;

import cofh.core.block.BlockOre;
import cofh.core.block.BlockStorageMetal;
import cofh.core.item.ItemBlockCoFH;
import cofh.lib.util.RandomDrop;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.core.init.ItemsTSeries.*;

public class BlocksTF {

	private BlocksTF() {

	}

	// region INITIALIZATION
	public static void initialize() {

		((BlockOre) blockOreSulfur).addDrop(new RandomDrop(dustSulfur, 2, 4));
		((BlockOre) blockOreNiter).addDrop(new RandomDrop(dustNiter, 2, 4));
		((BlockOre) blockOreCinnabar).addDrop(new RandomDrop(mineralCinnabar, 1, 2));
	}
	// endregion

	// region REGISTRATION
	public static void registerBlocks() {

		registerThermalOres();
		registerExtraOres();

		registerThermalMetals();
		registerExtraMetals();
	}

	private static void registerThermalOres() {

		String pickaxe = TOOL_PICKAXE;

		blockOreSulfur = new BlockOre().setHarvestParams(pickaxe, 1).setXPDrop(1, 3);
		blockOreNiter = new BlockOre().setHarvestParams(pickaxe, 1).setXPDrop(1, 3);
		blockOreCinnabar = new BlockOre().setHarvestParams(pickaxe, 1).setXPDrop(1, 3);

		oreSulfur = registerBlock("ore_sulfur", "oreSulfur", new ItemBlockCoFH(blockOreSulfur, GROUP_ORES));
		oreNiter = registerBlock("ore_niter", "oreNiter", new ItemBlockCoFH(blockOreNiter, GROUP_ORES));
		oreCinnabar = registerBlock("ore_cinnabar", "oreCinnabar", new ItemBlockCoFH(blockOreCinnabar, GROUP_ORES));

		oreCopper = registerBlock("ore_copper", "oreCopper", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		oreSilver = registerBlock("ore_silver", "oreSilver", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
		oreNickel = registerBlock("ore_nickel", "oreNickel", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
	}

	private static void registerExtraOres() {

		String pickaxe = TOOL_PICKAXE;

		oreTin = registerBlock("ore_tin", "oreTin", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		oreAluminum = registerBlock("ore_aluminum", "oreAluminum", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		oreLead = registerBlock("ore_lead", "oreLead", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
		orePlatinum = registerBlock("ore_platinum", "orePlatinum", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 3), GROUP_ORES).setRarity(EnumRarity.UNCOMMON));
		oreIridium = registerBlock("ore_iridium", "oreIridium", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 3), GROUP_ORES).setRarity(EnumRarity.UNCOMMON));
	}

	private static void registerThermalMetals() {

		String pickaxe = TOOL_PICKAXE;

		storageCopper = registerBlock("block_copper", "blockCopper", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
		storageSilver = registerBlock("block_silver", "blockSilver", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageNickel = registerBlock("block_nickel", "blockNickel", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));

		storageInvar = registerBlock("block_invar", "blockInvar", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageConstantan = registerBlock("block_constantan", "blockConstantan", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageElectrum = registerBlock("block_electrum", "blockElectrum", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
	}

	private static void registerExtraMetals() {

		String pickaxe = TOOL_PICKAXE;

		storageTin = registerBlock("block_tin", "blockTin", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
		storageAluminum = registerBlock("block_aluminum", "blockAluminum", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
		storageLead = registerBlock("block_lead", "blockLead", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storagePlatinum = registerBlock("block_platinum", "blockPlatinum", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));
		storageIridium = registerBlock("block_iridium", "blockIridium", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));

		storageSteel = registerBlock("block_steel", "blockSteel", new ItemBlockCoFH(new BlockStorageMetal().setHardness(7.5F).setResistance(15.0F), GROUP_STORAGE));
		storageBronze = registerBlock("block_bronze", "blockBronze", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
	}
	// endregion

	// region BLOCKS
	public static Block blockOreSulfur;
	public static Block blockOreNiter;
	public static Block blockOreCinnabar;
	// endregion

	// region THERMAL ORES
	public static ItemStack oreSulfur;
	public static ItemStack oreNiter;
	public static ItemStack oreCinnabar;

	public static ItemStack oreCopper;
	public static ItemStack oreSilver;
	public static ItemStack oreNickel;
	// endregion

	// region EXTRA ORES
	public static ItemStack oreTin;
	public static ItemStack oreAluminum;
	public static ItemStack oreLead;
	public static ItemStack orePlatinum;
	public static ItemStack oreIridium;
	// endregion

	// region THERMAL METALS
	public static ItemStack storageCopper;
	public static ItemStack storageSilver;
	public static ItemStack storageNickel;

	public static ItemStack storageInvar;
	public static ItemStack storageConstantan;
	public static ItemStack storageElectrum;
	// endregion

	// region EXTRA METALS
	public static ItemStack storageTin;
	public static ItemStack storageAluminum;
	public static ItemStack storageLead;
	public static ItemStack storagePlatinum;
	public static ItemStack storageIridium;

	public static ItemStack storageSteel;
	public static ItemStack storageBronze;
	// endregion
}
