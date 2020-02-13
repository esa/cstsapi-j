package esa.egos.csts.api.directives;

import b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation.DirectiveQualifier.FunctResourceDirQualifier;
import b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation.DirectiveQualifier.ServiceProcDirQualifier;
import b1.ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;

public class DirectiveQualifier {

	private final DirectiveQualifierType type;

	private final ProcedureInstanceIdentifier procedureInstanceIdentifier;

	// this should probably be a FunctionalResourceName
	// probably a mistake in the CCSDS ASN.1 file
	private final int functionalResourceInstanceNumber;

	private DirectiveQualifierValues values;

	private EmbeddedData extension;
	
	public DirectiveQualifier(DirectiveQualifierValuesType valuesType) {
		this.type = DirectiveQualifierType.LOCAL_PROCEDURE_DIRECTIVE_QUALIFIER;
		procedureInstanceIdentifier = null;
		functionalResourceInstanceNumber = -1;
		values = new DirectiveQualifierValues(valuesType);
		extension = null;
	}

	public DirectiveQualifier(ProcedureInstanceIdentifier procedureInstanceIdentifier,
			DirectiveQualifierValuesType valuesType) {
		this.type = DirectiveQualifierType.SERVICE_PROCEDURE_DIRECTIVE_QUALIFIER;
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
		functionalResourceInstanceNumber = -1;
		values = new DirectiveQualifierValues(valuesType);
		extension = null;
	}

	public DirectiveQualifier(int functionalResourceInstanceNumber, DirectiveQualifierValuesType valuesType) {
		this.type = DirectiveQualifierType.FUNCTIONAL_RESOURCE_DIRECTIVE_QUALIFIER;
		this.procedureInstanceIdentifier = null;
		this.functionalResourceInstanceNumber = functionalResourceInstanceNumber;
		values = new DirectiveQualifierValues(valuesType);
		extension = null;
	}

	public DirectiveQualifier(EmbeddedData extension) {
		this.type = DirectiveQualifierType.EXTENDED;
		procedureInstanceIdentifier = null;
		functionalResourceInstanceNumber = -1;
		values = null;
		this.extension = extension;
	}
	
	public DirectiveQualifierType getType() {
		return type;
	}

	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

	public int getFunctionalResourceInstanceNumber() {
		return functionalResourceInstanceNumber;
	}

	public DirectiveQualifierValues getValues() {
		return values;
	}

	public void setValues(DirectiveQualifierValues values) {
		this.values = values;
	}
	
	public EmbeddedData getExtension() {
		return extension;
	}
	
	public void setExtension(EmbeddedData extension) {
		this.extension = extension;
	}
	
	public b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation.DirectiveQualifier encode() {
		b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation.DirectiveQualifier directiveQualifier =
				new b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation.DirectiveQualifier();
		switch (type) {
		case LOCAL_PROCEDURE_DIRECTIVE_QUALIFIER:
			directiveQualifier.setLocalProcDirQualifier(values.encode());
			break;
		case SERVICE_PROCEDURE_DIRECTIVE_QUALIFIER:
			ServiceProcDirQualifier serviceProcDirQualifier = new ServiceProcDirQualifier();
			serviceProcDirQualifier.setProcedureInstanceId(procedureInstanceIdentifier.encode());
			serviceProcDirQualifier.setServiceProcDirQualifierValues(values.encode());
			directiveQualifier.setServiceProcDirQualifier(serviceProcDirQualifier);
			break;
		case FUNCTIONAL_RESOURCE_DIRECTIVE_QUALIFIER:
			FunctResourceDirQualifier functResourceDirQualifier = new FunctResourceDirQualifier();
			functResourceDirQualifier.setFunctResourceInstanceNumber(new FunctionalResourceInstanceNumber(functionalResourceInstanceNumber));
			functResourceDirQualifier.setFunctionalResourceQualifiers(values.encode());
			directiveQualifier.setFunctResourceDirQualifier(functResourceDirQualifier);
			break;
		case EXTENDED:
			directiveQualifier.setDirectiveQualifierExtension(extension.encode());
			break;
		}
		return directiveQualifier;
	}

	public static DirectiveQualifier decode(b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation.DirectiveQualifier qualifier) {
		DirectiveQualifier directiveQualifier = null;
		if (qualifier.getLocalProcDirQualifier() != null) {
			if (qualifier.getLocalProcDirQualifier().getNoQualifierValues() != null) {
				directiveQualifier = new DirectiveQualifier(DirectiveQualifierValuesType.NO_QUALIFIER_VALUES);
			} else if (qualifier.getLocalProcDirQualifier().getParameterlessValues() != null) {
				directiveQualifier = new DirectiveQualifier(DirectiveQualifierValuesType.PARAMETERLESS_VALUES);
			} else if (qualifier.getLocalProcDirQualifier().getSequenceOfParamIdsAndValues()!= null) {
				directiveQualifier = new DirectiveQualifier(DirectiveQualifierValuesType.SEQUENCE_OF_PARAMETER_IDS_AND_VALUES);
			}
			directiveQualifier.setValues(DirectiveQualifierValues.decode(qualifier.getLocalProcDirQualifier()));
		} else if (qualifier.getServiceProcDirQualifier() != null) {
			ProcedureInstanceIdentifier pid = ProcedureInstanceIdentifier.decode(qualifier.getServiceProcDirQualifier().getProcedureInstanceId());
			if (qualifier.getServiceProcDirQualifier().getServiceProcDirQualifierValues().getNoQualifierValues() != null) {
				directiveQualifier = new DirectiveQualifier(pid, DirectiveQualifierValuesType.NO_QUALIFIER_VALUES);
			} else if (qualifier.getServiceProcDirQualifier().getServiceProcDirQualifierValues().getParameterlessValues() != null) {
				directiveQualifier = new DirectiveQualifier(pid, DirectiveQualifierValuesType.PARAMETERLESS_VALUES);
			} else if (qualifier.getServiceProcDirQualifier().getServiceProcDirQualifierValues().getSequenceOfParamIdsAndValues()!= null) {
				directiveQualifier = new DirectiveQualifier(pid, DirectiveQualifierValuesType.SEQUENCE_OF_PARAMETER_IDS_AND_VALUES);
			}
			directiveQualifier.setValues(DirectiveQualifierValues.decode(qualifier.getServiceProcDirQualifier().getServiceProcDirQualifierValues()));
		} else if (qualifier.getFunctResourceDirQualifier() != null) {
			int instanceNumber = qualifier.getFunctResourceDirQualifier().getFunctResourceInstanceNumber().intValue();
			if (qualifier.getFunctResourceDirQualifier().getFunctionalResourceQualifiers().getNoQualifierValues() != null) {
				directiveQualifier = new DirectiveQualifier(instanceNumber, DirectiveQualifierValuesType.NO_QUALIFIER_VALUES);
			} else if (qualifier.getFunctResourceDirQualifier().getFunctionalResourceQualifiers().getParameterlessValues() != null) {
				directiveQualifier = new DirectiveQualifier(instanceNumber, DirectiveQualifierValuesType.PARAMETERLESS_VALUES);
			} else if (qualifier.getFunctResourceDirQualifier().getFunctionalResourceQualifiers().getSequenceOfParamIdsAndValues()!= null) {
				directiveQualifier = new DirectiveQualifier(instanceNumber, DirectiveQualifierValuesType.SEQUENCE_OF_PARAMETER_IDS_AND_VALUES);
			}
			directiveQualifier.setValues(DirectiveQualifierValues.decode(qualifier.getFunctResourceDirQualifier().getFunctionalResourceQualifiers()));
		} else if (qualifier.getDirectiveQualifierExtension() != null) {
			directiveQualifier = new DirectiveQualifier(EmbeddedData.decode(qualifier.getDirectiveQualifierExtension()));
		}
		
		return directiveQualifier;
	}
	
}
