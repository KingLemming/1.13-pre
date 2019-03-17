package cofh.enstoragement.gui.client;

import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.tab.TabInfo;
import cofh.enstoragement.gui.container.ContainerStrongbox;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiStrongbox extends GuiContainerCoFH {

	private static final String TEX_PATH = "cofh:textures/gui/storage/";

	public GuiStrongbox(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerStrongbox(inventory, tile), inventory.player);
		ContainerStrongbox container = ((ContainerStrongbox) inventorySlots);

		texture = new ResourceLocation(TEX_PATH + "storage_" + container.slots + ".png");
		name = String.valueOf(container.getTile().getDisplayName());

		allowUserInput = false;
		ySize = 112 + 18 * MathHelper.clamp(container.rows, 2, 9);

		info = generateTabInfo("tab.enstoragement.strongbox");

		//		if (container.getContainerStack().isItemEnchantable() && !ItemSatchel.hasHoldingEnchant(container.getContainerStack())) {
		//			info += "\n\n" + localize("tab.cofh.storage.enchant");
		//		}
	}

	@Override
	public void initGui() {

		super.initGui();

		if (!info.isEmpty()) {
			addTab(new TabInfo(this, info));
		}
		//		if (SecurityHelper.isSecure(container.getContainerStack())) {
		//			addTab(new TabSecurity(this, (ISecurable) inventorySlots, playerName));
		//		}
	}

}
