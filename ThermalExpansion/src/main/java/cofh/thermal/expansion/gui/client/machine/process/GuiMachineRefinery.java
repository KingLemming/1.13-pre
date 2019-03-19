package cofh.thermal.expansion.gui.client.machine.process;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.core.gui.element.ElementScaledFluid;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.core.gui.client.GuiMachine;
import cofh.thermal.expansion.gui.container.machine.process.ContainerMachineRefinery;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.*;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineRefinery extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/refinery.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementScaledFluid progressFluid;

	public GuiMachineRefinery(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineRefinery(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_refinery"));
		name = StringHelper.localize("tile.thermal.machine_refinery.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		addElement(GuiHelper.createSmallFluidStorage(this, 34, 17, tile.getTank(0)));
		addElement(GuiHelper.createMediumFluidStorage(this, 133, 22, tile.getTank(1)));
		addElement(GuiHelper.createMediumFluidStorage(this, 151, 22, tile.getTank(2)));

		progressFluid = (ElementScaledFluid) addElement(new ElementScaledFluid(this, 65, 35).setFluid(tile.getRenderFluid()).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_DROP_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 35, 53).setSize(16, SPEED).setTexture(SCALE_FLAME, 32, 16));
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		progressFluid.setFluid(tile.getRenderFluid());
		progressFluid.setQuantity(tile.getScaledProgress(PROGRESS));
	}

}
