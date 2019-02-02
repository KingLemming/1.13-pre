package cofh.core.gui.element.tab;

import cofh.core.gui.client.GuiContainerCoFH;

public class TabResource extends TabBase {

	public static int defaultSide = 0;
	public static int defaultHeaderColor = 0xe1c92f;
	public static int defaultSubHeaderColor = 0xaaafb8;
	public static int defaultTextColor = 0x000000;
	public static int defaultBackgroundColorOut = 0xd0650b;
	public static int defaultBackgroundColorIn = 0x0a76d0;

	private boolean isProducer;
	private boolean displayMax = true;
	private boolean displayStored = true;

	public TabResource(GuiContainerCoFH gui) {

		this(gui, defaultSide);
	}

	public TabResource(GuiContainerCoFH gui, int sideIn) {

		super(gui, sideIn);

		headerColor = defaultHeaderColor;
		subheaderColor = defaultSubHeaderColor;
		textColor = defaultTextColor;
		backgroundColor = defaultBackgroundColorIn;

		maxHeight = 92;
		maxWidth = 100;
	}
}
