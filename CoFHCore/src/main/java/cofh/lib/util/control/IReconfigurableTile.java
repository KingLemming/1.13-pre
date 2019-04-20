package cofh.lib.util.control;

import cofh.lib.block.ITileCallback;
import net.minecraft.util.EnumFacing;

public interface IReconfigurableTile extends IReconfigurable, ITileCallback {

	ReconfigControlModule reconfigControl();

	// region IReconfigurable
	@Override
	default SideConfig getSideConfig(EnumFacing side) {

		return reconfigControl().getSideConfig(side);
	}

	@Override
	default boolean prevSideConfig(EnumFacing side) {

		return reconfigControl().prevSideConfig(side);
	}

	@Override
	default boolean nextSideConfig(EnumFacing side) {

		return reconfigControl().nextSideConfig(side);
	}

	@Override
	default boolean setSideConfig(EnumFacing side, SideConfig config) {

		return reconfigControl().setSideConfig(side, config);
	}

	@Override
	default boolean clearAllSides() {

		return reconfigControl().clearAllSides();
	}

	EnumFacing getFacing();
	// endregion
}
