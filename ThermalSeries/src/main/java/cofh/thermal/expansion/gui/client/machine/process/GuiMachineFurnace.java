package cofh.thermal.expansion.gui.client.machine.process;

import cofh.core.gui.element.ElementScaled;
import cofh.core.gui.element.ElementScaled.StartDirection;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.core.gui.client.GuiMachine;
import cofh.thermal.expansion.gui.container.machine.process.ContainerMachineFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.*;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineFurnace extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/furnace.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public GuiMachineFurnace(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineFurnace(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_furnace"));
		name = StringHelper.localize("tile.thermal.machine_furnace.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		progress = (ElementScaled) addElement(new ElementScaled(this, 79, 34).setDirection(StartDirection.LEFT).setSize(PROGRESS, 16).setTexture(PROG_ARROW_RIGHT, 64, 16));
		speed = (ElementScaled) addElement(new ElementScaled(this, 53, 44).setSize(16, SPEED).setTexture(SCALE_FLAME, 32, 16));
	}

}
