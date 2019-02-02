package cofh.enstoragement.proxy;

import cofh.enstoragement.gui.container.ContainerSatchel;
import cofh.enstoragement.item.ItemSatchel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

	private static final EventHandler INSTANCE = new EventHandler();
	private static boolean registered = false;

	public static void register() {

		if (registered) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registered = true;
	}

	private EventHandler() {

	}

	@SubscribeEvent
	public void handleEntityItemPickup(EntityItemPickupEvent event) {

		EntityPlayer player = event.getEntityPlayer();
		if (player.openContainer instanceof ContainerSatchel) { // || player.openContainer instanceof ContainerSatchelFilter) {
			return;
		}
		InventoryPlayer inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.getItem() instanceof ItemSatchel && ItemSatchel.onItemPickup(event, stack)) {
				event.setCanceled(true);
				return;
			}
		}
	}

}
