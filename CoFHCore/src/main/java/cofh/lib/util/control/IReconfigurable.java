package cofh.lib.util.control;

import net.minecraft.util.EnumFacing;

public interface IReconfigurable {

	boolean decrSide(EnumFacing side);

	boolean incrSide(EnumFacing side);

	boolean setSide(EnumFacing side, SideConfig config);

	boolean resetSides();

	default boolean isReconfigurable() {

		return true;
	}

	// region CONFIGS
	enum SideConfig {
		NONE, INPUT, OUTPUT
	}
	// endregion
}
