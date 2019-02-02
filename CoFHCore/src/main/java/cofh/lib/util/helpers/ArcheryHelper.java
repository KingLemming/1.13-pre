package cofh.lib.util.helpers;

import cofh.lib.item.IToolBow;
import cofh.lib.item.IToolQuiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ArcheryHelper {

	private ArcheryHelper() {

	}

	public static boolean isSimpleArrow(ItemStack stack) {

		return stack.getItem().equals(Items.ARROW);
	}

	public static boolean isArrow(ItemStack stack) {

		return stack.getItem() instanceof ItemArrow;
	}

	public static boolean isQuiver(ItemStack stack) {

		return stack.getItem() instanceof IToolQuiver;
	}

	public static EntityArrow createArrow(World world, ItemStack arrowStack, ItemStack bowStack, boolean customArrow, EntityPlayer player) {

		if (customArrow) {
			return ((IToolBow) bowStack.getItem()).createEntityArrow(world, bowStack, player);
		}
		if (isArrow(arrowStack)) {
			return ((ItemArrow) arrowStack.getItem()).createArrow(world, arrowStack, player);
		}
		if (isQuiver(arrowStack)) {
			return ((IToolQuiver) arrowStack.getItem()).createEntityArrow(world, arrowStack, player);
		}
		return ((ItemArrow) Items.ARROW).createArrow(world, arrowStack, player);
	}

	public static ItemStack findAmmo(EntityPlayer player) {

		ItemStack offHand = player.getHeldItemOffhand();
		ItemStack mainHand = player.getHeldItemMainhand();

		if (isQuiver(offHand) && !((IToolQuiver) offHand.getItem()).isEmpty(offHand, player) || isArrow(offHand)) {
			return offHand;
		} else if (isQuiver(mainHand) && !((IToolQuiver) mainHand.getItem()).isEmpty(mainHand, player) || isArrow(mainHand)) {
			return mainHand;
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (isQuiver(stack) && !((IToolQuiver) stack.getItem()).isEmpty(stack, player) || isArrow(stack)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

}
