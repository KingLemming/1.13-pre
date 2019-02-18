package cofh.lib.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EnchantableItemWrapper implements ICapabilityProvider {

	final Set<ResourceLocation> enchants = new HashSet<>();

	final IEnchantHandlerItem enchantableCap;

	public EnchantableItemWrapper(Collection<ResourceLocation> enchantsIn) {

		this.enchants.addAll(enchantsIn);
		this.enchantableCap = (stack, enchantment) -> enchants.contains(enchantment);
	}

	// region ICapabilityProvider
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		return capability == CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing facing) {

		if (!hasCapability(capability, facing)) {
			return null;
		}
		return CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY.cast(enchantableCap);
	}
	// endregion
}
