package cofh.thermal.core.block;

import cofh.core.block.TileCoFH;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.container.ContainerCoFH;
import cofh.thermal.core.ThermalSeries;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class AbstractTileStruct {

	public final String name;

	public final Class<? extends TileCoFH> tileEntityClass;
	public final Class<? extends ContainerCoFH> guiServerClass;
	public final Class<? extends GuiContainerCoFH> guiClientClass;

	public AbstractTileStruct(String name, Class<? extends TileCoFH> tileEntityClass, Class<? extends ContainerCoFH> guiServerClass, Class<? extends GuiContainerCoFH> guiClientClass) {

		this.name = name;

		this.tileEntityClass = tileEntityClass;
		this.guiServerClass = guiServerClass;
		this.guiClientClass = guiClientClass;
	}

	public String getName() {

		return name;
	}

	public TileEntity createTileEntity(World world, IBlockState state) {

		try {
			return tileEntityClass.getConstructor().newInstance();
		} catch (Exception e) {
			ThermalSeries.log.error("Unable to create instance of TileEntity from {}.", tileEntityClass.getName());
			e.printStackTrace();
			return null;
		}
	}

	public Object getGuiClient(InventoryPlayer inventory, TileEntity tile) {

		try {
			return guiClientClass.getConstructor(InventoryPlayer.class, TileEntity.class).newInstance(inventory, tile);
		} catch (Exception e) {
			ThermalSeries.log.error("Unable to create instance of Gui from {}.", tileEntityClass.getName());
			e.printStackTrace();
		}
		return null;
	}

	public Object getGuiServer(InventoryPlayer inventory, TileEntity tile) {

		try {
			return guiServerClass.getConstructor(InventoryPlayer.class, TileEntity.class).newInstance(inventory, tile);
		} catch (Exception e) {
			ThermalSeries.log.error("Unable to create instance of Container from {}.", tileEntityClass.getName());
			e.printStackTrace();
		}
		return null;
	}

}
