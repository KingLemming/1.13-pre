package cofh.thermal.expansion.gui.client.machine.world;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.element.ElementScaled;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.core.gui.client.GuiMachine;
import cofh.thermal.expansion.gui.container.machine.world.ContainerMachineWaterGen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.SCALE_BUBBLE;
import static cofh.core.gui.GuiHelper.SPEED;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineWaterGen extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/water_gen.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public GuiMachineWaterGen(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineWaterGen(inventory, tile), (TileMachine) tile, inventory.player, generateTabInfo("tab.thermal.machine_water_gen"));
		name = StringHelper.localize("tile.thermal.machine_water_gen.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		addElement(GuiHelper.createLargeFluidStorage(this, 151, 8, tile.getTank(0)));

		speed = (ElementScaled) addElement(new ElementScaled(this, 57, 34).setSize(16, SPEED).setTexture(SCALE_BUBBLE, 32, 16));
	}

}
