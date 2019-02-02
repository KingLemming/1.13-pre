package cofh.thermal.expansion.gui.client.dynamo;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoSteam;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiDynamoSteam extends GuiDynamo {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/dynamo/steam.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public GuiDynamoSteam(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerDynamoSteam(inventory, tile), generateTabInfo("tab.thermal.dynamo_steam"));
		name = StringHelper.localize("tile.thermal.dynamo_steam.name");
		texture = TEXTURE;
	}

}
