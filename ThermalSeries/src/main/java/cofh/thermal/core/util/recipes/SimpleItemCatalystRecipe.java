package cofh.thermal.core.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE;
import static cofh.lib.util.Constants.BASE_CHANCE_LOCKED;

/**
 * Abstract catalyst-supporting recipe class - single item key'd, catalyst as 2nd input.
 */
public abstract class SimpleItemCatalystRecipe extends AbstractRecipe {

	// region SINGLE ITEM OUTPUT
	public SimpleItemCatalystRecipe(int energy, ItemStack input, ItemStack output, float chance) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.add(output);
		this.outputItemChances.add(chance);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public SimpleItemCatalystRecipe(int energy, ItemStack input, List<ItemStack> output, @Nullable List<Float> chance) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.addAll(output);

		if (chance != null) {
			this.outputItemChances.addAll(chance);
		}
		if (this.outputItemChances.size() < this.outputItems.size()) {
			for (int i = this.outputItemChances.size(); i < this.outputItems.size(); i++) {
				this.outputItemChances.add(BASE_CHANCE);
			}
		}
	}
	// endregion

	public abstract IRecipeCatalyst getCatalyst(ItemStack input);

	public abstract ComparableItemStackValidated catalystInput(ItemStack input);

	// region IMachineRecipe
	@Override
	public List<Float> getOutputItemChances(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		ArrayList<Float> modifiedChances = new ArrayList<>(outputItemChances);

		if (inputSlots.size() > 1) {
			ItemStack catalystStack = inputSlots.get(1).getItemStack();
			IRecipeCatalyst catalyst = getCatalyst(catalystStack);
			if (catalyst != null) {
				modifiedChances.set(0, Math.max(modifiedChances.get(0) * catalyst.getPrimaryMod(), catalyst.getMinChance()));
				for (int i = 1; i < modifiedChances.size(); i++) {
					modifiedChances.set(i, Math.max(modifiedChances.get(1) * catalyst.getSecondaryMod(), catalyst.getMinChance()));
				}
			}
		}
		for (int i = 0; i < modifiedChances.size(); i++) {
			if (modifiedChances.get(i) < 0.0F) {
				modifiedChances.set(i, modifiedChances.get(i) * -BASE_CHANCE_LOCKED);
			}
		}
		return modifiedChances;
	}

	@Override
	public List<Integer> getInputItemCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		ArrayList<Integer> ret = new ArrayList<>();
		ret.add(this.inputItems.get(0).getCount());

		if (inputSlots.size() > 1) {
			ItemStack catalystStack = inputSlots.get(1).getItemStack();
			IRecipeCatalyst catalyst = getCatalyst(catalystStack);
			if (catalyst != null && MathHelper.RANDOM.nextFloat() < catalyst.getUseChance()) {
				ret.add(1);
			}
		}
		return ret;
	}

	@Override
	public List<Integer> getInputFluidCounts(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		return inputFluids.isEmpty() ? Collections.emptyList() : Collections.singletonList(this.inputFluids.get(0).amount);
	}

	@Override
	public int getEnergy(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputSlots.size() > 1) {
			ItemStack catalystStack = inputSlots.get(1).getItemStack();
			IRecipeCatalyst catalyst = getCatalyst(catalystStack);
			return catalyst == null ? energy : (int) (energy * catalyst.getEnergyMod());
		}
		return energy;
	}
	// endregion
}
