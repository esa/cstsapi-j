package esa.egos.csts.api.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import esa.egos.csts.api.enumerations.TimeType;

/**
 * This class represents the CCSDS Time type.
 * 
 * Time objects support the representation of a time type in the CCSDS day
 * segmented time code in milliseconds or the CCSDS day segmented time code in
 * picoseconds.
 * 
 * Time objects do not hold time zones and only represent a date and a time
 * value. Epoch is set to 1958-01-01 00:00:00.
 * 
 * This class is immutable.
 */
public class Time implements Comparable<Time> {

	private static final LocalDate CCSDS_EPOCH_DATE = LocalDate.of(1958, 1, 1);
	private static final LocalDateTime CCSDS_EPOCH_DATE_TIME = LocalDateTime.of(CCSDS_EPOCH_DATE, LocalTime.of(0, 0, 0));

	private final byte[] value;
	private final TimeType type;

	private Time(byte[] value) {
		this.value = value;
		if (value.length == 8) {
			type = TimeType.MILLISECONDS;
		} else {
			type = TimeType.PICOSECONDS;
		}
	}

	/**
	 * Returns the value array.
	 * 
	 * @return the value array
	 */
	public byte[] toArray() {
		return value;
	}

	/**
	 * Returns the type, which indicates whether the underlying Time value is
	 * encoded in CCSDS day segmented time code in milliseconds or CCSDS day
	 * segmented time code in picoseconds.
	 * 
	 * @return the type of this Time value.
	 */
	public TimeType getType() {
		return type;
	}

	/**
	 * Creates a Time object from the specified value array.
	 * 
	 * @param value the specified value array which must be of length 8 for CCSDS
	 *              day segmented time code in milliseconds, or of length 10 for
	 *              CCSDS day segmented time code in picoseconds
	 * @return a new Time object of the specified value array
	 * @throws IllegalArgumentException if the value array is not of length 8 or 10
	 */
	public static Time of(byte[] value) {
		if (value.length != 8 && value.length != 10) {
			throw new IllegalArgumentException("The length of the value array of a Time type must be of length 8 or 10.");
		}
		return new Time(value);
	}

	/**
	 * Creates a Time object from the specified Instant object.
	 * 
	 * @param instant the specified Instant object
	 * @return a new Time object of the specified LocalDateTime object which is
	 *         encoded in the CCSDS day segmented time code in milliseconds, since
	 *         there is no support for picoseconds in the Java 8 LocalDateTime API
	 */
	public static Time of(Instant instant) {
		return new Time(encodeInstantToCCSDSMillis(instant));
	}

	/**
	 * Creates a Time object from the specified LocalDateTime object.
	 * 
	 * @param localDateTime the specified LocalDateTime object
	 * @return a new Time object of the specified LocalDateTime object which is
	 *         encoded in the CCSDS day segmented time code in milliseconds, since
	 *         there is no support for picoseconds in the Java 8 LocalDateTime API
	 */
	public static Time of(LocalDateTime localDateTime) {
		return new Time(encodeLocalDateTimeToCCSDSMillis(localDateTime));
	}

	/**
	 * Creates a Time object from the current date and time from the system UTC
	 * clock.
	 * 
	 * @return a new Time object of the current date and time from the system clock
	 *         which is encoded in the CCSDS day segmented time code in
	 *         milliseconds, since there is no support for picoseconds in the Java 8
	 *         LocalDateTime API
	 */
	public static Time now() {
		return Time.of(Instant.now());
	}

	/**
	 * Creates a Time object from the current date and time from the system clock in
	 * the default time-zone.
	 * 
	 * @return a new Time object of the current date and time from the system clock
	 *         which is encoded in the CCSDS day segmented time code in
	 *         milliseconds, since there is no support for picoseconds in the Java 8
	 *         LocalDateTime API
	 */
	public static Time nowLocal() {
		return Time.of(LocalDateTime.now());
	}

	/**
	 * Returns this Time object as an {@link Instant} object.
	 * 
	 * @return this Time object as an {@link Instant} object
	 */
	public Instant toInstant() {
		Instant instant = null;
		switch (type) {
		case MILLISECONDS:
			instant = decodeCCSDSMillisToInstant(value);
			break;
		case PICOSECONDS:
			instant = decodeCCSDSPicosToInstant(value);
			break;
		}
		return instant;
	}

	/**
	 * Returns this Time object as a {@link LocalDateTime} object.
	 * 
	 * @return this Time object as a {@link LocalDateTime} object
	 */
	public LocalDateTime toLocalDateTime() {
		LocalDateTime time = null;
		switch (type) {
		case MILLISECONDS:
			time = decodeCCSDSMillisToLocalDateTime(value);
			break;
		case PICOSECONDS:
			time = decodeCCSDSPicosToLocalDateTime(value);
			break;
		}
		return time;
	}

	/**
	 * Indicates whether this Time is after the specified Time.
	 * 
	 * @param other the specified Time
	 * @return true if the specified Time is after this Time, false otherwise
	 */
	public boolean isAfter(Time other) {
		return toLocalDateTime().isAfter(other.toLocalDateTime());
	}

	/**
	 * Indicates whether this Time is equal to the specified Time.
	 * 
	 * @param other the specified Time
	 * @return true if the specified Time is equal to this Time, false otherwise
	 */
	public boolean isEqual(Time other) {
		return toLocalDateTime().isEqual(other.toLocalDateTime());
	}

	/**
	 * Indicates whether this Time is before the specified Time.
	 * 
	 * @param other the specified Time
	 * @return true if the specified Time is before this Time, false otherwise
	 */
	public boolean isBefore(Time other) {
		return toLocalDateTime().isBefore(other.toLocalDateTime());
	}

	/**
	 * Indicates whether this Time is after the specified {@link LocalDateTime}.
	 * 
	 * @param other the specified Time
	 * @return true if the specified {@link LocalDateTime} is after this Time, false
	 *         otherwise
	 */
	public boolean isAfter(LocalDateTime other) {
		return toLocalDateTime().isAfter(other);
	}

	/**
	 * Indicates whether this Time is equal to the specified {@link LocalDateTime}.
	 * 
	 * @param other the specified Time
	 * @return true if the specified {@link LocalDateTime} is equal to this Time,
	 *         false otherwise
	 */
	public boolean isEqual(LocalDateTime other) {
		return toLocalDateTime().isEqual(other);
	}

	/**
	 * Indicates whether this Time is before the specified {@link LocalDateTime}.
	 * 
	 * @param other the specified Time
	 * @return true if the specified {@link LocalDateTime} is before this Time,
	 *         false otherwise
	 */
	public boolean isBefore(LocalDateTime other) {
		return toLocalDateTime().isBefore(other);
	}

	/**
	 * Indicates whether this Time is after the specified {@link Instant}.
	 * 
	 * @param other the specified Time
	 * @return true if the specified {@link Instant} is after this Time, false
	 *         otherwise
	 */
	public boolean isAfter(Instant other) {
		return toInstant().isAfter(other);
	}

	/**
	 * Indicates whether this Time is equal to the specified {@link Instant}.
	 * 
	 * @param other the specified Time
	 * @return true if the specified {@link Instant} is equal this Time, false
	 *         otherwise
	 */
	public boolean isEqual(Instant other) {
		return toInstant().equals(other);
	}

	/**
	 * Indicates whether this Time is before the specified {@link Instant}.
	 * 
	 * @param other the specified Time
	 * @return true if the specified {@link Instant} is before this Time, false
	 *         otherwise
	 */
	public boolean isBefore(Instant other) {
		return toInstant().isBefore(other);
	}

	/**
	 * Encodes this Time object into a CCSDS Time type.
	 * 
	 * @return the CCSDS Time type representing this Time object
	 */
	public b1.ccsds.csts.common.types.Time encode(b1.ccsds.csts.common.types.Time time) {
		if (value.length == 8) {
			time.setCcsdsFormatMilliseconds(new b1.ccsds.csts.common.types.TimeCCSDSMilli(value));
		} else if (value.length == 10) {
			time.setCcsdsFormatPicoseconds(new b1.ccsds.csts.common.types.TimeCCSDSPico(value));
		}
		return time;
	}
	
	public b2.ccsds.csts.common.types.Time encode(b2.ccsds.csts.common.types.Time time) {
		if (value.length == 8) {
			time.setCcsdsFormatMilliseconds(new b2.ccsds.csts.common.types.TimeCCSDSMilli(value));
		} else if (value.length == 10) {
			time.setCcsdsFormatPicoseconds(new b2.ccsds.csts.common.types.TimeCCSDSPico(value));
		}
		return time;
	}

	/**
	 * Decodes a specified CCSDS Time type.
	 * 
	 * @param time the specified CCSDS Time type
	 * @return a new Time object decoded from the specified CCSDS Duration type
	 */
	public static Time decode(b1.ccsds.csts.common.types.Time time) {
		Time newTime = null;
		if (time.getCcsdsFormatMilliseconds() != null) {
			newTime = new Time(time.getCcsdsFormatMilliseconds().value);
		} else if (time.getCcsdsFormatPicoseconds() != null) {
			newTime = new Time(time.getCcsdsFormatPicoseconds().value);
		}
		return newTime;
	}
	
	public static Time decode(b2.ccsds.csts.common.types.Time time) {
		Time newTime = null;
		if (time.getCcsdsFormatMilliseconds() != null) {
			newTime = new Time(time.getCcsdsFormatMilliseconds().value);
		} else if (time.getCcsdsFormatPicoseconds() != null) {
			newTime = new Time(time.getCcsdsFormatPicoseconds().value);
		}
		return newTime;
	}

	/**
	 * Encodes a given Instant object to CCSDS day segmented time code in
	 * milliseconds.
	 * 
	 * @param instant the specified Instant object
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in milliseconds
	 */
	public static byte[] encodeInstantToCCSDSMillis(Instant instant) {
		return encodeLocalDateTimeToCCSDSMillis(LocalDateTime.ofInstant(instant, ZoneOffset.UTC));
	}

	/**
	 * Encodes a given LocalDateTime object to CCSDS day segmented time code in
	 * milliseconds.
	 * 
	 * @param localDateTime the specified LocalDateTime object
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in milliseconds
	 */
	public static byte[] encodeLocalDateTimeToCCSDSMillis(LocalDateTime localDateTime) {

		if (localDateTime.isBefore(CCSDS_EPOCH_DATE_TIME)) {
			throw new IllegalArgumentException("Specified date is before CCSDS epoch (1958-01-01 00:00:00).");
		}

		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long daysSinceEpoch = ChronoUnit.DAYS.between(CCSDS_EPOCH_DATE, localDateTime);

		// truncate to two bytes
		short truncatedDaysSinceEpoch = (short) daysSinceEpoch;
		buffer.putShort(truncatedDaysSinceEpoch);

		// milliseconds of day
		long millisOfDay = localDateTime.getLong(ChronoField.MILLI_OF_DAY);

		// truncate to four bytes
		int truncatedMillisOfDay = (int) millisOfDay;
		buffer.putInt(truncatedMillisOfDay);

		// microseconds of day (could be a value filled with trailing zeros if computer
		// clock does not support this level of resolution)
		long microsOfDay = localDateTime.getLong(ChronoField.MICRO_OF_DAY);

		// microseconds of millisecond
		long microsOfMilli = microsOfDay - TimeUnit.MILLISECONDS.toMicros(millisOfDay);

		// truncate to two bytes
		short truncatedMicrosOfMillis = (short) microsOfMilli;
		buffer.putShort(truncatedMicrosOfMillis);

		return buffer.array();

	}

	/**
	 * Decodes a given CCSDS day segmented time code in milliseconds to Instant.
	 * 
	 * @param CCSDSMillis the specified byte array with the length of 8, containing
	 *                    the CCSDS day segmented time code in milliseconds
	 * @return the decoded Instant object
	 */
	public static Instant decodeCCSDSMillisToInstant(byte[] CCSDSMillis) {
		return decodeCCSDSMillisToLocalDateTime(CCSDSMillis).toInstant(ZoneOffset.UTC);
	}

	/**
	 * Decodes a given CCSDS day segmented time code in milliseconds to
	 * LocalDateTime.
	 * 
	 * @param CCSDSMillis the specified byte array with the length of 8, containing
	 *                    the CCSDS day segmented time code in milliseconds
	 * @return the decoded LocalDateTime object
	 */
	public static LocalDateTime decodeCCSDSMillisToLocalDateTime(byte[] CCSDSMillis) {

		if (CCSDSMillis.length != 8) {
			throw new IllegalArgumentException("Specified array does not have required length of 8.");
		}

		ByteBuffer buffer = ByteBuffer.wrap(CCSDSMillis);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long daysSinceEpoch = Short.toUnsignedLong(buffer.getShort());
		LocalDate localDate = CCSDS_EPOCH_DATE.plusDays(daysSinceEpoch);

		// milliseconds of day
		long millisOfDay = Integer.toUnsignedLong(buffer.getInt());
		if (millisOfDay < 0 || millisOfDay > 86399999) {
			throw new IllegalArgumentException("Invalid value for milliseconds of day (0 - 86399999): " + Long.toUnsignedString(millisOfDay));
		}

		// nanoseconds of days derived from milliseconds
		long nanosOfDay = TimeUnit.MILLISECONDS.toNanos(millisOfDay);

		// microseconds of millisecond
		long microsOfMilli = Short.toUnsignedLong(buffer.getShort());
		nanosOfDay += TimeUnit.MICROSECONDS.toNanos(microsOfMilli);

		return LocalDateTime.of(localDate, LocalTime.ofNanoOfDay(nanosOfDay));

	}

	/**
	 * Encodes a given Instant object to CCSDS day segmented time code in
	 * picoseconds.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The picoseconds
	 * need to be specified as an integer value, which is encoded in the result.
	 * 
	 * @param instant      the specified Instant object which will automatically be
	 *                     truncated to milliseconds, since microseconds and
	 *                     nanoseconds are specified using the picoseconds parameter
	 * @param picosOfMilli the specified picoseconds of milliseconds, where 0 <=
	 *                     picosOfMilli <= 999,999,999
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in picoseconds
	 */
	public static byte[] encodeInstantToCCSDSPicos(Instant instant, int picosOfMilli) {
		return encodeLocalDateTimeToCCSDSPicos(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), picosOfMilli);
	}

	/**
	 * Encodes a given LocalDateTime object to CCSDS day segmented time code in
	 * picoseconds.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The picoseconds
	 * need to be specified as an integer value, which is encoded in the result.
	 * 
	 * @param localDateTime the specified LocalDateTime object which will
	 *                      automatically be truncated to milliseconds, since
	 *                      microseconds and nanoseconds are specified using the
	 *                      picoseconds parameter
	 * @param picosOfMilli  the specified picoseconds of milliseconds, where 0 <=
	 *                      picosOfMilli <= 999,999,999
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in picoseconds
	 */
	public static byte[] encodeLocalDateTimeToCCSDSPicos(LocalDateTime localDateTime, int picosOfMilli) {

		if (localDateTime.isBefore(CCSDS_EPOCH_DATE_TIME)) {
			throw new IllegalArgumentException("Specified date is before CCSDS epoch (1958-01-01 00:00:00).");
		}

		if (picosOfMilli < 0 || picosOfMilli > 999999999) {
			throw new IllegalArgumentException("Invalid value for picoseconds of millisecond (0 - 999999999): " + picosOfMilli);
		}

		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long daysSinceEpoch = ChronoUnit.DAYS.between(CCSDS_EPOCH_DATE, localDateTime);

		// truncate to two bytes
		short truncatedDaysSinceEpoch = (short) daysSinceEpoch;
		buffer.putShort(truncatedDaysSinceEpoch);

		// milliseconds of day
		long millisOfDay = localDateTime.getLong(ChronoField.MILLI_OF_DAY);

		// truncate to four bytes
		int truncatedMillisOfDay = (int) millisOfDay;
		buffer.putInt(truncatedMillisOfDay);

		// picoseconds of millisecond
		buffer.putInt(picosOfMilli);

		return buffer.array();

	}

	/**
	 * Decodes a given CCSDS day segmented time code in picoseconds to Instant.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The returned
	 * LocalDateTime object will be truncated to nanosecond resolution.
	 * 
	 * @param CCSDSPicos the specified byte array with the length of 8, containing
	 *                   the CCSDS day segmented time code in picoseconds
	 * @return the decoded Instant object
	 */
	public static Instant decodeCCSDSPicosToInstant(byte[] CCSDSPicos) {
		return decodeCCSDSPicosToLocalDateTime(CCSDSPicos).toInstant(ZoneOffset.UTC);
	}

	/**
	 * Decodes a given CCSDS day segmented time code in picoseconds to
	 * LocalDateTime.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The returned
	 * LocalDateTime object will be truncated to nanosecond resolution.
	 * 
	 * @param CCSDSPicos the specified byte array with the length of 8, containing
	 *                   the CCSDS day segmented time code in picoseconds
	 * @return the decoded LocalDateTime object
	 */
	public static LocalDateTime decodeCCSDSPicosToLocalDateTime(byte[] CCSDSPicos) {

		if (CCSDSPicos.length != 10) {
			throw new IllegalArgumentException("Specified array does not have required length of 10.");
		}

		ByteBuffer buffer = ByteBuffer.wrap(CCSDSPicos);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long daysSinceEpoch = Short.toUnsignedLong(buffer.getShort());
		LocalDate localDate = CCSDS_EPOCH_DATE.plusDays(daysSinceEpoch);

		// milliseconds of day
		long millisOfDay = Integer.toUnsignedLong(buffer.getInt());

		if (millisOfDay < 0 || millisOfDay > 86399999) {
			throw new IllegalArgumentException("Invalid value for milliseconds of day (0 - 86399999): " + Long.toUnsignedString(millisOfDay));
		}

		// nanoseconds of days derived from milliseconds
		long nanosOfDay = TimeUnit.MILLISECONDS.toNanos(millisOfDay);

		// picoseconds of millisecond
		long picosOfMilli = Integer.toUnsignedLong(buffer.getInt());

		if (picosOfMilli < 0 || picosOfMilli > 999999999) {
			throw new IllegalArgumentException("Invalid value for picoseconds of miliseconds (0 - 999999999): " + picosOfMilli);
		}

		// convert manually, since there is no support for picosecond conversion through
		// Java API
		nanosOfDay += picosOfMilli / 1000;

		return LocalDateTime.of(localDate, LocalTime.ofNanoOfDay(nanosOfDay));

	}

	@Override
	public int compareTo(Time o) {
		return toLocalDateTime().compareTo(o.toLocalDateTime());
	}
	
	@Override
	public String toString() {
	    return "Time [type=" + this.type.name() + ", value=[" + Arrays.toString(this.value) + "]]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Time)) {
			return false;
		}
		Time time = (Time)o;
		if (time.getType() != this.type) {
			return false;
		}
		return Arrays.equals(time.toArray(), this.value);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31*hash + this.type.ordinal();
		for (int i = 0; i < this.value.length; i++) {
			hash = 31*hash + this.value[i];
		}
		return hash;
	}

}