package cofh.thermal.core.item;

import cofh.core.fluid.FluidPotion;
import cofh.lib.fluid.IFluidContainerItem;
import cofh.lib.item.IColorableItem;
import cofh.lib.util.helpers.ColorHelper;
import cofh.thermal.core.init.FluidsTSeries;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.TINT_INDEX_1;

public abstract class ItemPotionContainer extends ItemFluidContainer implements IColorableItem, IFluidContainerItem {

	public ItemPotionContainer(int fluidCapacity) {

		super(fluidCapacity);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {

		return colorMultiplier(stack, 1);
	}

	// region IItemColor
	public int colorMultiplier(ItemStack stack, int tintIndex) {

		if (tintIndex == TINT_INDEX_1) {
			FluidStack fluid = getFluid(stack);
			if (fluid != null) {
				return FluidPotion.getPotionColor(fluid);
			}
		}
		return ColorHelper.getColor(stack, tintIndex);
	}
	// endregion

	// region IFluidContainerItem
	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {

		if (!FluidsTSeries.isPotion(resource)) {
			return 0;
		}
		return super.fill(container, resource, doFill);
	}
	// endregion
}
