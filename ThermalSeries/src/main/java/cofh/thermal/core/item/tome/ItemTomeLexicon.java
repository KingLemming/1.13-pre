package cofh.thermal.core.item.tome;

import cofh.core.key.KeyMultiModeItem;
import cofh.lib.item.IInventoryContainerItem;
import cofh.lib.util.Utils;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.gui.GuiHandler;
import cofh.thermal.core.util.managers.LexiconManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.lib.util.helpers.StringHelper.*;

public class ItemTomeLexicon extends ItemTome implements IInventoryContainerItem {

	public static final String LEXICON_TIMER = "thermal.lexicon_timer";

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(getInfoText("info.thermal.tome_lexicon.0"));
		tooltip.add(localize("info.thermal.tome_lexicon.a." + (isEmpowered(stack) ? 1 : 0)) + END);
		tooltip.add(localizeFormat("info.thermal.tome_lexicon.b." + (isEmpowered(stack) ? 1 : 0), getKeyName(KeyMultiModeItem.INSTANCE.getKey())));
		tooltip.add(getFlavorText("info.thermal.tome_lexicon.1"));
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {

		if (!isEmpowered(stack)) {
			return;
		}
		NBTTagCompound tag = entity.getEntityData();
		tag.setLong(LEXICON_TIMER, entity.world.getTotalWorldTime());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (Utils.isFakePlayer(player) || hand != EnumHand.MAIN_HAND) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		if (Utils.isServerWorld(world) && LexiconManager.getSortedOreNames().size() > 0) {
			if (isEmpowered(stack)) {
				player.openGui(ThermalSeries.instance, GuiHandler.GUI_LEXICON_TRANSMUTE, world, 0, 0, 0);
			} else {
				player.openGui(ThermalSeries.instance, GuiHandler.GUI_LEXICON_STUDY, world, 0, 0, 0);
			}
		}
		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	// region IInventoryContainerItem
	@Override
	public int getSizeInventory(ItemStack container) {

		return 3;
	}
	// endregion
}
