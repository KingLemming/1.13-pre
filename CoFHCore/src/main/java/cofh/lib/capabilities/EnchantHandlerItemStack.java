package cofh.lib.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnchantHandlerItemStack implements IEnchantHandlerItem, ICapabilityProvider {

	Set<ResourceLocation> validEnchantments = new HashSet<>();

	public EnchantHandlerItemStack(List<ResourceLocation> enchantments) {

		validEnchantments.addAll(enchantments);
	}

	@Override
	public boolean canEnchant(ItemStack stack, ResourceLocation enchantment) {

		return validEnchantments.contains(enchantment);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

		return capability == CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY;
	}

	@SuppressWarnings ("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

		return capability == CapabilityEnchantable.ENCHANTABLE_ITEM_CAPABILITY ? (T) this : null;
	}

}
