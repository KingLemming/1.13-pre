package cofh.thermal.core.gui.client;

import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.element.ElementButton;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.util.oredict.OreDictionaryArbiter;
import cofh.thermal.core.gui.container.ContainerLexiconTransmute;
import cofh.thermal.core.network.PacketLexiconTransmute;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.localize;
import static cofh.thermal.core.network.PacketLexiconTransmute.*;

public class GuiLexiconTransmute extends GuiContainerCoFH {

	private static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/tome/lexicon_transmute.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public static final String BUTTON_PREV_ORE = "PrevOre";
	public static final String BUTTON_NEXT_ORE = "NextOre";
	public static final String BUTTON_PREV_NAME = "PrevName";
	public static final String BUTTON_NEXT_NAME = "NextName";
	public static final String BUTTON_TRANSMUTE = "Transmute";

	ElementButton prevOre;
	ElementButton nextOre;
	ElementButton prevName;
	ElementButton nextName;
	ElementButton transmute;

	ContainerLexiconTransmute lexicon;

	public GuiLexiconTransmute(InventoryPlayer inventory, ContainerLexiconTransmute container) {

		super(container);
		lexicon = container;
		texture = TEXTURE;
		name = "gui.thermal.lexicon.transmute";
		allowUserInput = false;

		drawTitle = false;
		drawInventory = false;

		xSize = 206;
		ySize = 204;

		info = localize("tab.thermal.lexicon.transmute.0") + "\n\n" + localize("tab.thermal.lexicon.transmute.1");
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabInfo(this, info).setOffsets(12, 10));

		prevOre = new ElementButton(this, 57, 31, BUTTON_PREV_ORE, 206, 0, 206, 20, 206, 40, 20, 20, TEX_PATH);
		nextOre = new ElementButton(this, 129, 31, BUTTON_NEXT_ORE, 226, 0, 226, 20, 226, 40, 20, 20, TEX_PATH);
		prevName = new ElementButton(this, 20, 81, BUTTON_PREV_NAME, 206, 0, 206, 20, 206, 40, 20, 20, TEX_PATH);
		nextName = new ElementButton(this, 166, 81, BUTTON_NEXT_NAME, 226, 0, 226, 20, 226, 40, 20, 20, TEX_PATH);
		transmute = new ElementButton(this, 83, 59, BUTTON_TRANSMUTE, 206, 60, 206, 80, 206, 100, 40, 20, TEX_PATH);

		addElement(prevOre);
		addElement(nextOre);
		addElement(prevName);
		addElement(nextName);
		addElement(transmute);
	}

	@Override
	protected void updateElementInformation() {

		if (lexicon.hasMultipleOres()) {
			prevOre.setActive();
			nextOre.setActive();
			// prevOre.setToolTip("gui.thermal.lexicon.prev_entry");
			// nextOre.setToolTip("gui.thermal.lexicon.next_entry");
		} else {
			prevOre.setDisabled();
			nextOre.setDisabled();
			// prevOre.setToolTip("gui.thermal.lexicon.single_entry");
			// nextOre.setToolTip("gui.thermal.lexicon.single_entry");
		}
		if (lexicon.hasMultipleNames()) {
			prevName.setActive();
			nextName.setActive();
		} else {
			prevName.setDisabled();
			nextName.setDisabled();
		}
		if (lexicon.canTransmute()) {
			transmute.setActive();
		} else {
			transmute.setDisabled();
		}
	}

	@Override
	public void handleElementButtonClick(String buttonName, int mouseButton) {

		switch (buttonName) {
			case BUTTON_PREV_ORE:
				PacketLexiconTransmute.sendToServer(ORE_PREV);
				break;
			case BUTTON_NEXT_ORE:
				PacketLexiconTransmute.sendToServer(ORE_NEXT);
				break;
			case BUTTON_PREV_NAME:
				PacketLexiconTransmute.sendToServer(NAME_PREV);
				break;
			case BUTTON_NEXT_NAME:
				PacketLexiconTransmute.sendToServer(NAME_NEXT);
				break;
			case BUTTON_TRANSMUTE:
				PacketLexiconTransmute.sendToServer(TRANSMUTE);
				break;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {

		fontRenderer.drawString(localize(name), getCenteredOffset(localize(name)), 16, 0xddbb1d);
		// 0xd2c0a3
		if (lexicon != null) {
			String oreName = lexicon.getOreName();
			if (!oreName.equals(OreDictionaryArbiter.UNKNOWN)) {
				fontRenderer.drawString(oreName, getCenteredOffset(oreName), 88, 0xffffff);
			}
		}
		super.drawGuiContainerForegroundLayer(x, y);
	}

}
