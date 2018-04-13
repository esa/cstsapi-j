package esa.egos.csts.api.types.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import ccsds.csts.common.types.TimeCCSDSMilli;
import ccsds.csts.common.types.TimeCCSDSPico;
import esa.egos.csts.api.enums.TimeEnum;

// TODO use unsigned representations while encoding to / decoding from LocalDateTime
public class Time {

	private final TimeEnum enumeration;
	private byte[] milliseconds;
	private byte[] picoseconds;

	public Time(TimeEnum enumeration) {
		this.enumeration = enumeration;
	}

	public TimeEnum getEnumeration() {
		return enumeration;
	}

	public byte[] getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(byte[] milliseconds) {
		if (enumeration != TimeEnum.MILLISECONDS) {
			throw new java.lang.IllegalAccessError("This Time type is declared as " + enumeration
					+ ". Setting this Time type as " + TimeEnum.MILLISECONDS + " is not supported.");
		}
		if (milliseconds.length != 8) {
			throw new IllegalArgumentException("Time type in " + TimeEnum.MILLISECONDS + " needs to specify 8 bytes.");
		}
		this.milliseconds = milliseconds;
	}

	public byte[] getPicoseconds() {
		return picoseconds;
	}

	public void setPicoseconds(byte[] picoseconds) {
		if (enumeration != TimeEnum.PICOSECONDS) {
			throw new java.lang.IllegalAccessError("This Time type is declared as " + enumeration
					+ ". Setting this Time type as " + TimeEnum.PICOSECONDS + " is not supported.");
		}
		if (picoseconds.length != 10) {
			throw new IllegalArgumentException("Time type in " + TimeEnum.PICOSECONDS + " needs to specify 10 bytes.");
		}
		this.picoseconds = picoseconds;
	}

	public ccsds.csts.common.types.Time encode() {
		ccsds.csts.common.types.Time time = new ccsds.csts.common.types.Time();
		if (enumeration == TimeEnum.MILLISECONDS) {
			time.setCcsdsFormatMilliseconds(new TimeCCSDSMilli(milliseconds));
		} else if (enumeration == TimeEnum.PICOSECONDS) {
			time.setCcsdsFormatPicoseconds(new TimeCCSDSPico(picoseconds));
		}
		return time;
	}

	public static Time decode(ccsds.csts.common.types.Time time) {
		Time newTime = null;
		if (time.getCcsdsFormatMilliseconds() != null) {
			newTime = new Time(TimeEnum.MILLISECONDS);
			newTime.setMilliseconds(time.getCcsdsFormatMilliseconds().value);
		} else if (time.getCcsdsFormatPicoseconds() != null) {
			newTime = new Time(TimeEnum.PICOSECONDS);
			newTime.setPicoseconds(time.getCcsdsFormatPicoseconds().value);
		}
		return newTime;
	}

	/**
	 * Encodes a given LocalDateTime object to CCSDS day segmented time code in
	 * milliseconds.
	 * 
	 * @param localDateTime
	 *            the specified LocalDateTime object
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in milliseconds
	 */
	public static byte[] encodeLocalDateTimeToCCSDSMillis(LocalDateTime localDateTime) {

		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long days = ChronoUnit.DAYS.between(LocalDate.of(1958, 01, 01), localDateTime);

		// truncate to two bytes
		short truncatedDays = (short) days;
		buffer.putShort(truncatedDays);

		// milliseconds of day
		long millis = localDateTime.getLong(ChronoField.MILLI_OF_DAY);

		// truncate to four bytes
		int truncatedMillis = (int) millis;
		buffer.putInt(truncatedMillis);

		// microseconds of day (could be a value filled with trailing zeros if computer
		// clock does not support this level of resolution)
		long micros = localDateTime.getLong(ChronoField.MICRO_OF_DAY);

		// microseconds of millisecond
		long remainingMicros = micros - TimeUnit.MICROSECONDS.convert(millis, TimeUnit.MILLISECONDS);

		// truncate to two bytes
		short truncatedMicrosOfMillis = (short) remainingMicros;
		buffer.putShort(truncatedMicrosOfMillis);

		return buffer.array();

	}

	/**
	 * Encodes a given Instant object to CCSDS day segmented time code in
	 * milliseconds.
	 * 
	 * @param instant
	 *            the specified Instant object
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in milliseconds
	 */
	public static byte[] encodeInstantToCCSDSMillis(Instant instant) {
		return encodeLocalDateTimeToCCSDSMillis(LocalDateTime.ofInstant(instant, ZoneOffset.UTC));
	}

	/**
	 * Decodes a given CCSDS day segmented time code in milliseconds to
	 * LocalDateTime.
	 * 
	 * @param CCSDSMillis
	 *            the specified byte array with the length of 8, containing the
	 *            CCSDS day segmented time code in milliseconds
	 * @return the decoded LocalDateTime object
	 */
	public static LocalDateTime decodeCCSDSMillisToLocalDateTime(byte[] CCSDSMillis) {

		if (CCSDSMillis.length != 8) {
			throw new IllegalArgumentException("Specified array does not have required length of 8.");
		}

		ByteBuffer buffer = ByteBuffer.wrap(CCSDSMillis);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long days = buffer.getShort();
		LocalDate localDate = LocalDate.of(1958, 01, 01).plusDays(days);

		// milliseconds of day
		long millis = buffer.getInt();

		if (millis < 0 || millis > 86399999) {
			throw new IllegalArgumentException(
					"Invalid value for milliseconds of day (0 - 86399999): " + Long.toUnsignedString(millis));
		}

		// nanoseconds of days derived from milliseconds
		long nanos = TimeUnit.NANOSECONDS.convert(millis, TimeUnit.MILLISECONDS);

		// microseconds of millisecond
		long micros = buffer.getShort();
		nanos += TimeUnit.NANOSECONDS.convert(micros, TimeUnit.MICROSECONDS);

		return LocalDateTime.of(localDate, LocalTime.ofNanoOfDay(nanos));

	}

	/**
	 * Decodes a given CCSDS day segmented time code in milliseconds to Instant.
	 * 
	 * @param CCSDSMillis
	 *            the specified byte array with the length of 8, containing the
	 *            CCSDS day segmented time code in milliseconds
	 * @return the decoded Instant object
	 */
	public static Instant decodeCCSDSMillisToInstant(byte[] CCSDSMillis) {
		return decodeCCSDSMillisToLocalDateTime(CCSDSMillis).toInstant(ZoneOffset.UTC);
	}

	/**
	 * Encodes a given LocalDateTime object to CCSDS day segmented time code in
	 * picoseconds.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The picoseconds
	 * need to be specified as an integer value, which is encoded in the result.
	 * 
	 * @param localDateTime
	 *            the specified LocalDateTime object which will automatically be
	 *            truncated to milliseconds, since microseconds and nanoseconds are
	 *            specified using the picoseconds parameter
	 * @param picos
	 *            the specified picoseconds of milliseconds, where 0 <= picos <=
	 *            999,999,999
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in picoseconds
	 */
	public static byte[] encodeLocalDateTimeToCCSDSPicos(LocalDateTime localDateTime, int picos) {

		if (picos < 0 || picos > 999999999) {
			throw new IllegalArgumentException(
					"Invalid value for picoseconds of millisecond (0 - 999999999): " + picos);
		}

		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long days = ChronoUnit.DAYS.between(LocalDate.of(1958, 01, 01), localDateTime);

		// truncate to two bytes
		short truncatedDays = (short) days;
		buffer.putShort(truncatedDays);

		// milliseconds of day
		long millis = localDateTime.getLong(ChronoField.MILLI_OF_DAY);

		// truncate to four bytes
		int truncatedMillis = (int) millis;
		buffer.putInt(truncatedMillis);

		// picoseconds of millisecond
		buffer.putInt(picos);

		return buffer.array();

	}

	/**
	 * Encodes a given Instant object to CCSDS day segmented time code in
	 * picoseconds.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The picoseconds
	 * need to be specified as an integer value, which is encoded in the result.
	 * 
	 * @param instant
	 *            the specified Instant object which will automatically be truncated
	 *            to milliseconds, since microseconds and nanoseconds are specified
	 *            using the picoseconds parameter
	 * @param picos
	 *            the specified picoseconds of milliseconds, where 0 <= picos <=
	 *            999,999,999
	 * @return a byte array with the length of 8, containing the CCSDS day segmented
	 *         time code in picoseconds
	 */
	public static byte[] encodeInstantToCCSDSPicos(Instant instant, int picos) {
		return encodeLocalDateTimeToCCSDSPicos(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), picos);
	}

	/**
	 * Decodes a given CCSDS day segmented time code in picoseconds to
	 * LocalDateTime.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The returned
	 * LocalDateTime object will be truncated to nanosecond resolution.
	 * 
	 * @param CCSDSPicos
	 *            the specified byte array with the length of 8, containing the
	 *            CCSDS day segmented time code in picoseconds
	 * @return the decoded LocalDateTime object
	 */
	public static LocalDateTime decodeCCSDSPicosToLocalDateTime(byte[] CCSDSPicos) {

		if (CCSDSPicos.length != 10) {
			throw new IllegalArgumentException("Specified array does not have required length of 10.");
		}

		ByteBuffer buffer = ByteBuffer.wrap(CCSDSPicos);
		buffer.order(ByteOrder.BIG_ENDIAN);

		// days since CCSDS epoch
		long days = buffer.getShort();
		LocalDate localDate = LocalDate.of(1958, 01, 01).plusDays(days);

		// milliseconds of day
		long millis = buffer.getInt();

		if (millis < 0 || millis > 86399999) {
			throw new IllegalArgumentException(
					"Invalid value for milliseconds of day (0 - 86399999): " + Long.toUnsignedString(millis));
		}

		// nanoseconds of days derived from milliseconds
		long nanos = TimeUnit.NANOSECONDS.convert(millis, TimeUnit.MILLISECONDS);

		// picoseconds of millisecond
		long picos = buffer.getInt();

		if (picos < 0 || picos > 999999999) {
			throw new IllegalArgumentException(
					"Invalid value for picoseconds of miliseconds (0 - 999999999): " + picos);
		}

		// convert manually, since there is no support for picosecond conversion through
		// Java API
		nanos += picos / 1000;

		return LocalDateTime.of(localDate, LocalTime.ofNanoOfDay(nanos));

	}

	/**
	 * Decodes a given CCSDS day segmented time code in picoseconds to Instant.
	 * 
	 * Note: LocalDateTime (Java 8) does not support picoseconds. The returned
	 * LocalDateTime object will be truncated to nanosecond resolution.
	 * 
	 * @param CCSDSPicos
	 *            the specified byte array with the length of 8, containing the
	 *            CCSDS day segmented time code in picoseconds
	 * @return the decoded Instant object
	 */
	public static Instant decodeCCSDSPicosToInstant(byte[] CCSDSPicos) {
		return decodeCCSDSPicosToLocalDateTime(CCSDSPicos).toInstant(ZoneOffset.UTC);
	}
}
