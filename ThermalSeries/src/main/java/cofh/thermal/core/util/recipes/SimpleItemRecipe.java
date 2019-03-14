package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE_LOCKED;

/**
 * Basic recipe class - single item key'd.
 */
public class SimpleItemRecipe extends AbstractRecipe {

	// region SINGLE ITEM OUTPUT
	public SimpleItemRecipe(int energy, ItemStack input, ItemStack output, float chance) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.add(output);
		this.outputItemChances.add(chance);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public SimpleItemRecipe(int energy, ItemStack input, List<ItemStack> output, @Nullable List<Float> chance) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.addAll(output);

		if (chance != null) {
			this.outputItemChances.addAll(chance);
		}
		if (this.outputItemChances.size() < this.outputItems.size()) {
			for (int i = this.outputItemChances.size(); i < this.outputItems.size(); i++) {
				this.outputItemChances.add(BASE_CHANCE_LOCKED);
			}
		}
	}
	// endregion

	// region SINGLE FLUID OUTPUT
	public SimpleItemRecipe(int energy, ItemStack input, FluidStack output) {

		super(energy);
		this.inputItems.add(input);
		this.outputFluids.add(output);
	}
	// endregion

	// region ITEM + FLUID OUTPUT
	public SimpleItemRecipe(int energy, ItemStack input, List<ItemStack> output, @Nullable List<Float> chance, @Nullable List<FluidStack> fluids) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.addAll(output);

		if (chance != null) {
			this.outputItemChances.addAll(chance);
		}
		if (this.outputItemChances.size() < this.outputItems.size()) {
			for (int i = this.outputItemChances.size(); i < this.outputItems.size(); i++) {
				this.outputItemChances.add(BASE_CHANCE_LOCKED);
			}
		}
		if (fluids != null) {
			this.outputFluids.addAll(fluids);
		}
	}
	// endregion

	// region IMachineRecipe
	@Override
	public List<Integer> getInputItemCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return Collections.singletonList(this.inputItems.get(0).getCount());
	}
	// endregion
}
