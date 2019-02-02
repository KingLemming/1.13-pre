package cofh.lib.util.control;

import cofh.lib.block.ITileCallback;

public interface ITransferControllableTile extends ITransferControllable, ITileCallback {

	TransferControlModule transferControl();

	// region ITransfercontrol
	@Override
	default boolean hasTransferIn() {

		return transferControl().hasTransferIn();
	}

	@Override
	default boolean hasTransferOut() {

		return transferControl().hasTransferOut();
	}

	@Override
	default boolean getTransferIn() {

		return transferControl().getTransferIn();
	}

	@Override
	default boolean getTransferOut() {

		return transferControl().getTransferOut();
	}

	@Override
	default void setControl(boolean input, boolean output) {

		transferControl().setControl(input, output);
	}

}
