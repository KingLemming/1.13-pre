package cofh.thermal.expansion.init;

import cofh.core.item.ItemBlockCoFH;
import cofh.thermal.core.block.BlockAbstractTile4Way;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.expansion.init.MachinesTE.*;

public class BlocksTE {

	private BlocksTE() {

	}

	// region REGISTRATION
	public static void registerBlocks() {

		machineFurnace = registerBlock("machine_furnace", new ItemBlockCoFH(new BlockAbstractTile4Way(FURNACE)));
		machinePulverizer = registerBlock("machine_pulverizer", new ItemBlockCoFH(new BlockAbstractTile4Way(PULVERIZER)));
		machineSawmill = registerBlock("machine_sawmill", new ItemBlockCoFH(new BlockAbstractTile4Way(SAWMILL)));
		machineInsolator = registerBlock("machine_insolator", new ItemBlockCoFH(new BlockAbstractTile4Way(INSOLATOR)));
		machineCrucible = registerBlock("machine_crucible", new ItemBlockCoFH(new BlockAbstractTile4Way(CRUCIBLE)));
		machineCentrifuge = registerBlock("machine_centrifuge", new ItemBlockCoFH(new BlockAbstractTile4Way(CENTRIFUGE)));

		//		dynamoSteam = registerBlock("dynamo_steam", new ItemBlockCoFH(new BlockAbstractTile6Way(STEAM), GROUP_DYNAMOS));
		//		dynamoMagmatic = registerBlock("dynamo_magmatic", new ItemBlockCoFH(new BlockAbstractTile6Way(MAGMATIC), GROUP_DYNAMOS));
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
	public static ItemStack dynamoSteam;
	public static ItemStack dynamoMagmatic;
	// endregion
}
