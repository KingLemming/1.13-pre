package cofh.thermal.core.item;

import cofh.core.item.ItemCoFH;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cofh.lib.util.Constants.GROUP_RESOURCES;

public class ItemBait extends ItemCoFH {

	private int potency;

	public ItemBait(int potency) {

		super(GROUP_RESOURCES);
		this.potency = MathHelper.clamp(potency, 0, 15);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.PASS;
		}
		// TODO: Fix
		if (false) {//onApplyBonemeal(stack, world, pos, player, hand, potency)) {
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

}
