package cofh.lib.fluid;

import cofh.lib.block.ITileCallback;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Basic Fluid Handler implementation using CoFH Fluid Storage objects.
 */
public class SimpleFluidHandler implements IFluidHandler {

	protected ITileCallback tile;
	protected List<FluidStorageCoFH> tanks;
	protected IFluidTankProperties[] properties;

	public SimpleFluidHandler(@Nullable ITileCallback tile, @Nonnull List<FluidStorageCoFH> tanks) {

		this.tile = tile;
		this.tanks = tanks;
		cacheProperties();
	}

	protected void cacheProperties() {

		properties = new IFluidTankProperties[tanks.size()];
		for (int i = 0; i < properties.length; i++) {
			properties[i] = tanks.get(i).getProperties();
		}
	}

	public boolean hasTanks() {

		return tanks.size() > 0;
	}

	public boolean isEmpty() {

		for (FluidStorageCoFH tank : tanks) {
			if (!tank.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public int getTanks() {

		return tanks.size();
	}

	// region IFluidHandler
	@Override
	public IFluidTankProperties[] getTankProperties() {

		return properties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {

		int ret;
		for (FluidStorageCoFH tank : tanks) {
			ret = tank.fill(resource, doFill);
			if (ret > 0) {
				return ret;
			}
		}
		return 0;
	}

	@Nullable
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {

		FluidStack ret;
		for (FluidStorageCoFH tank : tanks) {
			ret = tank.drain(resource, doDrain);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {

		FluidStack ret;
		for (FluidStorageCoFH tank : tanks) {
			ret = tank.drain(maxDrain, doDrain);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}
	// endregion
}
