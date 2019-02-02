package cofh.lib.util.comparison;

import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This is an implementation of a ComparableItemStack - where the oreName/Id is constrained to an allowed subset, specified by the validator.
 *
 * @author King Lemming
 */
public class ComparableItemStackValidated extends ComparableItemStack {

	private final OreValidator validator;

	public ComparableItemStackValidated(ItemStack stack) {

		super(stack);
		this.validator = DEFAULT_VALIDATOR;
		this.oreID = getOreID(stack);
		this.oreName = OreDictHelper.getOreName(oreID);
	}

	public ComparableItemStackValidated(ItemStack stack, @Nonnull OreValidator validator) {

		super(stack);
		this.validator = validator;
		this.oreID = getOreID(stack);
		this.oreName = OreDictHelper.getOreName(oreID);
	}

	public int getOreID(ItemStack stack) {

		List<Integer> ids = OreDictHelper.getAllOreIDs(stack);
		if (!ids.isEmpty()) {
			for (Integer id : ids) {
				if (id != -1 && validator.validate(OreDictHelper.getOreName(id))) {
					return id;
				}
			}
		}
		return -1;
	}

	public int getOreID(String oreName) {

		if (!validator.validate(oreName)) {
			return -1;
		}
		return OreDictHelper.getOreID(oreName);
	}

	public boolean hasOreID() {

		return oreID != -1;
	}

}
