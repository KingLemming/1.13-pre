package cofh.lib.util.control;

import cofh.lib.block.ITileCallback;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

public interface ISecurableTile extends ISecurable, ITileCallback {

	SecurityControlModule securityControl();

	@Override
	default boolean canAccess(EntityPlayer player) {

		return securityControl().canAccess(player);
	}

	@Override
	default void setAccess(AccessMode access) {

		securityControl().setAccess(access);
	}

	@Override
	default void setOwner(GameProfile profile) {

		securityControl().setOwner(profile);
	}

	@Override
	default AccessMode getAccess() {

		return securityControl().getAccess();
	}

	@Override
	default GameProfile getOwner() {

		return securityControl().getOwner();
	}

}
