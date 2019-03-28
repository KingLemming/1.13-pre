package cofh.thermal.expansion.util.managers.machine;

import cofh.lib.fluid.IFluidStackHolder;
import cofh.lib.fluid.SimpleFluidStackHolder;
import cofh.lib.inventory.IItemStackHolder;
import cofh.lib.inventory.SimpleItemStackHolder;
import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.init.FluidsTSeries;
import cofh.thermal.core.util.managers.AbstractManager;
import cofh.thermal.core.util.managers.IRecipeManager;
import cofh.thermal.core.util.recipes.IMachineRecipe;
import cofh.thermal.expansion.util.recipes.BrewerRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.Map.Entry;

import static cofh.lib.util.Constants.PREFIX_DUST;
import static cofh.lib.util.Constants.PREFIX_GEM;
import static cofh.thermal.core.ThermalSeries.config;
import static java.util.Arrays.asList;

public class BrewerRecipeManager extends AbstractManager implements IRecipeManager {

	protected Map<List<Integer>, IMachineRecipe> recipeMap = new Object2ObjectOpenHashMap<>();
	protected Set<String> validFluids = new ObjectOpenHashSet<>();
	protected Set<ComparableItemStackValidated> validItems = new ObjectOpenHashSet<>();

	private static final BrewerRecipeManager INSTANCE = new BrewerRecipeManager();
	protected static final int DEFAULT_ENERGY = 4000;
	protected static int defaultPotionAmount = 500;
	protected static boolean defaultPotionRecipes = true;

	public static BrewerRecipeManager instance() {

		return INSTANCE;
	}

	private BrewerRecipeManager() {

		super(DEFAULT_ENERGY);

		defaultValidator.addPrefix(PREFIX_DUST);
		defaultValidator.addPrefix(PREFIX_GEM);
	}

	public boolean validItem(ItemStack item) {

		return validItems.contains(convertInput(item));
	}

	public boolean validFluid(FluidStack fluid) {

		return fluid != null && validFluids.contains(fluid.getFluid().getName());
	}

	public boolean validRecipe(ItemStack item, FluidStack fluid) {

		return validRecipe(Collections.singletonList(new SimpleItemStackHolder(item)), Collections.singletonList(new SimpleFluidStackHolder(fluid)));
	}

	public IMachineRecipe removeRecipe(ItemStack item, FluidStack fluid) {

		return recipeMap.remove(asList(convertInput(item).hashCode(), FluidHelper.fluidHashcode(fluid)));
	}

	public IMachineRecipe addRecipe(int energy, ItemStack inputItem, FluidStack inputFluid, FluidStack outputFluid) {

		if (inputItem.isEmpty() || inputFluid == null || outputFluid == null || energy <= 0 || validRecipe(inputItem, inputFluid)) {
			return null;
		}
		IMachineRecipe recipe = new BrewerRecipe(energy, inputItem, inputFluid, outputFluid);
		recipeMap.put(asList(convertInput(inputItem).hashCode(), FluidHelper.fluidHashcode(inputFluid)), recipe);
		validItems.add(convertInput(inputItem));
		validFluids.add(inputFluid.getFluid().getName());
		return recipe;
	}

	// region HELPERS
	public ComparableItemStackValidated convertInput(ItemStack input) {

		return defaultInput(input);
	}

	public void addDefaultPotionRecipe(PotionType input, ItemStack reagent, PotionType output) {

		addRecipe(defaultEnergy, reagent, FluidsTSeries.getPotion(defaultPotionAmount, input), FluidsTSeries.getPotion(defaultPotionAmount, output));
	}
	// endregion

	// region IRecipeManager
	@Override
	public IMachineRecipe getRecipe(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

		if (inputSlots.isEmpty() || inputSlots.get(0).isEmpty() || inputTanks.isEmpty() || inputTanks.get(0).isEmpty()) {
			return null;
		}
		ItemStack inputItem = inputSlots.get(0).getItemStack();
		FluidStack inputFluid = inputTanks.get(0).getFluidStack();
		return recipeMap.get(asList(convertInput(inputItem).hashCode(), FluidHelper.fluidHashcode(inputFluid)));
	}

	@Override
	public List<IMachineRecipe> getRecipeList() {

		return new ArrayList<>(recipeMap.values());
	}
	// endregion

	// region IManager
	@Override
	public void config() {

		String category = "Machines.Brewer";
		String comment;

		comment = "Adjust this value to change the default energy value for this machine's recipes.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy cost of all of this machine's recipes. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		comment = "Adjust this value to change the default Potion amount for this machine.";
		defaultPotionAmount = config.getInt("Default Potion Amount", category, defaultPotionAmount, 50, Fluid.BUCKET_VOLUME * 4, comment);

		comment = "If TRUE, default Potion processing recipes will be created based on registered Potions.";
		defaultPotionRecipes = config.getBoolean("Default Potion Recipes", category, defaultPotionRecipes, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}

	@Override
	public void initialize() {

		if (defaultPotionRecipes) {
			ItemStack rabbitFoot = new ItemStack(Items.RABBIT_FOOT);
			ItemStack magmaCream = new ItemStack(Items.MAGMA_CREAM);
			ItemStack sugar = new ItemStack(Items.SUGAR);
			ItemStack melon = new ItemStack(Items.SPECKLED_MELON);
			ItemStack spiderEye = new ItemStack(Items.SPIDER_EYE);
			ItemStack ghastTear = new ItemStack(Items.GHAST_TEAR);
			ItemStack blazePowder = new ItemStack(Items.BLAZE_POWDER);
			ItemStack redstone = new ItemStack(Items.REDSTONE);
			ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
			ItemStack fermentedSpiderEye = new ItemStack(Items.FERMENTED_SPIDER_EYE);

			addDefaultPotionRecipe(PotionTypes.WATER, new ItemStack(Items.NETHER_WART), PotionTypes.AWKWARD);

			addDefaultPotionRecipe(PotionTypes.WATER, rabbitFoot, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, magmaCream, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, sugar, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, melon, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, spiderEye, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, ghastTear, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, blazePowder, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, redstone, PotionTypes.MUNDANE);
			addDefaultPotionRecipe(PotionTypes.WATER, glowstone, PotionTypes.THICK);
			addDefaultPotionRecipe(PotionTypes.WATER, fermentedSpiderEye, PotionTypes.WEAKNESS);

			addDefaultPotionRecipe(PotionTypes.AWKWARD, new ItemStack(Items.GOLDEN_CARROT), PotionTypes.NIGHT_VISION);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, rabbitFoot, PotionTypes.LEAPING);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, magmaCream, PotionTypes.FIRE_RESISTANCE);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, sugar, PotionTypes.SWIFTNESS);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, new ItemStack(Items.FISH, 1, 3), PotionTypes.WATER_BREATHING);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, melon, PotionTypes.HEALING);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, spiderEye, PotionTypes.POISON);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, ghastTear, PotionTypes.REGENERATION);
			addDefaultPotionRecipe(PotionTypes.AWKWARD, blazePowder, PotionTypes.STRENGTH);

			addDefaultPotionRecipe(PotionTypes.NIGHT_VISION, fermentedSpiderEye, PotionTypes.INVISIBILITY);
			addDefaultPotionRecipe(PotionTypes.LEAPING, fermentedSpiderEye, PotionTypes.SLOWNESS);
			addDefaultPotionRecipe(PotionTypes.SWIFTNESS, fermentedSpiderEye, PotionTypes.SLOWNESS);
			addDefaultPotionRecipe(PotionTypes.HEALING, fermentedSpiderEye, PotionTypes.HARMING);
			addDefaultPotionRecipe(PotionTypes.POISON, fermentedSpiderEye, PotionTypes.HARMING);

			addDefaultPotionRecipe(PotionTypes.NIGHT_VISION, redstone, PotionTypes.LONG_NIGHT_VISION);
			addDefaultPotionRecipe(PotionTypes.INVISIBILITY, redstone, PotionTypes.LONG_INVISIBILITY);
			addDefaultPotionRecipe(PotionTypes.LEAPING, redstone, PotionTypes.LONG_LEAPING);
			addDefaultPotionRecipe(PotionTypes.FIRE_RESISTANCE, redstone, PotionTypes.LONG_FIRE_RESISTANCE);
			addDefaultPotionRecipe(PotionTypes.SWIFTNESS, redstone, PotionTypes.LONG_SWIFTNESS);
			addDefaultPotionRecipe(PotionTypes.SLOWNESS, redstone, PotionTypes.LONG_SLOWNESS);
			addDefaultPotionRecipe(PotionTypes.WATER_BREATHING, redstone, PotionTypes.LONG_WATER_BREATHING);
			addDefaultPotionRecipe(PotionTypes.POISON, redstone, PotionTypes.LONG_POISON);
			addDefaultPotionRecipe(PotionTypes.REGENERATION, redstone, PotionTypes.LONG_REGENERATION);
			addDefaultPotionRecipe(PotionTypes.STRENGTH, redstone, PotionTypes.LONG_STRENGTH);
			addDefaultPotionRecipe(PotionTypes.WEAKNESS, redstone, PotionTypes.LONG_WEAKNESS);

			addDefaultPotionRecipe(PotionTypes.LEAPING, glowstone, PotionTypes.STRONG_LEAPING);
			addDefaultPotionRecipe(PotionTypes.SWIFTNESS, glowstone, PotionTypes.STRONG_SWIFTNESS);
			addDefaultPotionRecipe(PotionTypes.HEALING, glowstone, PotionTypes.STRONG_HEALING);
			addDefaultPotionRecipe(PotionTypes.HARMING, glowstone, PotionTypes.STRONG_HARMING);
			addDefaultPotionRecipe(PotionTypes.POISON, glowstone, PotionTypes.STRONG_POISON);
			addDefaultPotionRecipe(PotionTypes.REGENERATION, glowstone, PotionTypes.STRONG_REGENERATION);
			addDefaultPotionRecipe(PotionTypes.STRENGTH, glowstone, PotionTypes.STRONG_STRENGTH);

			addDefaultPotionRecipe(PotionTypes.LONG_NIGHT_VISION, fermentedSpiderEye, PotionTypes.LONG_INVISIBILITY);
			addDefaultPotionRecipe(PotionTypes.LONG_LEAPING, fermentedSpiderEye, PotionTypes.LONG_SLOWNESS);
			addDefaultPotionRecipe(PotionTypes.LONG_SWIFTNESS, fermentedSpiderEye, PotionTypes.LONG_SLOWNESS);
			addDefaultPotionRecipe(PotionTypes.LONG_POISON, fermentedSpiderEye, PotionTypes.HARMING);
			addDefaultPotionRecipe(PotionTypes.STRONG_HEALING, fermentedSpiderEye, PotionTypes.STRONG_HARMING);
			addDefaultPotionRecipe(PotionTypes.STRONG_POISON, fermentedSpiderEye, PotionTypes.STRONG_HARMING);
		}
	}

	@Override
	public void refresh() {

		Map<List<Integer>, IMachineRecipe> tempRecipes = new Object2ObjectOpenHashMap<>(recipeMap.size());
		Set<ComparableItemStackValidated> tempItems = new ObjectOpenHashSet<>();
		IMachineRecipe tempRecipe;

		for (Entry<List<Integer>, IMachineRecipe> entry : recipeMap.entrySet()) {
			tempRecipe = entry.getValue();
			ComparableItemStackValidated input = convertInput(tempRecipe.getInputItems().get(0));
			tempRecipes.put(asList(input.hashCode(), FluidHelper.fluidHashcode(tempRecipe.getInputFluids().get(0))), tempRecipe);
			tempItems.add(input);
		}
		recipeMap.clear();
		recipeMap = tempRecipes;

		validItems.clear();
		validItems = tempItems;
	}
	// endregion
}
