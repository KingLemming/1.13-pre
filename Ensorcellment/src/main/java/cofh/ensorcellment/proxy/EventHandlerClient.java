package cofh.ensorcellment.proxy;

import cofh.ensorcellment.enchantment.shield.EnchantmentPhalanx;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static cofh.lib.util.modhelpers.EnsorcellmentHelper.PHALANX;

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

	public static boolean hadPhalanx;
	public static double modPhalanx;
	public static long timePhalanx;

	@SubscribeEvent
	public void handleFOVUpdateEvent(FOVUpdateEvent event) {

		EntityPlayer entity = event.getEntity();
		ItemStack stack = event.getEntity().getActiveItemStack();

		if (stack.getItem().isShield(stack, entity)) {
			int encPhalanx = EnchantmentHelper.getEnchantmentLevel(PHALANX, stack);
			if (encPhalanx > 0) {
				modPhalanx = encPhalanx * EnchantmentPhalanx.SPEED / 2D;
				hadPhalanx = true;
				timePhalanx = entity.world.getTotalWorldTime();
			}
			event.setNewfov((float) MathHelper.clamp(event.getFov() - modPhalanx, 1.0D, 2.5D));
		} else if (hadPhalanx) {
			if (entity.world.getTotalWorldTime() - 20 > timePhalanx) {
				hadPhalanx = false;
				modPhalanx = 0;
			}
			event.setNewfov((float) MathHelper.clamp(event.getFov() - modPhalanx, 1.0D, 2.5D));
		}
	}

}
