package cofh.thermal.core.item;

import cofh.core.item.ItemCoFH;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import static cofh.lib.util.Constants.GROUP_RESOURCES;

public class ItemFertilizer extends ItemCoFH {

	private int radius;

	public ItemFertilizer(int radius) {

		super(GROUP_RESOURCES);
		this.radius = MathHelper.clamp(radius, 0, 15);
	}

	private boolean growBlock(World world, BlockPos pos, IBlockState state) {

		if (state.getBlock() instanceof IGrowable) {
			IGrowable growable = (IGrowable) state.getBlock();

			if (growable.canGrow(world, pos, state, world.isRemote)) {
				if (Utils.isServerWorld(world)) {
					if (growable.canUseBonemeal(world, world.rand, pos, state)) {
						growable.grow(world, world.rand, pos, state);
						world.playEvent(2005, pos, 0);
					}
				}
				return true;
			}
		}
		return false;
	}

	private boolean onApplyBonemeal(ItemStack stack, World world, BlockPos pos, EntityPlayer player, EnumHand hand, int radius) {

		IBlockState state = world.getBlockState(pos);

		int hook = ForgeEventFactory.onApplyBonemeal(player, world, pos, state, stack, hand);
		if (hook != 0) {
			return hook > 0;
		}
		if (radius <= 0) {
			return growBlock(world, pos, world.getBlockState(pos));
		}
		Iterable<BlockPos.MutableBlockPos> area = BlockPos.getAllInBoxMutable(pos.add(-radius, 0, -radius), pos.add(radius, 0, radius));
		boolean used = false;

		for (BlockPos pos2 : area) {
			used |= growBlock(world, pos2, world.getBlockState(pos2));
		}
		if (used) {
			stack.shrink(1);
		}
		return used;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.PASS;
		}
		if (onApplyBonemeal(stack, world, pos, player, hand, radius)) {
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

}
