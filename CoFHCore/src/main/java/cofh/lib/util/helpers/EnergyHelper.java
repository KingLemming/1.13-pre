package cofh.lib.util.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import static cofh.lib.util.Constants.TAG_ENERGY;

public class EnergyHelper {

	private EnergyHelper() {

	}

	public static ItemStack setDefaultEnergyTag(ItemStack container, int energy) {

		if (!container.hasTagCompound()) {
			container.setTagCompound(new NBTTagCompound());
		}
		container.getTagCompound().setInteger(TAG_ENERGY, energy);

		return container;
	}

	public static int attemptItemCharge(ItemStack stack, int maxReceive, boolean simulate) {

		IEnergyStorage handler = getEnergyHandler(stack);
		return handler == null ? 0 : handler.receiveEnergy(maxReceive, simulate);
	}

	public static boolean isEnergyHandler(ItemStack stack) {

		return !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null);
	}

	public static IEnergyStorage getEnergyHandler(ItemStack stack) {

		return stack.getCapability(CapabilityEnergy.ENERGY, null);
	}

}
