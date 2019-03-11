package cofh.thermal.expansion.block.dynamo;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.thermal.core.block.dynamo.TileDynamo;
import cofh.thermal.expansion.init.DynamosTE;
import cofh.thermal.expansion.util.managers.dynamo.CompressionFuelManager;
import cofh.thermal.expansion.util.managers.dynamo.MagmaticFuelManager;

import static cofh.lib.util.Constants.TANK_SMALL;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.thermal.core.util.managers.SimpleFluidFuelManager.FLUID_FUEL_AMOUNT;

public class TileDynamoCompression extends TileDynamo {

	protected FluidStorageCoFH fuelTank = new FluidStorageCoFH(TANK_SMALL, CompressionFuelManager.instance()::validFuel);

	public TileDynamoCompression() {

		super(DynamosTE.MAGMATIC);

		tankInv.addTank(fuelTank, INPUT);
		// tankInv.addTank(new FluidStorageCoFH(TANK_SMALL), INPUT); // TODO: Coolant
	}

	// region PROCESS
	protected boolean canProcessStart() {

		curFuel = MagmaticFuelManager.instance().getFuel(getInputSlots(), getInputTanks());
		if (curFuel != null) {
			return fuelTank.getFluidAmount() >= FLUID_FUEL_AMOUNT;
		}
		return false;
	}

	protected void processStart() {

		fuel += curFuel.getEnergy();
		fuelTank.drain(FLUID_FUEL_AMOUNT, true);
	}
	// endregion
}
