package cofh.thermal.core.gui.client;

import cofh.core.gui.GuiColor;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.ElementButton;
import cofh.core.gui.element.ElementListBox;
import cofh.core.gui.element.ElementTextField;
import cofh.core.gui.element.listbox.IListBoxElement;
import cofh.core.gui.element.listbox.ListBoxElementText;
import cofh.core.gui.element.listbox.SliderVertical;
import cofh.core.gui.element.tab.TabInfo;
import cofh.thermal.core.gui.container.ContainerLexiconStudy;
import cofh.thermal.core.network.PacketLexiconStudy;
import cofh.thermal.core.util.managers.LexiconManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;
import static cofh.lib.util.helpers.StringHelper.localize;
import static cofh.thermal.core.network.PacketLexiconStudy.*;

public class GuiLexiconStudy extends GuiContainerCoFH {

	private static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/tome/lexicon_study.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public static final String BUTTON_PREV_ORE = "PrevOre";
	public static final String BUTTON_NEXT_ORE = "NextOre";
	public static final String BUTTON_SET_PREF = "SetPreferred";
	public static final String BUTTON_CLEAR_PREF = "ClearPreferred";

	private String searchLocal = "<" + localize("gui.thermal.lexicon.search") + ">";

	ElementTextField searchBox = new ElementTextField(this, 42, 87, 124, 10) {

		public boolean searchUp = true;
		boolean rightClick = false;

		@Override
		public ElementTextField setFocused(boolean focused) {

			if (focused && searchUp) {
				setText("");
				searchUp = false;
			}
			return super.setFocused(focused);
		}

		@Override
		public boolean keyTyped(char charTyped, int keyTyped) {

			if (!super.keyTyped(charTyped, keyTyped)) {
				return false;
			}
			if (textLength <= 0) {
				buildFullOreList();
			} else {
				buildOreList(getText());
			}
			if (oreList.getElementCount() <= 0) {
				oreList.setSelectedIndex(-1);
			} else {
				oreList.setSelectedIndex(0);
			}
			if (oreList.getSelectedElement() != null) {
				lexicon.onSelectionChanged((String) oreList.getSelectedElement().getValue());
			}
			oreSlider.setLimits(0, oreList.getElementCount() - 8);
			return true;
		}

		@Override
		protected void onFocusLost() {

			if (textLength <= 0) {
				buildFullOreList();
				oreSlider.setLimits(0, oreList.getElementCount() - 8);
				setText("");
				searchUp = true;
			}
		}

		@Override
		public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

			if (mouseButton == 1) {
				rightClick = true;
				buildFullOreList();
				oreList.setSelectedIndex(0);
				oreSlider.setLimits(0, oreList.getElementCount() - 8);
				setText("");
				setFocused(true);
			}
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}

		@Override
		public void mouseReleased(int mouseX, int mouseY) {

			if (rightClick) {
				rightClick = false;
			} else {
				super.mouseReleased(mouseX, mouseY);
			}
		}

		@Override
		public void drawBackground(int mouseX, int mouseY) {

		}
	};

	ElementListBox oreList = new ElementListBox(this, 22, 104, 162, 82) {

		@Override
		protected void onSelectionChanged(int newIndex, IListBoxElement newElement) {

			if (newIndex > -1) {
				lexicon.onSelectionChanged((String) newElement.getValue());
			}
		}

		@Override
		protected void onScrollV(int newStartIndex) {

			oreSlider.setValue(newStartIndex);
		}

		@Override
		protected void onScrollH(int newStartIndex) {

		}
	};
	SliderVertical oreSlider;

	ElementButton prevOre;
	ElementButton nextOre;
	ElementButton setPreferredOre;
	ElementButton clearPreferredOre;

	ContainerLexiconStudy lexicon;

	public GuiLexiconStudy(InventoryPlayer inventory, ContainerLexiconStudy container) {

		super(container);

		lexicon = container;
		texture = TEXTURE;
		name = "gui.thermal.lexicon.study";
		allowUserInput = false;

		drawTitle = false;
		drawInventory = false;

		xSize = 206;
		ySize = 204;

		info = generateTabInfo("tab.thermal.lexicon.study");
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabInfo(this, info).setOffsets(12, 10));

		prevOre = new ElementButton(this, 57, 31, BUTTON_PREV_ORE, 206, 0, 206, 20, 206, 40, 20, 20, TEX_PATH);
		nextOre = new ElementButton(this, 129, 31, BUTTON_NEXT_ORE, 226, 0, 226, 20, 226, 40, 20, 20, TEX_PATH);
		setPreferredOre = new ElementButton(this, 78, 59, BUTTON_SET_PREF, 206, 60, 206, 80, 206, 100, 20, 20, TEX_PATH);
		clearPreferredOre = new ElementButton(this, 108, 59, BUTTON_CLEAR_PREF, 226, 60, 226, 80, 226, 100, 20, 20, TEX_PATH);

		oreList.borderColor = new GuiColor(120, 120, 120, 0).getColor();
		oreList.backgroundColor = new GuiColor(0, 0, 0, 48).getColor();

		addElement(prevOre);
		addElement(nextOre);
		addElement(setPreferredOre);
		addElement(clearPreferredOre);

		addElement(searchBox);
		addElement(oreList);

		buildFullOreList();
		lexicon.onSelectionChanged((String) oreList.getSelectedElement().getValue());

		oreSlider = new SliderVertical(this, 184, 105, 8, 80, oreList.getElementCount() - 8) {

			@Override
			public void onValueChanged(int value) {

				oreList.scrollToV(value);
			}
		};
		addElement(oreSlider);
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void onGuiClosed() {

		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	protected void updateElementInformation() {

		if (lexicon.hasMultipleOres()) {
			prevOre.setActive();
			nextOre.setActive();
			prevOre.setToolTip("gui.thermal.lexicon.prev_entry");
			nextOre.setToolTip("gui.thermal.lexicon.next_entry");
		} else {
			prevOre.setDisabled();
			nextOre.setDisabled();
			prevOre.setToolTip("gui.thermal.lexicon.single_entry");
			nextOre.setToolTip("gui.thermal.lexicon.single_entry");
		}
		if (lexicon.canSetPreferred()) {
			setPreferredOre.setActive();
			setPreferredOre.setToolTip("gui.thermal.lexicon.set_pref");
		} else {
			setPreferredOre.setDisabled();
			setPreferredOre.clearToolTip();
		}
		if (lexicon.hasPreferredOre()) {
			clearPreferredOre.setActive();
			clearPreferredOre.setToolTip("gui.thermal.lexicon.clear_pref");
		} else {
			clearPreferredOre.setDisabled();
			clearPreferredOre.clearToolTip();
		}
	}

	@Override
	public void handleElementButtonClick(String buttonName, int mouseButton) {

		if (buttonName.equalsIgnoreCase(BUTTON_PREV_ORE)) {
			PacketLexiconStudy.sendToServer(ORE_PREV);
		} else if (buttonName.equalsIgnoreCase(BUTTON_NEXT_ORE)) {
			PacketLexiconStudy.sendToServer(ORE_NEXT);
		} else if (buttonName.equalsIgnoreCase(BUTTON_SET_PREF)) {
			PacketLexiconStudy.sendToServer(SET_PREFERRED);
		} else if (buttonName.equalsIgnoreCase(BUTTON_CLEAR_PREF)) {
			PacketLexiconStudy.sendToServer(CLEAR_PREFERRED);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {

		fontRenderer.drawString(localize(name), getCenteredOffset(localize(name)), 16, 0xddbb1d);

		if (!searchBox.isFocused() && searchBox.getText().isEmpty()) {
			fontRenderer.drawString(searchLocal, getCenteredOffset(searchLocal), 88, 0xe0e0e0);
		}
		super.drawGuiContainerForegroundLayer(x, y);
	}

	protected void buildOreList(String search) {

		oreList.removeAll();
		for (String oreName : LexiconManager.getSortedOreNames()) {
			if (oreName.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
				oreList.add(new ListBoxElementText(oreName));
			}
		}
	}

	protected void buildFullOreList() {

		oreList.removeAll();
		for (String oreName : LexiconManager.getSortedOreNames()) {
			oreList.add(new ListBoxElementText(oreName));
		}
	}

}
