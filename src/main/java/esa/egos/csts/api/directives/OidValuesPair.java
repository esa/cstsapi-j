package esa.egos.csts.api.directives;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;

public class OidValuesPair {

	public final ObjectIdentifier oid;
	public final List<ParameterValue> values;

	public OidValuesPair(ObjectIdentifier oid) {
		this.oid = oid;
		values = new ArrayList<>();
	}

	public ObjectIdentifier getIdentifier() {
		return oid;
	}

	public List<ParameterValue> getValues() {
		return values;
	}
	
	public b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE encode(b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE sequence) {
		sequence.setParameterIdentifier(new b1.ccsds.csts.common.types.PublishedIdentifier(oid.toArray()));
		b1.ccsds.csts.common.types.TypeAndValueComplexQualified typeAndValueComplexQualified = new b1.ccsds.csts.common.types.TypeAndValueComplexQualified();
		if (values.size() == 1) {
			typeAndValueComplexQualified.setTypeAndValue(values.get(0).encode(new b1.ccsds.csts.common.types.TypeAndValue()));
		} else {
			b1.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence complexSequence = new b1.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence();
			for (ParameterValue p : values) {
				complexSequence.getTypeAndValue().add(p.encode(new b1.ccsds.csts.common.types.TypeAndValue()));
			}
			typeAndValueComplexQualified.setComplexSequence(complexSequence);
		}
		sequence.setParameterValue(typeAndValueComplexQualified);
		// ComplexSet will remain unused, since ComplexSequence provides the same
		// semantics in terms of Java
		return sequence;
	}
	
	public b2.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE encode(b2.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE sequence) {
		sequence.setParameterIdentifier(new b2.ccsds.csts.common.types.PublishedIdentifier(oid.toArray()));
		b2.ccsds.csts.common.types.TypeAndValue typeAndValueComplexQualified = new b2.ccsds.csts.common.types.TypeAndValue();
		if (values.size() == 1) {
			values.get(0).encode(typeAndValueComplexQualified);
		} else {
//			b2.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence complexSequence = new b2.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence();
//			for (ParameterValue p : values) {
//				complexSequence.getTypeAndValue().add(p.encode(new b1.ccsds.csts.common.types.TypeAndValue()));
//			}
//			typeAndValueComplexQualified.setComplexSequence(complexSequence);
		}
		sequence.setParameterValue(typeAndValueComplexQualified);
		// ComplexSet will remain unused, since ComplexSequence provides the same
		// semantics in terms of Java
		return sequence;
	}
	
	public static OidValuesPair decode(b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE sequence) {
		OidValuesPair oidValuesPair = null;
		if (sequence.getParameterValue().getTypeAndValue() != null) {
			oidValuesPair = new OidValuesPair(ObjectIdentifier.of(sequence.getParameterIdentifier().value));
			oidValuesPair.getValues().add(ParameterValue.decode(sequence.getParameterValue().getTypeAndValue()));
		} else if (sequence.getParameterValue().getComplexSequence() != null) {
			oidValuesPair = new OidValuesPair(ObjectIdentifier.of(sequence.getParameterIdentifier().value));
			for (b1.ccsds.csts.common.types.TypeAndValue value : sequence.getParameterValue().getComplexSequence().getTypeAndValue()) {
				oidValuesPair.getValues().add(ParameterValue.decode(value));
			}
		} else if (sequence.getParameterValue().getComplexSet() != null) {
			oidValuesPair = new OidValuesPair(ObjectIdentifier.of(sequence.getParameterIdentifier().value));
			for (b1.ccsds.csts.common.types.TypeAndValue value : sequence.getParameterValue().getComplexSet().getTypeAndValue()) {
				oidValuesPair.getValues().add(ParameterValue.decode(value));
			}
		}
		return oidValuesPair;
	}
	
	public static OidValuesPair decode(b2.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE sequence) {
		OidValuesPair oidValuesPair = null;
		if (sequence.getParameterValue() != null) {
			oidValuesPair = new OidValuesPair(ObjectIdentifier.of(sequence.getParameterIdentifier().value));
			oidValuesPair.getValues().add(ParameterValue.decode(sequence.getParameterValue()));
		} 
		return oidValuesPair;
	}

}
