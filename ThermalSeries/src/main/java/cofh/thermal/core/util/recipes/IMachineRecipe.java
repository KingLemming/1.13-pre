package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IMachineRecipe {

	List<ItemStack> getInputItems();

	List<FluidStack> getInputFluids();

	List<ItemStack> getOutputItems(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	List<FluidStack> getOutputFluids(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	List<Float> getOutputItemChances(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	List<Integer> getInputItemCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	List<Integer> getInputFluidCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

	int getEnergy(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks);

}
