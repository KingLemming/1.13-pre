package cofh.thermal.core.proxy;

import cofh.lib.util.helpers.ItemHelper;
import cofh.thermal.core.item.tome.ItemTomeExperience;
import cofh.thermal.core.item.tome.ItemTomeLexicon;
import cofh.thermal.core.util.managers.LexiconManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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

	// region Forge Lexicon
	@SubscribeEvent (priority = EventPriority.HIGHEST)
	public void handleEntityItemPickupEvent(EntityItemPickupEvent event) {

		EntityItem item = event.getItem();
		ItemStack stack = item.getItem();
		if (stack.isEmpty() || !LexiconManager.validOre(stack)) {
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		NBTTagCompound tag = player.getEntityData(); // Cannot be null
		if (player.world.getTotalWorldTime() - tag.getLong(ItemTomeLexicon.LEXICON_TIMER) > 20) {
			return;
		}
		ItemStack lexiconStack = LexiconManager.getPreferredStack(player, stack);
		if (ItemHelper.itemsIdentical(stack, lexiconStack)) {
			return;
		}
		item.setItem(lexiconStack.copy());
		event.setCanceled(true);
		item.onCollideWithPlayer(player);
	}

	@SubscribeEvent (priority = EventPriority.NORMAL)
	public void handlePlayerCloneEvent(PlayerEvent.Clone event) {

		NBTTagCompound newTag = event.getEntityPlayer().getEntityData();
		NBTTagCompound oldTag = event.getOriginal().getEntityData();
		if (oldTag.hasKey(LexiconManager.LEXICON_DATA)) {
			newTag.setTag(LexiconManager.LEXICON_DATA, oldTag.getCompoundTag(LexiconManager.LEXICON_DATA));
		}
	}
	// endregion

	// region Tome of Knowledge
	@SubscribeEvent (priority = EventPriority.HIGH)
	public void handlePlayerPickupXpEvent(PlayerPickupXpEvent event) {

		EntityPlayer player = event.getEntityPlayer();
		NBTTagCompound tag = player.getEntityData(); // Cannot be null
		if (player.world.getTotalWorldTime() - tag.getLong(ItemTomeExperience.EXPERIENCE_TIMER) > 20) {
			return;
		}
		InventoryPlayer inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.getItem() instanceof ItemTomeExperience && ItemTomeExperience.onXPPickup(event, stack)) {
				event.setCanceled(true);
				return;
			}
		}
	}
	// endregion
}
