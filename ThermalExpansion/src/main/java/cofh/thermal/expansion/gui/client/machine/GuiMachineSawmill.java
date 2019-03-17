package cofh.thermal.expansion.gui.client.machine;

import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineSawmill;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.PROG_ARROW_RIGHT;
import static cofh.core.gui.GuiHelper.SCALE_SAW;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineSawmill extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/sawmill.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementScaled progress;
	private ElementScaled speed;

	public GuiMachineSawmill(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineSawmill(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_sawmill"));
		name = StringHelper.localize("tile.thermal.machine_sawmill.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		progress = (ElementScaled) addElement(new ElementScaled(this, 72, 34).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_ARROW_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 44, 44).setSize(16, SPEED).setTexture(SCALE_SAW, 32, 16));
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		progress.setQuantity(tile.getScaledProgress(PROGRESS));
		speed.setQuantity(tile.getScaledSpeed(SPEED));
	}

}
