package cofh.thermal.expansion.block.dynamo;

import cofh.lib.inventory.ItemStorageCoFH;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.expansion.init.DynamosTE;
import cofh.thermal.expansion.util.managers.dynamo.LapidaryFuelManager;

import static cofh.lib.util.StorageGroup.INPUT;

public class TileDynamoLapidary extends TileDynamo {

	protected ItemStorageCoFH fuelSlot = new ItemStorageCoFH(LapidaryFuelManager.instance()::validFuel);

	public TileDynamoLapidary() {

		super(DynamosTE.LAPIDARY);

		inventory.addSlot(fuelSlot, INPUT);
	}

	// region PROCESS
	protected boolean canProcessStart() {

		return LapidaryFuelManager.instance().getEnergy(fuelSlot.getItemStack()) > 0;
	}

	protected void processStart() {

		fuel += LapidaryFuelManager.instance().getEnergy(fuelSlot.getItemStack());
		fuelSlot.consume();
	}
	// endregion

}
