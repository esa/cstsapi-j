package esa.egos.csts.api.procedures.informationquery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.information.query.pdus.InformationQueryPdu;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.AbstractProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;

public abstract class AbstractInformationQuery extends AbstractProcedure implements IInformationQuery {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.informationQuery);
	
	private static final int VERSION = 1;

	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	@Override
	public int getVersion() {
		return VERSION;
	}

	@Override
	protected void initOperationTypes() {
		addSupportedOperationType(OperationType.GET);
	}

	@Override
	public void terminate() {
		// nothing to clean up
	}
	
	@Override
	public CstsResult queryInformation(ListOfParameters listOfParameters) {
		IGet get = createGet();
		get.setListOfParameters(listOfParameters);
		return forwardInvocationToProxy(get);
	}
	
	@Override
	public LabelLists getLabelLists() {
		return (LabelLists) getConfigurationParameter(OIDs.pIQnamedLabelLists);
	}

	protected List<IParameter> getParameters() {
		return getServiceInstance().gatherParameters();
	}

	private void processLabelList(LabelList list, IGet get) {

		if (list == null) {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsType.UNDEFINED_DEFAULT);
			diag.setUndefinedDefault("Default list not defined for Service Instance " + getServiceInstance().toString());
			get.setListOfParametersDiagnostics(diag);
			return;
		}

		for (Label label : list.getLabels()) {
			getParameters().stream()
			.filter(p -> p.getLabel().equals(label))
			.forEach(p -> get.getQualifiedParameters().add(p.toQualifiedParameter()));
		}

		get.setPositiveResult();
	}

	private void processFunctionalResourceName(FunctionalResourceName funcResName, IGet get) {

		getParameters().stream()
		.filter(p -> funcResName.equals(p.getName().getFunctionalResourceName()))
		.forEach(p -> get.getQualifiedParameters().add(p.toQualifiedParameter()));
		
		if (!get.getQualifiedParameters().isEmpty()) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
			diag.setUnknownFunctionalResourceName(funcResName);
			get.setListOfParametersDiagnostics(diag);
		}

	}

	private void processFunctionalResourceType(FunctionalResourceType type, IGet get) {
		
		getParameters().stream()
		.filter(p -> type.equals(p.getLabel().getFunctionalResourceType()))
		.forEach(p -> get.getQualifiedParameters().add(p.toQualifiedParameter()));
		
		if (!get.getQualifiedParameters().isEmpty()) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
			diag.setUnknownFunctionalResourceType(get.getListOfParameters().getFunctionalResourceType());
			get.setListOfParametersDiagnostics(diag);
		}
		
	}

	private void processLabelsSet(List<Label> labels, IGet get) {

		ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
				ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);

		int prevParamCount = 0;
		for (Label label : labels) {
			getParameters().stream()
			.filter(p -> p.getLabel().equals(label))
			.forEach(p -> get.getQualifiedParameters().add(p.toQualifiedParameter()));
			
			int paramCount = get.getQualifiedParameters().size();
			if (paramCount == prevParamCount) {
				diag.getUnknownParameterLabels().add(label);
			}
			prevParamCount = paramCount;
		}

		if (diag.getUnknownParameterLabels().isEmpty()) {
			get.setPositiveResult();
		} else {
			get.setListOfParametersDiagnostics(diag);
		}

	}

	private void processNamesSet(List<Name> names, IGet get) {

		ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
				ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);

		int prevParamCount = 0;
		for (Name name : names) {
			getParameters().stream()
			.filter(p -> p.getName().equals(name))
			.forEach(p -> get.getQualifiedParameters().add(p.toQualifiedParameter()));
			
			int paramCount = get.getQualifiedParameters().size();
			if (paramCount == prevParamCount) {
				diag.getUnknownParameterNames().add(name);
			}
			prevParamCount = paramCount;
		}
		
		if (diag.getUnknownParameterNames().isEmpty()) {
			get.setPositiveResult();
		} else {
			get.setListOfParametersDiagnostics(diag);
		}
		
	}

	private void processPid(ProcedureInstanceIdentifier pid, IGet get) {

		getParameters().stream()
		.filter(p -> pid.equals(p.getName().getProcedureInstanceIdentifier()))
		.forEach(p -> get.getQualifiedParameters().add(p.toQualifiedParameter()));
		
		if (!get.getQualifiedParameters().isEmpty()) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
			diag.setUnknownProcedureInstanceIdentifier(pid);
			get.setListOfParametersDiagnostics(diag);
		}
		
	}

	private void processProcedureType(ProcedureType type, IGet get) {
		
		getParameters().stream()
		.filter(p -> type.equals(p.getLabel().getProcedureType()))
		.forEach(p -> get.getQualifiedParameters().add(p.toQualifiedParameter()));
		
		if (!get.getQualifiedParameters().isEmpty()) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_TYPE);
			diag.setUnknownProcedureType(type);
			get.setListOfParametersDiagnostics(diag);
		}
		
	}

	protected void processGetInvocation(IGet get) {
		
		switch (get.getListOfParameters().getType()) {

		case EMPTY:
			processLabelList(getLabelLists().queryDefaultList(), get);
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			processFunctionalResourceName(get.getListOfParameters().getFunctionalResourceName(), get);
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			processFunctionalResourceType(get.getListOfParameters().getFunctionalResourceType(), get);
			break;

		case LABELS_SET:
			processLabelsSet(get.getListOfParameters().getParameterLabels(), get);
			break;

		case LIST_NAME:
			processLabelList(getLabelLists().queryList(get.getListOfParameters().getListName()), get);
			break;

		case NAMES_SET:
			processNamesSet(get.getListOfParameters().getParameterNames(), get);
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			processPid(get.getListOfParameters().getProcedureInstanceIdentifier(), get);
			break;

		case PROCEDURE_TYPE:
			processProcedureType(get.getListOfParameters().getProcedureType(), get);
			break;

		}

	}
	
	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		InformationQueryPdu pdu = new InformationQueryPdu();

		if (operation.getType() == OperationType.GET) {
			IGet get = (IGet) operation;
			if (isInvoke) {
				pdu.setGetInvocation(get.encodeGetInvocation());
			} else {
				pdu.setGetReturn(get.encodeGetReturn());
			}
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		InformationQueryPdu pdu = new InformationQueryPdu();

		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IGet get = createGet();

		if (pdu.getGetInvocation() != null) {
			get.decodeGetInvocation(pdu.getGetInvocation());
		} else if (pdu.getGetReturn() != null) {
			get.decodeGetReturn(pdu.getGetReturn());
		}

		return get;
	}

}
