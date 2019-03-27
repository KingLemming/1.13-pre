package cofh.core.item;

import cofh.core.util.ChatHelper;
import cofh.lib.item.IPlacementItem;
import cofh.lib.util.Utils;
import cofh.lib.util.control.ISecurable;
import cofh.lib.util.control.ISecurable.AccessMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.lib.util.helpers.StringHelper.getInfoText;
import static cofh.lib.util.helpers.StringHelper.getNoticeText;

public class ItemLock extends ItemCoFH implements IPlacementItem {

	public ItemLock() {

		super("");
	}

	public ItemLock(String group) {

		super(group);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		tooltip.add(getInfoText("info.cofh.security_use"));
		tooltip.add(getNoticeText("info.cofh.offhand_use_block_place"));
	}

	private boolean doLockUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {

		if (Utils.isClientWorld(world)) {
			return false;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof ISecurable) {
			if (((ISecurable) tile).setOwner(player.getGameProfile())) {
				((ISecurable) tile).setAccess(AccessMode.PUBLIC);
				if (!player.capabilities.isCreativeMode) {
					stack.shrink(1);
				}
				player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 0.5F, 0.8F);
				ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("chat.cofh.secure_block"));
			}
			return true;
		}
		return false;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		return player.canPlayerEdit(pos.offset(facing), facing, player.getHeldItem(hand)) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

		return doLockUse(player.getHeldItem(hand), player, world, pos) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}

	// region IPlacementItem
	@Override
	public boolean onBlockPlacement(ItemStack stack, World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		return doLockUse(stack, player, world, pos);
	}
	// endregion
}
