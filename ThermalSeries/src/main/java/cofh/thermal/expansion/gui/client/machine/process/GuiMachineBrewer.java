package cofh.thermal.expansion.gui.client.machine.process;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.core.gui.element.ElementScaledFluid;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.core.gui.client.GuiMachine;
import cofh.thermal.expansion.gui.container.machine.process.ContainerMachineBrewer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.*;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineBrewer extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/brewer.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementScaledFluid progressFluid;

	public GuiMachineBrewer(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineBrewer(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_brewer"));
		name = StringHelper.localize("tile.thermal.machine_brewer.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		addElement(GuiHelper.createSmallFluidStorage(this, 35, 25, tile.getTank(0)));
		addElement(GuiHelper.createLargeFluidStorage(this, 151, 8, tile.getTank(1)));

		progressFluid = (ElementScaledFluid) addElement(new ElementScaledFluid(this, 112, 34).setFluid(tile.getRenderFluid()).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_DROP_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 57, 34).setSize(16, SPEED).setTexture(SCALE_ALCHEMY, 32, 16));
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		progressFluid.setFluid(tile.getRenderFluid());
		progressFluid.setQuantity(tile.getScaledProgress(PROGRESS));
	}

}
