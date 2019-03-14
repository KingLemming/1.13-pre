package cofh.thermal.expansion.gui.client.machine;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.core.gui.element.ElementScaledFluid;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineCrucible;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.PROG_DROP_RIGHT;
import static cofh.core.gui.GuiHelper.SCALE_FLAME;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineCrucible extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/crucible.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementScaledFluid progress;
	private ElementScaled speed;

	public GuiMachineCrucible(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineCrucible(inventory, tile), (TileMachine) tile, generateTabInfo("tab.thermal.machine_crucible"));
		name = StringHelper.localize("tile.thermal.machine_crucible.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		addElement(GuiHelper.createLargeFluidStorage(this, 151, 8, tile.getTank(0)));

		progress = (ElementScaledFluid) addElement(new ElementScaledFluid(this, 103, 34).setFluid(tile.getRenderFluid()).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_DROP_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 53, 44).setSize(16, SPEED).setTexture(SCALE_FLAME, 32, 16));
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		progress.setFluid(tile.getRenderFluid());
		progress.setQuantity(tile.getScaledProgress(PROGRESS));
		speed.setQuantity(tile.getScaledSpeed(SPEED));
	}
}
