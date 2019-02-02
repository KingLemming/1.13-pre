package cofh.lib.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.ArrayList;

public class CapabilityEnchantable {

	private static boolean registered = false;

	@CapabilityInject (IEnchantHandlerItem.class)
	public static Capability<IEnchantHandlerItem> ENCHANTABLE_ITEM_CAPABILITY = null;

	public static void register() {

		if (registered) {
			return;
		}
		registered = true;

		CapabilityManager.INSTANCE.register(IEnchantHandlerItem.class, new IStorage<IEnchantHandlerItem>() {

			@Override
			public NBTBase writeNBT(Capability<IEnchantHandlerItem> capability, IEnchantHandlerItem instance, EnumFacing side) {

				return null;
			}

			@Override
			public void readNBT(Capability<IEnchantHandlerItem> capability, IEnchantHandlerItem instance, EnumFacing side, NBTBase nbt) {

			}

		}, () -> new EnchantHandlerItemStack(new ArrayList<>()));
	}

}
