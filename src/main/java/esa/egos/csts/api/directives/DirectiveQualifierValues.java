package esa.egos.csts.api.directives;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues;
import ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE;
import ccsds.csts.common.types.TypeAndValue;
import ccsds.csts.common.types.TypeAndValueComplexQualified;
import ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence;
import esa.egos.csts.api.parameters.impl.ParameterValue;

public class DirectiveQualifierValues {

	private final DirectiveQualifierValuesType type;

	private final List<ParameterValue> values;

	private final List<OidValuesPair> idValuesPairs;
	
	public DirectiveQualifierValues(DirectiveQualifierValuesType type) {
		this.type = type;
		if (type == DirectiveQualifierValuesType.PARAMETERLESS_VALUES) {
			values = new ArrayList<>();
			idValuesPairs = null;
		} else if (type == DirectiveQualifierValuesType.SEQUENCE_OF_PARAMETER_IDS_AND_VALUES) {
			values = null;
			idValuesPairs = new ArrayList<>();
		} else {
			// no qualifier values chosen
			// modify in case of extension
			values = null;
			idValuesPairs = null;
		}
		
	}

	public DirectiveQualifierValuesType getType() {
		return type;
	}

	public List<ParameterValue> getValues() {
		return values;
	}

	public List<OidValuesPair> getIdValuesPairs() {
		return idValuesPairs;
	}
	
	public ccsds.csts.common.operations.pdus.DirectiveQualifierValues encode() {
		ccsds.csts.common.operations.pdus.DirectiveQualifierValues directiveQualifierValues =
				new ccsds.csts.common.operations.pdus.DirectiveQualifierValues();
		TypeAndValueComplexQualified typeAndValueComplexQualified;
		switch (type) {
		case NO_QUALIFIER_VALUES:
			directiveQualifierValues.setNoQualifierValues(new BerNull());
			break;
		case PARAMETERLESS_VALUES:
			typeAndValueComplexQualified = new TypeAndValueComplexQualified();
			if (values.size() == 1) {
				typeAndValueComplexQualified.setTypeAndValue(values.get(0).encode());
			} else {
				ComplexSequence complexSequence = new ComplexSequence();
				for (ParameterValue value : values) {
					complexSequence.getTypeAndValue().add(value.encode());
				}
				typeAndValueComplexQualified.setComplexSequence(complexSequence);
			}
			directiveQualifierValues.setParameterlessValues(typeAndValueComplexQualified);
			break;
		case SEQUENCE_OF_PARAMETER_IDS_AND_VALUES:
			SequenceOfParameterIdsAndValues sequence = new SequenceOfParameterIdsAndValues();
			for (OidValuesPair pair : idValuesPairs) {
				sequence.getSEQUENCE().add(pair.encode());
			}
			directiveQualifierValues.setSequenceOfParamIdsAndValues(sequence);
			break;
		}
		
		return directiveQualifierValues;
	}
	
	public static DirectiveQualifierValues decode(ccsds.csts.common.operations.pdus.DirectiveQualifierValues values) {
		DirectiveQualifierValues directiveQualifierValues = null;
		if (values.getNoQualifierValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.NO_QUALIFIER_VALUES);
		} else if (values.getParameterlessValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.PARAMETERLESS_VALUES);
			if (values.getParameterlessValues().getTypeAndValue() != null) {
				directiveQualifierValues.getValues().add(ParameterValue.decode(values.getParameterlessValues().getTypeAndValue()));
			} else if (values.getParameterlessValues().getComplexSequence() != null) {
				for (TypeAndValue value : values.getParameterlessValues().getComplexSequence().getTypeAndValue()) {
					directiveQualifierValues.getValues().add(ParameterValue.decode(value));
				}
			} else if (values.getParameterlessValues().getComplexSet() != null) {
				for (TypeAndValue value : values.getParameterlessValues().getComplexSet().getTypeAndValue()) {
					directiveQualifierValues.getValues().add(ParameterValue.decode(value));
				}
			}
		} else if (values.getSequenceOfParamIdsAndValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.SEQUENCE_OF_PARAMETER_IDS_AND_VALUES);
			for (SEQUENCE value : values.getSequenceOfParamIdsAndValues().getSEQUENCE()) {
				directiveQualifierValues.getIdValuesPairs().add(OidValuesPair.decode(value));
			}
		}
		return directiveQualifierValues;
	}

}
