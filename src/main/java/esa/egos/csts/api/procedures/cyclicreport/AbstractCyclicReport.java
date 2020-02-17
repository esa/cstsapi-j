package esa.egos.csts.api.procedures.cyclicreport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.common.types.IntPos;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartInvocExt;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef.QualifiedParameters;
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
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public abstract class AbstractCyclicReport extends AbstractUnbufferedDataDelivery implements ICyclicReportInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.cyclicReport);
	
	private static final int VERSION = 1;

	private ScheduledExecutorService executorService;

	// defined by User
	private long deliveryCycle;
	private ListOfParameters listOfParameters;
	
	// defined by Provider
	private List<QualifiedParameter> qualifiedParameters;
	private CyclicReportStartDiagnostics startDiagnostic;
	private long sequenceCounter;

	private boolean running;
	private ScheduledFuture<?> cyclicReport;

	protected AbstractCyclicReport() {
		executorService = Executors.newSingleThreadScheduledExecutor();
		qualifiedParameters = new ArrayList<>();
		sequenceCounter = 0;
		running = false;
	}

	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	@Override
	public int getVersion() {
		return VERSION;
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

	protected synchronized void createAndTransferData() {
		generateQualifiedParameters();
		ITransferData data = createTransferData();
		data.setGenerationTime(Time.now());
		data.setSequenceCounter(getCurrentSequenceCounter());
		data.setEmbeddedData(encodeDataRefinement());
		getState().process(data);
		//getQualifiedParameters().clear(); // https://sdejira.esa.int/browse/CSTSAPI-12
	}

	@Override
	public CstsResult initiateOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			start.setInvocationExtension(encodeInvocationExtension());
		}
		return doInitiateOperationInvoke(operation);
	}
	
	private EmbeddedData encodeInvocationExtension() {

		CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
		invocationExtension.setDeliveryCycle(new IntPos(deliveryCycle));
		invocationExtension.setListOfParameters(listOfParameters.encode());
		invocationExtension.setCyclicReportStartInvocExtExtension(encodeStartInvocationExtExtension().encode());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.crStartInvocExt, invocationExtension.code);
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
		return EmbeddedData.of(OIDs.crStartDiagExt, startDiagnostic.encode().code);
	}
	
	private EmbeddedData encodeDataRefinement() {

		CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
		QualifiedParameters qualifiedParameters = new QualifiedParameters();
		for (QualifiedParameter param : this.qualifiedParameters) {
			qualifiedParameters.getQualifiedParameter().add(param.encode());
		}
		dataRefinement.setQualifiedParameters(qualifiedParameters);
		dataRefinement.setCyclicReportTransferDataInvocDataRefExtension(encodeInvocDataRefExtension().encode());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			dataRefinement.encode(os);
			dataRefinement.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Throwable t) {
			t.printStackTrace();
		}

		return EmbeddedData.of(OIDs.crTransferDataInvocDataRef, dataRefinement.code);
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
			}
		}
		return doInformOperationReturn(confOperation);
	}
	
	private void decodeStartInvocationExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.crStartInvocExt)) {
			CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			deliveryCycle = invocationExtension.getDeliveryCycle().longValue();
			listOfParameters = ListOfParameters.decode(invocationExtension.getListOfParameters());
			decodeStartInvocationExtExtension(Extension.decode(invocationExtension.getCyclicReportStartInvocExtExtension()));
		}
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
		if (embeddedData.getOid().equals(OIDs.crTransferDataInvocDataRef)) {
			CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				dataRefinement.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			qualifiedParameters.clear();
			for (b1.ccsds.csts.common.types.QualifiedParameter param : dataRefinement.getQualifiedParameters().getQualifiedParameter()) {
				qualifiedParameters.add(QualifiedParameter.decode(param));
			}
			decodeInvocDataRefExtension(Extension.decode(dataRefinement.getCyclicReportTransferDataInvocDataRefExtension()));
		}
	}

	protected void decodeInvocDataRefExtension(Extension extension) {
		
	}

	protected void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.crStartDiagExt)) {
			CyclicReportStartDiagnosticExt cyclicReportStartDiagnosticExt = new CyclicReportStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				cyclicReportStartDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			startDiagnostic = CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt);
		}
	}

}
