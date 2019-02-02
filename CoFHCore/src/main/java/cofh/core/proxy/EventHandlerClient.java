package cofh.core.proxy;

import cofh.core.fluid.BlockFluidCoFH;
import cofh.core.gui.TexturesCoFH;
import cofh.lib.util.RayTracer;
import cofh.lib.util.helpers.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly (Side.CLIENT)
public class EventHandlerClient {

	private static final EventHandlerClient INSTANCE = new EventHandlerClient();
	private static boolean registered = false;

	public static void register() {

		if (registered) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registered = true;
	}

	private static final ResourceLocation UNDERWATER_GRAYSCALE = new ResourceLocation("cofh:textures/misc/underwater_grayscale.png");

	private EventHandlerClient() {

	}

	@SubscribeEvent
	public void handleFogDensityEvent(EntityViewRenderEvent.FogDensity event) {

		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			Vec3d playerEyePos = RayTracer.getCorrectedHeadVec(player);
			BlockPos pos = new BlockPos(playerEyePos);
			if (player.world.getBlockState(pos).getBlock() instanceof BlockFluidCoFH) {
				event.setCanceled(true);
				GlStateManager.setFog(GlStateManager.FogMode.EXP);
			}
		}
	}

	@SubscribeEvent
	public void handleFluidBlockOverlayEvent(RenderBlockOverlayEvent event) {

		if (event.getOverlayType() == OverlayType.WATER) {
			EntityPlayer player = event.getPlayer();
			Vec3d playerEyePos = RayTracer.getCorrectedHeadVec(player);
			BlockPos pos = new BlockPos(playerEyePos);
			IBlockState state = player.world.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof BlockFluidCoFH) {
				RenderHelper.bindTexture(UNDERWATER_GRAYSCALE);
				float brightness = player.getBrightness();
				Vec3d color = block.getFogColor(player.world, pos, state, player, new Vec3d(1, 1, 1), 0.0F).scale(brightness);

				GlStateManager.color((float) color.x, (float) color.y, (float) color.z, 1.0F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
				GlStateManager.pushMatrix();

				float yaw = -player.rotationYaw / 64.0F;
				float pitch = player.rotationPitch / 64.0F;

				Tessellator t = Tessellator.getInstance();
				BufferBuilder buffer = t.getBuffer();

				buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX);
				buffer.pos(-1.0D, -1.0D, -0.5D).tex(4.0F + yaw, 4.0F + pitch).endVertex();
				buffer.pos(1.0D, -1.0D, -0.5D).tex(0.0F + yaw, 4.0F + pitch).endVertex();
				buffer.pos(1.0D, 1.0D, -0.5D).tex(0.0F + yaw, 0.0F + pitch).endVertex();
				buffer.pos(-1.0D, 1.0D, -0.5D).tex(4.0F + yaw, 0.0F + pitch).endVertex();
				t.draw();

				GlStateManager.popMatrix();
				GlStateManager.disableBlend();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void handleTextureStitchPreEvent(TextureStitchEvent.Pre event) {

		TexturesCoFH.registerIcons(event.getMap());
	}

}
