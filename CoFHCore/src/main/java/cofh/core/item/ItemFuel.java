package cofh.core.item;

import net.minecraft.item.ItemStack;

public class ItemFuel extends ItemCoFH {

	protected int burnTime;

	public ItemFuel(int burnTime) {

		super();
		this.burnTime = burnTime < 0 ? -1 : burnTime;
	}

	public ItemFuel(int burnTime, String group) {

		super(group);
		this.burnTime = burnTime < 0 ? -1 : burnTime;
	}

	@Override
	public int getItemBurnTime(ItemStack stack) {

		return burnTime;
	}

}
