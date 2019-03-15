package cofh.lib.fluid;

import cofh.lib.block.ITileCallback;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ManagedFluidHandler implements IFluidHandler {

	protected ITileCallback tile;
	protected List<FluidStorageCoFH> tanks = new ArrayList<>();
	protected List<FluidStorageCoFH> inputTanks;
	protected List<FluidStorageCoFH> outputTanks;
	protected IFluidTankProperties[] properties;

	public ManagedFluidHandler(@Nullable ITileCallback tile, @Nonnull List<FluidStorageCoFH> inputTanks, @Nonnull List<FluidStorageCoFH> outputTanks) {

		this.tile = tile;
		this.inputTanks = inputTanks;
		this.outputTanks = outputTanks;
		this.tanks.addAll(inputTanks);
		this.tanks.addAll(outputTanks);
		cacheProperties();
	}

	public void cacheProperties() {

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
		for (FluidStorageCoFH tank : inputTanks) {
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
		for (FluidStorageCoFH tank : outputTanks) {
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
		for (FluidStorageCoFH tank : outputTanks) {
			ret = tank.drain(maxDrain, doDrain);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}
	// endregion
}
