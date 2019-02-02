package cofh.thermal.expansion.block.dynamo;

import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.expansion.init.DynamosTE;
import cofh.thermal.expansion.util.managers.dynamo.SteamFuelManager;

import static cofh.lib.util.StorageGroup.INPUT;

public class TileDynamoSteam extends TileDynamo {

	public TileDynamoSteam() {

		super(DynamosTE.STEAM);

		inventory.addSlot(SteamFuelManager.instance()::validFuel, INPUT);
	}

}
