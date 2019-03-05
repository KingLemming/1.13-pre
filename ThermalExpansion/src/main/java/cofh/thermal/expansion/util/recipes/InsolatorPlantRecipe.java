package cofh.thermal.expansion.util.recipes;

import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.thermal.core.util.recipes.IRecipeCatalyst;
import cofh.thermal.core.util.recipes.SimpleItemCatalystRecipe;
import cofh.thermal.expansion.util.managers.machine.InsolatorRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class InsolatorPlantRecipe extends SimpleItemCatalystRecipe {

	// region SINGLE ITEM OUTPUT
	public InsolatorPlantRecipe(ItemStack input, ItemStack output, float chance, int energy) {

		super(input, output, chance, energy);
		this.inputFluids.add(InsolatorRecipeManager.defaultPlantFluid);
	}

	public InsolatorPlantRecipe(ItemStack input, FluidStack inputFluid, ItemStack output, float chance, int energy, int water) {

		super(input, output, chance, energy);
		this.inputFluids.add(water > 0 ? new FluidStack(FluidRegistry.WATER, water) : InsolatorRecipeManager.defaultPlantFluid);
	}
	// endregion

	// region MULTIPLE ITEM OUTPUT
	public InsolatorPlantRecipe(ItemStack input, List<ItemStack> output, @Nullable List<Float> chance, int energy) {

		super(input, output, chance, energy);
		this.inputFluids.add(InsolatorRecipeManager.defaultPlantFluid);
	}

	public InsolatorPlantRecipe(ItemStack input, List<ItemStack> output, @Nullable List<Float> chance, int energy, int water) {

		super(input, output, chance, energy);
		this.inputFluids.add(water > 0 ? new FluidStack(FluidRegistry.WATER, water) : InsolatorRecipeManager.defaultPlantFluid);
	}
	// endregion

	// HELPERS
	public IRecipeCatalyst getCatalyst(ItemStack input) {

		return InsolatorRecipeManager.instance().getCatalyst(input);
	}

	public ComparableItemStackValidated catalystInput(ItemStack input) {

		return InsolatorRecipeManager.instance().catalystInput(input);
	}
	// endregion
}
