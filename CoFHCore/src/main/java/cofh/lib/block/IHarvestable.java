package cofh.lib.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHarvestable {

	boolean canHarvest(IBlockState state);

	boolean harvest(World world, BlockPos pos, IBlockState state, int fortune);

	default boolean harvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		return harvest(world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand()));
	}

}
