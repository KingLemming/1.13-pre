package cofh.thermal.expansion.util.managers.machine;

import cofh.thermal.core.util.managers.SimpleItemRecipeManager;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.util.recipes.ExtruderRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static cofh.thermal.core.ThermalSeries.config;

public class ExtruderRecipeManager extends SimpleItemRecipeManager {

	private static final ExtruderRecipeManager INSTANCE = new ExtruderRecipeManager();
	protected static final int DEFAULT_ENERGY = 800;

	public static ExtruderRecipeManager instance() {

		return INSTANCE;
	}

	private ExtruderRecipeManager() {

		super(DEFAULT_ENERGY, 1, 0);
	}

	public IMachineRecipe addRecipe(int energy, ItemStack stone, int lava, int water) {

		if (stone.isEmpty() || energy <= 0 || lava < 0 || water < 0 || validRecipe(stone)) {
			return null;
		}
		energy = (energy * scaleFactor) / 100;

		ExtruderRecipe recipe = new ExtruderRecipe(stone, new FluidStack(FluidRegistry.LAVA, lava), new FluidStack(FluidRegistry.WATER, water), energy);
		defaultMap.put(defaultInput(stone), recipe);
		return recipe;
	}

	// region IRecipeManager
	@Override
	public void config() {

		String category = "Machines.Extruder";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}
	// endregion
}
