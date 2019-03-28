package cofh.thermal.cultivation.block.dynamo;

import cofh.lib.inventory.ItemStorageCoFH;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.cultivation.init.DynamosTC;
import cofh.thermal.cultivation.util.managers.dynamo.GourmandFuelManager;

import static cofh.lib.util.StorageGroup.INPUT;

public class TileDynamoGourmand extends TileDynamo {

	protected ItemStorageCoFH fuelSlot = new ItemStorageCoFH(GourmandFuelManager.instance()::validFuel);

	public TileDynamoGourmand() {

		super(DynamosTC.GOURMAND);

		inventory.addSlot(fuelSlot, INPUT);
	}

	// region PROCESS
	protected boolean canProcessStart() {

		return GourmandFuelManager.instance().getEnergy(fuelSlot.getItemStack()) > 0;
	}

	protected void processStart() {

		fuel += GourmandFuelManager.instance().getEnergy(fuelSlot.getItemStack());
		fuelSlot.consume();
	}
	// endregion

}
