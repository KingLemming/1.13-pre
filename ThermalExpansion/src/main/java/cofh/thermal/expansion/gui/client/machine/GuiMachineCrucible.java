package cofh.thermal.expansion.gui.client.machine;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineCrucible;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineCrucible extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/crucible.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public GuiMachineCrucible(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineCrucible(inventory, tile), (TileMachine) tile, generateTabInfo("tab.thermal.machine_crucible"));
		name = StringHelper.localize("tile.thermal.machine_crucible.name");
		texture = TEXTURE;
	}

}
