package cofh.thermal.core.util.managers;

import cofh.lib.util.comparison.ComparableItemStackValidated;
import cofh.lib.util.comparison.OreValidator;
import net.minecraft.item.ItemStack;

public abstract class AbstractManager implements IManager {

	public static int DEFAULT_ENERGY_MIN = 1000;
	public static int DEFAULT_ENERGY_MAX = 10000000;

	public static final int DEFAULT_SCALE = 100;
	public static final int DEFAULT_SCALE_MIN = 10;
	public static final int DEFAULT_SCALE_MAX = 10000;

	protected OreValidator defaultValidator = new OreValidator();
	protected OreValidator customValidator = new OreValidator();

	protected int defaultEnergy;
	protected int scaleFactor = 100;

	protected AbstractManager(int defaultEnergy) {

		this.defaultEnergy = defaultEnergy;
	}

	protected AbstractManager setDefaultEnergy(int defaultEnergy) {

		if (defaultEnergy > 0) {
			this.defaultEnergy = defaultEnergy;
		}
		return this;
	}

	protected AbstractManager setScaleFactor(int scaleFactor) {

		if (scaleFactor > 0) {
			this.scaleFactor = scaleFactor;
		}
		return this;
	}

	public int getDefaultEnergy() {

		return defaultEnergy;
	}

	public int getDefaultScale() {

		return scaleFactor;
	}

	// region HELPERS
	public ComparableItemStackValidated defaultInput(ItemStack stack) {

		return new ComparableItemStackValidated(stack, defaultValidator);
	}

	public ComparableItemStackValidated customInput(ItemStack stack) {

		return new ComparableItemStackValidated(stack, customValidator);
	}

	public ComparableItemStackValidated convertInput(ItemStack stack) {

		if (hasCustomOreID(stack)) {
			return customInput(stack);
		}
		return defaultInput(stack);
	}

	public boolean hasCustomOreID(ItemStack stack) {

		return customInput(stack).hasOreID();
	}
	// endregion

	// region IManager
	@Override
	public void initialize() {

	}
	// endregion
}
