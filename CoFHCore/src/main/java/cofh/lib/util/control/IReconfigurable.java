package cofh.lib.util.control;

import net.minecraft.util.EnumFacing;

public interface IReconfigurable {

	SideConfig getSideConfig(EnumFacing side);

	boolean prevSideConfig(EnumFacing side);

	boolean nextSideConfig(EnumFacing side);

	boolean setSideConfig(EnumFacing side, SideConfig config);

	boolean clearAllSides();

	/**
	 * This returns whether or not reconfiguration functionality is enabled at all.
	 */
	default boolean isReconfigurable() {

		return true;
	}

	// region CONFIGS
	enum SideConfig {

		SIDE_NONE(false, false), SIDE_INPUT(true, false), SIDE_OUTPUT(false, true), SIDE_BOTH(true, true), SIDE_ACCESSIBLE(false, false);

		public static final SideConfig[] VALUES = values();

		boolean autoInput;
		boolean autoOutput;

		SideConfig(boolean autoInput, boolean autoOutput) {

			this.autoInput = autoInput;
			this.autoOutput = autoOutput;
		}

		public boolean autoInput() {

			return autoInput;
		}

		public boolean autoOutput() {

			return autoOutput;
		}

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
