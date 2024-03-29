package cofh.lib.util.helpers;

import cofh.lib.fluid.FluidStorageCoFH;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static cofh.lib.util.Constants.TAG_POTION;

public class FluidHelper {

	@CapabilityInject (IFluidHandler.class)
	public static final Capability<IFluidHandler> FLUID_HANDLER = null;

	@CapabilityInject (IFluidHandlerItem.class)
	public static final Capability<IFluidHandler> FLUID_HANDLER_ITEM = null;

	private FluidHelper() {

	}

	public static boolean isWater(IBlockState state) {

		return (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) && state.getValue(BlockLiquid.LEVEL) == 0;
	}

	public static boolean isWater(FluidStack fluid) {

		return FluidHelper.fluidsEqual(fluid, FluidRegistry.WATER);
	}

	public static boolean isLava(IBlockState state) {

		return (state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA) && state.getValue(BlockLiquid.LEVEL) == 0;
	}

	public static boolean isLava(FluidStack fluid) {

		return FluidHelper.fluidsEqual(fluid, FluidRegistry.LAVA);
	}

	public static int fluidHashcode(FluidStack stack) {

		return stack.tag != null ? stack.getFluid().getName().hashCode() + 31 * stack.tag.toString().hashCode() : stack.getFluid().getName().hashCode();
	}

	// region BLOCK TRANSFER
	public static boolean extractFromAdjacent(TileEntity tile, FluidStorageCoFH tank, EnumFacing side) {

		return tank.getFluidStack() == null ? extractFromAdjacent(tile, tank, tank.getCapacity(), side) : extractFromAdjacent(tile, tank, new FluidStack(tank.getFluidStack(), tank.getSpace()), side);
	}

	public static boolean extractFromAdjacent(TileEntity tile, FluidStorageCoFH tank, int amount, EnumFacing side) {

		TileEntity adjTile = BlockHelper.getAdjacentTileEntity(tile, side);
		EnumFacing opposite = side.getOpposite();

		if (hasFluidHandlerCap(adjTile, opposite)) {
			IFluidHandler handler = getFluidHandlerCap(adjTile, opposite);
			if (handler == null) {
				return false;
			}
			FluidStack drainStack = handler.drain(amount, false);
			int drainAmount = tank.fill(drainStack, true);
			if (drainAmount > 0) {
				handler.drain(drainAmount, true);
				return true;
			}
		}
		return false;
	}

	public static boolean extractFromAdjacent(TileEntity tile, FluidStorageCoFH tank, FluidStack resource, EnumFacing side) {

		TileEntity adjTile = BlockHelper.getAdjacentTileEntity(tile, side);
		EnumFacing opposite = side.getOpposite();

		if (hasFluidHandlerCap(adjTile, opposite)) {
			IFluidHandler handler = getFluidHandlerCap(adjTile, opposite);
			if (handler == null) {
				return false;
			}
			FluidStack drainStack = handler.drain(resource, false);
			int drainAmount = tank.fill(drainStack, true);
			if (drainAmount > 0) {
				handler.drain(new FluidStack(resource, drainAmount), true);
				return true;
			}
		}
		return false;
	}

	public static boolean insertIntoAdjacent(TileEntity tile, FluidStorageCoFH tank, EnumFacing side) {

		return insertIntoAdjacent(tile, tank, tank.getFluidAmount(), side);
	}

	public static boolean insertIntoAdjacent(TileEntity tile, FluidStorageCoFH tank, int amount, EnumFacing side) {

		if (tank.isEmpty()) {
			return false;
		}
		TileEntity adjTile = BlockHelper.getAdjacentTileEntity(tile, side);
		EnumFacing opposite = side.getOpposite();

		if (hasFluidHandlerCap(adjTile, opposite)) {
			IFluidHandler handler = getFluidHandlerCap(adjTile, opposite);
			if (handler == null) {
				return false;
			}
			int fillAmount = handler.fill(new FluidStack(tank.getFluidStack(), amount), true);
			if (fillAmount > 0) {
				tank.drain(fillAmount, true);
				return true;
			}
		}
		return false;
	}

	public static boolean hasFluidHandlerCap(TileEntity tile, EnumFacing face) {

		return tile != null && tile.hasCapability(FLUID_HANDLER, face);
	}

	public static IFluidHandler getFluidHandlerCap(TileEntity tile, EnumFacing face) {

		if (tile.hasCapability(FLUID_HANDLER, face)) {
			return tile.getCapability(FLUID_HANDLER, face);
		}
		return null;
	}
	// endregion

	// region COMPARISON
	public static boolean fluidsEqual(FluidStack resourceA, FluidStack resourceB) {

		return resourceA != null && resourceA.isFluidEqual(resourceB) || resourceA == null && resourceB == null;
	}

	public static boolean fluidsEqual(Fluid fluidA, FluidStack resourceB) {

		return fluidA != null && resourceB != null && fluidA == resourceB.getFluid();
	}

	public static boolean fluidsEqual(FluidStack resourceA, Fluid fluidB) {

		return fluidB != null && resourceA != null && fluidB == resourceA.getFluid();
	}

	public static boolean fluidsEqual(Fluid fluidA, Fluid fluidB) {

		return fluidA != null && fluidA.equals(fluidB);
	}
	// endregion

	// region POTION HELPERS
	public static boolean hasPotionTag(FluidStack stack) {

		return stack != null && stack.tag != null && stack.tag.hasKey(TAG_POTION);
	}

	public static void addPotionTooltip(FluidStack stack, List<String> list) {

		addPotionTooltip(stack, list, 1.0F);
	}

	public static void addPotionTooltip(FluidStack stack, List<String> list, float durationFactor) {

		if (stack == null) {
			return;
		}
		List<PotionEffect> effects = PotionUtils.getEffectsFromTag(stack.tag);
		List<Tuple<String, AttributeModifier>> list1 = Lists.newArrayList();

		if (effects.isEmpty()) {
			String s = StringHelper.localize("effect.none").trim();
			list.add(TextFormatting.GRAY + s);
		} else {
			for (PotionEffect effect : effects) {
				String s1 = StringHelper.localize(effect.getEffectName()).trim();
				Potion potion = effect.getPotion();
				Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

				if (!map.isEmpty()) {
					for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(effect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Tuple((entry.getKey()).getName(), attributemodifier1));
					}
				}
				if (effect.getAmplifier() > 0) {
					s1 = s1 + " " + StringHelper.localize("potion.potency." + effect.getAmplifier()).trim();
				}

				if (effect.getDuration() > 20) {
					s1 = s1 + " (" + Potion.getPotionDurationString(effect, durationFactor) + ")";
				}
				if (potion.isBadEffect()) {
					list.add(TextFormatting.RED + s1);
				} else {
					list.add(TextFormatting.BLUE + s1);
				}
			}
		}
		if (!list1.isEmpty()) {
			list.add("");
			list.add(TextFormatting.DARK_PURPLE + StringHelper.localize("potion.whenDrank"));

			for (Tuple<String, AttributeModifier> tuple : list1) {
				AttributeModifier attributemodifier2 = tuple.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;

				if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}
				if (d0 > 0.0D) {
					list.add(TextFormatting.BLUE + StringHelper.localizeFormat("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), StringHelper.localize("attribute.name." + tuple.getFirst())));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					list.add(TextFormatting.RED + StringHelper.localizeFormat("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), StringHelper.localize("attribute.name." + tuple.getFirst())));
				}
			}
		}
	}
	// endregion

	// region CAPABILITY HELPERS
	public static boolean isFluidHandler(ItemStack stack) {

		return !stack.isEmpty() && stack.hasCapability(FLUID_HANDLER_ITEM, null);
	}

	/**
	 * Attempts to drain the item to an IFluidHandler.
	 *
	 * @param stack   The stack to drain from.
	 * @param handler The IFluidHandler to fill.
	 * @param player  The player using the item.
	 * @param hand    The hand the player is holding the item in.
	 * @return If the interaction was successful.
	 */
	public static boolean drainItemToHandler(ItemStack stack, IFluidHandler handler, EntityPlayer player, EnumHand hand) {

		if (stack.isEmpty() || handler == null || player == null) {
			return false;
		}
		IItemHandler playerInv = new InvWrapper(player.inventory);
		FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(stack, handler, playerInv, Integer.MAX_VALUE, player, true);
		if (result.isSuccess()) {
			player.setHeldItem(hand, result.getResult());
			return true;
		}
		return false;
	}

	/**
	 * Attempts to fill the item from an IFluidHandler.
	 *
	 * @param stack   The stack to fill.
	 * @param handler The IFluidHandler to drain from.
	 * @param player  The player using the item.
	 * @param hand    The hand the player is holding the item in.
	 * @return If the interaction was successful.
	 */
	public static boolean fillItemFromHandler(ItemStack stack, IFluidHandler handler, EntityPlayer player, EnumHand hand) {

		if (stack.isEmpty() || handler == null || player == null) {
			return false;
		}
		IItemHandler playerInv = new InvWrapper(player.inventory);
		FluidActionResult result = FluidUtil.tryFillContainerAndStow(stack, handler, playerInv, Integer.MAX_VALUE, player, true);
		if (result.isSuccess()) {
			player.setHeldItem(hand, result.getResult());
			return true;
		}
		return false;
	}

	/**
	 * Attempts to interact the item with an IFluidHandler.
	 * Interaction will always try and fill the item first, if this fails it will attempt to drain the item.
	 *
	 * @param stack   The stack to interact with.
	 * @param handler The Handler to fill / drain.
	 * @param player  The player using the item.
	 * @param hand    The hand the player is holding the item in.
	 * @return If any interaction with the handler was successful.
	 */
	public static boolean interactWithHandler(ItemStack stack, IFluidHandler handler, EntityPlayer player, EnumHand hand) {

		return fillItemFromHandler(stack, handler, player, hand) || drainItemToHandler(stack, handler, player, hand);
	}

	// endregion

	// region PROPERTY HELPERS
	public static int luminosity(Fluid fluid) {

		return fluid == null ? 0 : fluid.getLuminosity();
	}

	public static int luminosity(FluidStack stack) {

		return stack == null ? 0 : luminosity(stack.getFluid());
	}

	public static int density(Fluid fluid) {

		return fluid == null ? 0 : fluid.getDensity();
	}

	public static int density(FluidStack stack) {

		return stack == null ? 0 : density(stack.getFluid());
	}

	public static int temperature(Fluid fluid) {

		return fluid == null ? 0 : fluid.getTemperature();
	}

	public static int temperature(FluidStack stack) {

		return stack == null ? 0 : temperature(stack.getFluid());
	}

	public static int viscosity(Fluid fluid) {

		return fluid == null ? 0 : fluid.getViscosity();
	}

	public static int viscosity(FluidStack stack) {

		return stack == null ? 0 : viscosity(stack.getFluid());
	}
	// endregion

}
