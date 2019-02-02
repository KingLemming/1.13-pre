package cofh.thermal.core.item;

import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;

import static cofh.lib.util.Constants.AOE_BREAK_FACTOR;

public abstract class ItemRFTool extends ItemRFContainer {

	protected THashSet<Material> effectiveMaterials = new THashSet<>();

	protected int energyPerUse = 200;
	protected int harvestLevel;
	protected float efficiency;

	public ItemRFTool(int maxEnergy, int maxReceive, int harvestLevel, float efficiency) {

		super(maxEnergy, maxReceive);
		this.harvestLevel = harvestLevel;
		this.efficiency = efficiency;
	}

	protected boolean harvestBlock(World world, BlockPos pos, EntityPlayer player) {

		if (world.isAirBlock(pos)) {
			return false;
		}
		EntityPlayerMP playerMP = null;
		if (player instanceof EntityPlayerMP) {
			playerMP = (EntityPlayerMP) player;
		}
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (!ForgeHooks.canHarvestBlock(block, player, world, pos)) {
			return false;
		}
		// send the blockbreak event
		int xpToDrop = 0;
		if (playerMP != null) {
			xpToDrop = ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos);
			if (xpToDrop == -1) {
				return false;
			}
		}
		if (Utils.isServerWorld(world)) {
			if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
				if (!player.capabilities.isCreativeMode) {
					block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
					if (xpToDrop > 0) {
						block.dropXpOnBlockBreak(world, pos, xpToDrop);
					}
				}
			}
			// always send block update to client
			playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
		} else {
			if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
			}
			Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
		}
		return true;
	}

	protected THashSet<Material> getEffectiveMaterials() {

		return effectiveMaterials;
	}

	protected float getEfficiency() {

		return efficiency;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		if (EnumEnchantmentType.BREAKABLE.equals(enchantment.type)) {
			return enchantment.equals(Enchantments.UNBREAKING);
		}
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {

		return harvestLevel >= state.getBlock().getHarvestLevel(state) && getDestroySpeed(stack, state) > 1.0F;
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {

		return !oldStack.equals(newStack) && (getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {

		for (String type : getToolClasses(stack)) {
			if (state.getBlock().isToolEffective(type, state)) {
				return getEfficiency();
			}
		}
		return getEffectiveMaterials().contains(state.getMaterial()) ? getEfficiency() : 1.0F;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		useEnergy(stack, 2, true);
		return true;
	}

	// region HELPERS
	protected int useEnergy(ItemStack stack, int count, boolean simulate) {

		if (isCreative()) {
			return 0;
		}
		int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 10);
		if (MathHelper.RANDOM.nextInt(2 + unbreakingLevel) >= 2) {
			return 0;
		}
		return extractEnergy(stack, count * energyPerUse, simulate);
	}
	// endregion

	// region AREA EFFECT
	protected int breakTunnel2(EntityPlayer player, World world, BlockPos pos, RayTraceResult traceResult, float refStrength) {

		BlockPos adjPos;
		IBlockState adjState;
		float strength;
		float maxStrength = refStrength / AOE_BREAK_FACTOR;

		switch (traceResult.sideHit) {
			case DOWN:
			case UP:
				break;
			default:
				adjPos = new BlockPos(pos.down());
				adjState = world.getBlockState(adjPos);
				strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
				if (strength > 0F && strength >= maxStrength) {
					if (harvestBlock(world, adjPos, player)) {
						return 1;
					}
				}
		}
		return 0;
	}

	protected int breakTunnel3(EntityPlayer player, World world, BlockPos pos, RayTraceResult traceResult, float refStrength) {

		BlockPos adjPos;
		IBlockState adjState;
		float strength;
		float maxStrength = refStrength / AOE_BREAK_FACTOR;
		int count = 0;

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
						if (harvestBlock(world, adjPos, player)) {
							count++;
						}
					}
					adjPos = new BlockPos(x, y, z + 1);
					adjState = world.getBlockState(adjPos);
					strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
					if (strength > 0F && strength >= maxStrength) {
						if (harvestBlock(world, adjPos, player)) {
							count++;
						}
					}
				} else {
					adjPos = new BlockPos(x - 1, y, z);
					adjState = world.getBlockState(adjPos);
					strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
					if (strength > 0F && strength >= maxStrength) {
						if (harvestBlock(world, adjPos, player)) {
							count++;
						}
					}
					adjPos = new BlockPos(x + 1, y, z);
					adjState = world.getBlockState(adjPos);
					strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
					if (strength > 0F && strength >= maxStrength) {
						if (harvestBlock(world, adjPos, player)) {
							count++;
						}
					}
				}
				break;
			default:
				adjPos = new BlockPos(x, y - 1, z);
				adjState = world.getBlockState(adjPos);
				strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
				if (strength > 0F && strength >= maxStrength) {
					if (harvestBlock(world, adjPos, player)) {
						count++;
					}
				}
				adjPos = new BlockPos(x, y + 1, z);
				adjState = world.getBlockState(adjPos);
				strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
				if (strength > 0F && strength >= maxStrength) {
					if (harvestBlock(world, adjPos, player)) {
						count++;
					}
				}
		}
		return count;
	}

	protected int breakArea3(EntityPlayer player, World world, BlockPos pos, RayTraceResult traceResult, float refStrength) {

		BlockPos adjPos;
		IBlockState adjState;
		float strength;
		float maxStrength = refStrength / AOE_BREAK_FACTOR;
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
						if (strength > 0F && strength >= maxStrength) {
							if (harvestBlock(world, adjPos, player)) {
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
						if (strength > 0F && strength >= maxStrength) {
							if (harvestBlock(world, adjPos, player)) {
								count++;
							}
						}
					}
				}
				break;
			default:
				for (int j = y - radius; j <= y + radius; j++) {
					for (int k = z - radius; k <= z + radius; k++) {
						if (j == y && k == z) {
							continue;
						}
						adjPos = new BlockPos(x, j, k);
						adjState = world.getBlockState(adjPos);
						strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
						if (strength > 0F && strength >= maxStrength) {
							if (harvestBlock(world, adjPos, player)) {
								count++;
							}
						}
					}
				}
				break;
		}
		return count;
	}

	protected int breakCube3(EntityPlayer player, World world, BlockPos pos, RayTraceResult traceResult, float refStrength) {

		BlockPos adjPos;
		IBlockState adjState;
		float strength;
		float maxStrength = refStrength / AOE_BREAK_FACTOR;
		int count = 0;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = 1;
		int depth = 2;
		int depth_min = depth;
		int depth_max = 0;

		switch (traceResult.sideHit) {
			case DOWN:
				depth_min = 0;
				depth_max = depth;
			case UP:
				for (int i = x - radius; i <= x + radius; i++) {
					for (int j = y - depth_min; j <= y + depth_max; j++) {
						for (int k = z - radius; k <= z + radius; k++) {
							if (i == x && j == y && k == z) {
								continue;
							}
							adjPos = new BlockPos(i, j, k);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength > 0F && strength >= maxStrength) {
								if (harvestBlock(world, adjPos, player)) {
									count++;
								}
							}
						}
					}
				}
				break;
			case NORTH:
				depth_min = 0;
				depth_max = depth;
			case SOUTH:
				for (int i = x - radius; i <= x + radius; i++) {
					for (int j = y - radius; j <= y + radius; j++) {
						for (int k = z - depth_min; k <= z + depth_max; k++) {
							if (i == x && j == y && k == z) {
								continue;
							}
							adjPos = new BlockPos(i, j, k);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength > 0F && strength >= maxStrength) {
								if (harvestBlock(world, adjPos, player)) {
									count++;
								}
							}
						}
					}
				}
				break;
			case WEST:
				depth_min = 0;
				depth_max = depth;
			case EAST:
				for (int i = x - depth_min; i <= x + depth_max; i++) {
					for (int j = y - radius; j <= y + radius; j++) {
						for (int k = z - radius; k <= z + radius; k++) {
							if (i == x && j == y && k == z) {
								continue;
							}
							adjPos = new BlockPos(i, j, k);
							adjState = world.getBlockState(adjPos);
							strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
							if (strength > 0F && strength >= maxStrength) {
								if (harvestBlock(world, adjPos, player)) {
									count++;
								}
							}
						}
					}
				}
				break;
		}
		return count;
	}

	protected int breakArea5(EntityPlayer player, World world, BlockPos pos, RayTraceResult traceResult, float refStrength) {

		BlockPos adjPos;
		IBlockState adjState;
		float strength;
		float maxStrength = refStrength / AOE_BREAK_FACTOR;
		int count = 0;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = 2;

		switch (traceResult.sideHit) {
			case DOWN:
			case UP:
				for (int i = x - radius; i <= x + radius; i++) {
					for (int k = z - radius; k <= z + radius; k++) {
						adjPos = new BlockPos(i, y, k);
						adjState = world.getBlockState(adjPos);
						strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
						if (strength > 0F && strength >= maxStrength) {
							if (harvestBlock(world, adjPos, player)) {
								count++;
							}
						}
					}
				}
				break;
			case NORTH:
			case SOUTH:
				int posY = y;
				y += 1;     // Offset for 5x5
				for (int i = x - radius; i <= x + radius; i++) {
					for (int j = y - radius; j <= y + radius; j++) {
						if (i == x && j == posY) {
							continue;
						}
						adjPos = new BlockPos(i, j, z);
						adjState = world.getBlockState(adjPos);
						strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
						if (strength > 0F && strength >= maxStrength) {
							if (harvestBlock(world, adjPos, player)) {
								count++;
							}
						}
					}
				}
				break;
			default:
				posY = y;
				y += 1;     // Offset for 5x5
				for (int j = y - radius; j <= y + radius; j++) {
					for (int k = z - radius; k <= z + radius; k++) {
						if (j == posY && k == z) {
							continue;
						}
						adjPos = new BlockPos(x, j, k);
						adjState = world.getBlockState(adjPos);
						strength = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);
						if (strength > 0F && strength >= maxStrength) {
							if (harvestBlock(world, adjPos, player)) {
								count++;
							}
						}
					}
				}
		}
		return count;
	}

	protected void getAOEBlocksTunnel2(ItemStack stack, World world, BlockPos pos, RayTraceResult traceResult, ArrayList<BlockPos> area) {

		BlockPos harvestPos;

		switch (traceResult.sideHit) {
			case DOWN:
			case UP:
				break;
			default:
				harvestPos = new BlockPos(pos.down());
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				break;
		}
	}

	protected void getAOEBlocksTunnel3(ItemStack stack, World world, EntityPlayer player, BlockPos pos, RayTraceResult traceResult, ArrayList<BlockPos> area) {

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
	}

	protected void getAOEBlocksArea3(ItemStack stack, World world, BlockPos pos, RayTraceResult traceResult, ArrayList<BlockPos> area) {

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
			default:
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
		}
	}

	protected void getAOEBlocksCube3(ItemStack stack, World world, BlockPos pos, RayTraceResult traceResult, ArrayList<BlockPos> area) {

		BlockPos harvestPos;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = 1;
		int depth = 2;
		int depth_min = depth;
		int depth_max = 0;

		switch (traceResult.sideHit) {
			case DOWN:
				depth_min = 0;
				depth_max = depth;
			case UP:
				for (int i = x - radius; i <= x + radius; i++) {
					for (int j = y - depth_min; j <= y + depth_max; j++) {
						for (int k = z - radius; k <= z + radius; k++) {
							if (i == x && j == y && k == z) {
								continue;
							}
							harvestPos = new BlockPos(i, j, k);
							if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
								area.add(harvestPos);
							}
						}
					}
				}
				break;
			case NORTH:
				depth_min = 0;
				depth_max = depth;
			case SOUTH:
				for (int i = x - radius; i <= x + radius; i++) {
					for (int j = y - radius; j <= y + radius; j++) {
						for (int k = z - depth_min; k <= z + depth_max; k++) {
							if (i == x && j == y && k == z) {
								continue;
							}
							harvestPos = new BlockPos(i, j, k);
							if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
								area.add(harvestPos);
							}
						}
					}
				}
				break;
			case WEST:
				depth_min = 0;
				depth_max = depth;
			case EAST:
				for (int i = x - depth_min; i <= x + depth_max; i++) {
					for (int j = y - radius; j <= y + radius; j++) {
						for (int k = z - radius; k <= z + radius; k++) {
							if (i == x && j == y && k == z) {
								continue;
							}
							harvestPos = new BlockPos(i, j, k);
							if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
								area.add(harvestPos);
							}
						}
					}
				}
				break;
		}
	}

	protected void getAOEBlocksArea5(ItemStack stack, World world, BlockPos pos, RayTraceResult traceResult, ArrayList<BlockPos> area) {

		BlockPos harvestPos;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = 2;

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
				int posY = y;
				y += 1;     // Offset for 5x5
				for (int i = x - radius; i <= x + radius; i++) {
					for (int j = y - radius; j <= y + radius; j++) {
						if (i == x && j == posY) {
							continue;
						}
						harvestPos = new BlockPos(i, j, z);
						if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
							area.add(harvestPos);
						}
					}
				}
				break;
			default:
				posY = y;
				y += 1;     // Offset for 5x5
				for (int j = y - radius; j <= y + radius; j++) {
					for (int k = z - radius; k <= z + radius; k++) {
						if (j == posY && k == z) {
							continue;
						}
						harvestPos = new BlockPos(x, j, k);
						if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
							area.add(harvestPos);
						}
					}
				}
		}
	}

	protected void getAOEBlocksCross1(ItemStack stack, World world, BlockPos pos, RayTraceResult traceResult, ArrayList<BlockPos> area) {

		BlockPos harvestPos;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		switch (traceResult.sideHit) {
			case DOWN:
			case UP:
				harvestPos = new BlockPos(x - 1, y, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x + 1, y, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y, z - 1);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y, z + 1);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				break;
			case NORTH:
			case SOUTH:
				harvestPos = new BlockPos(x - 1, y, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x + 1, y, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y - 1, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y + 1, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				break;
			default:
				harvestPos = new BlockPos(x, y - 1, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y + 1, z);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y, z - 1);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
				harvestPos = new BlockPos(x, y, z + 1);
				if (canHarvestBlock(world.getBlockState(harvestPos), stack)) {
					area.add(harvestPos);
				}
		}
	}
	// endregion
}
