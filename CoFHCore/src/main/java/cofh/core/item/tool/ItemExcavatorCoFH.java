package cofh.core.item.tool;

import cofh.lib.item.IAreaEffectItem;
import cofh.lib.util.RayTracer;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;

import static cofh.lib.util.Constants.*;

public class ItemExcavatorCoFH extends ItemToolCoFH implements IAreaEffectItem {

	public ItemExcavatorCoFH(ToolMaterial toolMaterial) {

		super(2.0F, -3.2F, toolMaterial);

		setHarvestLevel(TOOL_SHOVEL, toolMaterial.getHarvestLevel());
		setHarvestLevel(TOOL_EXCAVATOR, toolMaterial.getHarvestLevel());
		setMaxDamage(toolMaterial.getMaxUses() * 2);

		effectiveMaterials.add(Material.GROUND);
		effectiveMaterials.add(Material.GRASS);
		effectiveMaterials.add(Material.SAND);
		effectiveMaterials.add(Material.SNOW);
		effectiveMaterials.add(Material.CRAFTED_SNOW);
		effectiveMaterials.add(Material.CLAY);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.canApply(new ItemStack(Items.IRON_SHOVEL));
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

		World world = player.world;
		IBlockState state = world.getBlockState(pos);

		if (state.getBlockHardness(world, pos) == 0.0F) {
			return false;
		}
		if (!canHarvestBlock(state, stack)) {
			if (!player.capabilities.isCreativeMode) {
				stack.damageItem(1, player);
			}
			return false;
		}
		if (player.isSneaking()) {
			if (!player.capabilities.isCreativeMode) {
				stack.damageItem(1, player);
			}
			return false;
		}
		float refStrength = state.getPlayerRelativeBlockHardness(player, world, pos);
		float maxStrength = refStrength / AOE_BREAK_FACTOR;

		if (refStrength != 0.0F) {
			RayTraceResult traceResult = RayTracer.retrace(player, false);

			if (traceResult == null || traceResult.sideHit == null) {
				return false;
			}
			BlockPos adjPos;
			IBlockState adjState;
			float strength;
			int count = 0;

			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			int radius = 1;

			switch (traceResult.sideHit) {
				case DOWN:
				case UP:
					for (int i = x - radius; i <= x + radius; i++) {
						for (int k = z - radius; k <= z + radius; k++) {
							if (i == x && k == z) {
								continue;
							}
							adjPos = new BlockPos(i, y, k);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength >= maxStrength) {
								if (harvestBlock(world, adjPos, player) && strength > 0F) {
									count++;
								}
							}
						}
					}
					break;
				case NORTH:
				case SOUTH:
					for (int i = x - radius; i <= x + radius; i++) {
						for (int j = y - radius; j <= y + radius; j++) {
							if (i == x && j == y) {
								continue;
							}
							adjPos = new BlockPos(i, j, z);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength >= maxStrength) {
								if (harvestBlock(world, adjPos, player) && strength > 0F) {
									count++;
								}
							}
						}
					}
					break;
				case WEST:
				case EAST:
					for (int j = y - radius; j <= y + radius; j++) {
						for (int k = z - radius; k <= z + radius; k++) {
							if (j == y && k == z) {
								continue;
							}
							adjPos = new BlockPos(x, j, k);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength >= maxStrength) {
								if (harvestBlock(world, adjPos, player) && strength > 0F) {
									count++;
								}
							}
						}
					}
					break;
			}
			if (count > 0 && !player.capabilities.isCreativeMode) {
				stack.damageItem(count, player);
			}
		}
		return false;
	}

	// region IAreaEffectItem
	@Override
	public ImmutableList<BlockPos> getAreaEffectBlocks(ItemStack stack, BlockPos pos, EntityPlayer player) {

		ArrayList<BlockPos> area = new ArrayList<>();
		World world = player.getEntityWorld();

		RayTraceResult traceResult = RayTracer.retrace(player, false);
		if (traceResult == null || traceResult.sideHit == null || !canHarvestBlock(world.getBlockState(pos), stack) || player.isSneaking()) {
			return ImmutableList.copyOf(area);
		}
		BlockPos harvestPos;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = 1;

		switch (traceResult.sideHit) {
			case DOWN:
			case UP:
				for (int i = x - radius; i <= x + radius; i++) {
					for (int k = z - radius; k <= z + radius; k++) {
						if (i == x && k == z) {
							continue;
						}
						harvestPos = new BlockPos(i, y, k);
						if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
							area.add(harvestPos);
						}
					}
				}
				break;
			case NORTH:
			case SOUTH:
				for (int i = x - radius; i <= x + radius; i++) {
					for (int j = y - radius; j <= y + radius; j++) {
						if (i == x && j == y) {
							continue;
						}
						harvestPos = new BlockPos(i, j, z);
						if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
							area.add(harvestPos);
						}
					}
				}
				break;
			case WEST:
			case EAST:
				for (int j = y - radius; j <= y + radius; j++) {
					for (int k = z - radius; k <= z + radius; k++) {
						if (j == y && k == z) {
							continue;
						}
						harvestPos = new BlockPos(x, j, k);
						if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
							area.add(harvestPos);
						}
					}
				}
				break;
		}
		return ImmutableList.copyOf(area);
	}
	// endregion
}
