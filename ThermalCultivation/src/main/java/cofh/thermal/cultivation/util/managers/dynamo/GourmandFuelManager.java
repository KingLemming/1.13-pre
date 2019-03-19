package cofh.thermal.cultivation.util.managers.dynamo;

import cofh.thermal.core.util.managers.SimpleItemFuelManager;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.ThermalSeries.config;

public class GourmandFuelManager extends SimpleItemFuelManager {

	private static final GourmandFuelManager INSTANCE = new GourmandFuelManager();
	protected static int DEFAULT_ENERGY = 1000;

	public static GourmandFuelManager instance() {

		return INSTANCE;
	}

	private GourmandFuelManager() {

		super(DEFAULT_ENERGY);
	}

	// region HELPERS
	public int getEnergy(ItemStack stack) {

		IDynamoFuel fuel = getFuel(stack);
		return fuel != null ? fuel.getEnergy() : getEnergyFood(stack);
	}

	public static int getEnergyFood(ItemStack stack) {

		if (stack.isEmpty()) {
			return 0;
		}
		if (stack.getItem().hasContainerItem(stack)) {
			return 0;
		}
		int energy = 0;

		if (stack.getItem() instanceof ItemFood) {
			ItemFood food = (ItemFood) stack.getItem();
			int foodEnergy = food.getHealAmount(stack) * DEFAULT_ENERGY;
			int satEnergy = (int) (food.getSaturationModifier(stack) * foodEnergy * 2);
			energy = foodEnergy + satEnergy;
		}
		return energy >= MIN_ENERGY ? energy : 0;
	}
	// endregion

	// region IManager
	@Override
	public void config() {

		String category = "Dynamos.Gourmand";
		String comment;

		comment = "Adjust this value to change the default energy value for this dynamo's fuels.";
		int defaultEnergy = config.getInt("Default Energy", category, DEFAULT_ENERGY, DEFAULT_ENERGY_MIN, DEFAULT_ENERGY_MAX, comment);

		comment = "Adjust this value to change the relative energy production of all of this dynamo's fuels. Scale is in percentage.";
		int scaleFactor = config.getInt("Energy Factor", category, DEFAULT_SCALE, DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, comment);

		setDefaultEnergy(defaultEnergy);
		setScaleFactor(scaleFactor);
	}
	// endregion
}
