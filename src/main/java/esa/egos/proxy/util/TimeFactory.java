package esa.egos.proxy.util;

import esa.egos.proxy.util.impl.TimeSource;

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
