package cofh.core.gui.element;

import cofh.core.gui.IGuiAccess;
import cofh.lib.util.helpers.FluidHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.RenderHelper;
import cofh.lib.util.helpers.StringHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;

import java.util.List;

import static cofh.lib.util.Constants.PATH_ELEMENTS;
import static cofh.lib.util.helpers.StringHelper.formatNumber;
import static cofh.lib.util.helpers.StringHelper.localize;

public class ElementFluidTank extends ElementBase {

	public static final ResourceLocation LARGE_TEXTURE = new ResourceLocation(PATH_ELEMENTS + "fluid_tank_large.png");
	public static final ResourceLocation MEDIUM_TEXTURE = new ResourceLocation(PATH_ELEMENTS + "fluid_tank_medium.png");
	public static final ResourceLocation SMALL_TEXTURE = new ResourceLocation(PATH_ELEMENTS + "fluid_tank_small.png");

	protected IFluidTank tank;
	protected float durationFactor = 1.0F;
	protected boolean infinite;
	protected String unit = "mB";

	// If this is enabled, 1 pixel of fluid will always show in the tank as long as fluid is present.
	protected boolean alwaysShowMinimum = false;

	protected int gaugeType;
	protected boolean drawTank;
	protected boolean isThin;

	protected TextureAtlasSprite fluidTextureOverride;

	public ElementFluidTank(IGuiAccess gui, int posX, int posY, IFluidTank tank) {

		this(gui, posX, posY, tank, LARGE_TEXTURE);
	}

	public ElementFluidTank(IGuiAccess gui, int posX, int posY, IFluidTank tank, ResourceLocation texture) {

		super(gui, posX, posY);
		this.tank = tank;

		this.texture = texture;
		this.texW = 64;
		this.texH = 64;

		this.width = 16;
		this.height = 60;
	}

	public ElementFluidTank setGauge(int gaugeType) {

		this.gaugeType = gaugeType;
		return this;
	}

	public ElementFluidTank setLarge() {

		this.texture = LARGE_TEXTURE;
		this.width = 16;
		this.height = 60;
		return this;
	}

	public ElementFluidTank setMedium() {

		this.texture = MEDIUM_TEXTURE;
		this.height = 40;
		return this;
	}

	public ElementFluidTank setSmall() {

		this.texture = SMALL_TEXTURE;
		this.height = 30;
		return this;
	}

	public ElementFluidTank setFluidTextureOverride(TextureAtlasSprite fluidTextureOverride) {

		this.fluidTextureOverride = fluidTextureOverride;
		return this;
	}

	public ElementFluidTank drawTank(boolean drawTank) {

		this.drawTank = drawTank;
		return this;
	}

	public ElementFluidTank setAlwaysShow(boolean show) {

		alwaysShowMinimum = show;
		return this;
	}

	public ElementFluidTank setInfinite(boolean infinite) {

		this.infinite = infinite;
		return this;
	}

	public ElementFluidTank setThin(boolean thin) {

		this.isThin = thin;
		this.width = 7;
		return this;
	}

	public ElementFluidTank setDurationFactor(float durationFactor) {

		this.durationFactor = durationFactor;
		return this;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY) {

		if (drawTank) {
			RenderHelper.bindTexture(texture);
			if (isThin) {
				drawTexturedModalRect(posX - 1, posY - 1, 0, 0, width, height + 2);
				drawTexturedModalRect(posX - 1 + width, posY - 1, width, 0, 2, height + 2);
			} else {
				drawTexturedModalRect(posX - 1, posY - 1, 0, 0, width + 2, height + 2);
			}
		}
		drawFluid();
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 32 + gaugeType * 16 + (isThin ? 3 : 0), 1, width, height);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}

	@Override
	public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {

		if (tank.getFluid() != null && tank.getFluidAmount() > 0) {
			tooltip.add(StringHelper.getFluidName(tank.getFluid()));
			if (FluidHelper.hasPotionTag(tank.getFluid())) {
				FluidHelper.addPotionTooltip(tank.getFluid(), tooltip, durationFactor);
			}
		}
		if (infinite) {
			tooltip.add(localize("info.cofh.infinite_fluid"));
		} else {
			tooltip.add(formatNumber(tank.getFluidAmount()) + " / " + formatNumber(tank.getCapacity()) + " " + unit);
		}
	}

	protected int getScaled() {

		if (tank.getCapacity() < 0) {
			return height;
		}
		long fraction = (long) tank.getFluidAmount() * height / tank.getCapacity();

		return alwaysShowMinimum && tank.getFluidAmount() > 0 ? Math.max(1, MathHelper.round(fraction)) : MathHelper.round(fraction);
	}

	protected void drawFluid() {

		int amount = getScaled();

		if (fluidTextureOverride != null) {
			RenderHelper.setBlockTextureSheet();
			RenderHelper.drawTiledTexture(posX, posY + height - amount, fluidTextureOverride, width, amount);
		} else {
			RenderHelper.drawFluid(posX, posY + height - amount, tank.getFluid(), width, amount);
		}
	}

}
