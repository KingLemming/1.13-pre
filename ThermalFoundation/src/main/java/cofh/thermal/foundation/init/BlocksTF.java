package cofh.thermal.foundation.init;

import cofh.core.block.BlockOre;
import cofh.core.block.BlockStorageMetal;
import cofh.core.item.ItemBlockCoFH;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerBlock;

public class BlocksTF {

	private BlocksTF() {

	}

	// region REGISTRATION
	public static void registerBlocks() {

		String pickaxe = TOOL_PICKAXE;

		oreCopper = registerBlock("ore_copper", "oreCopper", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		oreTin = registerBlock("ore_tin", "oreTin", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		oreLead = registerBlock("ore_lead", "oreLead", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
		oreSilver = registerBlock("ore_silver", "oreSilver", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
		oreNickel = registerBlock("ore_nickel", "oreNickel", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
		orePlatinum = registerBlock("ore_platinum", "orePlatinum", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 3), GROUP_ORES).setRarity(EnumRarity.UNCOMMON));

		//		oreZinc = registerBlock("ore_zinc", "oreZinc", new BlockOre().setHarvestParams(pickaxe, 1));
		//		oreAluminum = registerBlock("ore_aluminum", "oreAluminum", new BlockOre().setHarvestParams(pickaxe, 1));
		//		oreTitanium = registerBlock("ore_titanium", "oreTitanium", new BlockOre());
		//		oreOsmium = registerBlock("ore_osmium", "oreOsmium", new BlockOre());
		//		oreIridium = registerBlock("ore_iridium", "oreIridium", new BlockOre().setHarvestParams(pickaxe, 3), EnumRarity.UNCOMMON);
		//		oreTungsten = registerBlock("ore_tungsten", "oreTungsten", new BlockOre().setHarvestParams(pickaxe, 3), EnumRarity.UNCOMMON);

		storageCopper = registerBlock("block_copper", "blockCopper", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
		storageTin = registerBlock("block_tin", "blockTin", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
		storageLead = registerBlock("block_lead", "blockLead", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageSilver = registerBlock("block_silver", "blockSilver", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageNickel = registerBlock("block_nickel", "blockNickel", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storagePlatinum = registerBlock("block_platinum", "blockPlatinum", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));

		//		storageZinc = registerBlock("block_zinc", "blockZinc", new ItemBlockCore(new BlockStorageMetal().setHarvestParams(pickaxe, 1)));
		//		storageAluminum = registerBlock("block_aluminum", "blockAluminum", new ItemBlockCore(new BlockStorageMetal().setHarvestParams(pickaxe, 1)));
		//		storageTitanium = registerBlock("block_titanium", "blockTitanium", new ItemBlockCore(new BlockStorageMetal()));
		//		storageOsmium = registerBlock("block_osmium", "blockOsmium", new ItemBlockCore(new BlockStorageMetal()));
		//		storageIridium = registerBlock("block_iridium", "blockIridium", new ItemBlockCore(new BlockStorageMetal(), EnumRarity.UNCOMMON));
		//		storageTungsten = registerBlock("block_tungsten", "blockTungsten", new ItemBlockCore(new BlockStorageMetal(), EnumRarity.UNCOMMON));

		storageBronze = registerBlock("block_bronze", "blockCopper", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
		storageInvar = registerBlock("block_invar", "blockInvar", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageElectrum = registerBlock("block_electrum", "blockElectrum", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageConstantan = registerBlock("block_constantan", "blockConstantan", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
	}
	// endregion

	// region ORES
	public static ItemStack oreCopper;
	public static ItemStack oreTin;
	public static ItemStack oreLead;
	public static ItemStack oreSilver;
	public static ItemStack oreNickel;
	public static ItemStack orePlatinum;

	public static ItemStack oreZinc;
	public static ItemStack oreAluminum;
	public static ItemStack oreTitanium;
	public static ItemStack oreTungsten;
	public static ItemStack oreOsmium;
	public static ItemStack oreIridium;
	// endregion

	// region STORAGE
	public static ItemStack storageCopper;
	public static ItemStack storageTin;
	public static ItemStack storageSilver;
	public static ItemStack storageLead;
	public static ItemStack storageNickel;
	public static ItemStack storagePlatinum;

	public static ItemStack storageZinc;
	public static ItemStack storageAluminum;
	public static ItemStack storageTitanium;
	public static ItemStack storageTungsten;
	public static ItemStack storageOsmium;
	public static ItemStack storageIridium;

	public static ItemStack storageBronze;
	public static ItemStack storageInvar;
	public static ItemStack storageElectrum;
	public static ItemStack storageConstantan;
	// endregion
}
