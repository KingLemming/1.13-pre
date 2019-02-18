package cofh.core.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemSeedCoFH extends ItemCoFH implements IPlantable {

	protected final Block plant;
	protected final EnumPlantType type;

	public ItemSeedCoFH(Block plant, String group) {

		this(plant, EnumPlantType.Crop, group);
	}

	public ItemSeedCoFH(Block plant, EnumPlantType type, String group) {

		super(group);
		this.plant = plant;
		this.type = type;
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		IBlockState state = worldIn.getBlockState(pos);
		if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, stack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up())) {
			worldIn.setBlockState(pos.up(), this.plant.getDefaultState());
			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), stack);
			}
			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	// region IPlantable
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {

		return type;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {

		return plant.getDefaultState();
	}
	// endregion
}
