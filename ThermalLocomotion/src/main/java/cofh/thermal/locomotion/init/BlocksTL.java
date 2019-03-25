package cofh.thermal.locomotion.init;

import cofh.core.block.rails.BlockRailCoFH;
import cofh.core.item.ItemBlockCoFH;
import cofh.core.block.rails.BlockRailCrossover;
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

		railWood = registerBlock("rail_wood", new ItemBlockCoFH(new BlockRailCoFH().setMaxSpeed(0.2F).setSoundType(SoundType.WOOD), GROUP_RAILS));
		railReinforced = registerBlock("rail_reinforced", new ItemBlockCoFH(new BlockRailCoFH().setMaxSpeed(0.5F).setHardness(25.0F).setResistance(200.0F), GROUP_RAILS));
		railLumium = registerBlock("rail_lumium", new ItemBlockCoFH(new BlockRailCoFH().setLightLevel(15).setMaxSpeed(0.5F), GROUP_RAILS).setRarity(EnumRarity.UNCOMMON));

		railCrossover = registerBlock("rail_crossover", new ItemBlockCoFH(new BlockRailCrossover(), GROUP_RAILS));
	}
	// endregion

	// region RAILS
	public static ItemStack railWood;
	public static ItemStack railReinforced;
	public static ItemStack railLumium;

	public static ItemStack railCrossover;
	// endregion
}
