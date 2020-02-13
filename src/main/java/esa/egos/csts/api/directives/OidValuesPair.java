package esa.egos.csts.api.directives;

import java.util.ArrayList;
import java.util.List;

import b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE;
import b1.ccsds.csts.common.types.PublishedIdentifier;
import b1.ccsds.csts.common.types.TypeAndValue;
import b1.ccsds.csts.common.types.TypeAndValueComplexQualified;
import b1.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence;
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
	
	public SEQUENCE encode() {
		SEQUENCE sequence = new SEQUENCE();
		sequence.setParameterIdentifier(new PublishedIdentifier(oid.toArray()));
		TypeAndValueComplexQualified typeAndValueComplexQualified = new TypeAndValueComplexQualified();
		if (values.size() == 1) {
			typeAndValueComplexQualified.setTypeAndValue(values.get(0).encode());
		} else {
			ComplexSequence complexSequence = new ComplexSequence();
			for (ParameterValue p : values) {
				complexSequence.getTypeAndValue().add(p.encode());
			}
			typeAndValueComplexQualified.setComplexSequence(complexSequence);
		}
		sequence.setParameterValue(typeAndValueComplexQualified);
		// ComplexSet will remain unused, since ComplexSequence provides the same
		// semantics in terms of Java
		return sequence;
	}
	
	public static OidValuesPair decode(SEQUENCE sequence) {
		OidValuesPair oidValuesPair = null;
		if (sequence.getParameterValue().getTypeAndValue() != null) {
			oidValuesPair = new OidValuesPair(ObjectIdentifier.of(sequence.getParameterIdentifier().value));
			oidValuesPair.getValues().add(ParameterValue.decode(sequence.getParameterValue().getTypeAndValue()));
		} else if (sequence.getParameterValue().getComplexSequence() != null) {
			oidValuesPair = new OidValuesPair(ObjectIdentifier.of(sequence.getParameterIdentifier().value));
			for (TypeAndValue value : sequence.getParameterValue().getComplexSequence().getTypeAndValue()) {
				oidValuesPair.getValues().add(ParameterValue.decode(value));
			}
		} else if (sequence.getParameterValue().getComplexSet() != null) {
			oidValuesPair = new OidValuesPair(ObjectIdentifier.of(sequence.getParameterIdentifier().value));
			for (TypeAndValue value : sequence.getParameterValue().getComplexSet().getTypeAndValue()) {
				oidValuesPair.getValues().add(ParameterValue.decode(value));
			}
		}
		return oidValuesPair;
	}

}
