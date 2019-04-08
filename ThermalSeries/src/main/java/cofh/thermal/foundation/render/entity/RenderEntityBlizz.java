package cofh.thermal.foundation.render.entity;

import cofh.lib.util.helpers.HolidayHelper;
import cofh.thermal.foundation.entity.monster.EntityBlizz;
import cofh.thermal.foundation.render.model.ModelElemental;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

@SideOnly (Side.CLIENT)
public class RenderEntityBlizz extends RenderLiving<EntityBlizz> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(ID_THERMAL_SERIES + ":textures/entity/" + "blizz.png");
	private static final ResourceLocation TEXTURE_XMAS = new ResourceLocation(ID_THERMAL_SERIES + ":textures/entity/" + "blizz_xmas.png");

	public RenderEntityBlizz(RenderManager manager) {

		super(manager, ModelElemental.INSTANCE, 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBlizz par1Entity) {

		return HolidayHelper.isChristmas(7, 3) ? TEXTURE_XMAS : TEXTURE;
	}

}
