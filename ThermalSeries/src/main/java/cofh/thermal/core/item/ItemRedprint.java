package cofh.thermal.core.item;

import cofh.core.item.ItemCoFH;
import cofh.lib.item.IPlacementItem;
import cofh.lib.util.IPortableData;
import cofh.lib.util.Utils;
import cofh.lib.util.control.ISecurable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.lib.util.Constants.GROUP_UTILS;
import static cofh.lib.util.Constants.TAG_TYPE;
import static cofh.lib.util.helpers.StringHelper.localize;

public class ItemRedprint extends ItemCoFH implements IPlacementItem {

	public ItemRedprint() {

		super(GROUP_UTILS);

		setMaxStackSize(1);
		setNoRepair();
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		// RedprintHelper.addInformation(stack, tooltip);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {

		String baseName = localize(getUnlocalizedName(stack) + ".name");
		// baseName += RedprintHelper.getDisplayName(stack);
		return baseName;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return EnumRarity.COMMON;
		// return RedprintHelper.getDisplayName(stack).isEmpty() ? EnumRarity.COMMON : EnumRarity.UNCOMMON;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		return player.canPlayerEdit(pos.offset(facing), facing, player.getHeldItem(hand)) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) {
			if (stack.getTagCompound() != null) {
				player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 0.3F);
				stack.setTagCompound(null);
			}
			return EnumActionResult.SUCCESS;
		}
		IBlockState state = world.getBlockState(pos);
		Block block = world.getBlockState(pos).getBlock();

		if (!block.hasTileEntity(state)) {
			return EnumActionResult.PASS;
		}
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof ISecurable && !((ISecurable) tile).canAccess(player)) {
			return EnumActionResult.PASS;
		}
		if (tile instanceof IPortableData) {
			if (Utils.isServerWorld(world)) {
				if (!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
					((IPortableData) tile).writePortableData(player, stack.getTagCompound());
					if (stack.getTagCompound() == null || stack.getTagCompound().hasNoTags()) {
						stack.setTagCompound(null);
					} else {
						stack.getTagCompound().setString(TAG_TYPE, ((IPortableData) tile).getDataType());
						player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 0.7F);
					}
				} else {
					if (stack.getTagCompound().getString(TAG_TYPE).equals(((IPortableData) tile).getDataType())) {
						((IPortableData) tile).readPortableData(player, stack.getTagCompound());
						player.world.playSound(null, player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5F, 0.8F);
					}
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			if (stack.getTagCompound() != null) {
				player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.3F);
			}
			stack.setTagCompound(null);
		}
		player.swingArm(hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	// region IPlacementItem
	@Override
	public boolean onBlockPlacement(ItemStack stack, World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof ISecurable && !((ISecurable) tile).canAccess(player)) {
			return false;
		}
		if (tile instanceof IPortableData) {
			if (Utils.isServerWorld(world)) {
				if (!stack.hasTagCompound()) {
					return false;
				} else {
					if (stack.getTagCompound().getString("Type").equals(((IPortableData) tile).getDataType())) {
						((IPortableData) tile).readPortableData(player, stack.getTagCompound());
						player.world.playSound(null, player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5F, 0.8F);
					}
				}
			}
			return true;
		}
		return false;
	}
	// endregion
}
