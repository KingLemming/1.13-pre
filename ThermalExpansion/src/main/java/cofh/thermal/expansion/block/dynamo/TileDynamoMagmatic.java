package cofh.thermal.expansion.block.dynamo;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.expansion.init.DynamosTE;
import cofh.thermal.expansion.util.managers.dynamo.MagmaticFuelManager;

import static cofh.lib.util.Constants.TANK_SMALL;
import static cofh.lib.util.StorageGroup.INPUT;

public class TileDynamoMagmatic extends TileDynamo {

	public TileDynamoMagmatic() {

		super(DynamosTE.MAGMATIC);

		tankInv.addTank(new FluidStorageCoFH(TANK_SMALL, MagmaticFuelManager.instance()::validFuel), INPUT);
		// tankInv.addTank(new FluidStorageCoFH(TANK_SMALL), INPUT); // TODO: Coolant
	}

}
