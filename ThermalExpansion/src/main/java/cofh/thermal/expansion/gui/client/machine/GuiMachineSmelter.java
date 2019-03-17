package cofh.thermal.expansion.gui.client.machine;

import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineSmelter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.PROG_ARROW_RIGHT;
import static cofh.core.gui.GuiHelper.SCALE_FLAME;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineSmelter extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/smelter.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementScaled progress;
	private ElementScaled speed;

	public GuiMachineSmelter(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineSmelter(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_smelter"));
		name = StringHelper.localize("tile.thermal.machine_smelter.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		progress = (ElementScaled) addElement(new ElementScaled(this, 72, 25).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_ARROW_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 44, 35).setSize(16, SPEED).setTexture(SCALE_FLAME, 32, 16));
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		progress.setQuantity(tile.getScaledProgress(PROGRESS));
		speed.setQuantity(tile.getScaledSpeed(SPEED));
	}

}
