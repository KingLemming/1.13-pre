package cofh.orbulation.render.entity;

import cofh.orbulation.entity.projectile.EntityMorb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.MathHelper.clamp;
import static cofh.orbulation.Orbulation.itemMorb;
import static cofh.orbulation.Orbulation.itemMorbReusable;

public class RenderEntityMorb extends Render<EntityMorb> {

	public RenderEntityMorb(RenderManager renderManager) {

		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMorb entity) {

		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender(EntityMorb morb, double d0, double d1, double d2, float f, float f1) {

		GlStateManager.pushMatrix();
		GlStateManager.translate(d0, d1, d2);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		int type = clamp(morb.getType(), 0, 1);

		ItemStack stack = type == 1 ? cloneStack(itemMorbReusable) : cloneStack(itemMorb);
		stack.setTagCompound(morb.getEntityData());
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.GROUND);

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
