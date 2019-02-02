package cofh.thermal.core.block.machine;

import cofh.core.gui.client.GuiContainerCoFH;
import cofh.core.gui.container.ContainerCoFH;
import cofh.thermal.core.block.AbstractTileStruct;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class Machine extends AbstractTileStruct {

	public final int light;

	public Machine(String name, int light, Class<? extends TileMachine> tileEntityClass, Class<? extends ContainerCoFH> guiServerClass, Class<? extends GuiContainerCoFH> guiClientClass) {

		super(name, tileEntityClass, guiServerClass, guiClientClass);
		this.light = light;

		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(ID_THERMAL_SERIES + ":machine_" + this.name));
	}

	public int getLight() {

		return light;
	}

}
