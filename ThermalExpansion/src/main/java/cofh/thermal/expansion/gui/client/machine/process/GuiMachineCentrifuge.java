package cofh.thermal.expansion.gui.client.machine.process;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.core.gui.element.ElementScaledFluid;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.core.gui.client.GuiMachine;
import cofh.thermal.expansion.gui.container.machine.process.ContainerMachineCentrifuge;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.*;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineCentrifuge extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/centrifuge.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementScaledFluid progressFluid;

	public GuiMachineCentrifuge(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineCentrifuge(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_centrifuge"));
		name = StringHelper.localize("tile.thermal.machine_centrifuge.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		addElement(GuiHelper.createLargeFluidStorage(this, 151, 8, tile.getTank(0)));

		progressFluid = (ElementScaledFluid) addElement(new ElementScaledFluid(this, 72, 34).setFluid(tile.getRenderFluid()).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_ARROW_FLUID_RIGHT, 64, 16));
		progress = (ElementScaled) addElement(new ElementScaled(this, 72, 34).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_ARROW_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 44, 44).setSize(16, SPEED).setTexture(SCALE_SPIN, 32, 16));
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		if (tile.getRenderFluid() != null) {
			progressFluid.setFluid(tile.getRenderFluid());
			progressFluid.setQuantity(tile.getScaledProgress(PROGRESS));
			progressFluid.setVisible(true);
			progress.setVisible(false);
		} else {
			progress.setQuantity(tile.getScaledProgress(PROGRESS));
			progress.setVisible(true);
			progressFluid.setVisible(false);
		}
		speed.setQuantity(tile.getScaledSpeed(SPEED));
	}

}
