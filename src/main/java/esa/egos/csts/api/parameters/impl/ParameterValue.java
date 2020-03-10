package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.beanit.jasn1.ber.types.BerBoolean;
import com.beanit.jasn1.ber.types.BerInteger;
import com.beanit.jasn1.ber.types.BerObjectIdentifier;
import com.beanit.jasn1.ber.types.BerOctetString;
import com.beanit.jasn1.ber.types.BerReal;
import com.beanit.jasn1.ber.types.string.BerVisibleString;

import b1.ccsds.csts.common.types.IntPos;
import b1.ccsds.csts.common.types.IntUnsigned;
import b1.ccsds.csts.common.types.PublishedIdentifier;
import b1.ccsds.csts.common.types.TypeAndValue;
import b1.ccsds.csts.common.types.TypeAndValue.CharacterString;
import b1.ccsds.csts.common.types.TypeAndValue.Enumerated;
import b1.ccsds.csts.common.types.TypeAndValue.Float;
import b1.ccsds.csts.common.types.TypeAndValue.Integer;
import b1.ccsds.csts.common.types.TypeAndValue.IntegerPositive;
import b1.ccsds.csts.common.types.TypeAndValue.OctetString;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.Duration;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents the CCSDS TypeAndValue type.
 */
public class ParameterValue {

	private final ParameterType type;
	private List<Long> integerParameterValues;
	private List<Duration> durationParameterValues;
	private List<String> stringParameterValues;
	private List<Boolean> boolParameterValues;
	private List<byte[]> octetStringParameterValues;
	private List<Double> realParameterValues;
	private List<Time> timeParameterValues;
	private List<ObjectIdentifier> OIDparameterValues;
	private final EmbeddedData extension;

	/**
	 * Instantiates a new Parameter Value with its specified type.
	 * 
	 * @param type
	 *            the type of the Parameter Value
	 */
	public ParameterValue(ParameterType type) {
		this.type = type;
		switch (this.type) {
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
		extension = null;
	}

	/**
	 * Instantiates a new extended Parameter Value.
	 * 
	 * @param extension
	 *            the Parameter Value extension
	 */
	public ParameterValue(EmbeddedData extension) {
		type = ParameterType.EXTENDED;
		this.extension = extension;
	}

	/**
	 * Returns the type of this Parameter Value.
	 * 
	 * @return the type of this Parameter Value
	 */
	public ParameterType getType() {
		return type;
	}

	/**
	 * Returns the list of Integer Parameter Values.
	 * 
	 * @return the list of Integer Parameter Values
	 */
	public List<Long> getIntegerParameterValues() {
		return integerParameterValues;
	}

	/**
	 * Returns the list of Duration Parameter Values.
	 * 
	 * @return the list of Duration Parameter Values
	 */
	public List<Duration> getDurationParameterValues() {
		return durationParameterValues;
	}

	/**
	 * Returns the list of String Parameter Values.
	 * 
	 * @return the list of String Parameter Values
	 */
	public List<String> getStringParameterValues() {
		return stringParameterValues;
	}

	/**
	 * Returns the list of Boolean Parameter Values.
	 * 
	 * @return the list of Boolean Parameter Values
	 */
	public List<Boolean> getBoolParameterValues() {
		return boolParameterValues;
	}

	/**
	 * Returns the list of Octet String Parameter Values.
	 * 
	 * @return the list of Octet String Parameter Values
	 */
	public List<byte[]> getOctetStringParameterValues() {
		return octetStringParameterValues;
	}

	/**
	 * Returns the list of Real Parameter Values.
	 * 
	 * @return the list of Real Parameter Values
	 */
	public List<Double> getRealParameterValues() {
		return realParameterValues;
	}

	/**
	 * Returns the list of Time Parameter Values.
	 * 
	 * @return the list of Time Parameter Values
	 */
	public List<Time> getTimeParameterValues() {
		return timeParameterValues;
	}

	/**
	 * Returns the list of Object Identifier Parameter Values.
	 * 
	 * @return the list of Object Identifier Parameter Values
	 */
	public List<ObjectIdentifier> getOIDparameterValues() {
		return OIDparameterValues;
	}

	/**
	 * Encodes these Parameter Value into a CCSDS TypeAndValue type.
	 * 
	 * @return the CCSDS TypeAndValue type representing this object
	 */
	public TypeAndValue encode() {
		TypeAndValue typeAndValue = new TypeAndValue();
		switch (type) {
		case BOOLEAN:
			b1.ccsds.csts.common.types.TypeAndValue.Boolean bool = new b1.ccsds.csts.common.types.TypeAndValue.Boolean();
			for (Boolean b : boolParameterValues) {
				bool.getBerBoolean().add(new BerBoolean(b));
			}
			typeAndValue.setBoolean(bool);
			break;
		case CHARACTER_STRING:
			CharacterString characterString = new CharacterString();
			for (String s : stringParameterValues) {
				characterString.getBerVisibleString().add(new BerVisibleString(CSTSUtils.encodeString(s)));
			}
			typeAndValue.setCharacterString(characterString);
			break;
		case DURATION:
			b1.ccsds.csts.common.types.TypeAndValue.Duration duration = new b1.ccsds.csts.common.types.TypeAndValue.Duration();
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
			typeAndValue.setTypeAndValueExtension(extension.encode());
			break;
		case INTEGER:
			Integer integer = new Integer();
			for (Long l : integerParameterValues) {
				integer.getBerInteger().add(new BerInteger(l));
			}
			typeAndValue.setInteger(integer);
			break;
		case OBJECT_IDENTIFIER:
			b1.ccsds.csts.common.types.TypeAndValue.ObjectIdentifier objectIdentifier = new b1.ccsds.csts.common.types.TypeAndValue.ObjectIdentifier();
			for (ObjectIdentifier OID : OIDparameterValues) {
				objectIdentifier.getBerObjectIdentifier().add(new BerObjectIdentifier(OID.toArray()));
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
			b1.ccsds.csts.common.types.TypeAndValue.PublishedIdentifier publishedIdentifier = new b1.ccsds.csts.common.types.TypeAndValue.PublishedIdentifier();
			for (ObjectIdentifier OID : OIDparameterValues) {
				publishedIdentifier.getPublishedIdentifier().add(new PublishedIdentifier(OID.toArray()));
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
			b1.ccsds.csts.common.types.TypeAndValue.Time time = new b1.ccsds.csts.common.types.TypeAndValue.Time();
			for (Time t : timeParameterValues) {
				time.getTime().add(t.encode());
			}
			typeAndValue.setTime(time);
			break;
		case UNSIGNED_INTEGER:
			b1.ccsds.csts.common.types.TypeAndValue.IntUnsigned intUnsigned = new b1.ccsds.csts.common.types.TypeAndValue.IntUnsigned();
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

	/**
	 * Decodes a specified CCSDS TypeAndValue type.
	 * 
	 * @param typeAndValue
	 *            the specified CCSDS TypeAndValue type
	 * @return a new Parameter Value decoded from the specified CCSDS TypeAndValue
	 *         type
	 */
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
				parameterValue.getStringParameterValues().add(CSTSUtils.decodeString(string.value));
			}
		} else if (typeAndValue.getDuration() != null) {
			parameterValue = new ParameterValue(ParameterType.DURATION);
			for (b1.ccsds.csts.common.types.Duration d : typeAndValue.getDuration().getDuration()) {
				parameterValue.getDurationParameterValues().add(Duration.decode(d));
			}
		} else if (typeAndValue.getEnumerated() != null) {
			parameterValue = new ParameterValue(ParameterType.ENUMERATED);
			for (BerInteger i : typeAndValue.getEnumerated().getBerInteger()) {
				parameterValue.getIntegerParameterValues().add(i.longValue());
			}
		} else if (typeAndValue.getTypeAndValueExtension() != null) {
			parameterValue = new ParameterValue(EmbeddedData.decode(typeAndValue.getTypeAndValueExtension()));
		} else if (typeAndValue.getInteger() != null) {
			parameterValue = new ParameterValue(ParameterType.INTEGER);
			for (BerInteger i : typeAndValue.getInteger().getBerInteger()) {
				parameterValue.getIntegerParameterValues().add(i.longValue());
			}
		} else if (typeAndValue.getObjectIdentifier() != null) {
			parameterValue = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
			for (BerObjectIdentifier oid : typeAndValue.getObjectIdentifier().getBerObjectIdentifier()) {
				parameterValue.getOIDparameterValues().add(ObjectIdentifier.of(oid.value));
			}
		} else if (typeAndValue.getOctetString() != null) {
			parameterValue = new ParameterValue(ParameterType.OCTET_STRING);
			for (BerOctetString octets : typeAndValue.getOctetString().getBerOctetString()) {
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
				parameterValue.getOIDparameterValues().add(ObjectIdentifier.of(pid.value));
			}
		} else if (typeAndValue.getFloat() != null) {
			parameterValue = new ParameterValue(ParameterType.REAL);
			for (BerReal real : typeAndValue.getFloat().getBerReal()) {
				parameterValue.getRealParameterValues().add(real.value);
			}
		} else if (typeAndValue.getTime() != null) {
			parameterValue = new ParameterValue(ParameterType.TIME);
			for (b1.ccsds.csts.common.types.Time t : typeAndValue.getTime().getTime()) {
				parameterValue.getTimeParameterValues().add(Time.decode(t));
			}
		} else if (typeAndValue.getIntUnsigned() != null) {
			parameterValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
			for (IntUnsigned i : typeAndValue.getIntUnsigned().getIntUnsigned()) {
				parameterValue.getIntegerParameterValues().add(i.longValue());
			}
		}

		return parameterValue;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder("ParameterValue [type=");
	    sb.append(this.type.name());
	    if (this.type == ParameterType.EXTENDED) {
	        sb.append(", extension=");
	        sb.append(this.extension.toString());
	    } else if (this.type == ParameterType.OCTET_STRING) {
            sb.append(", values=[");
            Iterator<byte[]> it = this.octetStringParameterValues.iterator();
            while (it.hasNext()) {
                sb.append(Arrays.toString(it.next()));
                if (it.hasNext()) sb.append(", ");
            }
	    } else {
            sb.append(", values=[");
            Iterator<?> it = null;
            switch (this.type) {
            case BOOLEAN:
                it = this.boolParameterValues.iterator();
                break;
            case CHARACTER_STRING:
                it = this.stringParameterValues.iterator();
                break;
            case DURATION:
                it = this.durationParameterValues.iterator();
                break;
            case ENUMERATED:
            case INTEGER:
            case POSITIVE_INTEGER:
            case UNSIGNED_INTEGER:
                it = this.integerParameterValues.iterator();
                break;
            case OBJECT_IDENTIFIER:
            case PUBLISHED_IDENTIFIER:
                it = this.OIDparameterValues.iterator();
                break;
            case REAL:
                it = this.realParameterValues.iterator();
                break;
            case TIME:
                it = this.timeParameterValues.iterator();
                break;
            default:
                break; // should not get here
            }
            while (it.hasNext()) {
                sb.append(it.next().toString());
                if (it.hasNext()) sb.append(", ");
            }
		}
	    sb.append("]]");
	    return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ParameterValue)) {
			return false;
		}
		ParameterValue parameterValue = (ParameterValue)o;
		if (!parameterValue.getType().equals(this.type)) {
			return false;
		}
		switch (this.type) {
		case BOOLEAN:
			return boolParameterValues.equals(parameterValue.getBoolParameterValues());
		case CHARACTER_STRING:
			return stringParameterValues.equals(parameterValue.getStringParameterValues());
		case DURATION:
			return durationParameterValues.equals(parameterValue.getDurationParameterValues());
		case ENUMERATED:
		case INTEGER:
		case POSITIVE_INTEGER:
		case UNSIGNED_INTEGER:
			return integerParameterValues.equals(parameterValue.getIntegerParameterValues());
		case EXTENDED:
			return (hashCode() == parameterValue.hashCode());
		case OBJECT_IDENTIFIER:
		case PUBLISHED_IDENTIFIER:
			return OIDparameterValues.equals(parameterValue.getOIDparameterValues());
		case OCTET_STRING:
			if (octetStringParameterValues.size() != parameterValue.getOctetStringParameterValues().size()) {
				return false;
			}
			if (octetStringParameterValues.size() == 0) {
				return true;
			}
			Iterator<byte[]> i1 = octetStringParameterValues.iterator();
			Iterator<byte[]> i2 = parameterValue.getOctetStringParameterValues().iterator();
			while (i1.hasNext()) {
				if (!Arrays.equals(i1.next(), i2.next())) {
					return false;
				}
			}
			return true;
		case REAL:
			return realParameterValues.equals(parameterValue.getRealParameterValues());
		case TIME:
			return timeParameterValues.equals(parameterValue.getTimeParameterValues());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31*hash + this.type.ordinal();
		switch (this.type) {
		case BOOLEAN:
			for (Boolean b : this.boolParameterValues) {
				hash = 31*hash + ((Boolean.TRUE.equals(b)) ? 1 : 0);
			}
			break;
		case CHARACTER_STRING:
			for (String s : this.stringParameterValues) {
				hash = 31*hash + ((s != null) ? s.hashCode() : 0);
			}
			break;
		case DURATION:
			for (Duration d : this.durationParameterValues) {
				hash = 31*hash + ((d != null) ? d.hashCode() : 0);
			}
			break;
		case ENUMERATED:
		case INTEGER:
		case POSITIVE_INTEGER:
		case UNSIGNED_INTEGER:
			for (Long l : this.integerParameterValues) {
				hash = 31*hash + ((l != null) ? l.hashCode() : 0);
			}
			break;
		case EXTENDED:
		    if (this.extension != null) {
		        hash = 31*hash + this.extension.hashCode();
		    }
			break;
		case OBJECT_IDENTIFIER:
		case PUBLISHED_IDENTIFIER:
			for (ObjectIdentifier oi : this.OIDparameterValues) {
				hash = 31*hash + ((oi != null) ? oi.hashCode() : 0);
			}
			break;
		case OCTET_STRING:
			for (byte[] ba : this.octetStringParameterValues) {
				if (ba != null) {
					for (int i = 0; i < ba.length; i++) {
						hash = 31*hash + ba[i];
					}
				}
			}
			break;
		case REAL:
			for (Double d : this.realParameterValues) {
				hash = 31*hash + ((d != null) ? d.hashCode() : 0);
			}
			break;
		case TIME:
			for (Time t : this.timeParameterValues) {
				hash = 31*hash + ((t != null) ? t.hashCode() : 0);
			}
			break;
		}
		return hash;
	}

}
