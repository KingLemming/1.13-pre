package cofh.thermal.core.gui.client;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.gui.element.tab.TabRedstoneControl;
import cofh.thermal.core.block.dynamo.TileDynamo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import static cofh.core.gui.GuiHelper.DURATION;

public class GuiDynamo extends GuiContainerCoFH {

	protected TileDynamo tile;

	protected ElementScaled duration;

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

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		if (duration != null) {
			duration.setQuantity(tile.getScaledDuration(DURATION));
		}
	}

}
