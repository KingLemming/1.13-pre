package cofh.thermal.cultivation.gui.client.dynamo;

import cofh.core.gui.element.ElementScaled;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.core.gui.client.GuiDynamo;
import cofh.thermal.cultivation.gui.container.dynamo.ContainerDynamoGourmand;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.core.gui.GuiHelper.SCALE_FLAME_GREEN;
import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiDynamoGourmand extends GuiDynamo {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/dynamo/gourmand.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public GuiDynamoGourmand(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerDynamoGourmand(inventory, tile), (TileDynamo) tile, inventory.player, generateTabInfo("tab.thermal.dynamo_gourmand"));
		name = StringHelper.localize("tile.thermal.dynamo_gourmand.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		duration = (ElementScaled) addElement(new ElementScaled(this, 115, 35).setSize(16, 16).setTexture(SCALE_FLAME_GREEN, 32, 16));
	}

}
