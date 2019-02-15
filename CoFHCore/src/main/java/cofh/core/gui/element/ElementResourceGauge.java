package cofh.core.gui.element;

import cofh.core.gui.IGuiAccess;
import cofh.lib.util.IResourceStorage;

public class ElementResourceGauge extends ElementBase {

	protected IResourceStorage storage;

	protected boolean infinite;
	protected int minLevel;

	public ElementResourceGauge(IGuiAccess gui, int posX, int posY, IResourceStorage storage) {

		super(gui, posX, posY);
	}

}
