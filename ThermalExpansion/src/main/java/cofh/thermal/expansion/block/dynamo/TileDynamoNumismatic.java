package cofh.thermal.expansion.block.dynamo;

import cofh.lib.inventory.ItemStorageCoFH;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.expansion.init.DynamosTE;
import cofh.thermal.expansion.util.managers.dynamo.NumismaticFuelManager;

import static cofh.lib.util.StorageGroup.INPUT;

public class TileDynamoNumismatic extends TileDynamo {

	protected ItemStorageCoFH fuelSlot = new ItemStorageCoFH(NumismaticFuelManager.instance()::validFuel);

	public TileDynamoNumismatic() {

		super(DynamosTE.NUMISMATIC);

		inventory.addSlot(fuelSlot, INPUT);
	}

	// region PROCESS
	protected boolean canProcessStart() {

		return NumismaticFuelManager.instance().getEnergy(fuelSlot.getItemStack()) > 0;
	}

	protected void processStart() {

		fuel += NumismaticFuelManager.instance().getEnergy(fuelSlot.getItemStack());
		fuelSlot.consume();
	}
	// endregion

}
