package esa.egos.csts.api.types;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.beanit.jasn1.ber.types.BerNull;

/**
 * This class represents the CCSDS ConditionalTime type.
 * 
 * This class is immutable.
 */
public class ConditionalTime {

	private final Optional<Time> time;

	private ConditionalTime() {
		time = Optional.empty();
	}

	private ConditionalTime(Time time) {
		this.time = Optional.of(time);
	}

	/**
	 * Indicates whether the Time value is known.
	 * 
	 * @return true if the Time value is known, false otherwise
	 */
	public boolean isKnown() {
		return time.isPresent();
	}

	/**
	 * Returns the Time.
	 * @return the Time
	 * @throws NoSuchElementException if the Time value is unknown.
	 */
	public Time getTime() {
		return time.get();
	}
	
	/**
	 * Creates a new unknown Conditional Time object.
	 * 
	 * @return an unknown Conditional Time object
	 */
	public static ConditionalTime unknown() {
		return new ConditionalTime();
	}

	/**
	 * Creates a new defined Conditional Time object.
	 * 
	 * @param time
	 *            the used time to define the Conditional Time object
	 * @return a defined Conditional Time object
	 */
	public static ConditionalTime of(Time time) {
		return new ConditionalTime(time);
	}

	/**
	 * Encodes this Conditional Time into a CCSDS Conditional Time type.
	 * 
	 * @return the CCSDS Conditional Time type representing this object
	 */
	public b1.ccsds.csts.common.types.ConditionalTime encode(b1.ccsds.csts.common.types.ConditionalTime time) {
		if (this.time.isPresent()) {
			time.setKnown(this.time.get().encode(new b1.ccsds.csts.common.types.Time()));
		} else {
			time.setUndefined(new BerNull());
		}
		return time;
	}
	
	public b2.ccsds.csts.common.types.ConditionalTime encode(b2.ccsds.csts.common.types.ConditionalTime time) {
		if (this.time.isPresent()) {
			time.setKnown(this.time.get().encode(new b2.ccsds.csts.common.types.Time()));
		} else {
			time.setUndefined(new BerNull());
		}
		return time;
	}

	/**
	 * Decodes a specified CCSDS Conditional Time type.
	 * 
	 * @param conditionalTime
	 *            the specified CCSDS Conditional Time type
	 * @return a new Conditional Time decoded from the specified CCSDS Conditional
	 *         Time type
	 */
	public static ConditionalTime decode(b1.ccsds.csts.common.types.ConditionalTime conditionalTime) {
		ConditionalTime newConditionalTime = null;
		if (conditionalTime.getKnown() != null) {
			newConditionalTime = new ConditionalTime(Time.decode(conditionalTime.getKnown()));
		} else if (conditionalTime.getUndefined() != null) {
			newConditionalTime = new ConditionalTime();
		}
		return newConditionalTime;
	}
	
	public static ConditionalTime decode(b2.ccsds.csts.common.types.ConditionalTime conditionalTime) {
		ConditionalTime newConditionalTime = null;
		if (conditionalTime.getKnown() != null) {
			newConditionalTime = new ConditionalTime(Time.decode(conditionalTime.getKnown()));
		} else if (conditionalTime.getUndefined() != null) {
			newConditionalTime = new ConditionalTime();
		}
		return newConditionalTime;
	}

}
