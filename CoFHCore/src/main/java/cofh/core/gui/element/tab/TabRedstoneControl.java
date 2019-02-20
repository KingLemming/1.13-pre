package cofh.core.gui.element.tab;

import cofh.core.gui.IGuiAccess;
import cofh.core.gui.TexturesCoFH;
import cofh.lib.util.control.IRedstoneControllable;
import cofh.lib.util.helpers.SoundHelper;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

import static cofh.core.gui.TexturesCoFH.*;
import static cofh.lib.util.control.IRedstoneControllable.ControlMode.*;
import static cofh.lib.util.helpers.StringHelper.YELLOW;
import static cofh.lib.util.helpers.StringHelper.localize;

public class TabRedstoneControl extends TabBase {

	public static int defaultSide = RIGHT;
	public static int defaultHeaderColor = 0xe1c92f;
	public static int defaultSubHeaderColor = 0xaaafb8;
	public static int defaultTextColor = 0x000000;
	public static int defaultBackgroundColor = 0xd0230a;

	private IRedstoneControllable myRSControllable;

	public TabRedstoneControl(IGuiAccess gui, IRedstoneControllable rsControllable) {

		this(gui, defaultSide, rsControllable);
	}

	public TabRedstoneControl(IGuiAccess gui, int side, IRedstoneControllable rsControllable) {

		super(gui, side);

		headerColor = defaultHeaderColor;
		subheaderColor = defaultSubHeaderColor;
		textColor = defaultTextColor;
		backgroundColor = defaultBackgroundColor;

		maxHeight = 92;
		maxWidth = 112;
		myRSControllable = rsControllable;

		this.setVisible(myRSControllable.isControllable());
	}

	// TODO: Fully support new Redstone Control system.
	@Override
	protected void drawForeground() {

		drawTabIcon(TexturesCoFH.ICON_REDSTONE_ON);
		if (!fullyOpen) {
			return;
		}
		getFontRenderer().drawStringWithShadow(localize("info.cofh.redstone_control"), sideOffset() + 18, 6, headerColor);
		getFontRenderer().drawStringWithShadow(localize("info.cofh.control_status") + ":", sideOffset() + 6, 42, subheaderColor);
		getFontRenderer().drawStringWithShadow(localize("info.cofh.signal_required") + ":", sideOffset() + 6, 66, subheaderColor);

		gui.drawIcon(ICON_BUTTON, 28, 20);
		gui.drawIcon(ICON_BUTTON, 48, 20);
		gui.drawIcon(ICON_BUTTON, 68, 20);

		switch (myRSControllable.getMode()) {
			case DISABLED:
				gui.drawIcon(TexturesCoFH.ICON_BUTTON_HIGHLIGHT, 28, 20);
				getFontRenderer().drawString(localize("info.cofh.disabled"), sideOffset() + 14, 54, textColor);
				getFontRenderer().drawString(localize("info.cofh.ignored"), sideOffset() + 14, 78, textColor);
				break;
			case LOW:
				gui.drawIcon(TexturesCoFH.ICON_BUTTON_HIGHLIGHT, 48, 20);
				getFontRenderer().drawString(localize("info.cofh.enabled"), sideOffset() + 14, 54, textColor);
				getFontRenderer().drawString(localize("info.cofh.low"), sideOffset() + 14, 78, textColor);
				break;
			case HIGH:
				gui.drawIcon(TexturesCoFH.ICON_BUTTON_HIGHLIGHT, 68, 20);
				getFontRenderer().drawString(localize("info.cofh.enabled"), sideOffset() + 14, 54, textColor);
				getFontRenderer().drawString(localize("info.cofh.high"), sideOffset() + 14, 78, textColor);
				break;
			default:
		}
		gui.drawIcon(ICON_REDSTONE_OFF, 28, 20);
		gui.drawIcon(ICON_RS_TORCH_OFF, 48, 20);
		gui.drawIcon(ICON_RS_TORCH_ON, 68, 20);

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
		gui.drawTexturedModalRect(24, 16, 16, 20, 64, 24);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {

		if (!fullyOpen) {
			tooltip.add(localize("info.cofh.redstone_control"));
			switch (myRSControllable.getMode()) {
				case DISABLED:
					tooltip.add(YELLOW + localize("info.cofh.disabled"));
					break;
				case LOW:
					tooltip.add(YELLOW + localize("info.cofh.low"));
					break;
				case HIGH:
					tooltip.add(YELLOW + localize("info.cofh.high"));
					break;
				default:
			}
			return;
		}
		int x = mouseX - this.posX();
		int y = mouseY - this.posY;

		if (28 <= x && x < 44 && 20 <= y && y < 36) {
			tooltip.add(localize("info.cofh.ignored"));
		} else if (48 <= x && x < 64 && 20 <= y && y < 36) {
			tooltip.add(localize("info.cofh.low"));
		} else if (68 <= x && x < 84 && 20 <= y && y < 36) {
			tooltip.add(localize("info.cofh.high"));
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (!fullyOpen) {
			return false;
		}
		int x = mouseX - this.posX();
		int y = mouseY - this.posY;

		if (x < 24 || x >= 88 || y < 16 || y >= 40) {
			return false;
		}
		if (28 <= x && x < 44 && 20 <= y && y < 36) {
			if (myRSControllable.getMode() != DISABLED) {
				myRSControllable.setControl(0, DISABLED);
				SoundHelper.playClickSound(0.4F);
			}
		} else if (48 <= x && x < 64 && 20 <= y && y < 36) {
			if (myRSControllable.getMode() != LOW) {
				myRSControllable.setControl(0, LOW);
				SoundHelper.playClickSound(0.6F);
			}
		} else if (68 <= x && x < 84 && 20 <= y && y < 36) {
			if (myRSControllable.getMode() != HIGH) {
				myRSControllable.setControl(0, HIGH);
				SoundHelper.playClickSound(0.8F);
			}
		}
		return true;
	}

}
