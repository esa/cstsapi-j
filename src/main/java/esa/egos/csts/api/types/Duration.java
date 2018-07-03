package esa.egos.csts.api.types;

import java.time.temporal.ChronoUnit;

import ccsds.csts.common.types.IntUnsigned;
import esa.egos.csts.api.enumerations.DurationEnum;

public class Duration {

	private final DurationEnum enumeration;
	private java.time.Duration duration;

	public Duration(DurationEnum enumeration) {
		this.enumeration = enumeration;
	}

	public java.time.Duration getDuration() {
		return duration;
	}

	public void setDuration(java.time.Duration duration) {
		this.duration = duration;
	}

	public DurationEnum getEnumeration() {
		return enumeration;
	}

	public ccsds.csts.common.types.Duration encode() {
		ccsds.csts.common.types.Duration duration = new ccsds.csts.common.types.Duration();
		switch (enumeration) {
		case MICROSECONDS:
			duration.setMicroseconds(new IntUnsigned(this.duration.toNanos() / 1000));
			break;
		case MILLISECONDS:
			duration.setMilliseconds(new IntUnsigned(this.duration.toMillis()));
			break;
		case SECONDS:
			duration.setSeconds(new IntUnsigned(this.duration.getSeconds()));
			break;
		}
		return duration;
	}

	public static Duration decode(ccsds.csts.common.types.Duration dur) {
		Duration duration = null;
		if (dur.getMicroseconds() != null) {
			duration = new Duration(DurationEnum.MICROSECONDS);
			duration.setDuration(java.time.Duration.of(dur.getMicroseconds().longValue(), ChronoUnit.MICROS));
		} else if (dur.getMilliseconds() != null) {
			duration = new Duration(DurationEnum.MILLISECONDS);
			duration.setDuration(java.time.Duration.ofMillis(dur.getMilliseconds().longValue()));
		} else if (dur.getSeconds() != null) {
			duration = new Duration(DurationEnum.SECONDS);
			duration.setDuration(java.time.Duration.ofSeconds(dur.getSeconds().longValue()));
		}
		return duration;
	}

}
