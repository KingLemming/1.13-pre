package cofh.thermal.expansion.init;

import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.expansion.block.machine.TileMachineLexicon;
import cofh.thermal.expansion.block.machine.process.*;
import cofh.thermal.expansion.block.machine.world.TileMachineWaterGen;
import cofh.thermal.expansion.gui.client.machine.GuiMachineLexicon;
import cofh.thermal.expansion.gui.client.machine.process.*;
import cofh.thermal.expansion.gui.client.machine.world.GuiMachineWaterGen;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineLexicon;
import cofh.thermal.expansion.gui.container.machine.process.*;
import cofh.thermal.expansion.gui.container.machine.world.ContainerMachineWaterGen;

import static cofh.thermal.core.block.AbstractTileType.registerMachine;

public class MachinesTE {

	private MachinesTE() {

	}

	public static final AbstractTileType FURNACE = registerMachine("furnace", 14, TileMachineFurnace.class, ContainerMachineFurnace.class, () -> GuiMachineFurnace.class);
	public static final AbstractTileType PULVERIZER = registerMachine("pulverizer", 7, TileMachinePulverizer.class, ContainerMachinePulverizer.class, () -> GuiMachinePulverizer.class);
	public static final AbstractTileType SAWMILL = registerMachine("sawmill", 7, TileMachineSawmill.class, ContainerMachineSawmill.class, () -> GuiMachineSawmill.class);
	public static final AbstractTileType INSOLATOR = registerMachine("insolator", 14, TileMachineInsolator.class, ContainerMachineInsolator.class, () -> GuiMachineInsolator.class);
	public static final AbstractTileType CENTRIFUGE = registerMachine("centrifuge", 4, TileMachineCentrifuge.class, ContainerMachineCentrifuge.class, () -> GuiMachineCentrifuge.class);
	public static final AbstractTileType CRUCIBLE = registerMachine("crucible", 14, TileMachineCrucible.class, ContainerMachineCrucible.class, () -> GuiMachineCrucible.class);
	public static final AbstractTileType REFINERY = registerMachine("refinery", 4, TileMachineRefinery.class, ContainerMachineRefinery.class, () -> GuiMachineRefinery.class);

	// public static final AbstractTileType CRAFTER = registerMachine("crafter", 7, TileMachineCrafter.class, ContainerMachineCrafter.class, () -> GuiMachineCrafter.class);
	public static final AbstractTileType BREWER = registerMachine("brewer", 12, TileMachineBrewer.class, ContainerMachineBrewer.class, () -> GuiMachineBrewer.class);
	// public static final TileStruct ENCHANTER = registerMachine("enchanter", 12, TileMachineEnchanter.class, ContainerMachineEnchanter.class, () -> GuiMachineEnchanter.class);

	public static final AbstractTileType LEXICON = registerMachine("lexicon", 12, TileMachineLexicon.class, ContainerMachineLexicon.class, () -> GuiMachineLexicon.class);

	public static final AbstractTileType SMELTER = registerMachine("smelter", 14, TileMachineSmelter.class, ContainerMachineSmelter.class, () -> GuiMachineSmelter.class);
	// public static final AbstractTileType EXTRUDER = registerMachine("extruder", 14, TileMachineExtruder.class, ContainerMachineExtruder.class, () -> GuiMachineExtruder.class);

	public static final AbstractTileType WATER_GEN = registerMachine("water_gen", 4, TileMachineWaterGen.class, ContainerMachineWaterGen.class, () -> GuiMachineWaterGen.class);

}
