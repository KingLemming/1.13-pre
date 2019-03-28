package cofh.thermal.innovation.proxy;

import cofh.lib.util.helpers.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static cofh.thermal.innovation.init.ItemsTI.*;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);
	}

	@Override
	public void initialize(FMLInitializationEvent event) {

		super.initialize(event);

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ColorHelper::getColor, itemDrillBasic);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ColorHelper::getColor, itemSawBasic);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ColorHelper::getColor, itemCapacitorBasic);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ColorHelper::getColor, itemMagnetBasic);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemInjectorBasic::colorMultiplier, itemInjectorBasic);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemQuiverBasic::colorMultiplier, itemQuiverBasic);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		super.postInit(event);
	}
	// endregion
}
