package cofh.lib.util.helpers;

import java.time.MonthDay;

/**
 * The class contains helper functions related to Holidays!
 *
 * Yes, they are US-centric. Feel free to suggest others!
 *
 * @author King Lemming
 */
public class HolidayHelper {

	// TODO: Finish
	private HolidayHelper() {

	}

	// region HOLIDAYS
	public enum Holiday {

		// @formatter:off
		NEW_YEAR(1, 1),
		VALENTINES(2, 14),
		LEAP(2, 29),
		ST_PATRICKS(3, 17),
		APRIL_FOOLS(4, 1),
		EARTH(4, 22),
		US_INDEPENDENCE(7, 4),
		HALLOWEEN(10, 31),
		DAY_OF_DEAD(11, 1),
		VETERANS(11, 11),
		CHRISTMAS(12, 25),
		BOXING_DAY(12, 26),
		NEW_YEARS_EVE(12, 31);
		// @formatter:on

		private final MonthDay date;

		Holiday(int month, int dayOfMonth) {

			this.date = MonthDay.of(month, dayOfMonth);
		}
	}
	// endregion
}
