package cofh.lib.util.control;

import net.minecraft.util.EnumFacing;

public interface IReconfigurable {

	SideConfig get(EnumFacing side);

	boolean prev(EnumFacing side);

	boolean next(EnumFacing side);

	boolean set(EnumFacing side, SideConfig config);

	boolean clear();

	/**
	 * This returns whether or not reconfiguration functionality is enabled at all.
	 */
	default boolean isReconfigurable() {

		return true;
	}

	// region CONFIGS
	enum SideConfig {
		SIDE_NONE, SIDE_INPUT, SIDE_OUTPUT, SIDE_BOTH, SIDE_ACCESSIBLE;

		public static final SideConfig[] VALUES = values();

		SideConfig prev() {

			switch (this) {
				case SIDE_INPUT:
					return SIDE_NONE;
				case SIDE_OUTPUT:
					return SIDE_INPUT;
				case SIDE_BOTH:
					return SIDE_OUTPUT;
				case SIDE_ACCESSIBLE:
					return SIDE_BOTH;
				default:
					return SIDE_ACCESSIBLE;
			}
		}

		SideConfig next() {

			switch (this) {
				case SIDE_NONE:
					return SIDE_INPUT;
				case SIDE_INPUT:
					return SIDE_OUTPUT;
				case SIDE_OUTPUT:
					return SIDE_BOTH;
				case SIDE_BOTH:
					return SIDE_ACCESSIBLE;
				default:
					return SIDE_NONE;
			}
		}
	}
	// endregion
}
