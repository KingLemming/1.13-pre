package cofh.lib.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IEnchantHandlerItem {

	/**
	 * Simple boolean to determine if an enchantment applies to an ItemStack.
	 */
	boolean canEnchant(ItemStack stack, ResourceLocation enchantment);

}
