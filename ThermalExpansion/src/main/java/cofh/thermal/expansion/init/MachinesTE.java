package cofh.thermal.expansion.init;

import cofh.thermal.core.block.machine.Machine;
import cofh.thermal.expansion.block.machine.TileMachineCrucible;
import cofh.thermal.expansion.block.machine.TileMachineFurnace;
import cofh.thermal.expansion.block.machine.TileMachinePulverizer;
import cofh.thermal.expansion.block.machine.TileMachineSawmill;
import cofh.thermal.expansion.gui.client.machine.GuiMachineCrucible;
import cofh.thermal.expansion.gui.client.machine.GuiMachineFurnace;
import cofh.thermal.expansion.gui.client.machine.GuiMachinePulverizer;
import cofh.thermal.expansion.gui.client.machine.GuiMachineSawmill;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineCrucible;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineFurnace;
import cofh.thermal.expansion.gui.container.machine.ContainerMachinePulverizer;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineSawmill;

public class MachinesTE {

	private MachinesTE() {

	}

	public static final Machine FURNACE = new Machine("furnace", 14, TileMachineFurnace.class, ContainerMachineFurnace.class, GuiMachineFurnace.class);
	public static final Machine PULVERIZER = new Machine("pulverizer", 7, TileMachinePulverizer.class, ContainerMachinePulverizer.class, GuiMachinePulverizer.class);
	public static final Machine SAWMILL = new Machine("sawmill", 7, TileMachineSawmill.class, ContainerMachineSawmill.class, GuiMachineSawmill.class);

	public static final Machine CRUCIBLE = new Machine("crucible", 15, TileMachineCrucible.class, ContainerMachineCrucible.class, GuiMachineCrucible.class);

}
