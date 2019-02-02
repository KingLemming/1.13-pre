package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE;

public class SimpleItemRecipe extends AbstractRecipe {

	// region SINGLE ITEM OUTPUT
	public SimpleItemRecipe(ItemStack input, ItemStack output, int energy) {

		this(input, output, -BASE_CHANCE, energy);
	}

	public SimpleItemRecipe(ItemStack input, ItemStack output, float chance, int energy) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.add(output);
		this.outputChances.add(chance);
	}

	public SimpleItemRecipe(ItemStack input, List<ItemStack> output, int energy) {

		this(input, output, null, energy);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public SimpleItemRecipe(ItemStack input, List<ItemStack> output, @Nullable List<Float> chance, int energy) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.addAll(output);

		if (chance != null) {
			this.outputChances.addAll(chance);
		}
		if (this.outputChances.size() < this.outputItems.size()) {
			for (int i = this.outputChances.size(); i < this.outputItems.size(); i++) {
				this.outputChances.add(-BASE_CHANCE);
			}
		}
	}
	// endregion

	// region SINGLE FLUID OUTPUT
	public SimpleItemRecipe(ItemStack input, FluidStack output, int energy) {

		super(energy);
		this.inputItems.add(input);
		this.outputFluids.add(output);
	}
	// endregion

	@Override
	public List<Integer> getInputItemCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return Collections.singletonList(this.inputItems.get(0).getCount());
	}

}
