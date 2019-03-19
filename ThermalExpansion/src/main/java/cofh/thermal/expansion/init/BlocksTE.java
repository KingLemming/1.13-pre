package cofh.thermal.expansion.init;

import cofh.core.item.ItemBlockCoFH;
import cofh.thermal.core.block.BlockDynamo;
import cofh.thermal.core.block.BlockMachine;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.expansion.init.DynamosTE.*;
import static cofh.thermal.expansion.init.MachinesTE.*;

public class BlocksTE {

	private BlocksTE() {

	}

	// region REGISTRATION
	public static void registerBlocks() {

		machineFurnace = registerBlock("machine_furnace", new ItemBlockCoFH(new BlockMachine(FURNACE)));
		machinePulverizer = registerBlock("machine_pulverizer", new ItemBlockCoFH(new BlockMachine(PULVERIZER)));
		machineSawmill = registerBlock("machine_sawmill", new ItemBlockCoFH(new BlockMachine(SAWMILL)));
		machineInsolator = registerBlock("machine_insolator", new ItemBlockCoFH(new BlockMachine(INSOLATOR)));
		machineCentrifuge = registerBlock("machine_centrifuge", new ItemBlockCoFH(new BlockMachine(CENTRIFUGE)));
		machineCrucible = registerBlock("machine_crucible", new ItemBlockCoFH(new BlockMachine(CRUCIBLE)));
		machineRefinery = registerBlock("machine_refinery", new ItemBlockCoFH(new BlockMachine(REFINERY)));
		// machineCrafter = registerBlock("machine_crafter", new ItemBlockCoFH(new BlockMachine(CRAFTER)));
		machineBrewer = registerBlock("machine_brewer", new ItemBlockCoFH(new BlockMachine(BREWER)));
		// machineEnchanter = registerBlock("machine_enchanter", new ItemBlockCoFH(new BlockMachine(ENCHANTER)));
		// machineWaterGen = registerBlock("machine_water_gen", new ItemBlockCoFH(new BlockMachine(WATER_GEN)));

		dynamoStirling = registerBlock("dynamo_stirling", new ItemBlockCoFH(new BlockDynamo(STIRLING)));
		dynamoCompression = registerBlock("dynamo_compression", new ItemBlockCoFH(new BlockDynamo(COMPRESSION)));
		dynamoMagmatic = registerBlock("dynamo_magmatic", new ItemBlockCoFH(new BlockDynamo(MAGMATIC)));
		dynamoNumismatic = registerBlock("dynamo_numismatic", new ItemBlockCoFH(new BlockDynamo(NUMISMATIC)));
		dynamoLapidary = registerBlock("dynamo_lapidary", new ItemBlockCoFH(new BlockDynamo(LAPIDARY)));
	}
	// endregion

	// region MACHINES
	public static ItemStack machineFurnace;
	public static ItemStack machinePulverizer;
	public static ItemStack machineSawmill;
	public static ItemStack machineInsolator;
	public static ItemStack machineCentrifuge;
	public static ItemStack machineCrucible;
	public static ItemStack machineRefinery;
	public static ItemStack machineCrafter;
	public static ItemStack machineBrewer;
	public static ItemStack machineEnchanter;

	public static ItemStack machineWaterGen;
	// endregion

	// region DYNAMOS
	public static ItemStack dynamoStirling;
	public static ItemStack dynamoCompression;
	public static ItemStack dynamoMagmatic;
	public static ItemStack dynamoNumismatic;
	public static ItemStack dynamoLapidary;
	// endregion
}
