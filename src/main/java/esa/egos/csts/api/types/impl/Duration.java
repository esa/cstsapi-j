package esa.egos.csts.api.types.impl;

import java.time.temporal.ChronoUnit;

import ccsds.csts.common.types.IntUnsigned;
import esa.egos.csts.api.enums.DurationEnum;

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
			duration.setMicroseconds(new IntUnsigned(this.duration.toNanos()/1000));
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
	
	public static Duration decode(ccsds.csts.common.types.Duration newDuration) {
		Duration duration = null;
		if (newDuration.getMicroseconds() != null) {
			duration = new Duration(DurationEnum.MICROSECONDS);
			duration.setDuration(java.time.Duration.of(newDuration.getMicroseconds().longValue(), ChronoUnit.MICROS));
		} else if (newDuration.getMilliseconds() != null) {
			duration = new Duration(DurationEnum.MILLISECONDS);
			duration.setDuration(java.time.Duration.ofMillis(newDuration.getMilliseconds().longValue()));
		} else if (newDuration.getSeconds() != null) {
			duration = new Duration(DurationEnum.SECONDS);
			duration.setDuration(java.time.Duration.ofSeconds(newDuration.getSeconds().longValue()));
		}
		return duration;
	}
	
}
