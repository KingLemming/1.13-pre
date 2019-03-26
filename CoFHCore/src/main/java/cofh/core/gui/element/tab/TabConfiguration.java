package cofh.core.gui.element.tab;

import cofh.core.gui.IGuiAccess;
import cofh.core.gui.TexturesCoFH;
import cofh.lib.util.control.IReconfigurable;
import cofh.lib.util.control.ITransferControllable;
import cofh.lib.util.helpers.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static cofh.lib.util.helpers.StringHelper.localize;

public class TabConfiguration extends TabBase {

	public static int defaultSide = RIGHT;
	public static int defaultHeaderColor = 0xe1c92f;
	public static int defaultSubHeaderColor = 0xaaafb8;
	public static int defaultTextColor = 0x000000;
	public static int defaultBackgroundColor = 0x226688;

	private IReconfigurable myReconfig;
	private ITransferControllable myTransfer;

	public TabConfiguration(IGuiAccess gui, IReconfigurable reconfig) {

		this(gui, defaultSide, reconfig, null);
	}

	public TabConfiguration(IGuiAccess gui, IReconfigurable reconfig, ITransferControllable transfer) {

		this(gui, defaultSide, reconfig, transfer);
	}

	public TabConfiguration(IGuiAccess gui, int side, IReconfigurable reconfig, ITransferControllable transfer) {

		super(gui, side);

		headerColor = defaultHeaderColor;
		subheaderColor = defaultSubHeaderColor;
		textColor = defaultTextColor;
		backgroundColor = defaultBackgroundColor;

		maxHeight = 92;
		maxWidth = 112;
		myReconfig = reconfig;
		myTransfer = transfer;

		this.setVisible(myReconfig.isReconfigurable());
	}

	// TODO: Figure out side configuration rendering.
	@Override
	protected void drawForeground() {

		drawTabIcon(TexturesCoFH.ICON_CONFIG);
		if (!fullyOpen) {
			return;
		}
		getFontRenderer().drawStringWithShadow(localize("info.cofh.configuration"), sideOffset() + 18, 6, headerColor);

		RenderHelper.setBlockTextureSheet();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if (myTransfer == null) {
			if (myTransfer.hasTransferIn()) {

			} else {

			}
			if (myTransfer.hasTransferOut()) {

			} else {

			}
		} else {

		}
		GlStateManager.disableBlend();
		RenderHelper.setDefaultFontTextureSheet();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void drawBackground() {

		super.drawBackground();

		if (!fullyOpen) {
			return;
		}
		float colorR = (backgroundColor >> 16 & 255) / 255.0F * 0.6F;
		float colorG = (backgroundColor >> 8 & 255) / 255.0F * 0.6F;
		float colorB = (backgroundColor & 255) / 255.0F * 0.6F;
		GlStateManager.color(colorR, colorG, colorB, 1.0F);

		if (myTransfer == null) {
			gui.drawTexturedModalRect(16, 20, 16, 20, 64, 64);
		} else {
			gui.drawTexturedModalRect(28, 20, 16, 20, 64, 64);
			gui.drawTexturedModalRect(6, 32, 16, 20, 20, 40);
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {

		if (!fullyOpen) {
			tooltip.add(localize("info.cofh.configuration"));
			return;
		}
		if (myTransfer == null) {
			return;
		}
		int x = mouseX - this.posX();
		int y = mouseY - this.posY;

		if (8 <= x && x < 24 && 34 <= y && y < 50) {
			if (myTransfer.hasTransferIn()) {
				tooltip.add(myTransfer.getTransferIn() ? localize("info.cofh.transfer_in_enabled") : localize("info.cofh.transfer_in_disabled"));
			} else {
				tooltip.add(localize("info.cofh.transfer_in_unavailable"));
			}
		} else if (8 <= x && x < 24 && 54 <= y && y < 68) {
			if (myTransfer.hasTransferOut()) {
				tooltip.add(myTransfer.getTransferOut() ? localize("info.cofh.transfer_out_enabled") : localize("info.cofh.transfer_out_disabled"));
			} else {
				tooltip.add(localize("info.cofh.transfer_out_unavailable"));
			}
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (!fullyOpen) {
			return false;
		}
		int x = mouseX - this.posX();
		int y = mouseY - this.posY;

		if (myTransfer == null) {
			if (x < 16 || x >= 80 || y < 20 || y >= 84) {
				return super.mouseClicked(x, y, mouseButton);
			}
		} else {
			if (x < 8 || x >= 92 || y < 20 || y >= 84) {
				return super.mouseClicked(x, y, mouseButton);
			}
		}
		return true;
	}

}
