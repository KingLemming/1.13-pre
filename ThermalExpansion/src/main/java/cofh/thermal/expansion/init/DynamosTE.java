package cofh.thermal.expansion.init;

import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.expansion.block.dynamo.TileDynamoMagmatic;
import cofh.thermal.expansion.block.dynamo.TileDynamoSteam;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoMagmatic;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoSteam;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoMagmatic;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoSteam;

import static cofh.thermal.core.block.AbstractTileType.registerDynamo;

public class DynamosTE {

	private DynamosTE() {

	}

	public static final AbstractTileType STEAM = registerDynamo("steam", 7, TileDynamoSteam.class, ContainerDynamoSteam.class, GuiDynamoSteam.class);
	public static final AbstractTileType MAGMATIC = registerDynamo("magmatic", 14, TileDynamoMagmatic.class, ContainerDynamoMagmatic.class, GuiDynamoMagmatic.class);
	// public static final Dynamo COMPRESSION = new Dynamo("compression", TileDynamoCompression.class, ContainerDynamoCompression.class, GuiDynamoCompression.class);

}
