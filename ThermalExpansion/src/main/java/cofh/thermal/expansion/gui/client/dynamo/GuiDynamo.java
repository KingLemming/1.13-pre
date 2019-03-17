package cofh.thermal.expansion.gui.client.dynamo;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.gui.element.tab.TabRedstoneControl;
import cofh.thermal.core.block.dynamo.TileDynamo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiDynamo extends GuiContainerCoFH {

	protected TileDynamo tile;

	public GuiDynamo(Container inventorySlotsIn, TileDynamo tile, EntityPlayer player, String info) {

		super(inventorySlotsIn, player, info);
		this.tile = tile;
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabInfo(this, info));
		addTab(new TabRedstoneControl(this, tile));

		if (tile.getEnergyStorage().getMaxEnergyStored() > 0) {
			addElement(GuiHelper.createDefaultEnergyStorage(this, 80, 18, tile.getEnergyStorage()));
		}
	}

}
