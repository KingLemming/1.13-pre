package cofh.lib.event;

import net.minecraftforge.common.MinecraftForge;

public class EventHandlerEnder {

	private static final EventHandlerEnder INSTANCE = new EventHandlerEnder();
	private static boolean registered = false;

	public static void register() {

		if (registered) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registered = true;
	}

	private EventHandlerEnder() {

	}

	// TODO: Finish
	//	@SubscribeEvent
	//	public void handleEnderTeleportEvent(EnderTeleportEvent event) {
	//
	//		if (event.isCanceled()) {
	//			return;
	//		}
	//		EntityLivingBase entity = event.getEntityLiving();
	//
	//		entity.getActivePotionEffects();
	//		if (entity.getActivePotionMap().containsKey(ANCHORING_EFFECT)) {
	//			event.setCanceled(true);
	//		}
	//	}
}
