package cofh.thermal.innovation.item;

import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.item.IAreaEffectItem;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.RayTracer;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ColorHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermal.core.item.ItemRFTool;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.StringHelper.*;

public class ItemRFSaw extends ItemRFTool implements IAreaEffectItem, IMultiModeItem {

	protected static final int SINGLE = 0;
	protected static final int TUNNEL_3 = 1;
	protected static final int AREA_3 = 2;
	protected static final int CUBE_3 = 3;
	protected static final int AREA_5 = 4;

	protected static boolean enableEnchantEffect = true;

	public ItemRFSaw(int maxEnergy, int maxReceive, int harvestLevel, float efficiency, int numModes) {

		super(maxEnergy, maxReceive, harvestLevel, efficiency);

		setHarvestLevel(TOOL_AXE, harvestLevel);
		setHarvestLevel(TOOL_SWORD, harvestLevel);
		setHarvestLevel(TOOL_SAW, harvestLevel);

		effectiveMaterials.add(Material.WOOD);
		effectiveMaterials.add(Material.CACTUS);
		effectiveMaterials.add(Material.GOURD);
		effectiveMaterials.add(Material.LEAVES);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.VINE);
		effectiveMaterials.add(Material.WEB);

		this.numModes = MathHelper.clamp(numModes, 1, 5);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		tooltip.add(getInfoText("info.thermal.rf_saw.0"));
		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(localize("info.thermal.rf_saw.b." + getMode(stack)));
		tooltip.add(localizeFormat("info.thermal.rf_saw.a.0", getKeyName(KeyMultiModeItem.INSTANCE.getKey())));

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {

		if (!isActive(stack)) {
			return;
		}
		long activeTime = stack.getTagCompound().getLong(TAG_ACTIVE);
		if (entity.world.getTotalWorldTime() > activeTime) {
			clearActive(stack);
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		if (EnumEnchantmentType.BREAKABLE.equals(enchantment.type)) {
			return enchantment.equals(Enchantments.UNBREAKING);
		}
		return enchantment.type.canEnchantItem(this) || enchantment.canApply(new ItemStack(Items.IRON_AXE));
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

		World world = player.world;
		IBlockState state = world.getBlockState(pos);

		if (!canHarvestBlock(state, stack)) {
			if (!player.capabilities.isCreativeMode) {
				useEnergy(stack, 1, false);
			}
			return false;
		}
		if (player.isSneaking()) {
			if (!player.capabilities.isCreativeMode) {
				useEnergy(stack, 1, false);
			}
			return false;
		}
		RayTraceResult traceResult = RayTracer.retrace(player, false);
		if (traceResult == null || traceResult.sideHit == null) {
			return false;
		}
		float refStrength = state.getPlayerRelativeBlockHardness(player, world, pos);
		int count = 1;
		int mode = getMode(stack);

		switch (mode) {
			case SINGLE:
				break;
			case TUNNEL_3:
				count += breakTunnel3(player, world, pos, traceResult, refStrength);
				break;
			case AREA_3:
				count += breakArea3(player, world, pos, traceResult, refStrength);
				break;
			case CUBE_3:
				count += breakCube3(player, world, pos, traceResult, refStrength);
				break;
			case AREA_5:
				count += breakArea5(player, world, pos, traceResult, refStrength);
				break;
		}
		if (count > 0 && !player.capabilities.isCreativeMode) {
			useEnergy(stack, count, false);
		}
		return false;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {

		setActive(stack, entityLiving);
		return true;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {

		return enableEnchantEffect && stack.isItemEnchanted();
	}

	// region IAreaEffectItem
	@Override
	public ImmutableList<BlockPos> getAreaEffectBlocks(ItemStack stack, BlockPos pos, EntityPlayer player) {

		ArrayList<BlockPos> area = new ArrayList<>();
		World world = player.getEntityWorld();
		int mode = getMode(stack);

		RayTraceResult traceResult = RayTracer.retrace(player, false);
		if (traceResult == null || traceResult.sideHit == null || !canHarvestBlock(world.getBlockState(pos), stack) || player.isSneaking()) {
			return ImmutableList.copyOf(area);
		}
		switch (mode) {
			case SINGLE:
				break;
			case TUNNEL_3:
				getAOEBlocksTunnel3(stack, player, world, pos, traceResult, area);
				break;
			case AREA_3:
				getAOEBlocksArea3(stack, player, world, pos, traceResult, area);
				break;
			case CUBE_3:
				getAOEBlocksCube3(stack, player, world, pos, traceResult, area);
				break;
			case AREA_5:
				getAOEBlocksArea5(stack, player, world, pos, traceResult, area);
				break;
		}
		return ImmutableList.copyOf(area);
	}
	// endregion

	// region IMultimodeItem
	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 0.6F, 1.0F - 0.1F * getMode(stack));
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.thermal.rf_saw.b." + getMode(stack)));
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s", ColorHelper.hasColor(stack, TINT_INDEX_3) ? 1 : 0, this.getEnergyStored(stack) > 0 ? isActive(stack) ? "active" : "charged" : "drained")));
		String[] states = { "charged", "active", "drained" };

		for (int color = 0; color < 2; color++) {
			for (int state = 0; state < 3; state++) {
				ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s", color, states[state])));
			}
		}
	}

	@Override
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/rf_saw/" + path + "\",\"layer1\":\"" + domain + ":items/tools/rf_saw/rf_saw_drained\",\"layer2\":\"" + domain + ":items/blank\",\"layer3\":\"" + domain + ":items/blank\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"x\":90},{\"y\":-90},{\"z\":45}],\"translation\":[0,0.35,0.1],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[{\"x\":90},{\"y\":90},{\"z\":-45}],\"translation\":[0,0.35,0.1],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":25}],\"translation\":[0,0.15,0.12],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":-25}],\"translation\":[0,0.15,0.12],\"scale\":[0.68,0.68,0.68]},\"ground\":{\"translation\":[0,0.15,0],\"scale\":[0.5,0.5,0.5]}}},\"variants\":{\"color\":{\"0\":{},\"1\":{\"textures\":{\"layer3\":\"" + domain + ":items/tools/rf_saw/rf_saw_color\"}}},\"state\":{\"charged\":{\"textures\":{\"layer1\":\"" + domain + ":items/tools/rf_saw/rf_saw_charged\"}},\"active\":{\"textures\":{\"layer1\":\"" + domain + ":items/tools/rf_saw/rf_saw_active_0\",\"layer2\":\"" + domain + ":items/tools/rf_saw/rf_saw_active_1\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"x\":90},{\"y\":-90},{\"z\":60}],\"translation\":[0,0.35,0],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[{\"x\":90},{\"y\":90},{\"z\":-60}],\"translation\":[0,0.35,0],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":45}],\"translation\":[0,0.1,0.1],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":-45}],\"translation\":[0,0.1,0.1],\"scale\":[0.68,0.68,0.68]}}},\"drained\":{}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
