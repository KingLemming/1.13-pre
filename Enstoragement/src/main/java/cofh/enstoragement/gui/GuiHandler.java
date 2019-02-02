package cofh.enstoragement.gui;

import cofh.core.block.TileCoFH;
import cofh.core.gui.client.GuiFilterItem;
import cofh.core.gui.container.ContainerFilterItem;
import cofh.enstoragement.gui.client.GuiSatchel;
import cofh.enstoragement.gui.container.ContainerSatchel;
import cofh.enstoragement.item.ItemSatchel;
import cofh.lib.item.IFilterContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import static cofh.lib.util.Constants.GUI_TILE;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiHandler implements IGuiHandler {

	public static final GuiHandler INSTANCE = new GuiHandler();

	public static final int GUI_SATCHEL = 16;
	public static final int GUI_SATCHEL_FILTER = 17;

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		switch (id) {
			case GUI_TILE:
				TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
				if (tile instanceof TileCoFH) {
					return ((TileCoFH) tile).getGuiClient(player.inventory);
				}
				return null;
			case GUI_SATCHEL:
				if (player.getHeldItemMainhand().getItem() instanceof ItemSatchel) {
					return new GuiSatchel(player.inventory, new ContainerSatchel(player.getHeldItemMainhand(), player.inventory));
				}
				return null;
			case GUI_SATCHEL_FILTER:
				if (player.getHeldItemMainhand().getItem() instanceof IFilterContainerItem) {
					return new GuiFilterItem(player.inventory, new ContainerFilterItem(player.getHeldItemMainhand(), player.inventory), generateTabInfo("tab.enstoragement.satchel.filter"));
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
			case GUI_SATCHEL:
				if (player.getHeldItemMainhand().getItem() instanceof ItemSatchel) {
					return new ContainerSatchel(player.getHeldItemMainhand(), player.inventory);
				}
				return null;
			case GUI_SATCHEL_FILTER:
				if (player.getHeldItemMainhand().getItem() instanceof IFilterContainerItem) {
					return new ContainerFilterItem(player.getHeldItemMainhand(), player.inventory);
				}
				return null;
			default:
				return null;
		}
	}

}
