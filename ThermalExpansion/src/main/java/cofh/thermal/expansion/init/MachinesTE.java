package cofh.thermal.expansion.init;

import cofh.thermal.core.block.machine.Machine;
import cofh.thermal.expansion.block.machine.*;
import cofh.thermal.expansion.gui.client.machine.*;
import cofh.thermal.expansion.gui.container.machine.*;

public class MachinesTE {

	private MachinesTE() {

	}

	public static final Machine FURNACE = new Machine("furnace", 14, TileMachineFurnace.class, ContainerMachineFurnace.class, GuiMachineFurnace.class);
	public static final Machine PULVERIZER = new Machine("pulverizer", 7, TileMachinePulverizer.class, ContainerMachinePulverizer.class, GuiMachinePulverizer.class);
	public static final Machine SAWMILL = new Machine("sawmill", 7, TileMachineSawmill.class, ContainerMachineSawmill.class, GuiMachineSawmill.class);
	public static final Machine SMELTER = new Machine("smelter", 15, TileMachineSmelter.class, ContainerMachineSmelter.class, GuiMachineSmelter.class);

	public static final Machine CRUCIBLE = new Machine("crucible", 15, TileMachineCrucible.class, ContainerMachineCrucible.class, GuiMachineCrucible.class);
	public static final Machine CENTRIFUGE = new Machine("centrifuge", 4, TileMachineCentrifuge.class, ContainerMachineCentrifuge.class, GuiMachineCentrifuge.class);
	// public static final Machine COMPACTOR = new Machine("compactor", 4, TileMachineCompactor.class, ContainerMachineCompactor.class, GuiMachineCompactor.class);

	// public static final Machine CRAFTER = new Machine("crafter", 7, TileMachineCrafter.class, ContainerMachineCrafter.class, GuiMachineCrafter.class);
	// public static final Machine BREWER = new Machine("brewer", 12, TileMachineBrewer.class, ContainerMachineBrewer.class, GuiMachineBrewer.class);
	// public static final Machine ENCHANTER = new Machine("enchanter", 12, TileMachineEnchanter.class, ContainerMachineEnchanter.class, GuiMachineEnchanter.class);
}
