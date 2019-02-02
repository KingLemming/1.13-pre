package cofh.thermal.core.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class CreativeTabsTSeries {

	private CreativeTabsTSeries() {

	}

	public static CreativeTabs tabBlocks = new CreativeTabs(ID_THERMAL_SERIES + ".blocks") {

		@Override
		public ItemStack getTabIconItem() {

			return BlocksTSeries.storageCharcoal;
		}
	};

	public static CreativeTabs tabItems = new CreativeTabs(ID_THERMAL_SERIES + ".items") {

		@Override
		public ItemStack getTabIconItem() {

			return ItemsTSeries.dustCharcoal;
		}
	};

	public static CreativeTabs tabTools = new CreativeTabs(ID_THERMAL_SERIES + ".tools") {

		@Override
		public ItemStack getTabIconItem() {

			return ItemsTSeries.utilWrench;
		}
	};

	public static CreativeTabs tabMisc;

}
