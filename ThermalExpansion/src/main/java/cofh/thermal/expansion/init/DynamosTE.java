package cofh.thermal.expansion.init;

import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.expansion.block.dynamo.TileDynamoCompression;
import cofh.thermal.expansion.block.dynamo.TileDynamoMagmatic;
import cofh.thermal.expansion.block.dynamo.TileDynamoStirling;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoCompression;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoMagmatic;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoStirling;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoCompression;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoMagmatic;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoStirling;

import static cofh.thermal.core.block.AbstractTileType.registerDynamo;

public class DynamosTE {

	private DynamosTE() {

	}

	public static final AbstractTileType STIRLING = registerDynamo("stirling", 7, TileDynamoStirling.class, ContainerDynamoStirling.class, GuiDynamoStirling.class);
	public static final AbstractTileType COMPRESSION = registerDynamo("compression", 7, TileDynamoCompression.class, ContainerDynamoCompression.class, GuiDynamoCompression.class);
	public static final AbstractTileType MAGMATIC = registerDynamo("magmatic", 12, TileDynamoMagmatic.class, ContainerDynamoMagmatic.class, GuiDynamoMagmatic.class);

}
