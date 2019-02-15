package cofh.thermal.expansion.util.recipes;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.thermal.core.util.recipes.AbstractRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE;

public class PulverizerOreRecipe extends AbstractRecipe {

	// TODO: Fix
	// protected static Map<ComparableItemStackValidated, IRecipeCatalyst>

	// region SINGLE ITEM OUTPUT
	public PulverizerOreRecipe(ItemStack input, ItemStack output, int energy) {

		this(input, output, BASE_CHANCE, energy);
	}

	public PulverizerOreRecipe(ItemStack input, ItemStack output, float chance, int energy) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.add(output);
		this.outputChances.add(chance);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public PulverizerOreRecipe(ItemStack input, List<ItemStack> output, int energy) {

		this(input, output, null, energy);
	}

	public PulverizerOreRecipe(ItemStack input, List<ItemStack> output, @Nullable List<Float> chance, int energy) {

		super(energy);
		this.inputItems.add(input);
		this.outputItems.addAll(output);

		if (chance != null) {
			this.outputChances.addAll(chance);
		}
		if (this.outputChances.size() < this.outputItems.size()) {
			for (int i = this.outputChances.size(); i < this.outputItems.size(); i++) {
				this.outputChances.add(BASE_CHANCE);
			}
		}
	}
	// endregion

	// TODO: Rethink - needs a validator
	public int getSubtype(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputItems.size() > 1) {
			ItemStack catalyst = this.inputItems.get(1);
			if (!catalyst.isEmpty()) {
				return Item.getIdFromItem(catalyst.getItem());
			}
		}
		return 0;
	}

}
