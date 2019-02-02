package cofh.thermal.expansion.gui.client.machine;

import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.gui.element.tab.TabRedstoneControl;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.inventory.Container;

public class GuiMachine extends GuiContainerCoFH {

	protected TileMachine tile;

	public GuiMachine(Container inventorySlotsIn, TileMachine tile, String info) {

		super(inventorySlotsIn, info);
		this.tile = tile;
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabInfo(this, info));
		addTab(new TabRedstoneControl(this, tile));
	}

}
