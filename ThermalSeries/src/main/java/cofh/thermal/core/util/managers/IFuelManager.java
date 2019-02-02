package cofh.thermal.core.util.managers;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.thermal.core.util.recipes.IDynamoFuel;

import java.util.List;

public interface IFuelManager extends IManager {

	boolean validFuel(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	IDynamoFuel getFuel(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	List<IDynamoFuel> getFuelList();
}
