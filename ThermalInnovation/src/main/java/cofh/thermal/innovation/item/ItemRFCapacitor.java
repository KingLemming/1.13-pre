package cofh.thermal.innovation.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.BaublesHelper;
import cofh.lib.util.helpers.ColorHelper;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.thermal.core.item.ItemRFContainer;
import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.TINT_INDEX_2;
import static cofh.lib.util.helpers.StringHelper.*;

@Optional.Interface (iface = "baubles.api.IBauble", modid = "baubles")
public class ItemRFCapacitor extends ItemRFContainer implements IMultiModeItem, IBauble {

	private static final int EQUIPMENT = 0;
	private static final int INVENTORY = 1;

	protected static boolean enableEnchantEffect = true;

	protected int maxSend;

	public ItemRFCapacitor(int maxEnergy, int maxReceive, int maxSend) {

		super(maxEnergy, maxReceive);
		this.maxSend = maxSend;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(getInfoText("info.thermal.rf_capacitor.0"));

		if (isActive(stack)) {
			tooltip.add(getNoticeText("info.thermal.rf_capacitor.b." + getMode(stack)));
			//			tooltip.add(getDeactivationText("info.thermal.rf_capacitor.2"));
		} else {
			//			tooltip.add(getActivationText("info.thermal.rf_capacitor.1"));
		}
		tooltip.add(localizeFormat("info.thermal.rf_capacitor.a.0", getKeyName(KeyMultiModeItem.INSTANCE.getKey())));

		if (!isCreative(stack)) {
			// tooltip.add(localize("info.cofh.send") + ": " + formatNumber(maxSend) + " RF/t");
			tooltip.add(localize("info.cofh.send") + "/" + localize("info.cofh.receive") + ": " + formatNumber(maxSend) + "/" + formatNumber(maxReceive) + " RF/t");
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {

		if (Utils.isClientWorld(world) || Utils.isFakePlayer(entity) || !isActive(stack)) {
			return;
		}
		Iterable<ItemStack> equipment;
		EntityPlayer player = (EntityPlayer) entity;

		switch (getMode(stack)) {
			case EQUIPMENT:
				equipment = Iterables.concat(player.getEquipmentAndArmor(), BaublesHelper.getBaubles(player));
				break;
			case INVENTORY:
				equipment = player.inventory.mainInventory;
				break;
			default:
				equipment = Iterables.concat(Arrays.asList(player.inventory.mainInventory, player.inventory.armorInventory, player.inventory.offHandInventory, BaublesHelper.getBaubles(player)));
		}
		for (ItemStack equipmentStack : equipment) {
			if (equipmentStack.equals(stack)) {
				continue;
			}
			extractEnergy(stack, EnergyHelper.attemptItemCharge(equipmentStack, Math.min(getEnergyStored(stack), maxSend), false), false);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (Utils.isFakePlayer(player)) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		if (player.isSneaking()) {
			if (isActive(stack)) {
				clearActive(stack);
				player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.5F);
			} else {
				setActive(stack, player);
				player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.8F);
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		return EnumActionResult.FAIL;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {

		return enableEnchantEffect && stack.isItemEnchanted();
	}

	// region IMultiModeItem
	@Override
	public int getNumModes(ItemStack stack) {

		return 3;
	}

	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 0.4F, (isActive(stack) ? 0.7F : 0.5F) + 0.1F * getMode(stack));
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.thermal.rf_capacitor.b." + getMode(stack)));
	}
	// endregion

	// region IBauble
	@Override
	public BaubleType getBaubleType(ItemStack stack) {

		return BaubleType.TRINKET;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {

		World world = player.world;
		if (Utils.isClientWorld(world) || !isActive(stack)) {
			return;
		}
		Iterable<ItemStack> equipment;
		EntityPlayer castPlayer = (EntityPlayer) player;

		switch (getMode(stack)) {
			case EQUIPMENT:
				equipment = Iterables.concat(player.getEquipmentAndArmor(), BaublesHelper.getBaubles(player));
				break;
			case INVENTORY:
				equipment = castPlayer.inventory.mainInventory;
				break;
			default:
				equipment = Iterables.concat(Arrays.asList(castPlayer.inventory.mainInventory, castPlayer.inventory.armorInventory, castPlayer.inventory.offHandInventory, BaublesHelper.getBaubles(player)));
		}
		for (ItemStack equipmentStack : equipment) {
			if (equipmentStack.equals(stack)) {
				continue;
			}
			extractEnergy(stack, EnergyHelper.attemptItemCharge(equipmentStack, Math.min(getEnergyStored(stack), maxSend), false), false);
		}
	}

	@Override
	public boolean willAutoSync(ItemStack stack, EntityLivingBase player) {

		return true;
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s_%s", ColorHelper.hasColor(stack, TINT_INDEX_2) ? 1 : 0, this.getEnergyStored(stack) > 0 && isActive(stack) ? 1 : 0, getMode(stack))));

		for (int color = 0; color < 2; color++) {
			for (int active = 0; active < 2; active++) {
				for (int mode = 0; mode < 3; mode++) {
					ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s_%s", color, active, mode)));
				}
			}
		}
	}

	@Override
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/utils/rf_capacitor/" + path + "\",\"layer1\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_state_0_0\",\"layer2\":\"" + domain + ":items/blank\"},\"transform\":\"forge:default-item\"},\"variants\":{\"color\":{\"0\":{},\"1\":{\"textures\":{\"layer2\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_color\"}}},\"state\":{\"0_0\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_state_0_0\"}},\"0_1\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_state_0_1\"}},\"0_2\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_state_0_2\"}},\"1_0\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_state_1_0\"}},\"1_1\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_state_1_1\"}},\"1_2\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/rf_capacitor/rf_capacitor_state_1_2\"}}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
