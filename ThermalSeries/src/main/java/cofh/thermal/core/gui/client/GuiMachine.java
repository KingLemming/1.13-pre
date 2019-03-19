package cofh.thermal.core.gui.client;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.gui.element.tab.TabRedstoneControl;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import static cofh.core.gui.GuiHelper.PROGRESS;
import static cofh.core.gui.GuiHelper.SPEED;

public class GuiMachine extends GuiContainerCoFH {

	protected TileMachine tile;

	protected ElementScaled progress;
	protected ElementScaled speed;

	public GuiMachine(Container inventorySlotsIn, TileMachine tile, EntityPlayer player, String info) {

		super(inventorySlotsIn, player, info);
		this.tile = tile;
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabInfo(this, info));
		addTab(new TabRedstoneControl(this, tile));

		if (tile.getEnergyStorage().getMaxEnergyStored() > 0) {
			addElement(GuiHelper.createDefaultEnergyStorage(this, 8, 8, tile.getEnergyStorage()));
		}
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		if (progress != null) {
			progress.setQuantity(tile.getScaledProgress(PROGRESS));
		}
		if (speed != null) {
			speed.setQuantity(tile.getScaledSpeed(SPEED));
		}
	}

}
