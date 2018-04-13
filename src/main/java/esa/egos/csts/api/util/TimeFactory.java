package esa.egos.csts.api.util;

import esa.egos.csts.api.util.impl.TimeSource;

public class TimeFactory {

	/**
	 * Can be overwritten.
	 * @return
	 */
	public static TimeSource createTimeSource() {
		TimeSource source = new TimeSource();
		return source;
	}

}
