package cofh.thermal.expansion.block.dynamo;

import cofh.lib.inventory.ItemStorageCoFH;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.expansion.init.DynamosTE;
import cofh.thermal.expansion.util.managers.dynamo.StirlingFuelManager;

import static cofh.lib.util.StorageGroup.INPUT;

public class TileDynamoStirling extends TileDynamo {

	protected ItemStorageCoFH fuelSlot = new ItemStorageCoFH(StirlingFuelManager.instance()::validFuel);

	public TileDynamoStirling() {

		super(DynamosTE.STIRLING);

		inventory.addSlot(fuelSlot, INPUT);
	}

	// region PROCESS
	protected boolean canProcessStart() {

		return StirlingFuelManager.instance().getEnergy(fuelSlot.getItemStack()) > 0;
	}

	protected void processStart() {

		fuel += StirlingFuelManager.instance().getEnergy(fuelSlot.getItemStack());
		fuelSlot.consume();
	}
	// endregion

}
