package cofh.enstoragement.gui.client;

import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.gui.element.tab.TabSecurity;
import cofh.enstoragement.gui.container.ContainerSatchel;
import cofh.enstoragement.item.ItemSatchel;
import cofh.lib.util.control.ISecurable;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.SecurityHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.helpers.StringHelper.generateTabInfo;
import static cofh.lib.util.helpers.StringHelper.localize;
import static cofh.lib.util.modhelpers.EnsorcellmentHelper.HOLDING;

public class GuiSatchel extends GuiContainerCoFH {

	private static final String TEX_PATH = "cofh:textures/gui/storage/";

	private boolean secure;

	public GuiSatchel(InventoryPlayer inventory, ContainerSatchel container) {

		super(container, inventory.player);

		secure = SecurityHelper.hasSecurity(container.getContainerStack());
		texture = new ResourceLocation(TEX_PATH + "storage_" + container.slots + ".png");
		name = container.getInventoryName();

		allowUserInput = false;
		ySize = 112 + 18 * MathHelper.clamp(container.rows, 2, 9);

		ItemStack satchelStack = container.getContainerStack();
		ItemSatchel satchelItem = (ItemSatchel) satchelStack.getItem();

		if (satchelItem.isCreative()) {
			info = generateTabInfo("tab.enstoragement.satchel_c");
		} else {
			info = generateTabInfo("tab.enstoragement.satchel");
			if (satchelStack.isItemEnchantable() && EnchantmentHelper.getEnchantmentLevel(HOLDING, satchelStack) <= 0) {
				info += "\n\n" + localize("tab.cofh.storage_enchant");
			}
		}
	}

	@Override
	public void initGui() {

		super.initGui();

		if (!info.isEmpty()) {
			addTab(new TabInfo(this, info));
		}
		if (secure) {
			addTab(new TabSecurity(this, (ISecurable) inventorySlots, SecurityHelper.getID(player)));
		}
	}

}
