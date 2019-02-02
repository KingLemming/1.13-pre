package cofh.core.proxy;

import cofh.core.item.tool.ItemShieldCoFH;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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

	@SubscribeEvent (priority = EventPriority.HIGH)
	public void handleLivingAttackEvent(LivingAttackEvent event) {

		Entity entity = event.getEntity();

		if (!(entity instanceof EntityPlayer)) {
			return;
		}
		DamageSource source = event.getSource();

		if (source instanceof EntityDamageSourceIndirect || source.isUnblockable() || source.isProjectile()) {
			return;
		}
		if (source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage()) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		ItemStack stack = player.getActiveItemStack();

		if (!stack.isEmpty() && stack.getItem() instanceof ItemShieldCoFH) {
			((ItemShieldCoFH) stack.getItem()).onBlock(stack, player, source.getTrueSource());
		}
	}

}
