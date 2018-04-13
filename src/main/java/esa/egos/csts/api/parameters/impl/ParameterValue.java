package esa.egos.csts.api.parameters.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerBoolean;
import org.openmuc.jasn1.ber.types.BerInteger;
import org.openmuc.jasn1.ber.types.BerObjectIdentifier;
import org.openmuc.jasn1.ber.types.BerOctetString;
import org.openmuc.jasn1.ber.types.BerReal;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.IntPos;
import ccsds.csts.common.types.IntUnsigned;
import ccsds.csts.common.types.PublishedIdentifier;
import ccsds.csts.common.types.TimeCCSDSMilli;
import ccsds.csts.common.types.TimeCCSDSPico;
import ccsds.csts.common.types.TypeAndValue;
import ccsds.csts.common.types.TypeAndValue.CharacterString;
import ccsds.csts.common.types.TypeAndValue.Enumerated;
import ccsds.csts.common.types.TypeAndValue.Float;
import ccsds.csts.common.types.TypeAndValue.Integer;
import ccsds.csts.common.types.TypeAndValue.IntegerPositive;
import ccsds.csts.common.types.TypeAndValue.OctetString;
import esa.egos.csts.api.enums.ParameterType;
import esa.egos.csts.api.enums.TimeEnum;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.types.impl.Duration;
import esa.egos.csts.api.types.impl.Time;

// TODO check extensibility
public class ParameterValue {

	private final ParameterType parameterType;
	private List<Long> integerParameterValues;
	private List<Duration> durationParameterValues;
	private List<String> stringParameterValues;
	private List<Boolean> boolParameterValues;
	private List<byte[]> octetStringParameterValues;
	private List<Double> realParameterValues;
	private List<Time> timeParameterValues;
	private List<ObjectIdentifier> OIDparameterValues;
	
	public ParameterValue(ParameterType parameterType) {
		this.parameterType = parameterType;
		switch (this.parameterType) {
		case BOOLEAN:
			boolParameterValues = new ArrayList<>();
			break;
		case CHARACTER_STRING:
			stringParameterValues = new ArrayList<>();
			break;
		case DURATION:
			durationParameterValues = new ArrayList<>();
			break;
		case ENUMERATED:
			integerParameterValues = new ArrayList<>();
			break;
		case EXTENDED:
			// no initialization
			break;
		case INTEGER:
			integerParameterValues = new ArrayList<>();
			break;
		case OBJECT_IDENTIFIER:
			OIDparameterValues = new ArrayList<>();
			break;
		case OCTET_STRING:
			octetStringParameterValues = new ArrayList<>();
			break;
		case POSITIVE_INTEGER:
			integerParameterValues = new ArrayList<>();
			break;
		case PUBLISHED_IDENTIFIER:
			OIDparameterValues = new ArrayList<>();
			break;
		case REAL:
			realParameterValues = new ArrayList<>();
			break;
		case TIME:
			timeParameterValues = new ArrayList<>();
			break;
		case UNSIGNED_INTEGER:
			integerParameterValues = new ArrayList<>();
			break;
		}
	}
	
	public ParameterType getParameterType() {
		return parameterType;
	}

	public List<Long> getIntegerParameterValues() {
		return integerParameterValues;
	}

	public List<Duration> getDurationParameterValues() {
		return durationParameterValues;
	}

	public List<String> getStringParameterValues() {
		return stringParameterValues;
	}

	public List<Boolean> getBoolParameterValues() {
		return boolParameterValues;
	}
	
	public List<byte[]> getOctetStringParameterValues() {
		return octetStringParameterValues;
	}

	public List<Double> getRealParameterValues() {
		return realParameterValues;
	}

	public List<Time> getTimeParameterValues() {
		return timeParameterValues;
	}

	public List<ObjectIdentifier> getOIDparameterValues() {
		return OIDparameterValues;
	}
	
	public TypeAndValue encode() {
		TypeAndValue typeAndValue = new TypeAndValue();
		switch (parameterType) {
		case BOOLEAN:
			ccsds.csts.common.types.TypeAndValue.Boolean bool = new ccsds.csts.common.types.TypeAndValue.Boolean();
			for (Boolean b : boolParameterValues) {
				bool.getBerBoolean().add(new BerBoolean(b));
			}
			typeAndValue.setBoolean(bool);
			break;
		case CHARACTER_STRING:
			CharacterString characterString = new CharacterString();
			for (String s : stringParameterValues) {
				characterString.getBerVisibleString().add(new BerVisibleString(s.getBytes(StandardCharsets.UTF_16BE)));
			}
			typeAndValue.setCharacterString(characterString);
			break;
		case DURATION:
			ccsds.csts.common.types.TypeAndValue.Duration duration = new ccsds.csts.common.types.TypeAndValue.Duration();
			for (Duration d : durationParameterValues) {
				duration.getDuration().add(d.encode());
			}
			typeAndValue.setDuration(duration);
			break;
		case ENUMERATED:
			Enumerated enumerated = new Enumerated();
			for (Long l : integerParameterValues) {
				enumerated.getBerInteger().add(new BerInteger(l));
			}
			typeAndValue.setEnumerated(enumerated);
			break;
		case EXTENDED:
			typeAndValue.setTypeAndValueExtension(encodeTypeAndValueExtension());
			break;
		case INTEGER:
			Integer integer = new Integer();
			for (Long l : integerParameterValues) {
				integer.getBerInteger().add(new BerInteger(l));
			}
			typeAndValue.setInteger(integer);
			break;
		case OBJECT_IDENTIFIER:
			ccsds.csts.common.types.TypeAndValue.ObjectIdentifier objectIdentifier = new ccsds.csts.common.types.TypeAndValue.ObjectIdentifier();
			for (ObjectIdentifier OID : OIDparameterValues) {
				objectIdentifier.getBerObjectIdentifier().add(new BerObjectIdentifier(OID.getOid()));
			}
			typeAndValue.setObjectIdentifier(objectIdentifier);
			break;
		case OCTET_STRING:
			OctetString octetString = new OctetString();
			for (byte[] octet : octetStringParameterValues) {
				octetString.getBerOctetString().add(new BerOctetString(octet));
			}
			typeAndValue.setOctetString(octetString);
			break;
		case POSITIVE_INTEGER:
			IntegerPositive integerPositive = new IntegerPositive();
			for (Long l : integerParameterValues) {
				integerPositive.getIntPos().add(new IntPos(l));
			}
			typeAndValue.setIntegerPositive(integerPositive);
			break;
		case PUBLISHED_IDENTIFIER:
			ccsds.csts.common.types.TypeAndValue.PublishedIdentifier publishedIdentifier = new ccsds.csts.common.types.TypeAndValue.PublishedIdentifier();
			for (ObjectIdentifier OID : OIDparameterValues) {
				publishedIdentifier.getPublishedIdentifier().add(new PublishedIdentifier(OID.getOid()));
			}
			typeAndValue.setPublishedIdentifier(publishedIdentifier);
			break;
		case REAL:
			Float real = new Float();
			for (Double d : realParameterValues) {
				real.getBerReal().add(new BerReal(d));
			}
			typeAndValue.setFloat(real);
			break;
		case TIME:
			ccsds.csts.common.types.TypeAndValue.Time time = new ccsds.csts.common.types.TypeAndValue.Time();
			for (Time t : timeParameterValues) {
				ccsds.csts.common.types.Time newTime = new ccsds.csts.common.types.Time();
				switch (t.getEnumeration()) {
				case MILLISECONDS:
					newTime.setCcsdsFormatMilliseconds(new TimeCCSDSMilli(t.getMilliseconds()));
					break;
				case PICOSECONDS:
					newTime.setCcsdsFormatPicoseconds(new TimeCCSDSPico(t.getPicoseconds()));
					break;
				}
				time.getTime().add(newTime);
			}
			typeAndValue.setTime(time);
			break;
		case UNSIGNED_INTEGER:
			ccsds.csts.common.types.TypeAndValue.IntUnsigned intUnsigned = new ccsds.csts.common.types.TypeAndValue.IntUnsigned();
			for (Long l : integerParameterValues) {
				intUnsigned.getIntUnsigned().add(new IntUnsigned(l));
			}
			typeAndValue.setIntUnsigned(intUnsigned);
			break;
		default:
			break;
		}
		return typeAndValue;
	}
	
	protected Embedded encodeTypeAndValueExtension() {
		Embedded embedded = new Embedded();
		// to override if used
		return embedded;
	}
	
	public static ParameterValue decode(TypeAndValue typeAndValue) {
		ParameterValue parameterValue = null;
		
		if (typeAndValue.getBoolean() != null) {
			parameterValue = new ParameterValue(ParameterType.BOOLEAN);
			for (BerBoolean bool : typeAndValue.getBoolean().getBerBoolean()) {
				parameterValue.getBoolParameterValues().add(bool.value);
			}
		} else if (typeAndValue.getCharacterString() != null) {
			parameterValue = new ParameterValue(ParameterType.CHARACTER_STRING);
			for (BerVisibleString string : typeAndValue.getCharacterString().getBerVisibleString()) {
				parameterValue.getStringParameterValues().add(new String(string.value, StandardCharsets.UTF_16BE));
			}
		} else if (typeAndValue.getDuration() != null) {
			parameterValue = new ParameterValue(ParameterType.DURATION);
			for (ccsds.csts.common.types.Duration d : typeAndValue.getDuration().getDuration()) {
				parameterValue.getDurationParameterValues().add(Duration.decode(d));
			}
		} else if (typeAndValue.getEnumerated() != null) {
			parameterValue = new ParameterValue(ParameterType.ENUMERATED);
			for (BerInteger i : typeAndValue.getEnumerated().getBerInteger()) {
				parameterValue.getIntegerParameterValues().add(i.longValue());
			}
		} else if (typeAndValue.getTypeAndValueExtension() != null) {
			parameterValue = new ParameterValue(ParameterType.EXTENDED);
			decodeTypeAndValueExtension(typeAndValue.getTypeAndValueExtension());
		} else if (typeAndValue.getInteger() != null) {
			parameterValue = new ParameterValue(ParameterType.INTEGER);
			for (BerInteger i : typeAndValue.getInteger().getBerInteger() ) {
				parameterValue.getIntegerParameterValues().add(i.longValue());
			}
		} else if (typeAndValue.getObjectIdentifier() != null) {
			parameterValue = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
			for (BerObjectIdentifier oid : typeAndValue.getObjectIdentifier().getBerObjectIdentifier()) {
				parameterValue.getOIDparameterValues().add(new ObjectIdentifier(oid.value));
			}
		} else if (typeAndValue.getOctetString() != null) {
			parameterValue = new ParameterValue(ParameterType.OCTET_STRING);
			for (BerOctetString octets : typeAndValue.getOctetString().getBerOctetString() ) {
				parameterValue.getOctetStringParameterValues().add(octets.value);
			}
		} else if (typeAndValue.getIntegerPositive() != null) {
			parameterValue = new ParameterValue(ParameterType.POSITIVE_INTEGER);
			for (IntPos i : typeAndValue.getIntegerPositive().getIntPos()) {
				parameterValue.getIntegerParameterValues().add(i.longValue());
			}
		} else if (typeAndValue.getPublishedIdentifier() != null) {
			parameterValue = new ParameterValue(ParameterType.PUBLISHED_IDENTIFIER);
			for (PublishedIdentifier pid : typeAndValue.getPublishedIdentifier().getPublishedIdentifier()) {
				parameterValue.getOIDparameterValues().add(new ObjectIdentifier(pid.value));
			}
		} else if (typeAndValue.getFloat() != null) {
			parameterValue = new ParameterValue(ParameterType.REAL);
			for (BerReal real : typeAndValue.getFloat().getBerReal() ) {
				parameterValue.getRealParameterValues().add(real.value);
			}
		} else if (typeAndValue.getTime() != null) {
			parameterValue = new ParameterValue(ParameterType.TIME);
			for (ccsds.csts.common.types.Time t : typeAndValue.getTime().getTime()) {
				Time time = null;
				if (t.getCcsdsFormatMilliseconds() != null) {
					time = new Time(TimeEnum.MILLISECONDS);
					time.setMilliseconds(t.getCcsdsFormatMilliseconds().value);
				} else if (t.getCcsdsFormatPicoseconds() != null) {
					time = new Time(TimeEnum.PICOSECONDS);
					time.setPicoseconds(t.getCcsdsFormatPicoseconds().value);
				}
				parameterValue.getTimeParameterValues().add(time);
			}
		} else if (typeAndValue.getIntUnsigned() != null) {
			parameterValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
			for (IntUnsigned i : typeAndValue.getIntUnsigned().getIntUnsigned()) {
				parameterValue.getIntegerParameterValues().add(i.longValue());
			}
		}
		
		return parameterValue;
	}
	
	protected static void decodeTypeAndValueExtension(Embedded typeAndValueExtension) {
		// to override if used
	}
	
}
