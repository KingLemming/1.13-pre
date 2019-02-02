package cofh.lib.item;

import net.minecraft.item.ItemStack;

/**
 * Implement this interface on Item classes that have Filtering capability.
 *
 * @author King Lemming
 */
public interface IFilterContainerItem {

	/**
	 * Get the size of this inventory of this container item.
	 */
	int getSizeFilter(ItemStack container);

}
