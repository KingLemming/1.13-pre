package cofh.redstonearsenal.item.tool;

import cofh.lib.item.IAreaEffectItem;
import cofh.lib.util.RayTracer;
import cofh.lib.util.helpers.MathHelper;
import com.google.common.collect.ImmutableList;
import gnu.trove.set.hash.THashSet;
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

import static cofh.lib.util.Constants.AOE_BREAK_FACTOR;
import static cofh.lib.util.Constants.TOOL_PICKAXE;

public class ItemPickaxeFlux extends ItemToolFlux implements IAreaEffectItem {

	protected THashSet<Material> effectiveMaterialsEmpowered = new THashSet<>();

	public ItemPickaxeFlux(ToolMaterial toolMaterial) {

		super(-2.8F, toolMaterial);

		setHarvestLevel(TOOL_PICKAXE, toolMaterial.getHarvestLevel());

		damage = 3;
		energyPerUseEmpowered = 800;

		effectiveMaterials.add(Material.IRON);
		effectiveMaterials.add(Material.ANVIL);
		effectiveMaterials.add(Material.ROCK);
		effectiveMaterials.add(Material.ICE);
		effectiveMaterials.add(Material.PACKED_ICE);
		effectiveMaterials.add(Material.GLASS);
		effectiveMaterials.add(Material.REDSTONE_LIGHT);

		effectiveMaterialsEmpowered.add(Material.IRON);
		effectiveMaterialsEmpowered.add(Material.ANVIL);
		effectiveMaterialsEmpowered.add(Material.ROCK);
		effectiveMaterialsEmpowered.add(Material.ICE);
		effectiveMaterialsEmpowered.add(Material.PACKED_ICE);
		effectiveMaterialsEmpowered.add(Material.GLASS);
		effectiveMaterialsEmpowered.add(Material.REDSTONE_LIGHT);
		effectiveMaterialsEmpowered.add(Material.GROUND);
		effectiveMaterialsEmpowered.add(Material.GRASS);
		effectiveMaterialsEmpowered.add(Material.SAND);
		effectiveMaterialsEmpowered.add(Material.SNOW);
		effectiveMaterialsEmpowered.add(Material.CRAFTED_SNOW);
		effectiveMaterialsEmpowered.add(Material.CLAY);
	}

	protected THashSet<Material> getEffectiveMaterials(ItemStack stack) {

		return isEmpowered(stack) ? effectiveMaterialsEmpowered : super.getEffectiveMaterials(stack);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.canApply(new ItemStack(Items.IRON_PICKAXE));
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

		World world = player.world;
		IBlockState state = world.getBlockState(pos);

		if (state.getBlockHardness(world, pos) == 0.0F) {
			return false;
		}
		if (!canHarvestBlock(state, stack)) {
			useEnergy(stack, player.capabilities.isCreativeMode);
			return false;
		}
		float refStrength = state.getPlayerRelativeBlockHardness(player, world, pos);
		float maxStrength = refStrength / AOE_BREAK_FACTOR;

		if (refStrength != 0.0F) {
			if (isEmpowered(stack) && canHarvestBlock(state, stack)) {
				RayTraceResult traceResult = RayTracer.retrace(player, false);

				if (traceResult == null || traceResult.sideHit == null) {
					return false;
				}
				BlockPos adjPos;
				IBlockState adjState;
				float strength;

				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				switch (traceResult.sideHit) {
					case DOWN:
					case UP:
						int facing = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
						if (facing % 2 == 0) {
							adjPos = new BlockPos(x, y, z - 1);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength > 0F && strength >= maxStrength) {
								harvestBlock(world, adjPos, player);
							}
							adjPos = new BlockPos(x, y, z + 1);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength > 0F && strength >= maxStrength) {
								harvestBlock(world, adjPos, player);
							}
						} else {
							adjPos = new BlockPos(x - 1, y, z);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength > 0F && strength >= maxStrength) {
								harvestBlock(world, adjPos, player);
							}
							adjPos = new BlockPos(x + 1, y, z);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength > 0F && strength >= maxStrength) {
								harvestBlock(world, adjPos, player);
							}
						}
						break;
					default:
						adjPos = new BlockPos(x, y - 1, z);
						adjState = world.getBlockState(adjPos);
						strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
						if (strength > 0F && strength >= maxStrength) {
							harvestBlock(world, adjPos, player);
						}
						adjPos = new BlockPos(x, y + 1, z);
						adjState = world.getBlockState(adjPos);
						strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
						if (strength > 0F && strength >= maxStrength) {
							harvestBlock(world, adjPos, player);
						}
				}
			}
			useEnergy(stack, player.capabilities.isCreativeMode);
		}
		return false;
	}

	// region IAreaEffectItem
	@Override
	public ImmutableList<BlockPos> getAreaEffectBlocks(ItemStack stack, BlockPos pos, EntityPlayer player) {

		ArrayList<BlockPos> area = new ArrayList<>();
		World world = player.getEntityWorld();

		RayTraceResult traceResult = RayTracer.retrace(player, false);
		if (traceResult == null || traceResult.sideHit == null || !isEmpowered(stack) || !canHarvestBlock(world.getBlockState(pos), stack)) {
			return ImmutableList.copyOf(area);
		}
		BlockPos harvestPos;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		switch (traceResult.sideHit) {
			case DOWN:
			case UP:
				int facing = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
				if (facing % 2 == 0) {
					harvestPos = new BlockPos(x, y, z - 1);
					if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
						area.add(harvestPos);
					}
					harvestPos = new BlockPos(x, y, z + 1);
					if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
						area.add(harvestPos);
					}
				} else {
					harvestPos = new BlockPos(x - 1, y, z);
					if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
						area.add(harvestPos);
					}
					harvestPos = new BlockPos(x + 1, y, z);
					if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
						area.add(harvestPos);
					}
				}
			default:
				harvestPos = new BlockPos(x, y - 1, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y + 1, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
		}
		return ImmutableList.copyOf(area);
	}
	// endregion
}
