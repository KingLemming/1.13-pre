package cofh.thermal.expansion.gui.client.dynamo;

import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.tab.TabInfo;
import net.minecraft.inventory.Container;

public class GuiDynamo extends GuiContainerCoFH {

	public GuiDynamo(Container inventorySlotsIn, String info) {

		super(inventorySlotsIn, info);
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabInfo(this, info));
	}

}
