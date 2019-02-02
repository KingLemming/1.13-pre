package cofh.core.key;

import cofh.core.util.CoreUtils;
import net.minecraft.entity.player.EntityPlayer;

public interface IKeyBinding {

	/**
	 * String identifier for the key.
	 */
	String getID();

	/**
	 * Numerical representation of key.
	 */
	default int getKey() {

		return CoreUtils.getKeyBind(getID());
	}

	/**
	 * If TRUE, packet will be sent to server, and keypress handled there.
	 */
	boolean hasServerSide();

	/**
	 * Callback for client-side conditional validity.
	 */
	boolean keyPressClient();

	/**
	 * Callback for server-side conditional validity.
	 */
	boolean keyPressServer(EntityPlayer player);

}
