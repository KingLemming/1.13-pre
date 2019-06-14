package cofh.core.gui.element.tab;

import codechicken.newlib.bakedmodel.CachedFormat;
import codechicken.newlib.util.MathHelper;
import codechicken.newlib.vec.Matrix4;
import codechicken.newlib.vec.Rotation;
import codechicken.newlib.vec.Vector3;
import cofh.core.gui.IGuiAccess;
import cofh.lib.util.control.IReconfigurable.SideConfig;
import cofh.lib.util.control.IReconfigurableTile;
import cofh.lib.util.control.ITransferControllable;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.RenderHelper;
import cofh.lib.util.helpers.SoundHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static cofh.core.gui.TexturesCoFH.*;
import static cofh.lib.util.helpers.StringHelper.localize;

public class TabConfiguration extends TabBase {

	//Hold the Matrices for converting between in-world face and gui.
	private static final EnumMap<EnumFacing, Matrix4> faceMatrices = new EnumMap<>(EnumFacing.class);

	public static int defaultSide = RIGHT;
	public static int defaultHeaderColor = 0xe1c92f;
	public static int defaultSubHeaderColor = 0xaaafb8;
	public static int defaultTextColor = 0x000000;
	public static int defaultBackgroundColor = 0x226688;

	static {
		Matrix4 mat; //Storage

		mat = new Matrix4(); // Above - Up
		mat.scale(16, 16, 0);
		mat.apply(Rotation.sideRotations[EnumFacing.UP.ordinal()].inverse());
		mat.apply(Rotation.sideRotations[EnumFacing.NORTH.ordinal()]);
		faceMatrices.put(EnumFacing.UP, mat);

		mat = new Matrix4(); // Left - East
		mat.scale(16, 16, 0);
		mat.apply(new Rotation(MathHelper.torad * -90F, new Vector3(0, 1, 0)).at(Vector3.center));
		mat.apply(new Rotation(MathHelper.torad * 180F, new Vector3(0, 0, 1)).at(Vector3.center));
		faceMatrices.put(EnumFacing.EAST, mat);

		mat = new Matrix4(); // Facing - North
		mat.scale(16, 16, 0);
		mat.apply(new Rotation(MathHelper.torad * 180F, new Vector3(0, 0, 1)).at(Vector3.center));
		faceMatrices.put(EnumFacing.NORTH, mat);

		mat = new Matrix4();//Right - West
		mat.scale(16, 16, 0);
		mat.apply(new Rotation(MathHelper.torad * 90F, new Vector3(0, 1, 0)).at(Vector3.center));
		mat.apply(new Rotation(MathHelper.torad * 180F, new Vector3(0, 0, 1)).at(Vector3.center));
		faceMatrices.put(EnumFacing.WEST, mat);

		mat = new Matrix4(); // Below - Down
		mat.translate(0, 16, 0);
		mat.scale(16, 16, 0);
		mat.apply(Rotation.sideRotations[EnumFacing.DOWN.ordinal()].inverse());
		mat.apply(Rotation.sideRotations[EnumFacing.NORTH.ordinal()]);
		faceMatrices.put(EnumFacing.DOWN, mat);

		mat = new Matrix4(); // Opposite - South
		mat.scale(16, 16, 0);
		mat.apply(new Rotation(MathHelper.torad * 180F, new Vector3(0, 1, 0)).at(Vector3.center));
		mat.apply(new Rotation(MathHelper.torad * 180F, new Vector3(0, 0, 1)).at(Vector3.center));
		faceMatrices.put(EnumFacing.SOUTH, mat);
	}

	private IReconfigurableTile myReconfig;
	private ITransferControllable myTransfer;

	public TabConfiguration(IGuiAccess gui, IReconfigurableTile reconfig) {

		this(gui, defaultSide, reconfig, null);
	}

	public TabConfiguration(IGuiAccess gui, IReconfigurableTile reconfig, ITransferControllable transfer) {

		this(gui, defaultSide, reconfig, transfer);
	}

	public TabConfiguration(IGuiAccess gui, int side, IReconfigurableTile reconfig, ITransferControllable transfer) {

		super(gui, side);

		headerColor = defaultHeaderColor;
		subheaderColor = defaultSubHeaderColor;
		textColor = defaultTextColor;
		backgroundColor = defaultBackgroundColor;

		maxHeight = 92;
		maxWidth = 112;
		myReconfig = reconfig;
		myTransfer = transfer;

		this.setVisible(myReconfig::isReconfigurable);
	}

	@Override
	protected void drawForeground() {

		drawTabIcon(ICON_CONFIG);
		if (!fullyOpen) {
			return;
		}
		getFontRenderer().drawStringWithShadow(localize("info.cofh.configuration"), sideOffset() + 18, 6, headerColor);

		RenderHelper.setBlockTextureSheet();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if (myTransfer != null && myTransfer.hasTransferIn()) {
			TextureAtlasSprite bs = myTransfer.getTransferIn() ? ICON_BUTTON_HIGHLIGHT : ICON_BUTTON;
			gui.drawIcon(bs, 8, 34);
			gui.drawIcon(ICON_INPUT, 8, 34);
		} else {
			gui.drawIcon(ICON_BUTTON_INACTIVE, 8, 34);
			gui.drawIcon(ICON_INPUT, 8, 34);
		}
		if (myTransfer != null && myTransfer.hasTransferOut()) {
			TextureAtlasSprite bs = myTransfer.getTransferOut() ? ICON_BUTTON_HIGHLIGHT : ICON_BUTTON;
			gui.drawIcon(bs, 8, 54);
			gui.drawIcon(ICON_OUTPUT, 8, 54);
		} else {
			gui.drawIcon(ICON_BUTTON_INACTIVE, 8, 54);
			gui.drawIcon(ICON_OUTPUT, 8, 54);
		}

		try {
			IBlockState state = myReconfig.world().getBlockState(myReconfig.pos());
			Block block = state.getBlock();

			try {
				state = state.getActualState(myReconfig.world(), myReconfig.pos());
			} catch (Throwable ignored) {
			}
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
			IBlockState extendedState = block.getExtendedState(state, myReconfig.world(), myReconfig.pos());
			EnumMap<EnumFacing, List<BakedQuad>> faceQuads = new EnumMap<>(EnumFacing.class);
			BlockRenderLayer prevLayer = MinecraftForgeClient.getRenderLayer();
			for (EnumFacing face : EnumFacing.VALUES) {
				List<BakedQuad> quads = new ArrayList<>();
				for (BlockRenderLayer layer : BlockRenderLayer.values()) {
					ForgeHooksClient.setRenderLayer(layer);
					if (block.canRenderInLayer(state, layer)) {
						quads.addAll(model.getQuads(extendedState, face, 0));
					}
				}
				faceQuads.put(face, quads);
			}
			ForgeHooksClient.setRenderLayer(prevLayer);

			Tessellator tess = Tessellator.getInstance();
			BufferBuilder buffer = tess.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			IVertexConsumer consumer = new VertexBufferConsumer(buffer);
			bufferBlockFace(consumer, BlockHelper.above(myReconfig.getFacing()), 52, 24, faceQuads);
			bufferBlockFace(consumer, BlockHelper.left(myReconfig.getFacing()), 32, 44, faceQuads);
			bufferBlockFace(consumer, myReconfig.getFacing(), 52, 44, faceQuads);
			bufferBlockFace(consumer, BlockHelper.right(myReconfig.getFacing()), 72, 44, faceQuads);
			bufferBlockFace(consumer, BlockHelper.below(myReconfig.getFacing()), 52, 64, faceQuads);
			bufferBlockFace(consumer, BlockHelper.opposite(myReconfig.getFacing()), 72, 64, faceQuads);

			tess.draw();

		} catch (Throwable t) {
			t.printStackTrace();
		}

		GlStateManager.disableBlend();
		RenderHelper.setDefaultFontTextureSheet();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void drawBackground() {

		super.drawBackground();

		if (!fullyOpen) {
			return;
		}
		float colorR = (backgroundColor >> 16 & 255) / 255.0F * 0.6F;
		float colorG = (backgroundColor >> 8 & 255) / 255.0F * 0.6F;
		float colorB = (backgroundColor & 255) / 255.0F * 0.6F;
		GlStateManager.color(colorR, colorG, colorB, 1.0F);

		if (myTransfer == null) {
			gui.drawTexturedModalRect(16, 20, 16, 20, 64, 64);
		} else {
			gui.drawTexturedModalRect(28, 20, 16, 20, 64, 64);
			gui.drawTexturedModalRect(6, 32, 16, 20, 20, 40);
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {

		if (!fullyOpen) {
			tooltip.add(localize("info.cofh.configuration"));
			return;
		}
		if (myTransfer == null) {
			return;
		}
		int x = mouseX - this.posX();
		int y = mouseY - this.posY;

		if (8 <= x && x < 24 && 34 <= y && y < 50) {
			if (myTransfer.hasTransferIn()) {
				tooltip.add(myTransfer.getTransferIn() ? localize("info.cofh.transfer_in_enabled") : localize("info.cofh.transfer_in_disabled"));
			} else {
				tooltip.add(localize("info.cofh.transfer_in_unavailable"));
			}
		} else if (8 <= x && x < 24 && 54 <= y && y < 68) {
			if (myTransfer.hasTransferOut()) {
				tooltip.add(myTransfer.getTransferOut() ? localize("info.cofh.transfer_out_enabled") : localize("info.cofh.transfer_out_disabled"));
			} else {
				tooltip.add(localize("info.cofh.transfer_out_unavailable"));
			}
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (!fullyOpen) {
			return false;
		}
		int x = mouseX - this.posX();
		int y = mouseY - this.posY;

		return myTransfer != null ? clickTransfer(x, y, mouseButton) : clickNoTransfer(x, y, mouseButton);
	}

	protected boolean clickNoTransfer(int x, int y, int mouseButton) {

		if (x < 16 || x >= 80 || y < 20 || y >= 84) {
			return false;
		}
		if (myTransfer != null) {
			if (40 <= x && x < 68 && 24 <= y && y < 40) {
				handleSideChange(BlockHelper.above(myReconfig.getFacing()), mouseButton);
			} else if (20 <= x && x < 36 && 44 <= y && y < 60) {
				handleSideChange(BlockHelper.left(myReconfig.getFacing()), mouseButton);
			} else if (40 <= x && x < 56 && 44 <= y && y < 60) {
				handleSideChange(myReconfig.getFacing(), mouseButton);
			} else if (60 <= x && x < 76 && 44 <= y && y < 60) {
				handleSideChange(BlockHelper.right(myReconfig.getFacing()), mouseButton);
			} else if (40 <= x && x < 56 && 64 <= y && y < 80) {
				handleSideChange(BlockHelper.below(myReconfig.getFacing()), mouseButton);
			} else if (60 <= x && x < 76 && 64 <= y && y < 80) {
				handleSideChange(BlockHelper.opposite(myReconfig.getFacing()), mouseButton);
			}
		}
		return true;
	}

	protected boolean clickTransfer(int x, int y, int mouseButton) {

		if (x < 4 || x >= 92 || y < 20 || y >= 84) {
			return false;
		}
		if (8 <= x && x < 24 && 34 <= y && y < 50) {
			handleTransferChange(true);
		} else if (8 <= x && x < 24 && 54 <= y && y < 68) {
			handleTransferChange(false);
		}
		if (myTransfer != null) {
			if (52 <= x && x < 68 && 24 <= y && y < 40) {
				handleSideChange(BlockHelper.above(myReconfig.getFacing()), mouseButton);
			} else if (32 <= x && x < 48 && 44 <= y && y < 60) {
				handleSideChange(BlockHelper.left(myReconfig.getFacing()), mouseButton);
			} else if (52 <= x && x < 68 && 44 <= y && y < 60) {
				handleSideChange(myReconfig.getFacing(), mouseButton);
			} else if (72 <= x && x < 88 && 44 <= y && y < 60) {
				handleSideChange(BlockHelper.right(myReconfig.getFacing()), mouseButton);
			} else if (52 <= x && x < 68 && 64 <= y && y < 80) {
				handleSideChange(BlockHelper.below(myReconfig.getFacing()), mouseButton);
			} else if (72 <= x && x < 88 && 64 <= y && y < 80) {
				handleSideChange(BlockHelper.opposite(myReconfig.getFacing()), mouseButton);
			}
		}
		return true;
	}

	protected void handleTransferChange(boolean input) {

		if (input) {
			if (myTransfer.hasTransferIn()) {
				myTransfer.setControl(!myTransfer.getTransferIn(), myTransfer.getTransferOut());
				SoundHelper.playClickSound(myTransfer.getTransferIn() ? 0.8F : 0.4F);
			}
		} else {
			if (myTransfer.hasTransferOut()) {
				myTransfer.setControl(myTransfer.getTransferIn(), !myTransfer.getTransferOut());
				SoundHelper.playClickSound(myTransfer.getTransferOut() ? 0.8F : 0.4F);
			}
		}
	}

	protected void handleSideChange(EnumFacing side, int mouseButton) {

		if (GuiScreen.isShiftKeyDown()) {
			if (side == myReconfig.getFacing()) {
				if (myReconfig.clearAllSides()) {
					SoundHelper.playClickSound(0.2F);
				}
			} else if (myReconfig.setSideConfig(side, SideConfig.SIDE_NONE)) {
				SoundHelper.playClickSound(0.4F);
			}
			myReconfig.callBlockUpdate();
			return;
		}
		if (mouseButton == 0) {
			if (myReconfig.nextSideConfig(side)) {
				SoundHelper.playClickSound(0.8F);
			}
		} else if (mouseButton == 1) {
			if (myReconfig.prevSideConfig(side)) {
				SoundHelper.playClickSound(0.6F);
			}
		}
		myReconfig.callBlockUpdate();
	}

	private void bufferBlockFace(IVertexConsumer parent, EnumFacing face, int xOff, int yOff, EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {

		Transformer transformer = new Transformer(parent, faceMatrices.get(face), xOff, yOff);

		for (BakedQuad quad : faceQuads.get(face)) {
			quad.pipe(transformer);
		}
	}

	private static class Transformer extends VertexTransformer {

		private final Vector3 vec = new Vector3();
		private final CachedFormat format;
		private final Matrix4 matrix;
		private final int xOff;
		private final int yOff;

		public Transformer(IVertexConsumer parent, Matrix4 matrix, int xOff, int yOff) {

			super(parent);
			format = CachedFormat.lookup(parent.getVertexFormat());
			this.matrix = matrix;
			this.xOff = xOff;
			this.yOff = yOff;
		}

		@Override
		public void put(int element, float... data) {

			if (element == format.positionIndex) {
				vec.set(Vector3.zero);
				vec.set(data);
				matrix.apply(vec);
				vec.add(xOff, yOff, 0);
				super.put(element, (float) vec.x, (float) vec.y, (float) vec.z, 0);
			} else {
				super.put(element, data);
			}
		}
	}

}
