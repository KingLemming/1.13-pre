package cofh.thermal.expansion.gui.client.dynamo;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoStirling;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiDynamoStirling extends GuiDynamo {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/dynamo/stirling.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public GuiDynamoStirling(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerDynamoStirling(inventory, tile), (TileDynamo) tile, inventory.player, generateTabInfo("tab.thermal.dynamo_stirling"));
		name = StringHelper.localize("tile.thermal.dynamo_stirling.name");
		texture = TEXTURE;
	}

}
