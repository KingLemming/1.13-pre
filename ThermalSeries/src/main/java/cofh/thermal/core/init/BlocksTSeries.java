package cofh.thermal.core.init;

import cofh.core.block.BlockOre;
import cofh.core.block.BlockStorageMetal;
import cofh.core.block.BlockStorageResource;
import cofh.core.item.ItemBlockCoFH;
import cofh.core.item.ItemBlockFuel;
import cofh.lib.util.RandomDrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.core.init.ConfigTSeries.fuelCoal;
import static cofh.thermal.core.init.ConfigTSeries.fuelCoalCoke;
import static cofh.thermal.core.init.ItemsTSeries.*;

public class BlocksTSeries {

	private BlocksTSeries() {

	}

	// region INITIALIZATION
	public static void initialize() {

		((BlockOre) blockOreSulfur).addDrop(new RandomDrop(dustSulfur, 2, 4));
		((BlockOre) blockOreNiter).addDrop(new RandomDrop(dustNiter, 2, 4));
		((BlockOre) blockOreCinnabar).addDrop(new RandomDrop(mineralCinnabar, 1, 2));

		// TODO: Fix
		//  blockOreFluidExperience.addDrop(new RandomDrop(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), 2, 4));
		//	blockOreFluidRedstone.addDrop(new RandomDrop(Items.REDSTONE, 4, 6));
		//	blockOreFluidGlowstoneNetherrack.addDrop(new RandomDrop(Items.GLOWSTONE_DUST, 2, 4));
		//	blockOreFluidEnderEndStone.addDrop(new RandomDrop(Items.ENDER_PEARL, 1, 3));
	}
	// endregion

	// region REGISTRATION
	public static void registerBlocks() {

		storageCharcoal = registerBlock("block_charcoal", "blockCharcoal", new ItemBlockFuel(new BlockStorageResource() {

			@Override
			public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

			@Override
			public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

		}, GROUP_STORAGE, fuelCoal * 10));
		storageCoalCoke = registerBlock("block_coal_coke", "blockFuelCoke", new ItemBlockFuel(new BlockStorageResource() {

			@Override
			public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

			@Override
			public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

		}, GROUP_STORAGE, fuelCoalCoke * 10));

		registerResources();
		registerThermalMetals();
		// registerExtraMetals();
	}

	private static void registerResources() {

		String pickaxe = TOOL_PICKAXE;

		blockOreSulfur = new BlockOre().setHarvestParams(pickaxe, 1).setXPDrop(1, 3);
		blockOreNiter = new BlockOre().setHarvestParams(pickaxe, 1).setXPDrop(1, 3);
		blockOreCinnabar = new BlockOre().setHarvestParams(pickaxe, 1).setXPDrop(1, 3);

		//  blockOreFluidExperience = new BlockOreFluid(blockFluidExperience).setXPDrop(7, 10);
		//	blockOreFluidRedstone = new BlockOreFluid(blockFluidRedstone).setXPDrop(1, 3);
		//	blockOreFluidGlowstoneNetherrack = new BlockOreFluid(MapColor.NETHERRACK, blockFluidGlowstone).setXPDrop(2, 4);
		//	blockOreFluidEnderEndStone = (BlockOre) new BlockOreFluid(MapColor.SAND, blockFluidEnder).setXPDrop(3, 5).setResistance(15.0F);

		oreSulfur = registerBlock("ore_sulfur", "oreSulfur", new ItemBlockCoFH(blockOreSulfur, GROUP_ORES));
		oreNiter = registerBlock("ore_niter", "oreNiter", new ItemBlockCoFH(blockOreNiter, GROUP_ORES));
		oreCinnabar = registerBlock("ore_cinnabar", "oreCinnabar", new ItemBlockCoFH(blockOreCinnabar, GROUP_ORES));

		//	oreFluidRedstone = registerBlock("ore_fluid_redstone", "oreFluidRedstone", new ItemBlockCoFH(blockOreFluidRedstone, GROUP_ORES));
		//	oreFluidGlowstoneNetherrack = registerBlock("ore_fluid_glowstone_netherrack", "oreFluidGlowstone", new ItemBlockCoFH(blockOreFluidGlowstoneNetherrack, GROUP_ORES));
		//	oreFluidEnderEndStone = registerBlock("ore_fluid_ender_end_stone", "oreFluidEnder", new ItemBlockCoFH(blockOreFluidEnderEndStone, GROUP_ORES));

		oreCopper = registerBlock("ore_copper", "oreCopper", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		oreSilver = registerBlock("ore_silver", "oreSilver", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
		oreNickel = registerBlock("ore_nickel", "oreNickel", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));

		//		oreTin = registerBlock("ore_tin", "oreTin", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		//		oreAluminum = registerBlock("ore_aluminum", "oreAluminum", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 1), GROUP_ORES));
		//		oreLead = registerBlock("ore_lead", "oreLead", new ItemBlockCoFH(new BlockOre(), GROUP_ORES));
		//		orePlatinum = registerBlock("ore_platinum", "orePlatinum", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 3), GROUP_ORES).setRarity(EnumRarity.UNCOMMON));
		//		oreIridium = registerBlock("ore_iridium", "oreIridium", new ItemBlockCoFH(new BlockOre().setHarvestParams(pickaxe, 3), GROUP_ORES).setRarity(EnumRarity.UNCOMMON));
	}

	private static void registerThermalMetals() {

		String pickaxe = TOOL_PICKAXE;

		storageCopper = registerBlock("block_copper", "blockCopper", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 1), GROUP_STORAGE));
		storageSilver = registerBlock("block_silver", "blockSilver", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageNickel = registerBlock("block_nickel", "blockNickel", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));

		storageInvar = registerBlock("block_invar", "blockInvar", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageConstantan = registerBlock("block_constantan", "blockConstantan", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));
		storageElectrum = registerBlock("block_electrum", "blockElectrum", new ItemBlockCoFH(new BlockStorageMetal(), GROUP_STORAGE));

		storageSignalum = registerBlock("block_signalum", "blockSignalum", new ItemBlockCoFH(new BlockStorageMetal() {

			@Override
			public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

				return 15;
			}

			@Override
			public boolean canProvidePower(IBlockState state) {

				return true;
			}

		}.setLightLevel(7), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));
		storageLumium = registerBlock("block_lumium", "blockLumium", new ItemBlockCoFH(new BlockStorageMetal().setLightLevel(15), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));
		storageEnderium = registerBlock("block_enderium", "blockEnderium", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 3).setLightLevel(3).setHardness(25.0F).setResistance(50.0F), GROUP_STORAGE).setRarity(EnumRarity.RARE));
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

	//  public static BlockOre blockOreFluidExperience;
	//	public static BlockOre blockOreFluidRedstone;
	//	public static BlockOre blockOreFluidGlowstoneNetherrack;
	//	public static BlockOre blockOreFluidEnderEndStone;
	// endregion

	// region RESOURCES
	public static ItemStack oreSulfur;
	public static ItemStack oreNiter;
	public static ItemStack oreCinnabar;

	public static ItemStack oreFluidExperience;
	public static ItemStack oreFluidRedstone;
	public static ItemStack oreFluidGlowstoneNetherrack;
	public static ItemStack oreFluidEnderEndStone;

	public static ItemStack oreCopper;
	public static ItemStack oreSilver;
	public static ItemStack oreNickel;

	public static ItemStack storageCharcoal;
	public static ItemStack storageCoalCoke;

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

	public static ItemStack storageSignalum;
	public static ItemStack storageLumium;
	public static ItemStack storageEnderium;
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
