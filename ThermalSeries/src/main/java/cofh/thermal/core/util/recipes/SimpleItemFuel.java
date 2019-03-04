package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class SimpleItemFuel extends AbstractFuel {

	public SimpleItemFuel(ItemStack input, int energy) {

		super(energy);
		this.inputItems.add(input);
	}

	// region OPTIMIZATION
	@Override
	public List<Integer> getInputItemCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return Collections.singletonList(this.inputItems.get(0).getCount());
	}
	// endregion
}
