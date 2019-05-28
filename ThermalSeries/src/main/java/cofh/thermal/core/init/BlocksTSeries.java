package cofh.thermal.core.init;

import cofh.core.block.deco.BlockGlassHard;
import cofh.core.block.storage.BlockStorageMetal;
import cofh.core.block.storage.BlockStorageResource;
import cofh.core.item.ItemBlockCoFH;
import cofh.core.item.ItemBlockFuel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static cofh.lib.util.Constants.*;
import static cofh.thermal.core.ThermalSeries.registerBlock;
import static cofh.thermal.core.init.ConfigTSeries.fuelCoal;
import static cofh.thermal.core.init.ConfigTSeries.fuelCoalCoke;
import static net.minecraft.item.EnumRarity.RARE;

public class BlocksTSeries {

	private BlocksTSeries() {

	}

	// region REGISTRATION
	public static void registerBlocks() {

		String pickaxe = TOOL_PICKAXE;

		storageCharcoal = registerBlock("block_charcoal", "blockCharcoal", new ItemBlockFuel(new BlockStorageResource() {

			@Override
			public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

			@Override
			public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

		}, GROUP_STORAGE, fuelCoal * 10));
		storageCoalCoke = registerBlock("block_coal_coke", "blockFuelCoke", new ItemBlockFuel(new BlockStorageResource() {

			@Override
			public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

			@Override
			public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

				return 5;
			}

		}, GROUP_STORAGE, fuelCoalCoke * 10));

		storageSignalum = registerBlock("block_signalum", "blockSignalum", new ItemBlockCoFH(new BlockStorageMetal() {

			@Override
			public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

				return 15;
			}

			@Override
			public boolean canProvidePower(IBlockState state) {

				return true;
			}

		}.setLightLevel(7), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));
		storageLumium = registerBlock("block_lumium", "blockLumium", new ItemBlockCoFH(new BlockStorageMetal().setLightLevel(15), GROUP_STORAGE).setRarity(EnumRarity.UNCOMMON));
		storageEnderium = registerBlock("block_enderium", "blockEnderium", new ItemBlockCoFH(new BlockStorageMetal().setHarvestParams(pickaxe, 3).setLightLevel(3).setHardness(25.0F).setResistance(50.0F), GROUP_STORAGE).setRarity(RARE));

		glassQuartz = registerBlock("glass_quartz", "blockGlassHardened", new ItemBlockCoFH(new BlockGlassHard(), GROUP_DECO));
	}

	// region REFERENCES
	public static ItemStack storageCharcoal;
	public static ItemStack storageCoalCoke;

	public static ItemStack storageSignalum;
	public static ItemStack storageLumium;
	public static ItemStack storageEnderium;

	public static ItemStack glassQuartz;
	// endregion
}
