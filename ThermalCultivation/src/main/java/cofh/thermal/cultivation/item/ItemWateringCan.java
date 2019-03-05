package cofh.thermal.cultivation.item;

import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.RayTracer;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ColorHelper;
import cofh.thermal.core.item.ItemFluidContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
import static cofh.lib.util.helpers.StringHelper.*;

public class ItemWateringCan extends ItemFluidContainer implements IMultiModeItem {

	public static final int MB_PER_USE = 50;

	protected static boolean enableEnchantEffect = true;
	protected static boolean allowFakePlayers = false;
	protected static boolean removeSourceBlocks = true;

	protected int radius;
	protected int effectiveness;

	public ItemWateringCan(int fluidCapacity, int radius, int effectiveness) {

		super(fluidCapacity);
		this.radius = radius;
		this.effectiveness = effectiveness;
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

		tooltip.add(getInfoText("info.thermal.watering_can.0"));
		tooltip.add(localize("info.cofh.area") + ": " + radius + "x" + radius);

		if (getNumModes(stack) > 1) {
			tooltip.add(localizeFormat("info.thermal.watering_can.a.0", getKeyName(KeyMultiModeItem.INSTANCE.getKey())));
		}
		if (isCreative(stack)) {
			tooltip.add(localize(FluidRegistry.WATER.getUnlocalizedName()) + ": " + localize("info.cofh.infinite"));
		} else {
			tooltip.add(localize(localize(FluidRegistry.WATER.getUnlocalizedName()) + ": " + formatNumber(getFluidAmount(stack)) + " / " + formatNumber(getCapacity(stack)) + " mB"));
			tooltip.add(getNoticeText("info.thermal.watering_can.1"));
		}
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
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || getFluidAmount(oldStack) > 0 != getFluidAmount(newStack) > 0 || getFluidAmount(newStack) > 0 && isActive(oldStack) != isActive(newStack));
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {

		return RGB_DURABILITY_WATER;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		RayTraceResult traceResult = RayTracer.retrace(player, true);
		ItemStack stack = player.getHeldItem(hand);

		if (traceResult == null || traceResult.sideHit == null) {
			return new ActionResult<>(EnumActionResult.PASS, stack);
		}
		BlockPos tracePos = traceResult.getBlockPos();

		if (!player.isSneaking() || !world.isBlockModifiable(player, tracePos) || player instanceof FakePlayer && !allowFakePlayers) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		if (isWater(world.getBlockState(tracePos)) && getSpace(stack) > 0) {
			if (removeSourceBlocks) {
				world.setBlockState(tracePos, Blocks.AIR.getDefaultState(), 11);
			}
			fill(stack, new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
			player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		RayTraceResult traceResult = RayTracer.retrace(player, true);

		if (traceResult == null || traceResult.sideHit == null || player instanceof FakePlayer && !allowFakePlayers) {
			return EnumActionResult.FAIL;
		}
		ItemStack stack = player.getHeldItem(hand);
		BlockPos tracePos = traceResult.getBlockPos();
		BlockPos offsetPos = tracePos.offset(traceResult.sideHit);

		if (getFluidAmount(stack) < MB_PER_USE) {
			return EnumActionResult.FAIL;
		}
		setActive(stack, player);

		int radius = getRadius(stack);
		int x = offsetPos.getX();
		double y = offsetPos.getY() + 0.4D;
		int z = offsetPos.getZ();

		for (int i = x - radius; i <= x + radius; i++) {
			for (int k = z - radius; k <= z + radius; k++) {
				world.spawnParticle(EnumParticleTypes.WATER_DROP, i + world.rand.nextFloat(), y, k + world.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
			}
		}
		Iterable<BlockPos.MutableBlockPos> area = BlockPos.getAllInBoxMutable(offsetPos.add(-radius, -1, -radius), offsetPos.add(radius, 1, radius));
		for (BlockPos scan : area) {
			IBlockState state = world.getBlockState(scan);

			if (state.getBlock() instanceof BlockFarmland) {
				int moisture = state.getValue(BlockFarmland.MOISTURE);
				if (moisture < 7) {
					world.setBlockState(scan, state.withProperty(BlockFarmland.MOISTURE, 7), 3);
				}
			}
		}
		if (Utils.isServerWorld(world)) {
			if (world.rand.nextInt(100) < Math.max(effectiveness - 5 * getMode(stack), 1)) {
				for (BlockPos scan : area) {
					Block plant = world.getBlockState(scan).getBlock();
					if (plant instanceof IGrowable || plant instanceof IPlantable || plant == Blocks.MYCELIUM || plant == Blocks.CHORUS_FLOWER) {
						world.scheduleBlockUpdate(scan, plant, 0, 1);
					}
				}
			}
			if (!player.capabilities.isCreativeMode) {
				drain(stack, MB_PER_USE * getRadius(stack) * 2, true);
			}
		}
		return EnumActionResult.FAIL;
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
		return stack;
	}

	protected static boolean isWater(IBlockState state) {

		return (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) && state.getValue(BlockLiquid.LEVEL) == 0;
	}

	public int getRadius(ItemStack stack) {

		return 1 + getMode(stack);
	}
	// endregion

	// region IFluidContainerItem
	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {

		if (resource == null || !FluidRegistry.WATER.equals(resource.getFluid())) {
			return 0;
		}
		return super.fill(container, resource, doFill);
	}
	// endregion

	// region IMultiModeItem
	@Override
	public int getNumModes(ItemStack stack) {

		return radius + 1;
	}

	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 0.6F, 1.0F - 0.1F * getMode(stack));
		int radius = getRadius(stack) * 2 + 1;
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.cofh.area").appendText(": " + radius + "x" + radius));
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s", ColorHelper.hasColor(stack, TINT_INDEX_2) ? 1 : 0, this.getFluidAmount(stack) > 0 ? isActive(stack) ? "tipped" : "level" : "empty")));
		String[] states = { "level", "tipped", "empty" };

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
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/watering_can/" + path + "\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"x\":90},{\"y\":-90},{\"z\":25}],\"translation\":[0,0.25,-0.2],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[{\"x\":90},{\"y\":90},{\"z\":-25}],\"translation\":[0,0.25,-0.2],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":25}],\"translation\":[0,0.15,0.12],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":-25}],\"translation\":[0,0.15,0.12],\"scale\":[0.68,0.68,0.68]},\"ground\":{\"translation\":[0,0.15,0],\"scale\":[0.5,0.5,0.5]}}},\"variants\":{\"color\":{\"0\":{},\"1\":{\"textures\":{\"layer2\":\"" + domain + ":items/tools/watering_can/watering_can_color\"}}},\"state\":{\"level\":{\"textures\":{\"layer1\":\"" + domain + ":items/tools/watering_can/watering_can_level\"}},\"tipped\":{\"textures\":{\"layer1\":\"" + domain + ":items/tools/watering_can/watering_can_tipped\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"x\":90},{\"y\":-90},{\"z\":55}],\"translation\":[0,0.1,-0.3],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[{\"x\":90},{\"y\":90},{\"z\":-55}],\"translation\":[0,0.1,-0.3],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":45}],\"translation\":[0,0.05,0.12],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":-45}],\"translation\":[0,0.05,0.12],\"scale\":[0.68,0.68,0.68]}}},\"empty\":{}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
