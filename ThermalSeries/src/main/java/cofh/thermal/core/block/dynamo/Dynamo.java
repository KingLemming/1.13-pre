package cofh.thermal.core.block.dynamo;

import cofh.core.block.TileCoFH;
import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.container.ContainerCoFH;
import cofh.thermal.core.block.AbstractTileStruct;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class Dynamo extends AbstractTileStruct {

	public Dynamo(String name, Class<? extends TileCoFH> tileEntityClass, Class<? extends ContainerCoFH> guiServerClass, Class<? extends GuiContainerCoFH> guiClientClass) {

		super(name, tileEntityClass, guiServerClass, guiClientClass);

		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(ID_THERMAL_SERIES + ":dynamo_" + this.name));
	}

}
