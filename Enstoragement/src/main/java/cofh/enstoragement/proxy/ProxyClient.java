package cofh.enstoragement.proxy;

import cofh.lib.util.helpers.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static cofh.enstoragement.Enstoragement.registrar;
import static cofh.enstoragement.init.ItemsEnstor.*;

public class ProxyClient extends ProxyCommon {

	// region INITIALIZATION
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		super.preInit(event);
	}

	@Override
	public void initialize(FMLInitializationEvent event) {

		super.initialize(event);

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ColorHelper::getColor, itemSatchelLeather, itemSatchelIron, itemSatchelGold, itemSatchelDiamond, itemSatchelEmerald, itemSatchelCreative);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		super.postInit(event);
	}
	// endregion

	// region HELPERS
	@Override
	public void registerBlockModels() {

		registrar.registerBlockModels();
	}

	@Override
	public void registerItemModels() {

		registrar.registerItemModels();
	}
	// endregion
}
