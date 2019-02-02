package cofh.redstonearsenal.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.TOOL_AXE;

public class ItemAxeFlux extends ItemToolFlux {

	public ItemAxeFlux(ToolMaterial toolMaterial) {

		super(-2.8F, toolMaterial);

		setHarvestLevel(TOOL_AXE, toolMaterial.getHarvestLevel());

		damage = 7;
		damageEmpowered = 6;
		energyPerUseEmpowered = 1600;

		effectiveMaterials.add(Material.WOOD);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.VINE);
		effectiveMaterials.add(Material.CACTUS);
		effectiveMaterials.add(Material.GOURD);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		if (EnumEnchantmentType.BREAKABLE.equals(enchantment.type)) {
			return enchantment.equals(Enchantments.UNBREAKING);
		}
		return enchantment.type.canEnchantItem(this) || enchantment.canApply(new ItemStack(Items.IRON_AXE));
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {

		return true;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

		World world = player.world;
		IBlockState state = world.getBlockState(pos);

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		Block block = state.getBlock();

		float refStrength = state.getPlayerRelativeBlockHardness(player, world, pos);
		if (refStrength != 0.0F) {
			if (isEmpowered(stack) && (block.isWood(world, pos) || canHarvestBlock(state, stack))) {
				for (int i = x - 1; i <= x + 1; i++) {
					for (int k = z - 1; k <= z + 1; k++) {
						for (int j = y - 2; j <= y + 2; j++) {
							BlockPos pos2 = new BlockPos(i, j, k);
							block = world.getBlockState(pos2).getBlock();
							if (block.isWood(world, pos2) || canHarvestBlock(state, stack)) {
								harvestBlock(world, pos2, player);
							}
						}
					}
				}
			}
			useEnergy(stack, player.capabilities.isCreativeMode);
		}
		return false;
	}

	// region IAreaEffectItem
	//	@Override
	//	public ImmutableList<BlockPos> getAreaEffectBlocks(ItemStack stack, BlockPos pos, EntityPlayer player) {
	//
	//	}
	// endregion
}
