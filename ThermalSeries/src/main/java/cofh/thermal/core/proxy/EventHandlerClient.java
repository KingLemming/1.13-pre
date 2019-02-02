package cofh.thermal.core.proxy;

import cofh.thermal.core.init.TexturesTSeries;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerClient {

	private static final EventHandlerClient INSTANCE = new EventHandlerClient();
	private static boolean registered = false;

	public static void register() {

		if (registered) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registered = true;
	}

	@SubscribeEvent
	public void handleTextureStitchPreEvent(TextureStitchEvent.Pre event) {

		TexturesTSeries.registerTextures(event.getMap());
	}

}
