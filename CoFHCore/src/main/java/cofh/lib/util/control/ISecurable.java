package cofh.lib.util.control;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

public interface ISecurable {

	boolean canAccess(EntityPlayer player);

	void setAccess(AccessMode access);

	void setOwner(GameProfile profile);

	AccessMode getAccess();

	GameProfile getOwner();

	default String getOwnerName() {

		return getOwner().getName();
	}

	/**
	 * This returns whether or not security functionality is enabled at all.
	 */
	default boolean isSecurable() {

		return true;
	}

	// region ACCESS MODE
	enum AccessMode {
		PUBLIC, PRIVATE, FRIENDS, TEAM;

		public static final AccessMode[] VALUES = values();

		public boolean isPublic() {

			return this == PUBLIC;
		}

		public boolean isPrivate() {

			return this == PRIVATE;
		}

		public boolean isTeamOnly() {

			return this == TEAM;
		}

		public boolean isFriendsOnly() {

			return this == FRIENDS;
		}
	}
	// endregion
}
