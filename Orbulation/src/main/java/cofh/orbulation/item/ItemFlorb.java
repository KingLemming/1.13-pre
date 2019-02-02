package cofh.orbulation.item;

import cofh.core.item.ItemCoFH;
import cofh.lib.fluid.FluidContainerItemWrapper;
import cofh.lib.fluid.IFluidContainerItem;
import cofh.lib.util.RayTracer;
import cofh.lib.util.Utils;
import cofh.orbulation.entity.projectile.EntityFlorb;
import cofh.orbulation.util.FlorbUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.MAGMATIC_TEMPERATURE;
import static cofh.lib.util.Constants.TAG_FLUID;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.StringHelper.*;
import static cofh.orbulation.util.FlorbUtils.FLORB_LIST;

public class ItemFlorb extends ItemCoFH implements IFluidContainerItem {

	private final boolean magmatic;

	public ItemFlorb(boolean magmatic) {

		this.magmatic = magmatic;
	}

	public boolean magmatic() {

		return magmatic;
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
		if (stack.getTagCompound() == null) {
			tooltip.add(localize("info.orbulation.florb.a." + (magmatic ? 1 : 0)));
			tooltip.add(getUseText("info.orbulation.florb.0"));
			tooltip.add(localize("info.orbulation.florb.1"));
		} else {
			tooltip.add(getUseText("info.orbulation.florb.2"));
			Fluid fluid = FluidRegistry.getFluid(stack.getTagCompound().getString(TAG_FLUID));
			if (fluid != null && fluid.getDensity() < 0) {
				tooltip.add(localize("info.orbulation.florb.a.1"));
				tooltip.add(localize("info.orbulation.florb.a.2"));
			}
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (isInCreativeTab(tab)) {
			items.add(new ItemStack(this));
			if (magmatic) {
				items.addAll(FLORB_LIST);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {

		String fluidName = "info.cofh.empty";
		String openParen = " (";
		String closeParen = END + ")";

		if (stack.getTagCompound() != null) {
			Fluid fluid = FluidRegistry.getFluid(stack.getTagCompound().getString(TAG_FLUID));

			if (fluid != null) {
				fluidName = fluid.getUnlocalizedName();

				if (fluid.getRarity() == EnumRarity.UNCOMMON) {
					openParen += YELLOW;
				} else if (fluid.getRarity() == EnumRarity.RARE) {
					openParen += BRIGHT_BLUE;
				} else if (fluid.getRarity() == EnumRarity.EPIC) {
					openParen += PINK;
				}
			}
		}
		return super.getItemStackDisplayName(stack) + openParen + localize(fluidName) + closeParen;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (!stack.hasTagCompound()) {
			RayTraceResult traceResult = RayTracer.retrace(player, true);

			if (traceResult == null || traceResult.sideHit == null) {
				return new ActionResult<>(EnumActionResult.PASS, stack);
			}
			ItemStack filledFlorb = FluidUtil.tryPickUpFluid(cloneStack(stack, 1), player, world, traceResult.getBlockPos(), traceResult.sideHit).getResult();

			if (filledFlorb.hasTagCompound()) {
				if (player.capabilities.isCreativeMode) {
					return new ActionResult<>(EnumActionResult.SUCCESS, stack);
				}
				if (!player.inventory.addItemStackToInventory(filledFlorb)) {
					player.dropItem(filledFlorb, false);
				}
				return new ActionResult<>(EnumActionResult.SUCCESS, cloneStack(stack, stack.getCount() - 1));
			}
			return new ActionResult<>(EnumActionResult.PASS, stack);
		}
		Fluid fluid = FluidRegistry.getFluid(stack.getTagCompound().getString(TAG_FLUID));
		if (fluid == null) {
			return new ActionResult<>(EnumActionResult.PASS, stack);
		}
		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}
		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (Utils.isServerWorld(world)) {
			EntityFlorb florb = new EntityFlorb(world, player, fluid);
			florb.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			world.spawnEntity(florb);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	// region IFluidContainerItem
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		return new FluidContainerItemWrapper(stack, this);
	}

	@Override
	public FluidStack getFluid(ItemStack container) {

		if (!container.hasTagCompound()) {
			return null;
		}
		return new FluidStack(FluidRegistry.getFluid(container.getTagCompound().getString(TAG_FLUID)), Fluid.BUCKET_VOLUME);
	}

	@Override
	public int getCapacity(ItemStack container) {

		return Fluid.BUCKET_VOLUME;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {

		if (container.hasTagCompound() || container.getCount() > 1) {
			return 0;
		}
		if (resource == null || resource.amount < Fluid.BUCKET_VOLUME) {
			return 0;
		}
		if (resource.getFluid().getTemperature(resource) > MAGMATIC_TEMPERATURE && !magmatic) {
			return 0;
		}
		if (!FlorbUtils.FLORB_MAP.containsKey(resource.getFluid().getName())) {
			return 0;
		}
		if (doFill) {
			FlorbUtils.setTag(container, resource.getFluid());
		}
		return Fluid.BUCKET_VOLUME;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {

		if (!container.hasTagCompound() || container.getCount() > 1) {
			return null;
		}
		if (maxDrain < Fluid.BUCKET_VOLUME) {
			return null;
		}
		FluidStack retStack = getFluid(container);

		if (doDrain) {
			container.setTagCompound(null);
		}
		return retStack;
	}
	// endregion

	// region IModelRegister
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"variants\":{\"inventory\":{\"model\":\"forge:forgebucket\",\"textures\":{\"base\":\"" + domain + ":items/" + path + "\",\"fluid\":\"" + path + ":items/florb_mask\"},\"transform\":\"forge:default-item\",\"custom\":{\"fluid\":\"water\",\"flipGas\":false}}}}";

		try {
			File itemModel = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(itemModel, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
