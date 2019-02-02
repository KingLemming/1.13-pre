package cofh.enstoragement.item;

import cofh.core.gui.container.InventoryContainerItemWrapper;
import cofh.core.item.ItemInventoryContainer;
import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.enstoragement.Enstoragement;
import cofh.lib.item.IColorableItem;
import cofh.lib.item.IFilterContainerItem;
import cofh.lib.item.IInventoryContainerItem;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.Utils;
import cofh.lib.util.control.ISecurable.AccessMode;
import cofh.lib.util.filter.ItemFilterWrapper;
import cofh.lib.util.helpers.ColorHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.SecurityHelper;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.enstoragement.gui.GuiHandler.GUI_SATCHEL;
import static cofh.enstoragement.gui.GuiHandler.GUI_SATCHEL_FILTER;
import static cofh.lib.util.Constants.TINT_INDEX_2;
import static cofh.lib.util.Constants.TINT_INDEX_3;
import static cofh.lib.util.helpers.StringHelper.*;

public class ItemSatchel extends ItemInventoryContainer implements IColorableItem, IFilterContainerItem, IInventoryContainerItem, IMultiModeItem {

	protected static boolean enableEnchantEffect = true;

	protected int filterSize;

	public ItemSatchel(int invSize, int filterSize) {

		super(invSize);
		this.filterSize = filterSize;
	}

	public ItemSatchel(int invSize, int filterSize, int stackLimit) {

		super(invSize, stackLimit);
		this.filterSize = filterSize;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		SecurityHelper.addAccessInformation(stack, tooltip);

		if (isCreative()) {
			tooltip.add(getInfoText("info.enstoragement.satchel.c"));
			tooltip.add(localize("info.enstoragement.satchel.1"));
			// ItemHelper.addInventoryInformation(stack, tooltip);
			return;
		}
		tooltip.add(getInfoText("info.enstoragement.satchel.0"));
		tooltip.add(localize("info.enstoragement.satchel.1"));
		tooltip.add(getNoticeText("info.enstoragement.satchel.2"));
		tooltip.add(localizeFormat("info.enstoragement.satchel.a." + getMode(stack), getKeyName(KeyMultiModeItem.INSTANCE.getKey())));
		// ItemHelper.addInventoryInformation(stack, tooltip);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (Utils.isFakePlayer(player) || hand != EnumHand.MAIN_HAND) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		if (needsDefaultTag(stack)) {
			setDefaultTag(stack);
		}
		if (Utils.isServerWorld(world)) {
			if (SecurityHelper.hasSecurity(stack) && SecurityHelper.isDefaultProfile(SecurityHelper.getOwner(stack))) {
				SecurityHelper.setOwner(stack, player.getGameProfile());
				ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("chat.cofh.secure.item.success"));
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
			if (SecurityHelper.canPlayerAccess(stack, player)) {
				if (player.isSneaking()) {
					player.openGui(Enstoragement.instance, GUI_SATCHEL_FILTER, world, 0, 0, 0);
				} else {
					player.openGui(Enstoragement.instance, GUI_SATCHEL, world, 0, 0, 0);
				}
			} else if (SecurityHelper.hasSecurity(stack)) {
				ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("chat.cofh.secure.warning", SecurityHelper.getOwnerName(stack)));
				return new ActionResult<>(EnumActionResult.FAIL, stack);
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

		if (world.isAirBlock(pos)) {
			return EnumActionResult.PASS;
		}
		PlayerInteractEvent event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
		if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY) {
			return EnumActionResult.PASS;
		}
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking() && SecurityHelper.canPlayerAccess(stack, player)) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
				IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
				if (Utils.isServerWorld(world)) {
					emptyInventoryIntoTarget(stack, cap);
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {

		return enableEnchantEffect && stack.isItemEnchanted();
	}

	// region HELPERS
	public static boolean onItemPickup(EntityItemPickupEvent event, ItemStack stack) {

		ItemSatchel satchel = ((ItemSatchel) stack.getItem());

		if (!SecurityHelper.canPlayerAccess(stack, event.getEntityPlayer()) || satchel.getMode(stack) <= 0 || satchel.isCreative()) {
			return false;
		}
		ItemStack eventStack = event.getItem().getItem();
		ItemFilterWrapper wrapper = new ItemFilterWrapper(stack, ((ItemSatchel) stack.getItem()).getSizeFilter(stack));
		if (!wrapper.getFilter().matches(eventStack)) {
			return false;
		}
		if (!(eventStack.getItem() instanceof IInventoryContainerItem) || ((IInventoryContainerItem) eventStack.getItem()).getSizeInventory(stack) <= 0) {
			int count = eventStack.getCount();
			InventoryContainerItemWrapper inv = new InventoryContainerItemWrapper(stack);
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack slot = inv.getStackInSlot(i);
				if (slot.isEmpty()) {
					inv.setInventorySlotContents(i, eventStack.copy());
					eventStack.setCount(0);
				} else if (ItemHandlerHelper.canItemStacksStack(eventStack, slot)) {
					int fill = slot.getMaxStackSize() - slot.getCount();
					if (fill > eventStack.getCount()) {
						slot.setCount(slot.getCount() + eventStack.getCount());
					} else {
						slot.setCount(slot.getMaxStackSize());
					}
					eventStack.splitStack(fill);
				}
				if (eventStack.isEmpty()) {
					break;
				}
			}
			if (eventStack.getCount() != count) {
				stack.setAnimationsToGo(5);
				EntityPlayer player = event.getEntityPlayer();
				player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((MathHelper.RANDOM.nextFloat() - MathHelper.RANDOM.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				inv.markDirty();
			}
		}
		return eventStack.isEmpty();
	}

	private void emptyInventoryIntoTarget(ItemStack stack, IItemHandler target) {

		InventoryContainerItemWrapper wrapper = new InventoryContainerItemWrapper(stack);
		for (int i = 0; i < getSizeInventory(stack); i++) {
			ItemStack slot = wrapper.getStackInSlot(i);
			if (!slot.isEmpty()) {
				ItemStack remainder = ItemHandlerHelper.insertItem(target, slot, false);
				if (!isCreative()) {
					wrapper.setInventorySlotContents(i, remainder);
				}
			}
		}
		wrapper.markDirty();
	}
	// endregion

	// region IFilterContainerItem
	public int getSizeFilter(ItemStack stack) {

		return filterSize;
	}
	// endregion

	// region IInventoryContainerItem
	@Override
	public int getSizeInventory(ItemStack container) {

		return invSize;
	}

	@Override
	public int getInventoryStackLimit(ItemStack container) {

		return stackLimit;
	}
	// endregion

	// region IMultiModeItem
	@Override
	public int getNumModes(ItemStack stack) {

		return isCreative() ? 1 : 2;
	}

	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.4F, 0.8F + 0.4F * getMode(stack));
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.enstoragement.satchel.b." + getMode(stack)));
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("access=%s,color_bottom=%s,color_top=%s", SecurityHelper.getAccess(stack).toString().toLowerCase(Locale.US), ColorHelper.hasColor(stack, TINT_INDEX_2) ? 1 : 0, ColorHelper.hasColor(stack, TINT_INDEX_3) ? 1 : 0)));

		for (int access = 0; access < AccessMode.VALUES.length; access++) {
			for (int color_bottom = 0; color_bottom < 2; color_bottom++) {
				for (int color_top = 0; color_top < 2; color_top++) {
					ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("access=%s,color_bottom=%s,color_top=%s", AccessMode.VALUES[access].toString().toLowerCase(Locale.US), color_bottom, color_top)));
				}
			}
		}
	}

	@Override
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/satchel/" + path + "\",\"layer1\":\"" + domain + ":items/satchel/access_public\",\"layer2\":\"" + domain + ":items/blank\",\"layer3\":\"" + domain + ":items/blank\"},\"transform\":\"forge:default-item\"},\"variants\":{\"access\":{\"public\":{\"textures\":{\"layer1\":\"" + domain + ":items/satchel/satchel_access_public\"}},\"friends\":{\"textures\":{\"layer1\":\"" + domain + ":items/satchel/satchel_access_friends\"}},\"team\":{\"textures\":{\"layer1\":\"" + domain + ":items/satchel/satchel_access_team\"}},\"private\":{\"textures\":{\"layer1\":\"" + domain + ":items/satchel/satchel_access_private\"}}},\"color_bottom\":{\"0\":{},\"1\":{\"textures\":{\"layer2\":\"" + domain + ":items/satchel/satchel_color_bottom\"}}},\"color_top\":{\"0\":{},\"1\":{\"textures\":{\"layer3\":\"" + domain + ":items/satchel/satchel_color_top\"}}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
