package cofh.lib.util.control;

import cofh.lib.block.ITileCallback;
import net.minecraft.util.EnumFacing;

public interface IReconfigurableTile extends IReconfigurable, ITileCallback {

	ReconfigControlModule reconfigControl();

	// region IReconfigurable
	@Override
	default SideConfig get(EnumFacing side) {

		return reconfigControl().get(side);
	}

	@Override
	default boolean prev(EnumFacing side) {

		return reconfigControl().prev(side);
	}

	@Override
	default boolean next(EnumFacing side) {

		return reconfigControl().next(side);
	}

	@Override
	default boolean set(EnumFacing side, SideConfig config) {

		return reconfigControl().set(side, config);
	}

	@Override
	default boolean clear() {

		return reconfigControl().clear();
	}
	// endregion
}
