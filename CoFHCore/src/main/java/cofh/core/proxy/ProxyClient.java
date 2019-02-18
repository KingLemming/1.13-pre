package cofh.core.proxy;

import cofh.core.key.KeyHandler;
import cofh.core.key.KeyMultiModeItem;
import cofh.core.model.LayeredTemplateModel;
import cofh.lib.event.EventHandlerClientLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import static cofh.lib.util.Constants.KEY_MULTIMODE;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);

		EventHandlerClient.register();
		EventHandlerClientLib.register();
        ModelLoaderRegistry.registerLoader(LayeredTemplateModel.Loader.INSTANCE);
	}

	@Override
	public void initialize(FMLInitializationEvent event) {

		super.initialize(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		super.postInit(event);

		EventHandlerClientLib.registerReload();
	}
	// endregion

	// region REGISTRATION
	@Override
	public void registerKeyBinds() {

		super.registerKeyBinds();

		KeyHandler.addClientKeyBind(KeyMultiModeItem.INSTANCE);
		ClientRegistry.registerKeyBinding(KEYBINDING_MULTIMODE);
	}
	// endregion

	// region HELPERS
	@Override
	public void addIndexedChatMessage(ITextComponent chat, int index) {

		if (chat == null) {
			Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(index);
		} else {
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(chat, index);
		}
	}

	@Override
	public int getKeyBind(String key) {

		if (key.equalsIgnoreCase(KEY_MULTIMODE)) {
			return KEYBINDING_MULTIMODE.getKeyCode();
		}
		//		else if (key.equalsIgnoreCase("cofh.augment")) {
		//			//return KEYBINDING_AUGMENTS.getKeyCode();
		//		}
		return -1;
	}

	@Override
	public EntityPlayer getClientPlayer() {

		return Minecraft.getMinecraft().player;
	}

	@Override
	public World getClientWorld() {

		return Minecraft.getMinecraft().world;
	}

	@Override
	public IThreadListener getClientListener() {

		return Minecraft.getMinecraft();
	}

	@Override
	public IThreadListener getServerListener() {

		return Minecraft.getMinecraft().getIntegratedServer();
	}

	@Override
	public boolean isClient() {

		return true;
	}
	// endregion

	// region REFERENCES
	public static final KeyBinding KEYBINDING_MULTIMODE = new KeyBinding("key.cofh.multimode", Keyboard.KEY_V, "key.cofh.category");
	// endregion
}
