package cofh.thermal.innovation.item;

import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.item.IToolQuiver;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ColorHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermal.core.item.ItemPotionContainer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.ArcheryHelper.isSimpleArrow;
import static cofh.lib.util.helpers.ItemHelper.areItemStacksEqualIgnoreTags;
import static cofh.lib.util.helpers.StringHelper.*;

public class ItemPotionQuiver extends ItemPotionContainer implements IMultiModeItem, IToolQuiver {

	public static final int MB_PER_ARROW = 50;
	protected static boolean enableEnchantEffect = true;

	protected int arrowCapacity;

	public ItemPotionQuiver(int fluidCapacity, int arrowCapacity) {

		super(fluidCapacity);
		this.arrowCapacity = arrowCapacity;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(getInfoText("info.thermal.potion_quiver.0"));
		tooltip.add(localize("info.thermal.potion_quiver.1"));
		tooltip.add(getNoticeText("info.thermal.potion_quiver.2"));
		tooltip.add(localizeFormat("info.thermal.potion_quiver.a." + getMode(stack), getKeyName(KeyMultiModeItem.INSTANCE.getKey())));

		if (isCreative()) {
			tooltip.add(localize("info.cofh.arrows") + ": " + localize("info.cofh.infinite"));
		} else {
			tooltip.add(localize("info.cofh.arrows") + ": " + getNumArrows(stack) + " / " + formatNumber(getMaxArrows(stack)));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || !areItemStacksEqualIgnoreTags(oldStack, newStack, TAG_ARROWS, TAG_FLUID));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) {
			ItemStack arrows = findArrows(player);
			if (!arrows.isEmpty() && arrows.getCount() < arrows.getMaxStackSize()) {
				arrows.grow(removeArrows(stack, arrows.getMaxStackSize() - arrows.getCount(), false));
			} else {
				arrows = new ItemStack(Items.ARROW, Math.min(getNumArrows(stack), 64));
				if (addToPlayerInventory(player, arrows)) {
					removeArrows(stack, arrows.getCount(), false);
				}
			}
		} else {
			ItemStack arrows = findArrows(player);
			arrows.shrink(addArrows(stack, arrows.getCount(), false));
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {

		return enableEnchantEffect && stack.isItemEnchanted();
	}

	// region HELPERS
	public int getNumArrows(ItemStack stack) {

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return isCreative() ? getMaxArrows(stack) : stack.getTagCompound().getInteger(TAG_ARROWS);
	}

	public int getMaxArrows(ItemStack stack) {

		return arrowCapacity;
	}

	public int getScaledArrowsStored(ItemStack stack, int scale) {

		return MathHelper.round((long) getNumArrows(stack) * scale / getMaxArrows(stack));
	}

	public int addArrows(ItemStack stack, int maxArrows, boolean simulate) {

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		int stored = getNumArrows(stack);
		int toAdd = Math.min(maxArrows, getMaxArrows(stack) - stored);

		if (!simulate && !isCreative()) {
			stored += toAdd;
			stack.getTagCompound().setInteger(TAG_ARROWS, stored);
		}
		return toAdd;
	}

	public int removeArrows(ItemStack stack, int maxArrows, boolean simulate) {

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (isCreative()) {
			return maxArrows;
		}
		int stored = Math.min(stack.getTagCompound().getInteger(TAG_ARROWS), getMaxArrows(stack));
		int toRemove = Math.min(maxArrows, stored);
		if (!simulate) {
			stored -= toRemove;
			stack.getTagCompound().setInteger(TAG_ARROWS, stored);
		}
		return toRemove;
	}

	public static ItemStack findArrows(EntityPlayer player) {

		ItemStack offHand = player.getHeldItemOffhand();
		ItemStack mainHand = player.getHeldItemMainhand();

		if (isSimpleArrow(offHand)) {
			return offHand;
		} else if (isSimpleArrow(mainHand)) {
			return mainHand;
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (isSimpleArrow(stack)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	public static boolean addToPlayerInventory(EntityPlayer player, ItemStack stack) {

		if (stack.isEmpty() || player == null) {
			return false;
		}
		InventoryPlayer inv = player.inventory;
		for (int i = 0; i < inv.mainInventory.size(); i++) {
			if (inv.mainInventory.get(i).isEmpty()) {
				inv.mainInventory.set(i, stack.copy());
				return true;
			}
		}
		return false;
	}
	// endregion

	// region IMultimodeItem
	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		int mode = getMode(stack);
		if (mode == 1) {
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 0.4F, 1.0F);
		} else {
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.6F);
		}
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.thermal.potion_quiver.b." + mode));
	}
	// endregion

	// region IToolQuiver
	@Override
	public EntityArrow createEntityArrow(World world, ItemStack item, EntityLivingBase shooter) {

		FluidStack fluid = getFluid(item);
		ItemStack arrowStack;

		if (getMode(item) == 1 && fluid != null && fluid.amount >= MB_PER_ARROW) {
			arrowStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), PotionUtils.getPotionTypeFromNBT(fluid.tag));
			return ((ItemTippedArrow) arrowStack.getItem()).createArrow(world, arrowStack, shooter);
		}
		arrowStack = new ItemStack(Items.ARROW);
		return ((ItemArrow) arrowStack.getItem()).createArrow(world, arrowStack, shooter);
	}

	@Override
	public boolean isEmpty(ItemStack item, EntityLivingBase shooter) {

		if (isCreative(item) || (shooter instanceof EntityPlayer && ((EntityPlayer) shooter).capabilities.isCreativeMode)) {
			return false;
		}
		return getNumArrows(item) <= 0;
	}

	@Override
	public void onArrowFired(ItemStack item, EntityLivingBase shooter) {

		if (shooter instanceof EntityPlayer) {
			if (!((EntityPlayer) shooter).capabilities.isCreativeMode) {
				removeArrows(item, 1, false);
				drain(item, MB_PER_ARROW, getMode(item) == 1);
			}
		}
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("arrows=%s,color_bottom=%s,color_top=%s,fill=%s", MathHelper.clamp(getNumArrows(stack) > 0 ? 1 + getScaledArrowsStored(stack, 4) : 0, 0, 4), ColorHelper.hasColor(stack, TINT_INDEX_2) ? 1 : 0, ColorHelper.hasColor(stack, TINT_INDEX_3) ? 1 : 0, MathHelper.clamp(getFluidAmount(stack) > 0 ? 1 + getScaledFluidStored(stack, 12) : 0, 0, 12))));

		for (int arrows = 0; arrows < 5; arrows++) {
			for (int color_bottom = 0; color_bottom < 2; color_bottom++) {
				for (int color_top = 0; color_top < 2; color_top++) {
					for (int fill = 0; fill < 13; fill++) {
						ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("arrows=%s,color_bottom=%s,color_top=%s,fill=%s", arrows, color_bottom, color_top, fill)));
					}
				}
			}
		}
	}

	@Override
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/utils/potion_quiver/" + path + "\",\"layer1\":\"" + domain + ":items/blank\",\"layer2\":\"" + domain + ":items/blank\",\"layer3\":\"" + domain + ":items/blank\",\"layer4\":\"" + domain + ":items/blank\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":-35}],\"translation\":[0,-0.15,0],\"scale\":[0.6,0.6,0.6]},\"thirdperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":35}],\"translation\":[0,-0.15,0],\"scale\":[0.6,0.6,0.6]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":-55}],\"translation\":[0,0.25,0.1],\"scale\":[0.55,0.55,0.55]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":55}],\"translation\":[0,0.25,0.1],\"scale\":[0.55,0.55,0.55]},\"ground\":{\"translation\":[0,0.15,0],\"scale\":[0.5,0.5,0.5]}}},\"variants\":{\"arrows\":{\"0\":{},\"1\":{\"textures\":{\"layer4\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_arrows_1\"}},\"2\":{\"textures\":{\"layer4\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_arrows_2\"}},\"3\":{\"textures\":{\"layer4\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_arrows_3\"}},\"4\":{\"textures\":{\"layer4\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_arrows_4\"}}},\"color_bottom\":{\"0\":{},\"1\":{\"textures\":{\"layer2\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_color_bottom\"}}},\"color_top\":{\"0\":{},\"1\":{\"textures\":{\"layer3\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_color_top\"}}},\"fill\":{\"0\":{},\"1\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_1\"}},\"2\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_2\"}},\"3\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_3\"}},\"4\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_4\"}},\"5\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_5\"}},\"6\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_6\"}},\"7\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_7\"}},\"8\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_8\"}},\"9\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_9\"}},\"10\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_10\"}},\"11\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_11\"}},\"12\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_quiver/potion_quiver_fill_12\"}}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
