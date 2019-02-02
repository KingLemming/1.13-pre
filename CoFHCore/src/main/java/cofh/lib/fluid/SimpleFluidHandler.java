package cofh.lib.fluid;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SimpleFluidHandler implements IFluidHandler {

	protected TileEntity tile;
	protected List<FluidStorageCoFH> tanks;
	protected IFluidTankProperties[] properties;

	public SimpleFluidHandler(@Nullable TileEntity tile, @Nonnull List<FluidStorageCoFH> tanks) {

		this.tile = tile;
		this.tanks = tanks;
		cacheProperties();
	}

	public void cacheProperties() {

		properties = new IFluidTankProperties[tanks.size()];
		for (int i = 0; i < properties.length; i++) {
			properties[i] = tanks.get(0).getProperties();
		}
	}

	public boolean hasTanks() {

		return tanks.size() > 0;
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
