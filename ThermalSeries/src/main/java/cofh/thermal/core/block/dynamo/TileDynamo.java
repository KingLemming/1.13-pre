package cofh.thermal.core.block.dynamo;

import cofh.lib.util.helpers.EnergyHelper;
import cofh.thermal.core.block.AbstractTileBase;
import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import static cofh.lib.util.Constants.*;

public class TileDynamo extends AbstractTileBase implements ITickable {

	protected IDynamoFuel curFuel;
	protected EnumFacing facing;

	protected int fuel;
	protected int coolant;

	protected int energyGen = 40;

	public TileDynamo(AbstractTileType type) {

		super(type);
	}

	@Override
	public void update() {

		boolean curActive = isActive;

		if (isActive) {
			processTick();
			if (canProcessFinish()) {
				processFinish();
				if (!redstoneControl.getState() || !canProcessStart()) {
					processOff();
				} else {
					processStart();
				}
			}
		} else if (redstoneControl.getState()) {
			if (timeCheck()) {
				if (canProcessStart()) {
					processStart();
					processTick();
					isActive = true;
				}
			}
		}
		updateActiveState(curActive);
	}

	@Override
	public void updateContainingBlockInfo() {

		super.updateContainingBlockInfo();
		updateFacing();
	}

	protected EnumFacing getFacing() {

		if (facing == null) {
			updateFacing();
		}
		return facing;
	}

	protected void updateFacing() {

		facing = getBlockState().getValue(FACING_ALL);
	}

	protected void transferEnergy(int energy) {

		EnergyHelper.insertEnergyIntoAdjacentEnergyHandler(this, getFacing(), energy, false);
	}

	// region PROCESS
	protected boolean canProcessStart() {

		return false;
	}

	protected boolean canProcessFinish() {

		return fuel <= 0;
	}

	protected void processStart() {

	}

	protected void processFinish() {

	}

	protected void processOff() {

		isActive = false;
		wasActive = true;
		if (world != null) {
			timeTracker.markTime(world);
		}
	}

	protected int processTick() {

		if (fuel <= 0) {
			return 0;
		}
		int energy = Math.min(fuel, energyGen);
		fuel -= energy;
		transferEnergy(energy);
		return energy;
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		fuel = nbt.getInteger(TAG_FUEL);
		coolant = nbt.getInteger(TAG_COOLANT);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setInteger(TAG_FUEL, fuel);
		nbt.setInteger(TAG_COOLANT, coolant);

		return nbt;
	}
	// endregion
}
