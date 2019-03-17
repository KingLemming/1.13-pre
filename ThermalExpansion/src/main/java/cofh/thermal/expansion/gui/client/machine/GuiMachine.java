package cofh.thermal.expansion.gui.client.machine;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.gui.element.tab.TabRedstoneControl;
import cofh.thermal.core.block.machine.TileMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiMachine extends GuiContainerCoFH {

	public static final int PROGRESS = 24;
	public static final int SPEED = 16;

	protected TileMachine tile;

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

}
