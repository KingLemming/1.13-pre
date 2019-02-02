package cofh.core.plugins.jei;

import cofh.core.gui.client.GuiContainerCoFH;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class SlotMover implements IAdvancedGuiHandler<GuiContainerCoFH> {

	@Override
	public Class<GuiContainerCoFH> getGuiContainerClass() {

		return GuiContainerCoFH.class;
	}

	@Override
	public List<Rectangle> getGuiExtraAreas(GuiContainerCoFH guiContainer) {

		return guiContainer.getTabBounds();
	}

	@Nullable
	@Override
	public Object getIngredientUnderMouse(GuiContainerCoFH guiContainer, int mouseX, int mouseY) {

		return null;
	}

}
