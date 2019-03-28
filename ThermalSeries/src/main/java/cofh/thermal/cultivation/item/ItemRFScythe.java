package cofh.thermal.cultivation.item;

import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.block.IHarvestable;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ColorHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermal.core.item.ItemRFContainer;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.TAG_MODE;
import static cofh.lib.util.Constants.TINT_INDEX_2;
import static cofh.lib.util.helpers.StringHelper.*;

public class ItemRFScythe extends ItemRFContainer implements IMultiModeItem {

	protected static final int ENERGY_PER_USE = 100;

	protected static boolean enableEnchantEffect = true;
	protected static boolean allowFakePlayers = false;

	protected int radius;

	public ItemRFScythe(int maxEnergy, int maxReceive, int radius) {

		super(maxEnergy, maxReceive);
		this.radius = radius;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		tooltip.add(getInfoText("info.thermal.rf_scythe.0"));
		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		int radius = getRadius(stack) * 2 + 1;
		tooltip.add(localize("info.cofh.area") + ": " + radius + "x" + radius);

		if (getNumModes(stack) > 1) {
			tooltip.add(localizeFormat("info.thermal.rf_scythe.a.0", getKeyName(KeyMultiModeItem.INSTANCE.getKey())));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (Utils.isFakePlayer(player) && !allowFakePlayers) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		BlockPos pos = player.getPosition();
		harvestBlocks(stack, world, pos, player);

		player.swingArm(hand);
		stack.setAnimationsToGo(5);
		player.world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.4F, 1.0F);

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

		if (world.isAirBlock(pos)) {
			return EnumActionResult.PASS;
		}
		if (Utils.isFakePlayer(player) && !allowFakePlayers) {
			return EnumActionResult.FAIL;
		}
		ItemStack stack = player.getHeldItem(hand);
		if (harvestBlocks(stack, world, pos, player)) {
			player.swingArm(hand);
			stack.setAnimationsToGo(5);
			player.world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.4F, 1.0F);

			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

		World world = player.world;
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		// Only Harvestable Plants
		if (!(block instanceof IGrowable)) {
			return false;
		}
		IGrowable grow = (IGrowable) block;
		if (grow.canGrow(world, pos, state, world.isRemote)) {
			return false;
		}
		if (!ForgeHooks.canHarvestBlock(block, player, world, pos)) {
			return false;
		}
		if (player.isSneaking()) {
			if (!player.capabilities.isCreativeMode) {
				useEnergy(stack, 1, false);
			}
			return false;
		}
		return harvestBlocks(stack, world, pos, player);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {

		return enableEnchantEffect && stack.isItemEnchanted();
	}

	// region HELPERS
	protected boolean harvestBlocks(ItemStack stack, World world, BlockPos pos, EntityPlayer player) {

		BlockPos adjPos;
		int count = 0;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = getRadius(stack);

		loop:
		for (int i = x - radius; i <= x + radius; i++) {
			for (int k = z - radius; k <= z + radius; k++) {
				adjPos = new BlockPos(i, y, k);
				if (!player.capabilities.isCreativeMode && getEnergyStored(stack) < ENERGY_PER_USE * count + 1) {
					break loop;
				}
				if (harvestBlock(world, adjPos, player)) {
					count++;
				}
			}
		}
		if (count > 0 && !player.capabilities.isCreativeMode) {
			useEnergy(stack, count, false);
			return true;
		}
		return false;
	}

	protected static boolean harvestBlock(World world, BlockPos pos, EntityPlayer player) {

		if (world.isAirBlock(pos)) {
			return false;
		}
		EntityPlayerMP playerMP = null;
		if (player instanceof EntityPlayerMP) {
			playerMP = (EntityPlayerMP) player;
		}
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		// Awesome CoFH Crops
		if (block instanceof IHarvestable) {
			if (((IHarvestable) block).canHarvest(state)) {
				return ((IHarvestable) block).harvest(world, pos, state, player);
			}
			return false;
		}
		// Only Harvestable Plants
		if (!(block instanceof IGrowable)) {
			return false;
		}
		IGrowable grow = (IGrowable) block;
		if (grow.canGrow(world, pos, state, world.isRemote)) {
			return false;
		}
		if (!ForgeHooks.canHarvestBlock(block, player, world, pos)) {
			return false;
		}
		// Send the Break Event
		int xpToDrop = 0;
		if (playerMP != null) {
			xpToDrop = ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos);
			if (xpToDrop == -1) {
				return false;
			}
		}
		if (Utils.isServerWorld(world)) {
			if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
				if (!player.capabilities.isCreativeMode) {
					block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
					if (xpToDrop > 0) {
						block.dropXpOnBlockBreak(world, pos, xpToDrop);
					}
				}
			}
			if (playerMP != null) {
				playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
			}
		} else {
			if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
			}
			NetHandlerPlayClient client = Minecraft.getMinecraft().getConnection();
			if (client != null) {
				Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
			}
		}
		return true;
	}

	protected int getRadius(ItemStack stack) {

		return 1 + getMode(stack);
	}

	protected int useEnergy(ItemStack stack, int count, boolean simulate) {

		if (isCreative()) {
			return 0;
		}
		int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 10);
		if (MathHelper.RANDOM.nextInt(2 + unbreakingLevel) >= 2) {
			return 0;
		}
		return extractEnergy(stack, count * ENERGY_PER_USE, simulate);
	}

	@Override
	public ItemStack setDefaultTag(ItemStack stack, int energy) {

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger(TAG_MODE, getNumModes(stack) - 1);
		return super.setDefaultTag(stack, energy);
	}
	// endregion

	// region IMultiModeItem
	@Override
	public int getNumModes(ItemStack stack) {

		return radius;
	}

	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.6F, 1.0F - 0.1F * getMode(stack));
		int radius = getRadius(stack) * 2 + 1;
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.cofh.area").appendText(": " + radius + "x" + radius));
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s", ColorHelper.hasColor(stack, TINT_INDEX_2) ? 1 : 0, this.getEnergyStored(stack) > 0 ? "charged" : "drained")));
		String[] states = { "charged", "drained" };

		for (int color = 0; color < 2; color++) {
			for (int state = 0; state < 2; state++) {
				ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s", color, states[state])));
			}
		}
	}

	@Override
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/rf_scythe/" + path + "\",\"layer1\":\"" + domain + ":items/tools/rf_scythe/rf_scythe_drained\",\"layer2\":\"" + domain + ":items/blank\"},\"transform\":\"forge:default-tool\"},\"variants\":{\"color\":{\"0\":{},\"1\":{\"textures\":{\"layer2\":\"" + domain + ":items/tools/rf_scythe/rf_scythe_color\"}}},\"state\":{\"charged\":{\"textures\":{\"layer1\":\"" + domain + ":items/tools/rf_scythe/rf_scythe_charged\"}},\"drained\":{}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
