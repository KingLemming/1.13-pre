package cofh.thermal.expansion.init;

import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.expansion.block.dynamo.*;
import cofh.thermal.expansion.gui.client.dynamo.*;
import cofh.thermal.expansion.gui.container.dynamo.*;

import static cofh.thermal.core.block.AbstractTileType.registerDynamo;

public class DynamosTE {

	private DynamosTE() {

	}

	public static final AbstractTileType STIRLING = registerDynamo("stirling", 7, TileDynamoStirling.class, ContainerDynamoStirling.class, GuiDynamoStirling.class);
	public static final AbstractTileType COMPRESSION = registerDynamo("compression", 7, TileDynamoCompression.class, ContainerDynamoCompression.class, GuiDynamoCompression.class);
	public static final AbstractTileType MAGMATIC = registerDynamo("magmatic", 12, TileDynamoMagmatic.class, ContainerDynamoMagmatic.class, GuiDynamoMagmatic.class);

	public static final AbstractTileType NUMISMATIC = registerDynamo("numismatic", 7, TileDynamoNumismatic.class, ContainerDynamoNumismatic.class, GuiDynamoNumismatic.class);
	public static final AbstractTileType LAPIDARY = registerDynamo("lapidary", 7, TileDynamoLapidary.class, ContainerDynamoLapidary.class, GuiDynamoLapidary.class);

}
