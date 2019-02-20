package cofh.lib.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.TAG_TANK;
import static cofh.lib.util.Constants.TAG_TANK_ARRAY;

public class TankArrayCoFH extends SimpleFluidHandler {

	protected String tag;

	public TankArrayCoFH(@Nullable TileEntity tile) {

		this(tile, new ArrayList<>(), TAG_TANK_ARRAY);
	}

	public TankArrayCoFH(@Nullable TileEntity tile, @Nonnull List<FluidStorageCoFH> tanks) {

		this(tile, tanks, TAG_TANK);
	}

	public TankArrayCoFH(@Nullable TileEntity tile, @Nonnull String tag) {

		this(tile, new ArrayList<>(), tag);
	}

	public TankArrayCoFH(@Nullable TileEntity tile, @Nonnull List<FluidStorageCoFH> tanks, @Nonnull String tag) {

		super(tile, tanks);
		this.tag = tag;
	}

	public void clear() {

		for (FluidStorageCoFH tank : tanks) {
			tank.clear();
		}
	}

	public void set(int tank, FluidStack stack) {

		tanks.get(tank).setFluid(stack);
	}

	public FluidStack get(int tank) {

		return tanks.get(tank).getFluidStack();
	}

	public FluidStorageCoFH getTank(int tank) {

		return tanks.get(tank);
	}

	// region NBT
	public TankArrayCoFH readFromNBT(NBTTagCompound nbt) {

		NBTTagCompound array = nbt.getCompoundTag(tag);
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).readFromNBT(array.getCompoundTag(TAG_TANK + i));
		}
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		NBTTagCompound array = new NBTTagCompound();
		for (int i = 0; i < tanks.size(); i++) {
			array.setTag(TAG_TANK + i, tanks.get(i).writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag(tag, array);
		return nbt;
	}
	// endregion
}
