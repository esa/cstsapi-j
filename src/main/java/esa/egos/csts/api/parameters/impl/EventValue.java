package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.QualifiedValues;
import ccsds.csts.common.types.SequenceOfQualifiedValues;
import esa.egos.csts.api.enums.EventValueEnum;
import esa.egos.csts.api.parameters.IQualifiedValue;

public class EventValue {

	private final EventValueEnum enumeration;
	private List<IQualifiedValue> qualifiedValues;

	public EventValue(EventValueEnum enumeration) {
		this.enumeration = enumeration;
		if (this.enumeration == EventValueEnum.QUALIFIED_VALUES) {
			qualifiedValues = new ArrayList<>();
		}
	}
	
	public EventValueEnum getEnumeration() {
		return enumeration;
	}

	public List<IQualifiedValue> getQualifiedValues() {
		return qualifiedValues;
	}

	public ccsds.csts.common.types.EventValue encode() {
		ccsds.csts.common.types.EventValue eventValue = new ccsds.csts.common.types.EventValue();
		switch (enumeration) {
		case QUALIFIED_VALUES:
			SequenceOfQualifiedValues qualifiedValues = new SequenceOfQualifiedValues();
			for (IQualifiedValue qualifiedValue : this.qualifiedValues) {
				qualifiedValues.getQualifiedValues().add(qualifiedValue.encode());
			}
			eventValue.setQualifiedValues(qualifiedValues);
			break;
		case EMPTY:
			eventValue.setEmpty(new BerNull());
			break;
		case EXTENDED:
			eventValue.setEventValueExtension(encodeEventValueExtension());
			break;
		}
		return eventValue;
	}
	
	protected Embedded encodeEventValueExtension() {
		Embedded embedded = new Embedded();
		// to override if used
		return embedded;
	}

	public static EventValue decode(ccsds.csts.common.types.EventValue value) {
		
		EventValue eventValue = null;
		
		if (value.getEmpty() != null) {
			eventValue = new EventValue(EventValueEnum.EMPTY);
		}
		
		if (value.getQualifiedValues() != null) {
			if (value.getQualifiedValues().getQualifiedValues() != null) {
				eventValue = new EventValue(EventValueEnum.QUALIFIED_VALUES);
				for (QualifiedValues qualifiedValues : value.getQualifiedValues().getQualifiedValues()) {
					eventValue.getQualifiedValues().add(QualifiedValue.decode(qualifiedValues));
				}
			}
		}
		
		if (value.getEventValueExtension() != null) {
			eventValue = new EventValue(EventValueEnum.EXTENDED);
			decodeEventValueExtension(value.getEventValueExtension());
		}
		
		return eventValue;
	}

	private static void decodeEventValueExtension(Embedded eventValueExtension) {
		// to override if used
	}

}
