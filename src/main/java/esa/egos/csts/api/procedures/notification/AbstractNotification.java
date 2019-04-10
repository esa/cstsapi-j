package esa.egos.csts.api.procedures.notification;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.notification.pdus.NotificationPdu;
import ccsds.csts.notification.pdus.NotificationStartDiagnosticExt;
import ccsds.csts.notification.pdus.NotificationStartInvocExt;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.AbstractStatefulProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;

public abstract class AbstractNotification extends AbstractStatefulProcedure implements INotificationInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.notification);
	
	private static final int VERSION = 1;
	
	private Set<IEvent> subscribedEvents;

	// defined by User
	private ListOfParameters listOfEvents;
	
	// defined by Provider
	private ListOfParametersDiagnostics listOfEventsDiagnostics;
	
	private boolean running;

	protected AbstractNotification() {
		subscribedEvents = new HashSet<>();
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
	protected void initOperationTypes() {
		addSupportedOperationType(OperationType.START);
		addSupportedOperationType(OperationType.STOP);
		addSupportedOperationType(OperationType.NOTIFY);
	}

	@Override
	public void terminate() {
		stopNotification();
		initializeState();
	}
	
	@Override
	public ListOfParameters getListOfEvents() {
		return listOfEvents;
	}

	@Override
	public void setListOfEvents(ListOfParameters listOfEvents) {
		this.listOfEvents = listOfEvents;
	}

	@Override
	public ListOfParametersDiagnostics getListOfParametersDiagnostics() {
		return listOfEventsDiagnostics;
	}

	@Override
	public void setListOfEventsDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics) {
		this.listOfEventsDiagnostics = listOfParametersDiagnostics;
	}

	@Override
	public CstsResult requestNotification(ListOfParameters listOfEvents) {
		this.listOfEvents = listOfEvents;
		IStart start = createStart();
		start.setInvocationExtension(encodeInvocationExtension());
		return forwardInvocationToProxy(start);
	}
	
	@Override
	public CstsResult endNotification() {
		IStop stop = createStop();
		return forwardInvocationToProxy(stop);
	}
	
	@Override
	public synchronized boolean checkListOfEvents() {
		return processListOfEvents();
	}
	
	@Override
	public synchronized void startNotification() {
		running = true;
	}
	
	@Override
	public synchronized void stopNotification() {
		subscribedEvents.forEach(e -> e.deleteObserver(this));
		subscribedEvents.clear();
		running = false;
	}
	
	protected void observeEvent(IEvent event) {
		event.addObserver(this);
		subscribedEvents.add(event);
	}
	
	/**
	 * Processes the List of Events extension in the START invocation.
	 * 
	 * @return true if the List of Events is valid, false otherwise
	 */
	protected synchronized boolean processListOfEvents() {

		LabelLists labelLists = (LabelLists) getConfigurationParameter(OIDs.pNnamedLabelLists);
		List<IEvent> events = getServiceInstance().gatherEvents();
		ListOfParametersDiagnostics diag;
		
		switch (listOfEvents.getType()) {

		case EMPTY:

			LabelList defaultList = labelLists.queryDefaultList();

			if (defaultList == null) {
				diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNDEFINED_DEFAULT);
				diag.setUndefinedDefault(
						"Default list not defined for Service Instance " + getServiceInstance().toString());
				setListOfEventsDiagnostics(diag);
				return false;
			}

			for (Label label : defaultList.getLabels()) {
				events.stream()
				.filter(e -> e.getLabel().equals(label))
				.forEach(this::observeEvent);
			}
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			
			events.stream()
			.filter(e -> listOfEvents.getFunctionalResourceName().equals(e.getName().getFunctionalResourceName()))
			.forEach(this::observeEvent);
			
			if (subscribedEvents.isEmpty()) {
				diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
				diag.setUnknownFunctionalResourceName(listOfEvents.getFunctionalResourceName());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			
			events.stream()
			.filter(e -> listOfEvents.getFunctionalResourceType().equals(e.getLabel().getFunctionalResourceType()))
			.forEach(this::observeEvent);
			
			if (subscribedEvents.isEmpty()) {
				diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
				diag.setUnknownFunctionalResourceType(listOfEvents.getFunctionalResourceType());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case LABELS_SET:
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);
			for (Label label : listOfEvents.getParameterLabels()) {
				
				int subscribedEventCount = subscribedEvents.size();
				events.stream()
				.filter(e -> e.getLabel().equals(label))
				.forEach(this::observeEvent);
				
				// check if new events were added
				if (subscribedEvents.size() == subscribedEventCount) {
					diag.getUnknownParameterLabels().add(label);
				}
			}
			if (!diag.getUnknownParameterLabels().isEmpty()) {
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case LIST_NAME:

			LabelList namedList = labelLists.queryList(listOfEvents.getListName());

			if (namedList == null) {
				diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_LIST_NAME);
				diag.setUnknownListName("List " + listOfEvents.getListName() + " not defined for Service Instance "
						+ getServiceInstance());
				setListOfEventsDiagnostics(diag);
				return false;
			}

			for (Label label : namedList.getLabels()) {
				events.stream()
				.filter(e -> e.getLabel().equals(label))
				.forEach(this::observeEvent);
			}
			break;

		case NAMES_SET:
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);
			
			for (Name name : listOfEvents.getParameterNames()) {
				int subscribedEventCount = subscribedEvents.size();
				
				events.stream()
				.filter(e -> e.getName().equals(name))
				.forEach(this::observeEvent);
				
				// check if new events were added
				if (subscribedEvents.size() == subscribedEventCount) {
					diag.getUnknownParameterNames().add(name);
				}
			}
			if (!diag.getUnknownParameterNames().isEmpty()) {
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			events.stream()
			.filter(e -> listOfEvents.getProcedureInstanceIdentifier().equals(e.getName().getProcedureInstanceIdentifier()))
			.forEach(this::observeEvent);
			if (subscribedEvents.isEmpty()) {
				diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
				diag.setUnknownProcedureInstanceIdentifier(listOfEvents.getProcedureInstanceIdentifier());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case PROCEDURE_TYPE:
			events.stream()
			.filter(e -> listOfEvents.getProcedureType().equals(e.getLabel().getProcedureType()))
			.forEach(this::observeEvent);
			if (subscribedEvents.isEmpty()) {
				diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_TYPE);
				diag.setUnknownProcedureType(listOfEvents.getProcedureType());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;
		}

		return true;
	}
	
	@Override
	protected void processIncomingEvent(IEvent event) {
		if (running) {
			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			getState().process(notify);
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
	
	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		NotificationPdu pdu = new NotificationPdu();

		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation(start.encodeStartInvocation());
			} else {
				pdu.setStartReturn(start.encodeStartReturn());
			}
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			if (isInvoke) {
				pdu.setStopInvocation(stop.encodeStopInvocation());
			} else {
				pdu.setStopReturn(stop.encodeStopReturn());
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (isInvoke) {
				pdu.setNotifyInvocation(notify.encodeNotifyInvocation());
			}
		}

		try (BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	private EmbeddedData encodeInvocationExtension() {

		NotificationStartInvocExt invocationExtension = new NotificationStartInvocExt();
		invocationExtension.setListOfEvents(listOfEvents.encode());
		invocationExtension.setNotificationStartInvocExtExtension(encodeStartInvocationExtExtension().encode());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.nStartInvocExt, invocationExtension.code);
	}

	/**
	 * This method should be overridden to encode the extension of a derived
	 * procedure.
	 * 
	 * @return the Extended object representing the extension of the derived
	 *         procedure
	 */
	protected Extension encodeStartInvocationExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeStartDiagnosticExt() {

		NotificationStartDiagnosticExt diagnosticExtension = new NotificationStartDiagnosticExt();
		if (listOfEventsDiagnostics != null) {
			diagnosticExtension.setCommon(listOfEventsDiagnostics.encode());
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			diagnosticExtension.encode(os);
			diagnosticExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.nStartDiagExt, diagnosticExtension.code);

	}

	@Override
	public CstsResult informOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			decodeStartInvocationExtension(start.getInvocationExtension());
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
	
	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		NotificationPdu pdu = new NotificationPdu();
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		}

		return operation;
	}

	protected void decodeStartInvocationExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.nStartInvocExt)) {
			NotificationStartInvocExt invocationExtension = new NotificationStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			listOfEvents = ListOfParameters.decode(invocationExtension.getListOfEvents());
			decodeStartInvocationExtExtension(Extension.decode(invocationExtension.getNotificationStartInvocExtExtension()));
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

	protected void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.nStartDiagExt)) {
			NotificationStartDiagnosticExt diagnosticExtension = new NotificationStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				diagnosticExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (diagnosticExtension.getCommon() != null) {
				listOfEventsDiagnostics = ListOfParametersDiagnostics.decode(diagnosticExtension.getCommon());
			}
		}
	}

}
