package esa.egos.csts.api.procedures.cyclicreport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnosticsType;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.procedures.unbuffereddatadelivery.AbstractUnbufferedDataDelivery;
import esa.egos.csts.api.states.State;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public abstract class AbstractCyclicReport extends AbstractUnbufferedDataDelivery implements ICyclicReportInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.cyclicReport);

	private ScheduledExecutorService executorService;

	// defined by User
	private long deliveryCycle;
	private ListOfParameters listOfParameters;
	
	// defined by Provider
	protected List<QualifiedParameter> qualifiedParameters;
	private CyclicReportStartDiagnostics startDiagnostic;
	private long sequenceCounter;

	private boolean running;
	private ScheduledFuture<?> cyclicReport;
	
	private CyclicReportCodec cyclicReportCodec;

	protected AbstractCyclicReport() {
		executorService = Executors.newSingleThreadScheduledExecutor();
		qualifiedParameters = new ArrayList<>();
		sequenceCounter = 0;
		running = false;
		cyclicReportCodec = new CyclicReportCodec(this);
	}

	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	@Override
	public long getDeliveryCycle() {
		return deliveryCycle;
	}

	@Override
	public void setDeliveryCycle(long deliveryCycle) {
		this.deliveryCycle = deliveryCycle;
	}

	@Override
	public ListOfParameters getListOfParameters() {
		return listOfParameters;
	}

	@Override
	public void setListOfParameters(ListOfParameters listOfParameters) {
		this.listOfParameters = listOfParameters;
	}

	@Override
	public List<QualifiedParameter> getQualifiedParameters() {
		return qualifiedParameters;
	}

	@Override
	public CyclicReportStartDiagnostics getStartDiagnostic() {
		return startDiagnostic;
	}

	@Override
	public void setStartDiagnostics(CyclicReportStartDiagnostics startDiagnostic) {
		this.startDiagnostic = startDiagnostic;
	}

	@Override
	public void terminate() {
		stopCyclicReport();
		executorService.shutdownNow();
		executorService = Executors.newSingleThreadScheduledExecutor();
		sequenceCounter = 0;
		initializeState();
	}
	
	@Override
	public CstsResult requestCyclicReport(long deliveryCycle, ListOfParameters listOfParameters) {
		this.deliveryCycle = deliveryCycle;
		this.listOfParameters = listOfParameters;
		IStart start = createStart();
		start.setInvocationExtension(encodeInvocationExtension());
		return forwardInvocationToProxy(start);
	}
	
	@Override
	public CstsResult endCyclicReport() {
		IStop stop = createStop();
		return forwardInvocationToProxy(stop);
	}
	
	@Override
	public synchronized boolean checkListOfParameters() {
		return !running && processListOfParameters();
	}
	
	@Override
	public synchronized void startCyclicReport() {
		if (!running) {
			cyclicReport = executorService.scheduleAtFixedRate(this::createAndTransferData, 0, deliveryCycle, TimeUnit.MILLISECONDS);
			running = true;
		}
	}

	@Override
	public synchronized void stopCyclicReport() {
		if (running) {
			cyclicReport.cancel(true);
			running = false;
		}
	}

	@Override
	public IntegerConfigurationParameter getMinimumAllowedDeliveryCycle() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pCRminimumAllowedDeliveryCycle);
	}

	@Override
	public LabelLists getLabelLists() {
		return (LabelLists) getConfigurationParameter(OIDs.pCRnamedLabelLists);
	}

	private synchronized long getCurrentSequenceCounter() {
		long tmp = sequenceCounter++;
		if (sequenceCounter < 0) {
			sequenceCounter = 0;
		}
		return tmp;
	}
	
	public synchronized String printStartDiagnostic() {
		return startDiagnostic.toString();
	}

	/**
	 * Processes the List of Parameters extension in the START invocation, returns
	 * the result and writes the List of Parameters Diagnostics if the result is
	 * negative.
	 * 
	 * @return true if the List of Parameters is valid, false otherwise
	 */
	protected synchronized boolean processListOfParameters() {

		LabelLists labelLists = (LabelLists) getConfigurationParameter(OIDs.pCRnamedLabelLists);
		List<IParameter> parameters = getServiceInstance().gatherParameters();
		ListOfParametersDiagnostics diag;
		boolean valid;
		switch (listOfParameters.getType()) {

		case EMPTY:

			LabelList defaultList = labelLists.queryDefaultList();

			if (defaultList != null) {
				return true;
			}
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNDEFINED_DEFAULT);
			diag.setUndefinedDefault("Default list not defined for Service Instance " + getServiceInstance().toString());
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		case FUNCTIONAL_RESOURCE_NAME:

			valid = parameters.stream()
			.filter(p -> listOfParameters.getFunctionalResourceName().equals(p.getName().getFunctionalResourceName()))
			.count() > 0;

			if (valid) {
				return true;
			}

			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
			diag.setUnknownFunctionalResourceName(listOfParameters.getFunctionalResourceName());
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		case FUNCTIONAL_RESOURCE_TYPE:

			valid = parameters.stream()
			.filter(p -> listOfParameters.getFunctionalResourceType().equals(p.getLabel().getFunctionalResourceType()))
			.count() > 0;

			if (valid) {
				return true;
			}

			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
			diag.setUnknownFunctionalResourceType(listOfParameters.getFunctionalResourceType());
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		case LABELS_SET:
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);
			for (Label label : listOfParameters.getParameterLabels()) {
				valid = parameters.stream()
						.filter(p -> p.getLabel().equals(label))
						.count() > 0;
				if (!valid) {
					diag.getUnknownParameterLabels().add(label);
				}
			}
			if (diag.getUnknownParameterLabels().isEmpty()) {
				return true;
			}
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		case LIST_NAME:

			LabelList namedList = labelLists.queryList(listOfParameters.getListName());

			if (namedList != null) {
				return true;
			}

			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_LIST_NAME);
			diag.setUnknownListName("List " + listOfParameters.getListName() + " not defined for Service Instance "
					+ getServiceInstance());
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		case NAMES_SET:
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);
			for (Name name : listOfParameters.getParameterNames()) {
				valid = parameters.stream()
						.filter(p -> p.getName().equals(name))
						.count() > 0;
				if (!valid) {
					diag.getUnknownParameterNames().add(name);
				}
			}
			if (diag.getUnknownParameterNames().isEmpty()) {
				return true;
			}
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:

			valid = parameters.stream()
			.filter(p -> listOfParameters.getProcedureInstanceIdentifier().equals(p.getName().getProcedureInstanceIdentifier()))
			.count() > 0;

			if (valid) {
				return true;
			}

			diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
			diag.setUnknownProcedureInstanceIdentifier(listOfParameters.getProcedureInstanceIdentifier());
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		case PROCEDURE_TYPE:

			valid = parameters.stream()
					.filter(p -> listOfParameters.getProcedureType().equals(p.getLabel().getProcedureType()))
					.count() > 0;

			if (valid) {
				return true;
			}

			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_TYPE);
			diag.setUnknownProcedureType(listOfParameters.getProcedureType());
			startDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			startDiagnostic.setListOfParametersDiagnostics(diag);
			break;

		}

		return false;
	}

	protected synchronized void generateQualifiedParameters() {

	    this.qualifiedParameters.clear();
		LabelLists labelLists = (LabelLists) getConfigurationParameter(OIDs.pCRnamedLabelLists);
		List<IParameter> parameters = getServiceInstance().gatherParameters();
		switch (listOfParameters.getType()) {

		case EMPTY:

			LabelList defaultList = labelLists.queryDefaultList();

			if (defaultList == null) {
				return;
			}

			for (Label label : defaultList.getLabels()) {
				parameters.stream()
				.filter(p -> p.getLabel().equals(label))
				.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			}
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			parameters.stream()
			.filter(p -> listOfParameters.getFunctionalResourceName().equals(p.getName().getFunctionalResourceName()))
			.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			parameters.stream()
			.filter(p -> listOfParameters.getFunctionalResourceType().equals(p.getLabel().getFunctionalResourceType()))
			.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			break;

		case LABELS_SET:
			for (Label label : listOfParameters.getParameterLabels()) {
				parameters.stream()
				.filter(p -> p.getLabel().equals(label))
				.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			}
			break;

		case LIST_NAME:

			LabelList namedList = labelLists.queryList(listOfParameters.getListName());

			if (namedList == null) {
				return;
			}

			for (Label label : namedList.getLabels()) {
				parameters.stream()
				.filter(p -> p.getLabel().equals(label))
				.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			}
			break;

		case NAMES_SET:
			for (Name name : listOfParameters.getParameterNames()) {
				parameters.stream()
				.filter(p -> p.getName().equals(name))
				.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			}
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			parameters.stream()
			.filter(p -> listOfParameters.getProcedureInstanceIdentifier().equals(p.getName().getProcedureInstanceIdentifier()))
			.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			break;

		case PROCEDURE_TYPE:
			parameters.stream()
			.filter(p -> listOfParameters.getProcedureType().equals(p.getLabel().getProcedureType()))
			.forEach(p -> qualifiedParameters.add(p.toQualifiedParameter()));
			break;
		}

	}

	protected void createAndTransferData() {
	    // do not lock this procedure
	    // first off get and lock this procedure state because it can be locked by other operation (e.g. STOP)
	    State<?> state = getState();
	    synchronized (state)
	    {
	        // lock this procedure
	        synchronized (this)
	        {
	            generateQualifiedParameters();
	            ITransferData data = createTransferData();
	            data.setGenerationTime(Time.now());
	            data.setSequenceCounter(getCurrentSequenceCounter());
	            data.setEmbeddedData(encodeDataRefinement());
	            CstsResult res = state.process(data);
	            if(res != CstsResult.SUCCESS)
	            {
	            	LOGGER.severe("State processing of " + data.print(100) + " failed fpor state " + state);
	            }
	            //getQualifiedParameters().clear(); // https://sdejira.esa.int/browse/CSTSAPI-12
	        }
	    }
	}

	@Override
	public CstsResult initiateOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			start.setInvocationExtension(encodeInvocationExtension());
		}
		return doInitiateOperationInvoke(operation);
	}
	
	
	protected EmbeddedData encodeInvocationExtension() {
		return cyclicReportCodec.encodeInvocationExtension();
	}
	/**
	 * This method should be overridden to encode the extension of a derived
	 * procedure.
	 * 
	 * @return the Extension object representing the extension of the derived
	 *         procedure
	 */
	protected Extension encodeStartInvocationExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeStartDiagnosticExt() {
		return cyclicReportCodec.encodeStartDiagnosticExt();
	}
	
	protected EmbeddedData encodeDataRefinement() {
		return cyclicReportCodec.encodeDataRefinement();
	}
	
	protected Extension encodeInvocDataRefExtension() {
		return Extension.notUsed();
	}

	@Override
	public CstsResult informOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			decodeStartInvocationExtension(start.getInvocationExtension());
		} else if (operation.getType() == OperationType.TRANSFER_DATA) {
			ITransferData transferData = (ITransferData) operation;
			decodeTransferDataRefinement(transferData.getEmbeddedData());
		}
		return doInformOperationInvoke(operation);
	}
	
	@Override
	public CstsResult informOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.START) {
			IStart start = (IStart) confOperation;
			if (start.getStartDiagnostic() != null) {
				decodeStartDiagnosticExt(start.getStartDiagnostic().getDiagnosticExtension());
				start.getStartDiagnostic().setMessage(getStartDiagnostic().toString());
			}
		}
		return doInformOperationReturn(confOperation);
	}
	
	protected void decodeStartInvocationExtension(Extension extension) {
		cyclicReportCodec.decodeStartInvocationExtension(extension);
	}

	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension
	 *            the Extended object representing the extension of the derived
	 *            procedure
	 */
	protected void decodeStartInvocationExtExtension(Extension extension) {
		// do nothing on default
	}

	protected void decodeTransferDataRefinement(EmbeddedData embeddedData) {
		cyclicReportCodec.decodeTransferDataRefinement(embeddedData);
	}

	protected void decodeInvocDataRefExtension(Extension extension) {
		
	}

	protected void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
		cyclicReportCodec.decodeStartDiagnosticExt(embeddedData);
	}

}
