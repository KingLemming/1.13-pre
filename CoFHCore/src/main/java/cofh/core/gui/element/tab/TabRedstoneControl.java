package cofh.core.gui.element.tab;

import cofh.core.gui.IGuiAccess;
import cofh.core.gui.TexturesCoFH;
import cofh.lib.util.control.IRedstoneControllable;
import net.minecraft.client.renderer.GlStateManager;

import static cofh.lib.util.helpers.StringHelper.localize;

public class TabRedstoneControl extends TabBase {

	public static int defaultSide = 1;
	public static int defaultHeaderColor = 0xe1c92f;
	public static int defaultSubHeaderColor = 0xaaafb8;
	public static int defaultTextColor = 0x000000;
	public static int defaultBackgroundColor = 0xd0230a;

	private IRedstoneControllable myContainer;

	public TabRedstoneControl(IGuiAccess gui, IRedstoneControllable container) {

		this(gui, defaultSide, container);
	}

	public TabRedstoneControl(IGuiAccess gui, int side, IRedstoneControllable container) {

		super(gui, side);

		headerColor = defaultHeaderColor;
		subheaderColor = defaultSubHeaderColor;
		textColor = defaultTextColor;
		backgroundColor = defaultBackgroundColor;

		maxHeight = 92;
		maxWidth = 112;
		myContainer = container;
	}

	@Override
	protected void drawForeground() {

		drawTabIcon(TexturesCoFH.ICON_REDSTONE_ON);
		if (!fullyOpen) {
			return;
		}
		getFontRenderer().drawStringWithShadow(localize("info.cofh.redstone_control"), sideOffset() + 18, 6, headerColor);
		// getFontRenderer().drawStringWithShadow(localize("info.cofh.control_status") + ":", sideOffset() + 6, 42, subheaderColor);
		// getFontRenderer().drawStringWithShadow(localize("info.cofh.signal_required") + ":", sideOffset() + 6, 66, subheaderColor);
		//
		//		if (myContainer.getControl().isDisabled()) {
		//			gui.drawButton(CoreTextures.ICON_REDSTONE_OFF, 28, 20, 1);
		//			gui.drawButton(CoreTextures.ICON_RS_TORCH_OFF, 48, 20, 0);
		//			gui.drawButton(CoreTextures.ICON_RS_TORCH_ON, 68, 20, 0);
		//			getFontRenderer().drawString(StringHelper.localize("info.cofh.disabled"), sideOffset() + 14, 54, textColor);
		//			getFontRenderer().drawString(StringHelper.localize("info.cofh.ignored"), sideOffset() + 14, 78, textColor);
		//		} else {
		//			getFontRenderer().drawString(StringHelper.localize("info.cofh.enabled"), sideOffset() + 14, 54, textColor);
		//			gui.drawButton(CoreTextures.ICON_REDSTONE_OFF, 28, 20, 0);
		//			if (myContainer.getControl().isLow()) {
		//				gui.drawButton(CoreTextures.ICON_RS_TORCH_OFF, 48, 20, 1);
		//				gui.drawButton(CoreTextures.ICON_RS_TORCH_ON, 68, 20, 0);
		//				getFontRenderer().drawString(StringHelper.localize("info.cofh.low"), sideOffset() + 14, 78, textColor);
		//			} else {
		//				gui.drawButton(CoreTextures.ICON_RS_TORCH_OFF, 48, 20, 0);
		//				gui.drawButton(CoreTextures.ICON_RS_TORCH_ON, 68, 20, 1);
		//				getFontRenderer().drawString(StringHelper.localize("info.cofh.high"), sideOffset() + 14, 78, textColor);
		//			}
		//		}
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

	//	@Override
	//	public void addTooltip(List<String> list) {
	//
	//		if (!isFullyOpened()) {
	//			list.add(StringHelper.localize("info.cofh.redstoneControl"));
	//			if (myContainer.getControl().isDisabled()) {
	//				list.add(StringHelper.YELLOW + StringHelper.localize("info.cofh.disabled"));
	//				return;
	//			} else if (myContainer.getControl().isLow()) {
	//				list.add(StringHelper.YELLOW + StringHelper.localize("info.cofh.enabled") + ", " + StringHelper.localize("info.cofh.low"));
	//				return;
	//			}
	//			list.add(StringHelper.YELLOW + StringHelper.localize("info.cofh.enabled") + ", " + StringHelper.localize("info.cofh.high"));
	//			return;
	//		}
	//		int x = gui.getMouseX() - currentShiftX;
	//		int y = gui.getMouseY() - currentShiftY;
	//		if (28 <= x && x < 44 && 20 <= y && y < 36) {
	//			list.add(StringHelper.localize("info.cofh.ignored"));
	//		} else if (48 <= x && x < 64 && 20 <= y && y < 36) {
	//			list.add(StringHelper.localize("info.cofh.low"));
	//		} else if (68 <= x && x < 84 && 20 <= y && y < 36) {
	//			list.add(StringHelper.localize("info.cofh.high"));
	//		}
	//	}
	//
	//	@Override
	//	public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {
	//
	//		if (!isFullyOpened()) {
	//			return false;
	//		}
	//		if (side == LEFT) {
	//			mouseX += currentWidth;
	//		}
	//		mouseX -= currentShiftX;
	//		mouseY -= currentShiftY;
	//
	//		if (mouseX < 24 || mouseX >= 88 || mouseY < 16 || mouseY >= 40) {
	//			return false;
	//		}
	//		if (28 <= mouseX && mouseX < 44 && 20 <= mouseY && mouseY < 36) {
	//			if (!myContainer.getControl().isDisabled()) {
	//				myContainer.setControl(IRedstoneControl.ControlMode.DISABLED);
	//				GuiContainerCore.playClickSound(0.4F);
	//			}
	//		} else if (48 <= mouseX && mouseX < 64 && 20 <= mouseY && mouseY < 36) {
	//			if (!myContainer.getControl().isLow()) {
	//				myContainer.setControl(IRedstoneControl.ControlMode.LOW);
	//				GuiContainerCore.playClickSound(0.6F);
	//			}
	//		} else if (68 <= mouseX && mouseX < 84 && 20 <= mouseY && mouseY < 36) {
	//			if (!myContainer.getControl().isHigh()) {
	//				myContainer.setControl(IRedstoneControl.ControlMode.HIGH);
	//				GuiContainerCore.playClickSound(0.8F);
	//			}
	//		}
	//		return true;
	//	}

}
