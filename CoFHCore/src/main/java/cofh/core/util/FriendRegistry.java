package cofh.core.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static cofh.core.util.CoreUtils.configDir;

// TODO: Fix
public class FriendRegistry {

	public static void initialize() {

		friendConfig = new Configuration(new File(configDir, "/cofh/core/friends.cfg"));
		friendConfig.load();
	}

	private static Configuration friendConfig;
	private static List<String> clientPlayerFriends = new LinkedList<>();

	public synchronized static boolean addFriend(GameProfile owner, String friendName) {

		if (owner == null || friendName == null) {
			return false;
		}
		friendConfig.get(owner.getId().toString(), friendName.toLowerCase(Locale.US), 1);
		friendConfig.save();
		return true;
	}

	public synchronized static boolean removeFriend(GameProfile owner, String friendName) {

		if (owner == null || friendName == null) {
			return false;
		}
		String id = owner.getId().toString();
		friendName = friendName.toLowerCase(Locale.US);
		if (friendConfig.hasCategory(id)) {
			if (friendConfig.getCategory(id).containsKey(friendName)) {
				friendConfig.getCategory(id).remove(friendName);
				friendConfig.save();
				return true;
			}
		}
		return false;
	}

	public static boolean playerHasAccess(GameProfile owner, EntityPlayer player) {

		if (owner == null || player == null) {
			return false;
		}
		String ownerID = owner.getId().toString();
		String playerID = player.getGameProfile().getId().toString();
		return (friendConfig.hasCategory(ownerID) && friendConfig.getCategory(ownerID).containsKey(playerID.toLowerCase(Locale.US)));
	}

	//	public synchronized static void sendFriendsToPlayer(EntityPlayerMP thePlayer) {
	//
	//		PacketSocial aPacket = new PacketSocial();
	//		aPacket.addByte(PacketTypes.FRIEND_LIST.ordinal());
	//		String id = thePlayer.getGameProfile().getId().toString();
	//		aPacket.addInt(friendConfig.getCategory(id).keySet().size());
	//		for (String theName : friendConfig.getCategory(id).keySet()) {
	//			aPacket.addString(theName);
	//		}
	//		PacketHandler.sendTo(aPacket, thePlayer);
	//	}

}
