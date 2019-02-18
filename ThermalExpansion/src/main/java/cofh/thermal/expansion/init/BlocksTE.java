package cofh.thermal.expansion.init;

import cofh.core.item.ItemBlockCoFH;
import cofh.thermal.core.block.dynamo.BlockDynamo;
import cofh.thermal.core.block.machine.BlockMachine;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.GROUP_DYNAMOS;
import static cofh.lib.util.Constants.GROUP_MACHINES;
import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.expansion.init.DynamosTE.MAGMATIC;
import static cofh.thermal.expansion.init.DynamosTE.STEAM;
import static cofh.thermal.expansion.init.MachinesTE.*;

public class BlocksTE {

	private BlocksTE() {

	}

	// region REGISTRATION
	public static void registerBlocks() {

		machineFurnace = registerBlock("machine_furnace", new ItemBlockCoFH(new BlockMachine(FURNACE), GROUP_MACHINES));
		machinePulverizer = registerBlock("machine_pulverizer", new ItemBlockCoFH(new BlockMachine(PULVERIZER), GROUP_MACHINES));
		machineSawmill = registerBlock("machine_sawmill", new ItemBlockCoFH(new BlockMachine(SAWMILL), GROUP_MACHINES));
		machineCrucible = registerBlock("machine_crucible", new ItemBlockCoFH(new BlockMachine(CRUCIBLE), GROUP_MACHINES));
		machineCentrifuge = registerBlock("machine_centrifuge", new ItemBlockCoFH(new BlockMachine(CENTRIFUGE), GROUP_MACHINES));

		dynamoSteam = registerBlock("dynamo_steam", new ItemBlockCoFH(new BlockDynamo(STEAM), GROUP_DYNAMOS));
		dynamoMagmatic = registerBlock("dynamo_magmatic", new ItemBlockCoFH(new BlockDynamo(MAGMATIC), GROUP_DYNAMOS));
	}
	// endregion

	// region MACHINES
	public static ItemStack machineFurnace;
	public static ItemStack machinePulverizer;
	public static ItemStack machineSawmill;
	public static ItemStack machineCrucible;
	public static ItemStack machineCentrifuge;
	// endregion

	// region DYNAMOS
	public static ItemStack dynamoSteam;
	public static ItemStack dynamoMagmatic;
	// endregion
}
