package cofh.lib.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IToolShield {

	default void onBlock(ItemStack item, EntityPlayer player, Entity source) {

	}

}
