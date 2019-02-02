package cofh.lib.util.control;

import cofh.lib.block.ITileCallback;

public interface IRedstoneControllableTile extends IRedstoneControllable, ITileCallback {

	RedstoneControlModule redstoneControl();

	// region IRedstoneControl
	@Override
	default int getPower() {

		return redstoneControl().getPower();
	}

	@Override
	default int getThreshold() {

		return redstoneControl().getThreshold();
	}

	@Override
	default ControlMode getMode() {

		return redstoneControl().getMode();
	}

	@Override
	default void setPower(int power) {

		redstoneControl().setPower(power);
	}

	@Override
	default void setControl(int threshold, ControlMode mode) {

		redstoneControl().setControl(threshold, mode);
	}

}
