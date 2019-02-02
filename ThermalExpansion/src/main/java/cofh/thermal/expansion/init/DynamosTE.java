package cofh.thermal.expansion.init;

import cofh.thermal.core.block.dynamo.Dynamo;
import cofh.thermal.expansion.block.dynamo.TileDynamoMagmatic;
import cofh.thermal.expansion.block.dynamo.TileDynamoSteam;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoMagmatic;
import cofh.thermal.expansion.gui.client.dynamo.GuiDynamoSteam;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoMagmatic;
import cofh.thermal.expansion.gui.container.dynamo.ContainerDynamoSteam;

public class DynamosTE {

	private DynamosTE() {

	}

	public static final Dynamo STEAM = new Dynamo("steam", TileDynamoSteam.class, ContainerDynamoSteam.class, GuiDynamoSteam.class);
	public static final Dynamo MAGMATIC = new Dynamo("magmatic", TileDynamoMagmatic.class, ContainerDynamoMagmatic.class, GuiDynamoMagmatic.class);
	// public static final Dynamo COMPRESSION = new Dynamo("compression", TileDynamoCompression.class, ContainerDynamoCompression.class, GuiDynamoCompression.class);

}
