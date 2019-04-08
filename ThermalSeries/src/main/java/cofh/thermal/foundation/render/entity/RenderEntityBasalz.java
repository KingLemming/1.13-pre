package cofh.thermal.foundation.render.entity;

import cofh.lib.util.helpers.HolidayHelper;
import cofh.thermal.foundation.entity.monster.EntityBasalz;
import cofh.thermal.foundation.render.model.ModelElemental;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

@SideOnly (Side.CLIENT)
public class RenderEntityBasalz extends RenderLiving<EntityBasalz> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(ID_THERMAL_SERIES + ":textures/entity/" + "basalz.png");
	private static final ResourceLocation TEXTURE_XMAS = new ResourceLocation(ID_THERMAL_SERIES + ":textures/entity/" + "basalz_xmas.png");

	public RenderEntityBasalz(RenderManager renderManager) {

		super(renderManager, ModelElemental.INSTANCE, 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBasalz entity) {

		return HolidayHelper.isChristmas(7, 3) ? TEXTURE_XMAS : TEXTURE;
	}

}
