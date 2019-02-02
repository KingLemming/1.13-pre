package cofh.core.util;

import cofh.core.CoFHCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import java.io.File;

public class CoreUtils {

	public static File configDir;

	private CoreUtils() {

	}

	public static void addIndexedChatMessage(ITextComponent chat, int index) {

		CoFHCore.proxy.addIndexedChatMessage(chat, index);
	}

	public static int getKeyBind(String key) {

		return CoFHCore.proxy.getKeyBind(key);
	}

	public static EntityPlayer getClientPlayer() {

		return CoFHCore.proxy.getClientPlayer();
	}

	public static boolean isClient() {

		return CoFHCore.proxy.isClient();
	}

	public static boolean isOp(EntityPlayer player) {

		return CoFHCore.proxy.isOp(player);
	}

}
