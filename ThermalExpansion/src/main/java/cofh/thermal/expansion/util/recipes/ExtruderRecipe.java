package cofh.thermal.expansion.util.recipes;

import cofh.thermal.core.util.recipes.AbstractRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.BASE_CHANCE_LOCKED;

public class ExtruderRecipe extends AbstractRecipe {

	public ExtruderRecipe(int energy, ItemStack stone, FluidStack lava, FluidStack water) {

		super(energy);
		this.inputFluids.add(lava);
		this.inputFluids.add(water);
		this.outputItems.add(stone);
		this.outputItemChances.add(BASE_CHANCE_LOCKED);
	}

}
