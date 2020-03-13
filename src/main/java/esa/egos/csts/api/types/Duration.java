package esa.egos.csts.api.types;

import java.time.temporal.ChronoUnit;

import b1.ccsds.csts.common.types.IntUnsigned;
import esa.egos.csts.api.enumerations.DurationType;

/**
 * This class represents the CCSDS Duration type.
 * 
 * A duration specifies a type which represents its SI unit as seconds,
 * milliseconds or microseconds. This type is used encoding and encoding.
 * 
 * However, since {@link java.time.Duration} is used as the internal duration
 * type, there are no limitations regarding SI units.
 * 
 * This class is immutable.
 */
public class Duration implements Comparable<Duration> {

	private final DurationType type;
	private final java.time.Duration duration;

	private Duration(DurationType type, java.time.Duration duration) {
		this.type = type;
		this.duration = duration;
	}

	/**
	 * Returns the duration.
	 * 
	 * @return the duration
	 */
	public java.time.Duration getDuration() {
		return duration;
	}

	/**
	 * Returns the duration type.
	 * 
	 * @return the duration type
	 */
	public DurationType getType() {
		return type;
	}

	public static Duration of(DurationType type, java.time.Duration duration) {
		return new Duration(type, duration);
	}

	/**
	 * Encodes this Duration into a CCSDS Duration type.
	 * 
	 * @return the CCSDS Duration type representing this object
	 */
	public b1.ccsds.csts.common.types.Duration encode() {
		b1.ccsds.csts.common.types.Duration duration = new b1.ccsds.csts.common.types.Duration();
		switch (type) {
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

	/**
	 * Decodes a specified CCSDS Duration type.
	 * 
	 * @param duration the specified CCSDS Duration type
	 * @return a new Duration decoded from the specified CCSDS Duration type
	 */
	public static Duration decode(b1.ccsds.csts.common.types.Duration duration) {
		Duration newDuration = null;
		if (duration.getMicroseconds() != null) {
			newDuration = new Duration(DurationType.MICROSECONDS, java.time.Duration.of(duration.getMicroseconds().longValue(), ChronoUnit.MICROS));
		} else if (duration.getMilliseconds() != null) {
			newDuration = new Duration(DurationType.MILLISECONDS, java.time.Duration.ofMillis(duration.getMilliseconds().longValue()));
		} else if (duration.getSeconds() != null) {
			newDuration = new Duration(DurationType.SECONDS, java.time.Duration.ofSeconds(duration.getSeconds().longValue()));
		}
		return newDuration;
	}

	@Override
	public int compareTo(Duration o) {
		return duration.compareTo(o.duration);
	}
	
	@Override
	public String toString() {
	    return "Duration [type=" + this.type.name() + ", duration=" + this.duration.toString() + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Duration)) {
			return false;
		}
		Duration duration = (Duration)o;
		if (!duration.getType().equals(this.type)) {
			return false;
		}
		return duration.getDuration().equals(this.duration);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31*hash + this.type.ordinal();
		hash = 31*hash + this.duration.hashCode();
		return hash;
	}

}
