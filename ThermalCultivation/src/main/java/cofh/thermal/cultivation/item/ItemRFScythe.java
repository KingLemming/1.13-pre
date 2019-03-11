package cofh.thermal.cultivation.item;

import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.item.IMultiModeItem;
import cofh.thermal.core.item.ItemRFContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.lib.util.Constants.TAG_MODE;
import static cofh.lib.util.helpers.StringHelper.*;

// TODO: Finish
public class ItemRFScythe extends ItemRFContainer implements IMultiModeItem {

	protected static final int ENERGY_PER_CROP = 25;

	protected static boolean enableEnchantEffect = true;

	protected int radius;

	public ItemRFScythe(int maxEnergy, int maxReceive, int radius) {

		super(maxEnergy, maxReceive);
		this.radius = radius;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		int radius = getRadius(stack) * 2 + 1;

		tooltip.add(getInfoText("info.thermal.rf_scythe.0"));
		tooltip.add(localize("info.cofh.area") + ": " + radius + "x" + radius);

		if (getNumModes(stack) > 1) {
			tooltip.add(localizeFormat("info.thermal.rf_scythe.a.0", getKeyName(KeyMultiModeItem.INSTANCE.getKey())));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {

		return enableEnchantEffect && stack.isItemEnchanted();
	}

	// region HELPERS
	public ItemStack setDefaultTag(ItemStack stack) {

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger(TAG_MODE, getNumModes(stack) - 1);

		if (isCreative()) {
			// fill(stack, new FluidStack(FluidRegistry.WATER, getCapacity(stack)), true);
		}
		return stack;
	}

	public int getRadius(ItemStack stack) {

		return 1 + getMode(stack);
	}
	// endregion

	// region IMultiModeItem
	@Override
	public int getNumModes(ItemStack stack) {

		return radius + 1;
	}

	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.6F, 1.0F - 0.1F * getMode(stack));
		int radius = getRadius(stack) * 2 + 1;
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.cofh.area").appendText(": " + radius + "x" + radius));
	}
	// endregion
}
