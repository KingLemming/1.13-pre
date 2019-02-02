package cofh.thermal.core.gui;

import cofh.core.block.TileCoFH;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermal.core.gui.client.GuiLexiconStudy;
import cofh.thermal.core.gui.client.GuiLexiconTransmute;
import cofh.thermal.core.gui.container.ContainerLexiconStudy;
import cofh.thermal.core.gui.container.ContainerLexiconTransmute;
import cofh.thermal.core.init.ItemsTSeries;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import static cofh.lib.util.Constants.GUI_TILE;

public class GuiHandler implements IGuiHandler {

	public static final GuiHandler INSTANCE = new GuiHandler();

	public static final int GUI_LEXICON_STUDY = 12;
	public static final int GUI_LEXICON_TRANSMUTE = 13;

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		switch (id) {
			case GUI_TILE:
				TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
				if (tile instanceof TileCoFH) {
					return ((TileCoFH) tile).getGuiClient(player.inventory);
				}
				return null;
			case GUI_LEXICON_STUDY:
				if (ItemHelper.itemsEqual(player.getHeldItemMainhand(), ItemsTSeries.utilTomeLexicon)) {
					return new GuiLexiconStudy(player.inventory, new ContainerLexiconStudy(player.getHeldItemMainhand(), player.inventory));
				}
				return null;
			case GUI_LEXICON_TRANSMUTE:
				if (ItemHelper.itemsEqual(player.getHeldItemMainhand(), ItemsTSeries.utilTomeLexicon)) {
					return new GuiLexiconTransmute(player.inventory, new ContainerLexiconTransmute(player.inventory));
				}
				return null;
			default:
				return null;
		}
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		switch (id) {
			case GUI_TILE:
				TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
				if (tile instanceof TileCoFH) {
					return ((TileCoFH) tile).getGuiServer(player.inventory);
				}
				return null;
			case GUI_LEXICON_STUDY:
				if (ItemHelper.itemsEqual(player.getHeldItemMainhand(), ItemsTSeries.utilTomeLexicon)) {
					return new ContainerLexiconStudy(player.getHeldItemMainhand(), player.inventory);
				}
				return null;
			case GUI_LEXICON_TRANSMUTE:
				if (ItemHelper.itemsEqual(player.getHeldItemMainhand(), ItemsTSeries.utilTomeLexicon)) {
					return new ContainerLexiconTransmute(player.inventory);
				}
				return null;
			default:
				return null;
		}
	}

}
