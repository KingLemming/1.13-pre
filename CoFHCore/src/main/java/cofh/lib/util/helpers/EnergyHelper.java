package cofh.lib.util.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import static cofh.lib.util.Constants.TAG_ENERGY;

/**
 * This class contains helper functions related to Redstone Flux, aka the Forge Energy system.
 *
 * @author King Lemming
 */
public class EnergyHelper {

	@CapabilityInject (IEnergyStorage.class)
	public static final Capability<IEnergyStorage> ENERGY_HANDLER = null;

	private EnergyHelper() {

	}

	public static int insertEnergyIntoAdjacentEnergyHandler(TileEntity tile, EnumFacing side, int energy, boolean simulate) {

		TileEntity handler = BlockHelper.getAdjacentTileEntity(tile, side);

		if (handler != null && handler.hasCapability(ENERGY_HANDLER, side.getOpposite())) {
			return handler.getCapability(ENERGY_HANDLER, side.getOpposite()).receiveEnergy(energy, simulate);
		}
		return 0;
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
