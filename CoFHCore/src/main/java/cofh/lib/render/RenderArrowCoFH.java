package cofh.lib.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderArrowCoFH extends RenderArrow {

	ResourceLocation res;

	public RenderArrowCoFH(RenderManager renderManagerIn, ResourceLocation res) {

		super(renderManagerIn);
		this.res = res;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {

		return res;
	}

}
