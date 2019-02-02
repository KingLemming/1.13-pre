package cofh.lib.util.control;

import cofh.core.network.packet.PacketSecurity;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.SecurityHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

import static cofh.lib.util.Constants.*;

public class SecurityControlModule implements ISecurable {

	protected ISecurableTile tile;

	protected GameProfile owner = SecurityHelper.DEFAULT_GAME_PROFILE;
	protected AccessMode access = AccessMode.PUBLIC;

	public SecurityControlModule(ISecurableTile tile) {

		this.tile = tile;
	}

	// region NBT
	public SecurityControlModule readFromNBT(NBTTagCompound nbt) {

		if (nbt.hasKey(TAG_OWNER_UUID)) {
			String uuid = nbt.getString(TAG_OWNER_UUID);
			String name = nbt.getString(TAG_OWNER_NAME);
			owner = new GameProfile(UUID.fromString(uuid), name);
		} else {
			owner = SecurityHelper.DEFAULT_GAME_PROFILE;
		}
		access = isSecurable() ? AccessMode.VALUES[nbt.getByte(TAG_ACCESS)] : AccessMode.PUBLIC;

		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		nbt.setString(TAG_OWNER_UUID, owner.getId().toString());
		nbt.setString(TAG_OWNER_NAME, owner.getName());
		nbt.setByte(TAG_ACCESS, (byte) access.ordinal());

		return nbt;
	}
	// endregion

	// region ISecurable
	@Override
	public boolean canAccess(EntityPlayer player) {

		if (SecurityHelper.isDefaultProfile(owner) || access.isPublic()) {
			return true;
		}
		// TODO: Op Logic
		UUID ownerID = owner.getId();
		UUID otherID = SecurityHelper.getID(player);
		if (ownerID.equals(otherID)) {
			return true;
		}
		//return access.isFriendsOnly() && RegistrySocial.playerHasAccess(name, owner);
		return false;
	}

	@Override
	public void setAccess(AccessMode access) {

		this.access = access;
		if (Utils.isClientWorld(tile.world())) {
			PacketSecurity.sendToServer(this.access);
		}
	}

	@Override
	public void setOwner(GameProfile profile) {

		if (SecurityHelper.isDefaultProfile(owner)) {
			owner = profile;
		}
	}

	@Override
	public AccessMode getAccess() {

		return access;
	}

	@Override
	public GameProfile getOwner() {

		return owner;
	}
	// endregion
}
