package cofh.core.proxy;

import cofh.core.key.KeyHandler;
import cofh.core.key.KeyMultiModeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyCommon {

	// region INITIALIZATION
	public void preInit(FMLPreInitializationEvent event) {

		EventHandler.register();
	}

	public void initialize(FMLInitializationEvent event) {

		registerKeyBinds();
	}

	public void postInit(FMLPostInitializationEvent event) {

	}
	// endregion

	// region REGISTRATION
	public void registerKeyBinds() {

		KeyHandler.addServerKeyBind(KeyMultiModeItem.INSTANCE);
	}
	// endregion

	// region HELPERS
	public void addIndexedChatMessage(ITextComponent chat, int index) {

	}

	public int getKeyBind(String key) {

		return 0;
	}

	public EntityPlayer getClientPlayer() {

		return null;
	}

	public World getClientWorld() {

		return null;
	}

	public IThreadListener getClientListener() {

		// If this is called on the server, expect a crash.
		return null;
	}

	public IThreadListener getServerListener() {

		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}

	public boolean isClient() {

		return false;
	}

	public boolean isOp(EntityPlayer player) {

		String name = player.getName().trim();
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		for (String opName : server.getPlayerList().getOppedPlayerNames()) {
			if (name.equalsIgnoreCase(opName)) {
				return true;
			}
		}
		return false;
	}
	// endregion
}
