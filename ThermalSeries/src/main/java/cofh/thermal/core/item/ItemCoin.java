package cofh.thermal.core.item;

import cofh.core.item.ItemCoFH;

import static cofh.lib.util.Constants.GROUP_COINS;

public class ItemCoin extends ItemCoFH {

	public ItemCoin() {

		this(GROUP_COINS);
	}

	public ItemCoin(String group) {

		super(group);
	}

}
