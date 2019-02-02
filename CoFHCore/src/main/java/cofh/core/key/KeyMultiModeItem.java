package cofh.core.key;

import cofh.core.util.CoreUtils;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.KEY_MULTIMODE;

public class KeyMultiModeItem implements IKeyBinding {

	public static final KeyMultiModeItem INSTANCE = new KeyMultiModeItem();

	@Override
	public String getID() {

		return KEY_MULTIMODE;
	}

	@Override
	public boolean hasServerSide() {

		return true;
	}

	@Override
	public boolean keyPressClient() {

		EntityPlayer player = CoreUtils.getClientPlayer();
		return player != null && ItemHelper.isPlayerHoldingMultiModeItem(player);
	}

	@Override
	public boolean keyPressServer(EntityPlayer player) {

		if (ItemHelper.isPlayerHoldingMultiModeItem(player) && ItemHelper.incrHeldMultiModeItemState(player)) {
			ItemStack heldItem = ItemHelper.getHeldMultiModeStack(player);
			((IMultiModeItem) heldItem.getItem()).onModeChange(player, heldItem);
			return true;
		}
		return false;
	}

}
