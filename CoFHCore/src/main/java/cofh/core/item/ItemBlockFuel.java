package cofh.core.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockFuel extends ItemBlockCoFH {

	protected int burnTime;

	public ItemBlockFuel(Block block, int burnTime) {

		super(block);
		this.burnTime = burnTime < 0 ? -1 : burnTime;
	}

	public ItemBlockFuel(Block block, String group, int burnTime) {

		super(block, group);
		this.burnTime = burnTime < 0 ? -1 : burnTime;
	}

	@Override
	public int getItemBurnTime(ItemStack stack) {

		return burnTime;
	}

}
