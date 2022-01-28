package esa.egos.csts.api.procedures.notification;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.rowset.spi.SyncResolver;

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
	
	private Set<IEvent> subscribedEvents;

	// defined by User
	private ListOfParameters listOfEvents;
	
	// defined by Provider
	private ListOfParametersDiagnostics listOfEventsDiagnostics;
	
	private boolean running;
	
	private NotificationCodec notificationCodec;

	protected AbstractNotification() {
		subscribedEvents = new HashSet<>();
		running = false;
		notificationCodec = new NotificationCodec(this);
	}
	
	protected ListOfParametersDiagnostics getListOfEventsDiagnostics() {
		return listOfEventsDiagnostics;
	}
	
	@Override
	public ProcedureType getType() {
		return TYPE;
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
	public LabelLists getLabelLists() {
		return (LabelLists) getConfigurationParameter(OIDs.pNnamedLabelLists);
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
	
	public synchronized String printStartDiagnostic() {
		return listOfEventsDiagnostics.toString();
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
		return notificationCodec.encodeOperation(operation, isInvoke);
	}

	protected EmbeddedData encodeInvocationExtension() {
		return notificationCodec.encodeInvocationExtension();
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
		return notificationCodec.encodeStartDiagnosticExt();
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
		return notificationCodec.decodeOperation(encodedPdu);
	}

	protected void decodeStartInvocationExtension(Extension extension) {
		notificationCodec.decodeStartInvocationExtension(extension);
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
		notificationCodec.decodeStartDiagnosticExt(embeddedData);
	}

}
