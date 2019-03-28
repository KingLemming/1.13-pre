package cofh.thermal.cultivation.init;

import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.cultivation.block.dynamo.TileDynamoGourmand;
import cofh.thermal.cultivation.gui.client.dynamo.GuiDynamoGourmand;
import cofh.thermal.cultivation.gui.container.dynamo.ContainerDynamoGourmand;

import static cofh.thermal.core.block.AbstractTileType.registerDynamo;

public class DynamosTC {

	private DynamosTC() {

	}

	public static final AbstractTileType GOURMAND = registerDynamo("gourmand", 7, TileDynamoGourmand.class, ContainerDynamoGourmand.class, GuiDynamoGourmand.class);

}
