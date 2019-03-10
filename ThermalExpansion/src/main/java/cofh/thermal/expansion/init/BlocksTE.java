package cofh.thermal.expansion.init;

import cofh.core.item.ItemBlockCoFH;
import cofh.thermal.core.block.BlockDynamo;
import cofh.thermal.core.block.BlockMachine;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.expansion.init.DynamosTE.MAGMATIC;
import static cofh.thermal.expansion.init.DynamosTE.STIRLING;
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
		machineCrucible = registerBlock("machine_crucible", new ItemBlockCoFH(new BlockMachine(CRUCIBLE)));
		machineCentrifuge = registerBlock("machine_centrifuge", new ItemBlockCoFH(new BlockMachine(CENTRIFUGE)));

		dynamoStirling = registerBlock("dynamo_stirling", new ItemBlockCoFH(new BlockDynamo(STIRLING)));
		dynamoMagmatic = registerBlock("dynamo_magmatic", new ItemBlockCoFH(new BlockDynamo(MAGMATIC)));
	}
	// endregion

	// region MACHINES
	public static ItemStack machineFurnace;
	public static ItemStack machinePulverizer;
	public static ItemStack machineSawmill;
	public static ItemStack machineInsolator;
	public static ItemStack machineCrucible;
	public static ItemStack machineCentrifuge;
	// endregion

	// region DYNAMOS
	public static ItemStack dynamoStirling;
	public static ItemStack dynamoMagmatic;
	// endregion
}
