package cofh.lib.potion;

import cofh.lib.util.helpers.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCoFH extends Potion {

	TextureAtlasSprite icon;

	public PotionCoFH(String id, boolean isBadEffectIn, int liquidColorIn) {

		super(isBadEffectIn, liquidColorIn);
		setRegistryName(id);
	}

	public PotionCoFH setIcon(TextureAtlasSprite icon) {

		this.icon = icon;
		return this;
	}

	@Override
	public String getName() {

		return "effect." + this.getRegistryName().getResourceDomain() + "." + this.getRegistryName().getResourcePath();
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {

		if (icon != null) {
			RenderHelper.setBlockTextureSheet();
			gui.drawTexturedModalRect(x + 7, y + 7, icon, 16, 16);
		}
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, net.minecraft.client.gui.Gui gui, int x, int y, float z, float alpha) {

		if (icon != null) {
			RenderHelper.setBlockTextureSheet();
			gui.drawTexturedModalRect(x + 4, y + 4, icon, 16, 16);
		}
	}

}
