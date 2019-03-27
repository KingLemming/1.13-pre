package cofh.lib.util.control;

import cofh.lib.block.ITileCallback;
import com.mojang.authlib.GameProfile;

public interface ISecurableTile extends ISecurable, ITileCallback {

	SecurityControlModule securityControl();

	// region ISecurable
	@Override
	default void setAccess(AccessMode access) {

		securityControl().setAccess(access);
	}

	@Override
	default boolean setOwner(GameProfile profile) {

		return securityControl().setOwner(profile);
	}

	@Override
	default AccessMode getAccess() {

		return securityControl().getAccess();
	}

	@Override
	default GameProfile getOwner() {

		return securityControl().getOwner();
	}

	@Override
	default boolean isSecurable() {

		return securityControl().isSecurable();
	}
	// endregion
}
