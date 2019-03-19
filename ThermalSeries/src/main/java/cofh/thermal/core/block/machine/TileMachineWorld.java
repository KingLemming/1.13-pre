package cofh.thermal.core.block.machine;

import cofh.thermal.core.block.AbstractTileType;

public abstract class TileMachineWorld extends TileMachine {

	protected boolean cached;
	protected int timeConstant = TIME_CONSTANT;
	protected int timeOffset;

	public TileMachineWorld(AbstractTileType type) {

		super(type);
	}

	@Override
	public void update() {

		if (!timeCheckOffset()) {
			return;
		}
		transferInput();

		boolean curActive = isActive;

		if (isActive) {
			if (!canProcess()) {
				isActive = false;
			} else {
				processTick();
			}
		} else if (canProcess()) {
			processTick();
			isActive = true;
		}
		if (!cached) {
			updateValidity();
		}
		transferOutput();

		updateActiveState(curActive);
	}

	@Override
	public void onNeighborBlockChange() {

		super.onNeighborBlockChange();
		updateValidity();
	}

	// region PROCESS
	protected boolean canProcess() {

		return redstoneControl.getState();
	}

	protected int processTick() {

		return 0;
	}
	// endregion

	// region HELPERS
	protected boolean timeCheckOffset() {

		return (world.getTotalWorldTime() + timeOffset) % timeConstant == 0;
	}

	protected void updateValidity() {

	}
	// endregion
}
