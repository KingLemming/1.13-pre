package cofh.thermal.locomotion.init;

import cofh.core.block.rails.BlockRailCrossover;
import cofh.core.block.rails.BlockRailDefault;
import cofh.core.item.ItemBlockCoFH;
import net.minecraft.block.SoundType;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.GROUP_RAILS;
import static cofh.thermal.core.ThermalSeries.registerBlock;

public class BlocksTL {

	private BlocksTL() {

	}

	// region REGISTRATION
	public static void registerBlocks() {

		railCrossover = registerBlock("rail_crossover", new ItemBlockCoFH(new BlockRailCrossover(), GROUP_RAILS));

		railWood = registerBlock("rail_wood", new ItemBlockCoFH(new BlockRailDefault().setMaxSpeed(0.2F).setSoundType(SoundType.WOOD), GROUP_RAILS));
		railWoodCrossover = registerBlock("rail_wood_crossover", new ItemBlockCoFH(new BlockRailCrossover().setMaxSpeed(0.2F).setSoundType(SoundType.WOOD), GROUP_RAILS));

		railReinforced = registerBlock("rail_reinforced", new ItemBlockCoFH(new BlockRailDefault().setMaxSpeed(0.6F).setHardness(25.0F).setResistance(200.0F), GROUP_RAILS));
		railReinforcedCrossover = registerBlock("rail_reinforced_crossover", new ItemBlockCoFH(new BlockRailCrossover().setMaxSpeed(0.6F).setHardness(25.0F).setResistance(200.0F), GROUP_RAILS));

		railLumium = registerBlock("rail_lumium", new ItemBlockCoFH(new BlockRailDefault().setLightLevel(15).setMaxSpeed(0.6F).setHardness(10.0F), GROUP_RAILS).setRarity(EnumRarity.UNCOMMON));
		railLumiumCrossover = registerBlock("rail_lumium_crossover", new ItemBlockCoFH(new BlockRailCrossover().setLightLevel(15).setMaxSpeed(0.6F).setHardness(10.0F), GROUP_RAILS).setRarity(EnumRarity.UNCOMMON));
	}
	// endregion

	// region RAILS
	public static ItemStack railCrossover;

	public static ItemStack railWood;
	public static ItemStack railWoodCrossover;

	public static ItemStack railReinforced;
	public static ItemStack railReinforcedCrossover;

	public static ItemStack railLumium;
	public static ItemStack railLumiumCrossover;
	// endregion
}
