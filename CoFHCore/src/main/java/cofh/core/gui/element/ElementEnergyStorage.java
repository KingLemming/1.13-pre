package cofh.core.gui.element;

import cofh.core.gui.IGuiAccess;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

import static cofh.lib.util.Constants.PATH_ELEMENTS;
import static cofh.lib.util.helpers.StringHelper.formatNumber;
import static cofh.lib.util.helpers.StringHelper.localize;

public class ElementEnergyStorage extends ElementBase {

	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(PATH_ELEMENTS + "energy_storage.png");
	public static final int DEFAULT_SCALE = 42;

	protected IEnergyStorage storage;
	protected boolean infinite;
	protected String unit = "RF";

	// If this is enabled, 1 pixel of energy will always show in the bar as long as it is non-zero.
	protected boolean alwaysShowMinimum = false;

	public ElementEnergyStorage(IGuiAccess gui, int posX, int posY, IEnergyStorage storage) {

		super(gui, posX, posY);
		this.storage = storage;

		this.texture = DEFAULT_TEXTURE;
		this.width = 16;
		this.height = DEFAULT_SCALE;

		this.texW = 32;
		this.texH = 64;
	}

	public ElementEnergyStorage setAlwaysShow(boolean show) {

		this.alwaysShowMinimum = show;
		return this;
	}

	public ElementEnergyStorage setInfinite(boolean infinite) {

		this.infinite = infinite;
		return this;
	}

	public ElementEnergyStorage setUnit(String unit) {

		this.unit = unit;
		return this;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY) {

		int amount = getScaled();
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 0, 0, width, height);
		drawTexturedModalRect(posX, posY + DEFAULT_SCALE - amount, 16, DEFAULT_SCALE - amount, width, amount);
	}

	@Override
	public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {

		if (infinite) {
			tooltip.add(localize("info.cofh.infinite_energy"));
		} else {
			tooltip.add(formatNumber(storage.getEnergyStored()) + " / " + formatNumber(storage.getMaxEnergyStored()) + " " + unit);
		}
	}

	protected int getScaled() {

		if (storage.getMaxEnergyStored() <= 0 || infinite) {
			return height;
		}
		long fraction = (long) storage.getEnergyStored() * height / storage.getMaxEnergyStored();

		return alwaysShowMinimum && storage.getEnergyStored() > 0 ? Math.max(1, MathHelper.round(fraction)) : MathHelper.round(fraction);
	}

}
