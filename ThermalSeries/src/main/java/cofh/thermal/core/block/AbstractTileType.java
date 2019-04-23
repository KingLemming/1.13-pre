package cofh.thermal.core.block;

import cofh.core.block.TileCoFH;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.container.ContainerCoFH;
import cofh.thermal.core.ThermalSeries;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.function.Supplier;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

/**
 * This isn't an Enum for modularity reasons.
 */
public class AbstractTileType {

	public final String name;
	public final int light;

	public final Class<? extends TileCoFH> tileEntityClass;
	public final Class<? extends ContainerCoFH> guiServerClass;
	public final Supplier<Class<? extends GuiContainerCoFH>> guiClientClass;

	private AbstractTileType(String name, int light, Class<? extends TileCoFH> tileEntityClass, Class<? extends ContainerCoFH> guiServerClass, Supplier<Class<? extends GuiContainerCoFH>> guiClientClass) {

		this.name = name;
		this.light = light;

		this.tileEntityClass = tileEntityClass;
		this.guiServerClass = guiServerClass;
		this.guiClientClass = guiClientClass;
	}

	public String getName() {

		return name;
	}

	public int getLight() {

		return light;
	}

	public TileEntity createTileEntity(World world, IBlockState state) {

		if (tileEntityClass == null) {
			return null;
		}
		try {
			return tileEntityClass.getConstructor().newInstance();
		} catch (Exception e) {
			ThermalSeries.log.error("Unable to create instance of TileEntity from {}.", tileEntityClass.getName());
			e.printStackTrace();
			return null;
		}
	}

	public Object getGuiClient(InventoryPlayer inventory, TileEntity tile) {

		if (guiClientClass == null) {
			return null;
		}
		try {
			return guiClientClass.get().getConstructor(InventoryPlayer.class, TileEntity.class).newInstance(inventory, tile);
		} catch (Exception e) {
			ThermalSeries.log.error("Unable to create instance of Gui from {}.", tileEntityClass.getName());
			e.printStackTrace();
		}
		return null;
	}

	public Object getGuiServer(InventoryPlayer inventory, TileEntity tile) {

		if (guiServerClass == null) {
			return null;
		}
		try {
			return guiServerClass.getConstructor(InventoryPlayer.class, TileEntity.class).newInstance(inventory, tile);
		} catch (Exception e) {
			ThermalSeries.log.error("Unable to create instance of Container from {}.", tileEntityClass.getName());
			e.printStackTrace();
		}
		return null;
	}

	// region HELPERS
	public static AbstractTileType registerMachine(String name, int light, Class<? extends TileCoFH> tileEntityClass, Class<? extends ContainerCoFH> guiServerClass, Supplier<Class<? extends GuiContainerCoFH>> guiClientClass) {

		AbstractTileType ret = new AbstractTileType(name, light, tileEntityClass, guiServerClass, guiClientClass);
		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(ID_THERMAL_SERIES + ":machine_" + ret.name));
		return ret;
	}

	public static AbstractTileType registerDynamo(String name, int light, Class<? extends TileCoFH> tileEntityClass, Class<? extends ContainerCoFH> guiServerClass, Supplier<Class<? extends GuiContainerCoFH>> guiClientClass) {

		AbstractTileType ret = new AbstractTileType(name, light, tileEntityClass, guiServerClass, guiClientClass);
		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(ID_THERMAL_SERIES + ":dynamo_" + ret.name));
		return ret;
	}
	// endregion
}
