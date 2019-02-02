package cofh.redstonearsenal.init;

import cofh.core.item.ItemBlockCoFH;
import cofh.redstonearsenal.block.BlockStorageFlux;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.GROUP_STORAGE;
import static cofh.redstonearsenal.RedstoneArsenal.registerBlock;

public class BlocksRSA {

	private BlocksRSA() {

	}

	// region REGISTRATION
	public static void registerBlocks() {

		blockFluxInfused = registerBlock("block_flux_infused", "blockFluxInfused", new ItemBlockCoFH(new BlockStorageFlux(), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));
		blockFluxCrystal = registerBlock("block_flux_crystal", "blockFluxCrystal", new ItemBlockCoFH(new BlockStorageFlux(), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));
	}
	// endregion

	// region STORAGE
	public static ItemStack blockFluxInfused;
	public static ItemStack blockFluxCrystal;
	// endregion
}
