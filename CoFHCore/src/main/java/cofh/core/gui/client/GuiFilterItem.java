package cofh.core.gui.client;

import cofh.core.gui.container.ContainerFilterItem;
import cofh.core.gui.element.ElementButton;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.gui.element.tab.TabSecurity;
import cofh.lib.util.IFilterable;
import cofh.lib.util.control.ISecurable;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.lib.util.helpers.SoundHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class GuiFilterItem extends GuiContainerCoFH {

	private static final String TEX_PATH = "cofh:textures/gui/filter/";

	public static final String BUTTON_LIST = "FilterList";
	public static final String BUTTON_ORE = "FilterOre";
	public static final String BUTTON_META = "FilterMeta";
	public static final String BUTTON_NBT = "FilterNbt";

	protected UUID playerID;
	private String texturePath;
	private boolean secure;

	protected ElementButton buttonList;
	protected ElementButton buttonOre;
	protected ElementButton buttonMeta;
	protected ElementButton buttonNbt;

	public GuiFilterItem(InventoryPlayer inventory, ContainerFilterItem container) {

		this(inventory, container, "");
	}

	public GuiFilterItem(InventoryPlayer inventory, ContainerFilterItem container, String info) {

		super(container, info);

		playerID = SecurityHelper.getID(inventory.player);
		secure = SecurityHelper.hasSecurity(container.getContainerStack());
		texture = new ResourceLocation(TEX_PATH + "filter_" + container.slots + ".png");
		texturePath = texture.toString();
		name = container.getInventoryName();

		allowUserInput = false;
	}

	@Override
	public void initGui() {

		super.initGui();

		if (!info.isEmpty()) {
			addTab(new TabInfo(this, info));
		}
		if (secure) {
			addTab(new TabSecurity(this, (ISecurable) inventorySlots, playerID));
		}
		buttonList = new ElementButton(this, 119, 20, BUTTON_LIST, 176, 0, 176, 20, 20, 20, texturePath);
		buttonOre = new ElementButton(this, 145, 20, BUTTON_ORE, 216, 0, 216, 20, 20, 20, texturePath);
		buttonMeta = new ElementButton(this, 119, 46, BUTTON_META, 176, 60, 176, 80, 20, 20, texturePath);
		buttonNbt = new ElementButton(this, 145, 46, BUTTON_NBT, 216, 60, 216, 80, 20, 20, texturePath);

		addElement(buttonList);
		addElement(buttonOre);
		addElement(buttonMeta);
		addElement(buttonNbt);

		updateButtons();
	}

	@Override
	public void handleElementButtonClick(String buttonName, int mouseButton) {

		ContainerFilterItem container = (ContainerFilterItem) inventorySlots;
		int flag = 0;
		switch (buttonName) {
			case BUTTON_LIST:
				flag = IFilterable.FLAG_WHITELIST;
				break;
			case BUTTON_ORE:
				flag = IFilterable.FLAG_ORE_DICT;
				break;
			case BUTTON_META:
				flag = IFilterable.FLAG_METADATA;
				break;
			case BUTTON_NBT:
				flag = IFilterable.FLAG_NBT;
				break;
		}
		SoundHelper.playClickSound(container.getFlag(flag) ? 0.5F : 0.8F);
		container.setFlag(flag, !container.getFlag(flag));
		updateButtons();
	}

	private void updateButtons() {

		ContainerFilterItem container = (ContainerFilterItem) inventorySlots;

		int x = container.getFlag(IFilterable.FLAG_WHITELIST) ? 176 : 196;
		buttonList.setSheetX(x);
		buttonList.setHoverX(x);
		buttonList.setToolTip("info.cofh.filter.list." + (container.getFlag(IFilterable.FLAG_WHITELIST) ? 1 : 0));

		x = container.getFlag(IFilterable.FLAG_ORE_DICT) ? 216 : 236;
		buttonOre.setSheetX(x);
		buttonOre.setHoverX(x);
		buttonOre.setToolTip("info.cofh.filter.ore." + (container.getFlag(IFilterable.FLAG_ORE_DICT) ? 1 : 0));

		x = container.getFlag(IFilterable.FLAG_METADATA) ? 176 : 196;
		buttonMeta.setSheetX(x);
		buttonMeta.setHoverX(x);
		buttonMeta.setToolTip("info.cofh.filter.meta." + (container.getFlag(IFilterable.FLAG_METADATA) ? 1 : 0));

		x = container.getFlag(IFilterable.FLAG_NBT) ? 216 : 236;
		buttonNbt.setSheetX(x);
		buttonNbt.setHoverX(x);
		buttonNbt.setToolTip("info.cofh.filter.nbt." + (container.getFlag(IFilterable.FLAG_NBT) ? 1 : 0));
	}

}
