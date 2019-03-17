package cofh.thermal.expansion.gui.client.machine;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.core.gui.element.ElementScaledFluid;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineInsolator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.PROG_ARROW_FLUID_RIGHT;
import static cofh.core.gui.GuiHelper.SCALE_SUN;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineInsolator extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/insolator.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementScaled progress;
	private ElementScaled speed;

	public GuiMachineInsolator(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineInsolator(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_insolator"));
		name = StringHelper.localize("tile.thermal.machine_insolator.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		addElement(GuiHelper.createMediumFluidStorage(this, 34, 22, tile.getTank(0)));

		progress = (ElementScaledFluid) addElement(new ElementScaledFluid(this, 85, 25).setFluid(tile.getRenderFluid()).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_ARROW_FLUID_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 62, 35).setSize(16, SPEED).setTexture(SCALE_SUN, 32, 16));
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		progress.setQuantity(tile.getScaledProgress(PROGRESS));
		speed.setQuantity(tile.getScaledSpeed(SPEED));
	}

}
