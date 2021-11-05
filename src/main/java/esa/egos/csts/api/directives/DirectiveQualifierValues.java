package esa.egos.csts.api.directives;

import java.util.ArrayList;
import java.util.List;

import com.beanit.jasn1.ber.types.BerNull;

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
	
	public b1.ccsds.csts.common.operations.pdus.DirectiveQualifierValues encode(b1.ccsds.csts.common.operations.pdus.DirectiveQualifierValues directiveQualifierValues) {

		 b1.ccsds.csts.common.types.TypeAndValueComplexQualified typeAndValueComplexQualified;
		switch (type) {
		case NO_QUALIFIER_VALUES:
			directiveQualifierValues.setNoQualifierValues(new BerNull());
			break;
		case PARAMETERLESS_VALUES:
			typeAndValueComplexQualified = new  b1.ccsds.csts.common.types.TypeAndValueComplexQualified();
			if (values.size() == 1) {
				typeAndValueComplexQualified.setTypeAndValue(values.get(0).encode(new b1.ccsds.csts.common.types.TypeAndValue()));
			} else {
				 b1.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence complexSequence = 
						 new b1.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence();
				for (ParameterValue value : values) {
					complexSequence.getTypeAndValue().add(value.encode(new b1.ccsds.csts.common.types.TypeAndValue()));
				}
				typeAndValueComplexQualified.setComplexSequence(complexSequence);
			}
			directiveQualifierValues.setParameterlessValues(typeAndValueComplexQualified);
			break;
		case SEQUENCE_OF_PARAMETER_IDS_AND_VALUES:
			b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues sequence = new b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues();
			for (OidValuesPair pair : idValuesPairs) {
				sequence.getSEQUENCE().add(pair.encode(new b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE()));
			}
			directiveQualifierValues.setSequenceOfParamIdsAndValues(sequence);
			break;
		}
		
		return directiveQualifierValues;
	}
	
	public b2.ccsds.csts.common.operations.pdus.DirectiveQualifierValues encode(b2.ccsds.csts.common.operations.pdus.DirectiveQualifierValues directiveQualifierValues) {

		switch (type) {
		case NO_QUALIFIER_VALUES:
			directiveQualifierValues.setNoQualifierValues(new BerNull());
			break;
		case PARAMETERLESS_VALUES:
			 b2.ccsds.csts.common.types.TypeAndValue typeAndValueComplexQualified = new  b2.ccsds.csts.common.types.TypeAndValue();
			if (values.size() == 1) {
				//typeAndValueComplexQualified.setTypeAndValue(values.get(0).encode(new b1.ccsds.csts.common.types.TypeAndValue()));
				values.get(0).encode(typeAndValueComplexQualified);
			} 
//			else {
//				ComplexSequence complexSequence = new ComplexSequence();
//				for (ParameterValue value : values) {
//					complexSequence.getTypeAndValue().add(value.encode(new b1.ccsds.csts.common.types.TypeAndValue()));
//				}
//				typeAndValueComplexQualified.setComplexSequence(complexSequence);
//			}
			directiveQualifierValues.setParameterlessValues(typeAndValueComplexQualified);
			break;
		case SEQUENCE_OF_PARAMETER_IDS_AND_VALUES:
			b2.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues sequence = new b2.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues();
			for (OidValuesPair pair : idValuesPairs) {
				sequence.getSEQUENCE().add(pair.encode(new b2.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE()));
			}
			directiveQualifierValues.setSequenceOfParamIdsAndValues(sequence);
			break;
		}
		
		return directiveQualifierValues;
	}
	
	public static DirectiveQualifierValues decode(b1.ccsds.csts.common.operations.pdus.DirectiveQualifierValues values) {
		DirectiveQualifierValues directiveQualifierValues = null;
		if (values.getNoQualifierValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.NO_QUALIFIER_VALUES);
		} else if (values.getParameterlessValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.PARAMETERLESS_VALUES);
			if (values.getParameterlessValues().getTypeAndValue() != null) {
				directiveQualifierValues.getValues().add(ParameterValue.decode(values.getParameterlessValues().getTypeAndValue()));
			} else if (values.getParameterlessValues().getComplexSequence() != null) {
				for ( b1.ccsds.csts.common.types.TypeAndValue value : values.getParameterlessValues().getComplexSequence().getTypeAndValue()) {
					directiveQualifierValues.getValues().add(ParameterValue.decode(value));
				}
			} else if (values.getParameterlessValues().getComplexSet() != null) {
				for ( b1.ccsds.csts.common.types.TypeAndValue value : values.getParameterlessValues().getComplexSet().getTypeAndValue()) {
					directiveQualifierValues.getValues().add(ParameterValue.decode(value));
				}
			}
		} else if (values.getSequenceOfParamIdsAndValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.SEQUENCE_OF_PARAMETER_IDS_AND_VALUES);
			for (b1.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE value : values.getSequenceOfParamIdsAndValues().getSEQUENCE()) {
				directiveQualifierValues.getIdValuesPairs().add(OidValuesPair.decode(value));
			}
		}
		return directiveQualifierValues;
	}
	
	public static DirectiveQualifierValues decode(b2.ccsds.csts.common.operations.pdus.DirectiveQualifierValues values) {
		DirectiveQualifierValues directiveQualifierValues = null;
		if (values.getNoQualifierValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.NO_QUALIFIER_VALUES);
		} else if (values.getParameterlessValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.PARAMETERLESS_VALUES);
			if (values.getParameterlessValues() != null) {
				directiveQualifierValues.getValues().add(ParameterValue.decode(values.getParameterlessValues()));
			} 
		} else if (values.getSequenceOfParamIdsAndValues() != null) {
			directiveQualifierValues = new DirectiveQualifierValues(DirectiveQualifierValuesType.SEQUENCE_OF_PARAMETER_IDS_AND_VALUES);
			for (b2.ccsds.csts.common.operations.pdus.SequenceOfParameterIdsAndValues.SEQUENCE value : values.getSequenceOfParamIdsAndValues().getSEQUENCE()) {
				directiveQualifierValues.getIdValuesPairs().add(OidValuesPair.decode(value));
			}
		}
		return directiveQualifierValues;
	}

}
