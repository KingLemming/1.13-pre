package cofh.lib.fluid;

import cofh.lib.block.ITileCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.Constants.TAG_TANK;
import static cofh.lib.util.Constants.TAG_TANK_ARRAY;

/**
 * Fluid "inventory" abstraction using CoFH Fluid Storage objects.
 */
public class TankArrayCoFH extends SimpleFluidHandler {

	protected String tag;

	public TankArrayCoFH(@Nullable ITileCallback tile) {

		this(tile, 0, TAG_TANK_ARRAY);
	}

	public TankArrayCoFH(@Nullable ITileCallback tile, int size) {

		this(tile, size, TAG_TANK_ARRAY);
	}

	public TankArrayCoFH(@Nullable ITileCallback tile, @Nonnull List<FluidStorageCoFH> tanks) {

		this(tile, tanks, TAG_TANK_ARRAY);
	}

	public TankArrayCoFH(@Nullable ITileCallback tile, @Nonnull String tag) {

		this(tile, 0, tag);
	}

	public TankArrayCoFH(@Nullable ITileCallback tile, @Nonnull List<FluidStorageCoFH> tanks, @Nonnull String tag) {

		super(tile, tanks);
		this.tag = tag;
	}

	public TankArrayCoFH(@Nullable ITileCallback tile, int size, @Nonnull String tag) {

		super(tile, new ArrayList<>(size));
		this.tile = tile;
		this.tag = tag;
		for (int i = 0; i < size; i++) {
			tanks.add(new FluidStorageCoFH(Fluid.BUCKET_VOLUME));
		}
		cacheProperties();
	}

	public void clear() {

		for (FluidStorageCoFH tank : tanks) {
			tank.clear();
		}
	}

	public void set(int tank, FluidStack stack) {

		tanks.get(tank).setFluidStack(stack);
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
		cacheProperties();
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
