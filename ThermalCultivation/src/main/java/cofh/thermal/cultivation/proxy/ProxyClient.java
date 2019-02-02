package cofh.thermal.cultivation.proxy;

import cofh.lib.util.helpers.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static cofh.thermal.cultivation.init.ItemsTC.itemWateringCanBasic;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);
	}

	@Override
	public void initialize(FMLInitializationEvent event) {

		super.initialize(event);

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ColorHelper::getColor, itemWateringCanBasic);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		super.postInit(event);
	}
	// endregion
}
