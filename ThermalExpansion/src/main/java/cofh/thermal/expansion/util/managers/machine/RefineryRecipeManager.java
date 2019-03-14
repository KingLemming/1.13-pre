package cofh.thermal.expansion.util.managers.machine;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.fluid.SimpleFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.util.managers.AbstractManager;
import cofh.thermal.core.util.managers.IRecipeManager;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.util.recipes.RefineryRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cofh.thermal.core.ThermalSeries.config;

public class RefineryRecipeManager extends AbstractManager implements IRecipeManager {

	protected Map<Integer, IMachineRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

	private static final RefineryRecipeManager INSTANCE = new RefineryRecipeManager();
	protected static final int DEFAULT_ENERGY = 8000;
	protected static boolean defaultPotionRecipes = true;

	public static RefineryRecipeManager instance() {

		return INSTANCE;
	}

	private RefineryRecipeManager() {

		super(DEFAULT_ENERGY);
	}

	public boolean validRecipe(FluidStack fluid) {

		return validRecipe(Collections.emptyList(), Collections.singletonList(new SimpleFluidStackHolder(fluid)));
	}

	public IMachineRecipe removeRecipe(FluidStack fluid) {

		return recipeMap.remove(FluidHelper.fluidHashcode(fluid));
	}

	public IMachineRecipe addRecipe(int energy, FluidStack inputFluid, FluidStack outputFluid) {

		if (inputFluid == null || outputFluid == null || energy <= 0 || validRecipe(inputFluid)) {
			return null;
		}
		IMachineRecipe recipe = new RefineryRecipe(energy, inputFluid, outputFluid);
		recipeMap.put(FluidHelper.fluidHashcode(inputFluid), recipe);
		return recipe;
	}

	public IMachineRecipe addRecipe(int energy, FluidStack inputFluid, FluidStack outputFluid, ItemStack outputItem) {

		if (inputFluid == null || outputFluid == null || outputItem.isEmpty() || energy <= 0 || validRecipe(inputFluid)) {
			return null;
		}
		IMachineRecipe recipe = new RefineryRecipe(energy, inputFluid, outputFluid, outputItem);
		recipeMap.put(FluidHelper.fluidHashcode(inputFluid), recipe);
		return recipe;
	}

	public IMachineRecipe addRecipe(int energy, FluidStack inputFluid, List<FluidStack> outputFluids) {

		if (inputFluid == null || outputFluids.isEmpty() || energy <= 0 || validRecipe(inputFluid)) {
			return null;
		}
		for (FluidStack stack : outputFluids) {
			if (stack == null) {
				return null;
			}
		}
		IMachineRecipe recipe = new RefineryRecipe(energy, inputFluid, outputFluids);
		recipeMap.put(FluidHelper.fluidHashcode(inputFluid), recipe);
		return recipe;
	}

	public IMachineRecipe addRecipe(int energy, FluidStack inputFluid, List<FluidStack> outputFluids, ItemStack outputItem) {

		if (inputFluid == null || outputFluids.isEmpty() || outputItem.isEmpty() || energy <= 0 || validRecipe(inputFluid)) {
			return null;
		}
		for (FluidStack stack : outputFluids) {
			if (stack == null) {
				return null;
			}
		}
		IMachineRecipe recipe = new RefineryRecipe(energy, inputFluid, outputFluids, outputItem);
		recipeMap.put(FluidHelper.fluidHashcode(inputFluid), recipe);
		return recipe;
	}

	// region HELPERS
	// TODO: Potion Recipes
	// endregion

	// region IRecipeManager
	@Override
	public IMachineRecipe getRecipe(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputTanks.isEmpty() || inputTanks.get(0).isEmpty()) {
			return null;
		}
		FluidStack inputFluid = inputTanks.get(0).getFluidStack();
		return recipeMap.get(FluidHelper.fluidHashcode(inputFluid));
	}

	@Override
	public List<IMachineRecipe> getRecipeList() {

		return new ArrayList<>(recipeMap.values());
	}
	// endregion

	// region IManager
	@Override
	public void config() {

		String category = "Machines.Refinery";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		comment = "If TRUE, default Potion processing recipes will be created based on registered Potions.";
		defaultPotionRecipes = config.getBoolean("Default Potion Recipes", category, defaultPotionRecipes, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}

	@Override
	public void initialize() {

		if (defaultPotionRecipes) {

		}

		// TODO: Remove
		addRecipe(defaultEnergy, new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME), new ItemStack(Items.FLINT));
	}

	@Override
	public void refresh() {

	}
	// endregion
}
