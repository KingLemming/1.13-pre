package cofh.redstonearsenal.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.TOOL_SICKLE;

public class ItemSickleFlux extends ItemToolFlux {

	public int radius = 3;

	public ItemSickleFlux(ToolMaterial toolMaterial) {

		super(-2.6F, toolMaterial);

		setHarvestLevel(TOOL_SICKLE, toolMaterial.getHarvestLevel());

		damage = 4;
		damageEmpowered = 6;
		energyPerUseEmpowered = 1200;

		effectiveMaterials.add(Material.LEAVES);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.VINE);
		effectiveMaterials.add(Material.WEB);
	}

	public ItemSickleFlux setRadius(int radius) {

		this.radius = radius;
		return this;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

		World world = player.world;
		IBlockState state = world.getBlockState(pos);

		if (!canHarvestBlock(state, stack)) {
			useEnergy(stack, player.capabilities.isCreativeMode);
			return false;
		}
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		boolean used = false;
		int boost = isEmpowered(stack) ? 2 : 0;

		if (player.isSneaking()) {
			useEnergy(stack, player.capabilities.isCreativeMode);
			return false;
		}
		for (int i = x - (radius + boost); i <= x + (radius + boost); i++) {
			for (int k = z - (radius + boost); k <= z + (radius + boost); k++) {
				for (int j = y - boost; j <= y + boost; j++) {
					used |= harvestBlock(world, new BlockPos(i, j, k), player);
				}
			}
		}
		if (used) {
			useEnergy(stack, player.capabilities.isCreativeMode);
		}
		return true;
	}

}
