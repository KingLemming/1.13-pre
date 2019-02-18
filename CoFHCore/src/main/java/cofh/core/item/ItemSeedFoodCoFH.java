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

public class ItemSeedFoodCoFH extends ItemFoodCoFH implements IPlantable {

	protected final Block plant;
	protected final EnumPlantType type;

	public ItemSeedFoodCoFH(int amount, float saturation, Block plant, String group) {

		this(amount, saturation, plant, EnumPlantType.Crop, group);
	}

	public ItemSeedFoodCoFH(int amount, float saturation, Block plant, EnumPlantType type, String group) {

		super(amount, saturation, group);
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
