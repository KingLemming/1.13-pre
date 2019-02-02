package cofh.lib.fluid;

import cofh.lib.capabilities.CapabilityEnchantable;
import cofh.lib.capabilities.IEnchantHandlerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FluidEnchantableItemWrapper extends FluidContainerItemWrapper {

	final Set<ResourceLocation> enchants = new HashSet<>();

	final IEnchantHandlerItem enchantableCap;

	public FluidEnchantableItemWrapper(ItemStack stackIn, IFluidContainerItem containerIn, boolean canFillIn, boolean canDrainIn, Collection<ResourceLocation> enchantsIn) {

		super(stackIn, containerIn, canFillIn, canDrainIn);

		this.enchants.addAll(enchantsIn);
		this.enchantableCap = (stack, enchantment) -> enchants.contains(enchantment);
	}

	public FluidEnchantableItemWrapper(ItemStack stackIn, IFluidContainerItem containerIn, Collection<ResourceLocation> enchantsIn) {

		this(stackIn, containerIn, true, true, enchantsIn);
	}

	// region ICapabilityProvider
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		return capability == CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		if (!hasCapability(capability, facing)) {
			return null;
		}
		if (capability == CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY) {
			return CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY.cast(enchantableCap);
		}
		return super.getCapability(capability, facing);
	}
	// endregion
}
