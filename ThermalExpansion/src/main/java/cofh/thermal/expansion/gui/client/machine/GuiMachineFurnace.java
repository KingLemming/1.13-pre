package cofh.thermal.expansion.gui.client.machine;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineFurnace extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/furnace.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public GuiMachineFurnace(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineFurnace(inventory, tile), (TileMachine) tile, generateTabInfo("tab.thermal.machine_furnace"));
		name = StringHelper.localize("tile.thermal.machine_furnace.name");
		texture = TEXTURE;
	}

}
