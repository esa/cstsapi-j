package esa.egos.csts.sim.impl.usr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.diagnostics.StartDiagnosticType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.EventValueType;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.ResourceIdentifier;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.impl.FunctionalResourceEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceMetadata;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsEmptyValue;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.procedures.informationquery.InformationQueryUser;
import esa.egos.csts.api.procedures.notification.NotificationUser;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportUser;
import esa.egos.csts.sim.impl.MdCstsSi;
import esa.egos.csts.sim.impl.MdCstsSiConfig;

/**
 * MD-CSTS User service inform
 */
public abstract class MdCstsSiUserInform extends
                                         MdCstsSi<MdCstsSiConfig, InformationQueryUser, OnChangeCyclicReportUser, NotificationUser>
{
    /** the size of buffer for printing an operation parameters */
    protected static final int PRINT_BUFF_SIZE = 1024;

    /**
     * the maximum number of the last received data updates in the
     * cyclicParameters and queriedParameters lists
     */
    protected static final int KEEP_MAX_LAST_DATA_UPDATES = 10;

    /** lock protecting this service instance */
    protected final Lock retLock;

    /** condition variable for signaling an operation return or abort */
    protected final Condition retCond;

    /** the last invoked operation result */
    protected OperationResult operationResult;

    /** the diagnostic of the last failed operation */
    protected AtomicReference<String> dignostic;
    
    protected final AtomicReference<CyclicReportStartDiagnostics> diagnosticObject = new AtomicReference<CyclicReportStartDiagnostics>();

    /**
     * parameters received on the GET operation return from all query inform
     * procedures
     */
    private List<List<QualifiedParameter>> queriedParameters;

    /**
     * parameters received on the TRANSFER-DATA operation invocation from all
     * cyclic report procedures
     */
    private List<List<QualifiedParameter>> cyclicParameters;

    /**
     * events received on the NOTIFY operation invocation from a cyclic report
     * procedure
     */
    private List<Name> notifiedEvents;

    /** FR parameters received from procedures */
    private Map<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>>> parameters;

    /** FR parameters received from procedures */
    private Map<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceEvent<?, FunctionalResourceValue<?>>>> events;

    /** Number of the received parameter updates by a procedure */
    protected Map<ProcedureInstanceIdentifier, Integer> parameterUpdateCounters;

    /** Number of the received event updates by a procedure */
    protected Map<ProcedureInstanceIdentifier, Integer> eventUpdateCounters;


    /**
     * Constructs an MD CSTS User SI
     * 
     * @param api The CSTS user API to work on
     * @param config The SI configuration to use
     * @param version The CSTS version to use
     * @throws ApiException
     */
    public MdCstsSiUserInform(ICstsApi api, MdCstsSiConfig config, int serviceVersion) throws ApiException
    {
        super(api, config, serviceVersion);

        System.out.println("MdCstsSiUserInform#MdCstsSiUser() begin");

        this.retLock = new ReentrantLock();
        this.retCond = retLock.newCondition();

        this.queriedParameters = new ArrayList<List<QualifiedParameter>>();
        this.cyclicParameters = new ArrayList<List<QualifiedParameter>>();
        this.notifiedEvents = new ArrayList<Name>();

        this.parameters = new HashMap<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>>>();
        this.events = new HashMap<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceEvent<?, FunctionalResourceValue<?>>>>();
        this.parameterUpdateCounters = new HashMap<ProcedureInstanceIdentifier, Integer>();
        this.eventUpdateCounters = new HashMap<ProcedureInstanceIdentifier, Integer>();
        for (ProcedureInstanceIdentifier piid : config.getProceduresIdentifiers())
        {
            if (piid.getType().equals(ProcedureType.of(OIDs.ocoCyclicReport))
                || piid.getType().equals(ProcedureType.of(OIDs.informationQuery)))
            {
                this.parameters.put(piid,
                                    new HashMap<Name, FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>>());
            }
            else if (piid.getType().equals(ProcedureType.of(OIDs.notification)))
            {
                this.events.put(piid, new HashMap<Name, FunctionalResourceEvent<?, FunctionalResourceValue<?>>>());
            }
        }

        resetOperationResult();

        System.out.println("MdCstsSiUserInform#MdCstsSiUser() end");
    }

    /**
     * Process an operation invocation from the provider
     * 
     * @param operation The invoked operation
     */
    @Override
    public void informOpInvocation(IOperation operation)
    {
        // System.out.println("MdCstsSiUserInform#informOpInvocation() begin");

        switch (operation.getType())
        {
        case TRANSFER_DATA:
            onTransferData((ITransferData) operation);
            break;

        case NOTIFY:
            onNotify((INotify) operation);
            break;
        case PEER_ABORT:
        	protocolAbort(); // TODO: call a TBD peer abort method
            break;
        default:
            System.err.print("unexpected operation " + operation.getClass().getSimpleName() + " invoked");
            break;
        }
        // System.out.println("MdCstsSiUserInform#informOpInvocation() end");
    }

    /**
     * Process an acknowledged operation from the provider
     * 
     * @param operation The acknowledged operation
     */
    @Override
    public void informOpAcknowledgement(IAcknowledgedOperation operation)
    {
        System.out.println("MdCstsSiUserInform#informOpAcknowledgement() begin");
        System.err.println("NOT IMPL!");
        System.out.println("MdCstsSiUserInform#informOpAcknowledgement() end");
    }

    /**
     * Process an operation return from the provider
     * 
     * @param operation The returned operation
     */
    @Override
    public void informOpReturn(IConfirmedOperation operation)
    {
        System.out.println("MdCstsSiUserInform#informOpReturn() begin");

        switch (operation.getType())
        {
        case BIND:
            onReturn((IBind) operation, () -> {
                return ((IBind) operation).getBindDiagnostic().name();
            });
            break;

        case UNBIND:
            onReturn((IUnbind) operation, () -> {
                return ((IUnbind) operation).getDiagnostic().getType().name();
            });
            break;

        case START:
            onStartReturn((IStart) operation);
            break;

        case STOP:
            onStopReturn((IStop) operation);
            break;

        case GET:
            onGetReturn((IGet) operation);
            break;

        default:
            System.err.print("unexpected operation " + operation.getClass().getSimpleName() + " returned");
            break;
        }

        System.out.println("MdCstsSiUserInform#informOpReturn() end");
    }

    /**
     * Process a protocol abort
     */
    @Override
    public void protocolAbort()
    {
        System.out.println("MdCstsSiUserInform#protocolAbort() begin");

        this.retLock.lock();

        try
        {
            System.out.println("PROTOCOL ABORT");
            this.retCond.signalAll();
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#protocolAbort() end");
    }

    /**
     * Process operation's result and diagnostic and signal return to the
     * invoker
     * 
     * @param op The returned operation
     * @param specDiagFn The supplier method for the operation diagnostic
     */
    private <OP extends IConfirmedOperation> void onReturn(OP op, Supplier<String> specDiagFn)
    {
        System.out.println("MdCstsSiUserInform#onReturn() begin");

        this.retLock.lock();

        try
        {
            this.operationResult = op.getResult();
            if (this.operationResult == OperationResult.NEGATIVE)
            {            	
                String specialDiagnostic = specDiagFn.get();
                if (!specialDiagnostic.equals("INVALID"))
                {
                    setDiagnostic(specialDiagnostic);
                }
                else
                {
                    setDiagnostic(op.getDiagnostic().getType().name());
                }
            }

            System.out.println("Signaling operation " + op.getClass().getSimpleName() + " return");
            this.retCond.signalAll();
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#onReturn() end");
    }

    private void setDiagnosticObject(CyclicReportStartDiagnostics cyclicReportStartDiagnostics) {
		this.diagnosticObject.set(cyclicReportStartDiagnostics);
		
	}
    
    public CyclicReportStartDiagnostics getDiagnosticObject() {
    	return this.diagnosticObject.get();
    }

	/**
     * Process a start operation return
     * 
     * @param start The returned start operation
     */
    private void onStartReturn(IStart start) {
    	if(this.getApiSi().getSfwVersion().equals(SfwVersion.B2)) {
    		onStartReturnB2(start);
    	} else {
    		onStartReturnB1(start);
    	}
    }
    
    private void onStartReturnB1(IStart start)
    {
        System.out.println("MdCstsSiUserInform#onStartReturn() begin(b1)");

        this.retLock.lock();

        try
        {
            if (start.getResult() == OperationResult.NEGATIVE)
            {
                onReturn(start, () -> {
                    StartDiagnostic sd = start.getStartDiagnostic();
                    if (sd.getType() == StartDiagnosticType.EXTENDED)
                    {
                        EmbeddedData ed = sd.getDiagnosticExtension();
                        if (ed.getOid().equals(OIDs.crStartDiagExt))
                        {
                        	b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt cyclicReportStartDiagnosticExt = 
                        			new b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt();
                            try (ByteArrayInputStream is = new ByteArrayInputStream(ed.getData()))
                            {
                                cyclicReportStartDiagnosticExt.decode(is);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            
                            setDiagnosticObject(CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt));
                            
                            return CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt)
                                    .getListOfParametersDiagnostics().toString();
                        }
                        else if (ed.getOid().equals(OIDs.nStartDiagExt))
                        {
                        	b1.ccsds.csts.notification.pdus.NotificationStartDiagnosticExt notificationStartDiagnosticExt = 
                        			new b1.ccsds.csts.notification.pdus.NotificationStartDiagnosticExt();
                            try (ByteArrayInputStream is = new ByteArrayInputStream(ed.getData()))
                            {
                                notificationStartDiagnosticExt.decode(is);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            return notificationStartDiagnosticExt.getCommon().toString();
                        }
                    }
                    return start.getStartDiagnostic().getType().name();
                });
            }
            else
            {
                onReturn(start, () -> {
                    return start.getStartDiagnostic().getType().name();
                });
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#onStartReturn() end(b1)");
    }
    
    private void onStartReturnB2(IStart start)
    {
        System.out.println("MdCstsSiUserInform#onStartReturn() begin(b2)");

        this.retLock.lock();

        try
        {
            if (start.getResult() == OperationResult.NEGATIVE)
            {
                onReturn(start, () -> {
                    StartDiagnostic sd = start.getStartDiagnostic();
                    if (sd.getType() == StartDiagnosticType.EXTENDED)
                    {
                        EmbeddedData ed = sd.getDiagnosticExtension();
                        if (ed.getOid().equals(OIDs.crStartDiagExt))
                        {
                        	b2.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt cyclicReportStartDiagnosticExt = 
                        			new b2.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt();
                            try (ByteArrayInputStream is = new ByteArrayInputStream(ed.getData()))
                            {
                                cyclicReportStartDiagnosticExt.decode(is);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            
                            setDiagnosticObject(CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt));
                            
                            return CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt)
                                    .getListOfParametersDiagnostics().toString();
                        }
                        else if (ed.getOid().equals(OIDs.nStartDiagExt))
                        {
                        	b2.ccsds.csts.notification.pdus.NotificationStartDiagnosticExt notificationStartDiagnosticExt = 
                        			new b2.ccsds.csts.notification.pdus.NotificationStartDiagnosticExt();
                            try (ByteArrayInputStream is = new ByteArrayInputStream(ed.getData()))
                            {
                                notificationStartDiagnosticExt.decode(is);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            return notificationStartDiagnosticExt.getCommon().toString();
                        }
                    }
                    return start.getStartDiagnostic().getType().name();
                });
            }
            else
            {
                onReturn(start, () -> {
                    return start.getStartDiagnostic().getType().name();
                });
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#onStartReturn() end(b2)");
    }

    /**
     * Process a stop operation return
     * 
     * @param stop The returned stop operation
     */
    private void onStopReturn(IStop stop)
    {
        System.out.println("MdCstsSiUserInform#onStopReturn() begin");

        this.retLock.lock();

        try
        {
            onReturn(stop, () -> {
                return stop.getDiagnostic().getType().name();
            });
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#onStopReturn() end");
    }

    private void processFunctionalResourceParameters(ProcedureInstanceIdentifier piid,
                                                     List<QualifiedParameter> qualifiedParameters) throws InstantiationException,
                                                                                                   IllegalAccessException,
                                                                                                   IOException,
                                                                                                   NoSuchFieldException,
                                                                                                   IllegalArgumentException,
                                                                                                   NoSuchMethodException,
                                                                                                   SecurityException,
                                                                                                   InvocationTargetException
    {
        synchronized (this.parameters)
        {
            Map<Name, FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> procedureParameters = this.parameters
                    .get(piid);

            for (QualifiedParameter qualifiedParameter : qualifiedParameters)
            {
                if (qualifiedParameter.getName().getResourceIdentifier() != ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME)
                {
                    // ignore non-FR parameters
                    continue;
                }

                if (!qualifiedParameter.getQualifiedValues().isEmpty())
                {
                    QualifiedValues qualifiedValues = qualifiedParameter.getQualifiedValues().get(0);
                    if (ParameterQualifier.VALID == qualifiedValues.getQualifier())
                    {
                        if (!qualifiedValues.getParameterValues().isEmpty())
                        {
                            ParameterValue parameterValue = qualifiedValues.getParameterValues().get(0);
                            if (parameterValue.getType() == ParameterType.EXTENDED)
                            {
                                FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter = getParameter(procedureParameters,
                                                                                                                      qualifiedParameter
                                                                                                                              .getName());
                                parameter.setValue(qualifiedParameter);
                            }
                            else
                            {
                                System.out.println("Received qualified parameter " + qualifiedParameter.getName()
                                                   + " w/ unexpected type " + parameterValue.getType());
                            }
                        }
                        else
                        {
                            System.out.println("Received qualified parameter " + qualifiedParameter.getName()
                                               + " w/o qualified values");
                        }
                    }
                    else
                    {
                        try
                        {
                            // ParameterQualifier.ERROR
                            // ParameterQualifier.UNAVAILABLE
                            // ParameterQualifier.UNDEFINED
                            FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter = getParameter(procedureParameters,
                                                                                                                  qualifiedParameter
                                                                                                                          .getName());
                            parameter.setCstsValue(CstsEmptyValue.empty(qualifiedValues.getQualifier()));
                        }
                        catch (IllegalArgumentException e)
                        {
                            // ignore a FR parameter w/o BER class
                        }
                    }
                }
            }
        }
    }

    /**
     * Process a get operation return
     * 
     * @param get The returned get operation
     */
    private void onGetReturn(IGet get)
    {
        // System.out.println("MdCstsSiUserInform#onGetReturn() begin");

        this.retLock.lock();
        try
        {
            Supplier<String> fnDiag = null;

            this.operationResult = get.getResult();
            if (this.operationResult == OperationResult.POSITIVE)
            {
                try
                {
                    synchronized (this.queriedParameters)
                    {
                        this.queriedParameters.add(get.getQualifiedParameters());
                        if (this.queriedParameters.size() > KEEP_MAX_LAST_DATA_UPDATES)
                        {
                            // remove oldest queried parameters
                            this.queriedParameters.remove(0);
                        }
                    }

                    processFunctionalResourceParameters(get.getProcedureInstanceIdentifier(),
                                                        get.getQualifiedParameters());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                if (get.getDiagnostic().getType() == DiagnosticType.EXTENDED)
                {
                    fnDiag = () -> {
                        return get.getListOfParametersDiagnostics().getType().name() + ", "
                               + get.getListOfParametersDiagnostics().toString();
                    };
                }
                else
                {
                    fnDiag = get.getDiagnostic().getType()::name;
                }
            }

            onReturn(get, fnDiag);
        }
        finally
        {
            this.retLock.unlock();
        }

        // System.out.println("MdCstsSiUserInform#onGetReturn() end");
    }

    private FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> getParameter(Map<Name, FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> procedureParameters,
                                                                                      Name name) throws InstantiationException,
                                                                                                 IllegalAccessException,
                                                                                                 NoSuchMethodException,
                                                                                                 SecurityException,
                                                                                                 IllegalArgumentException,
                                                                                                 InvocationTargetException
    {
        FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> ret = procedureParameters.get(name);

        if (ret == null)
        {
            ret = FunctionalResourceMetadata.getInstance().createParameter(name);
            procedureParameters.put(name, ret);
        }

        return ret;
    }

    /**
     * Increment a procedure invocation counter
     * 
     * @param piid The procedure identifier
     * @param counter The map with counters for each configured procedure
     */
    private static void incrementCounter(ProcedureInstanceIdentifier piid,
                                         Map<ProcedureInstanceIdentifier, Integer> counter,
                                         int increment)
    {
        synchronized (counter)
        {
            int count = counter.get(piid);
            counter.put(piid, count + increment);
        }
    }

    /**
     * Reset a of a procedure
     */
    protected static void resetCounter(ProcedureInstanceIdentifier piid,
                                       Map<ProcedureInstanceIdentifier, Integer> counter)
    {
        synchronized (counter)
        {
            counter.put(piid, 0);
        }
    }

    /**
     * Reset the parameter update count
     * 
     * @param piid The procedure identifier
     */
    public void resetParameterUpdateCount(ProcedureInstanceIdentifier piid)
    {
        resetCounter(piid, this.parameterUpdateCounters);
    }

    /**
     * Reset the parameter update count
     * 
     * @param piid The procedure identifier
     * @param increment The number of new parameters
     */
    protected void incrementParameterUpdateCount(ProcedureInstanceIdentifier piid, int increment)
    {
        incrementCounter(piid, this.parameterUpdateCounters, increment);
    }

    /**
     * Reset the event update counter
     * 
     * @param piid The procedure identifier
     */
    public void resetEventUpdateCount(ProcedureInstanceIdentifier piid)
    {
        resetCounter(piid, this.eventUpdateCounters);
    }

    /**
     * Reset the event update counter
     * 
     * @param piid The procedure identifier
     * @param increment The number of new events
     */
    protected void incrementEventUpdateCount(ProcedureInstanceIdentifier piid, int increment)
    {
        incrementCounter(piid, this.parameterUpdateCounters, increment);
    }

    private static int getProcedureUpdateCounter(ProcedureInstanceIdentifier piid,
                                                 Map<ProcedureInstanceIdentifier, Integer> counter)
    {
        int ret = 0;
        synchronized (counter)
        {
            if (counter.containsKey(piid))
            {
                ret = counter.get(piid);
            }
            else
            {
                throw new IllegalArgumentException("Not available counter for " + piid);
            }
        }
        return ret;
    }

    /**
     * Return the number of updated parameters since the last reset
     * 
     * @param piid The procedure identifier
     * @return The number of update parameters
     */
    public int getParameterUpdateCount(ProcedureInstanceIdentifier piid)
    {
        return getProcedureUpdateCounter(piid, this.parameterUpdateCounters);
    }

    /**
     * Return the number of updated events since the last reset
     * 
     * @param piid The procedure identifier
     * @return The number of update events
     */
    public int getEventUpdateCount(ProcedureInstanceIdentifier piid)
    {
        return getProcedureUpdateCounter(piid, this.eventUpdateCounters);
    }

    /**
     * Process a transferData operation invocation from the provider
     * 
     * @param transferData The invoked transferData operation
     */
    private void onTransferData(ITransferData transferData)
    {
        // System.out.println("MdCstsSiUserInform#onTransferData() begin");

        ProcedureInstanceIdentifier piid = transferData.getProcedureInstanceIdentifier();

        List<QualifiedParameter> params = ((OnChangeCyclicReportUser) this.getApiSi().getProcedure(piid))
                .getQualifiedParameters();

        synchronized (this.cyclicParameters)
        {
            this.cyclicParameters.add(params);
            incrementCounter(piid, this.parameterUpdateCounters, params.size());
            if (this.cyclicParameters.size() > KEEP_MAX_LAST_DATA_UPDATES)
            {
                // remove the oldest reported parameters
                this.cyclicParameters.remove(0);
            }
        }

        try
        {
            processFunctionalResourceParameters(piid, params);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        this.retLock.lock();
        try
        {
            this.retCond.signalAll();
        }
        finally
        {
            this.retLock.unlock();
        }

        // System.out.println("MdCstsSiUserInform#onTransferData() end");
    }

    /**
     * Wait for reception of a given number transfer data unit
     * @param numTd	the number of transfer data units to receive
     * @param ms	the time to wait in ms for each TD
     * @return the number of received transfer data or -1 for a timeout
     */
    public long waitTransferData(long numTd, long ms) {
    	long received = 0;
    	
    	while(received < numTd) {
	    	retLock.lock();
	    	try {
				boolean ret = this.retCond.await(ms, TimeUnit.MILLISECONDS);
				if(ret == false) {
					System.out.println("Timeout of " + ms + " ms. Received " + received + " transfer data operations");
					return -1;
				} else {
					received++;
				}
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} finally {
				retLock.unlock();
			}	    	
    	}
    	System.out.println("Received " + received + " transfer data operations");
    	return received;
    }
    
    /**
     * Wait for the specified timeout until the desired SI status is reached.
     * Wait on the retCond member, which is signaled for incoming operations changing the state
     * @param desiredState
     * @param msTimeout
     * @return
     */
    public ServiceStatus waitServiceStatus(ServiceStatus desiredState, long msTimeout) {
    	boolean signaled = false;
    	
    	if(getApiSi().getStatus() == desiredState) {
    		System.out.println("User SI has desired state, no wait for " + desiredState);
    		return desiredState;
    	}
    	
    	this.retLock.lock();
    	try {
    		signaled = this.retCond.await(msTimeout, TimeUnit.MILLISECONDS);
    	}
    	catch(InterruptedException ie) {
    		// ignore 
    	} finally {
    		this.retLock.unlock();
    	}
    	
    	if(desiredState == getApiSi().getStatus()) {
    		System.out.println("User SI has desired state " + desiredState + ", state change signaled: " + signaled);
    	} else {
    		System.err.println("User SI does not have desired state " + desiredState + ", state change signaled: " + signaled);
    	}
    	
    	return getApiSi().getStatus();
    }
    
    /**
     * Process a notify operation invocation from the provider
     * 
     * @param notify The invoked notify operation
     */
    private void onNotify(INotify notify)
    {
        System.out.println("MdCstsSiUserInform#onNotify() begin");

        ProcedureInstanceIdentifier piid = notify.getProcedureInstanceIdentifier();
        Name eventName = notify.getEventName();

        if (eventName != null)
        {
            System.out.println(eventName);

            synchronized (this.notifiedEvents)
            {
                this.notifiedEvents.add(Name.of(eventName.getOid(), eventName.getFunctionalResourceName()));
            }
        }

        try
        {
            synchronized (this.events)
            {
                incrementCounter(piid, this.eventUpdateCounters, 1);
                Map<Name, FunctionalResourceEvent<?, FunctionalResourceValue<?>>> procedureEvents = this.events
                        .get(piid);

                EventValue eventValue = notify.getEventValue();
                if (eventValue.getType() == EventValueType.EXTENDED)
                {
                    FunctionalResourceEvent<?, FunctionalResourceValue<?>> event = procedureEvents.get(eventName);
                    if (event == null)
                    {
                        event = FunctionalResourceMetadata.getInstance().createEvent(eventName);
                        procedureEvents.put(eventName, event);
                    }
                    event.setValue(eventValue);
                }
                else
                {
                    System.out.println("Received and event w/ usuppoerted EventValueType " + eventValue.getType());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("MdCstsSiUserInform#onNotify() end");

    }

    /**
     * Reset the operation result and diagnostic
     */
    protected void resetOperationResult()
    {
        this.operationResult = OperationResult.INVALID;
        this.dignostic = new AtomicReference<String>();
    }

    /**
     * Provide CTSTS result from the operation result
     * 
     * @parame operationResult The invoked operation result
     * @return CstsResult
     */
    protected static CstsResult convert(OperationResult operationResult)
    {
        CstsResult ret = CstsResult.FAILURE;

        if (operationResult == OperationResult.POSITIVE)
        {
            ret = CstsResult.SUCCESS;
        }

        return ret;
    }

    protected void setDiagnostic(String diag)
    {
        this.dignostic.set(diag);
    }

    protected void setDiagnostic(Enum<?> diag)
    {
        this.dignostic.set(diag.name());
    }

    /**
     * Get the diagnostic of the last returned operation w/ negative result
     * 
     * @return The diagnostic
     */
    public String getDiagnostic()
    {
        return this.dignostic.get();
    }

    private static int getCount(List<?> list)
    {
        int ret = 0;

        synchronized (list)
        {
            ret = list.size();
        }

        return ret;
    }

    public int getCyclicReportCount()
    {
        return getCount(this.cyclicParameters);
    }

    public int getNotifiedEventCount()
    {
        return getCount(this.notifiedEvents);
    }

    public List<Name> getNotifiedEvents()
    {
        return this.notifiedEvents;
    }

    public int getQueriedParameterCount()
    {
        return getCount(this.queriedParameters);
    }

    public static List<QualifiedParameter> getNthParameters(List<List<QualifiedParameter>> listOflist, int n)
    {
        List<QualifiedParameter> ret = new ArrayList<>();

        synchronized (listOflist)
        {
            if (listOflist.size() > n)
            {
                ret.addAll(listOflist.get(n));
            }
        }

        return ret;
    }

    public int getCyclicReportParametersCount()
    {
        return getCount(this.cyclicParameters);
    }

    public List<QualifiedParameter> getLastQueriedParameters()
    {
        return getNthParameters(this.queriedParameters, this.queriedParameters.size() - 1);
    }

    public List<QualifiedParameter> getQueriedParameters(int n)
    {
        return getNthParameters(this.queriedParameters, n);
    }

    public List<QualifiedParameter> getLastCyclicParameters()
    {
        return getNthParameters(this.cyclicParameters, this.cyclicParameters.size() - 1);
    }

    public List<QualifiedParameter> getCyclicParameters(int n)
    {
        return getNthParameters(this.cyclicParameters, n);
    }

    /**
     * Get procedure's FR parameters
     * 
     * @param piid The procedure identifier
     * @return The map of the procedure's FR parameters
     */
    protected Map<Name, FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getProcedureParameters(ProcedureInstanceIdentifier piid)
    {
        Map<Name, FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> ret = this.parameters.get(piid);
        if (ret == null)
        {
            throw new IllegalArgumentException(piid + " is not supported by "
                                               + this.getApiSi().getServiceInstanceIdentifier());
        }

        return ret;
    }

    /**
     * Get procedure's FR events
     * 
     * @param piid The procedure identifier
     * @return The map of the procedure's FR events
     */
    protected Map<Name, FunctionalResourceEvent<?, FunctionalResourceValue<?>>> getProcedureEvents(ProcedureInstanceIdentifier piid)
    {
        Map<Name, FunctionalResourceEvent<?, FunctionalResourceValue<?>>> ret = this.events.get(piid);
        if (ret == null)
        {
            throw new IllegalArgumentException(piid + " is not supported by "
                                               + this.getApiSi().getServiceInstanceIdentifier());
        }

        return ret;
    }

    // TODO clone the returned instances of FunctionalResourceParameterEx<?>
    // from methods below
    /**
     * Get a parameter from a procedure
     * 
     * @param piid The procedure identifier
     * @param name FR parameter name
     * @return the list of FR parameters
     */
    public FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> getParameter(ProcedureInstanceIdentifier piid,
                                                                                     Name name)
    {
        FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> ret;
        synchronized (this.parameters)
        {
            ret = getProcedureParameters(piid).get(name);
            if (ret == null)
            {
                throw new IllegalArgumentException("FR parameter " + name + " has not been received by procedure "
                                                   + piid);
            }
        }

        return ret;
    }

    /**
     * Get an event from a procedure
     * 
     * @param piid The procedure identifier
     * @param name FR event name
     * @return the list of FR parameters
     */
    public FunctionalResourceEvent<?, FunctionalResourceValue<?>> getEvent(ProcedureInstanceIdentifier piid, Name name)
    {
        FunctionalResourceEvent<?, FunctionalResourceValue<?>> ret;
        synchronized (this.parameters)
        {
            ret = getProcedureEvents(piid).get(name);
            if (ret == null)
            {
                throw new IllegalArgumentException("FR event " + name + " has not been received by procedure " + piid);
            }
        }

        return ret;
    }

    /**
     * Get parameter from the prime procedure
     * 
     * @param name FR parameter name
     * @return FR parameter
     */
    @Override
    public FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> getParameter(Name name)
    {
        return getParameter(getPrimeProcedureIdentifier(), name);
    }

    /**
     * Get event from the prime procedure
     * 
     * @param name FR event name
     * @return FR event
     */
    @Override
    public FunctionalResourceEvent<?, FunctionalResourceValue<?>> getEvent(Name name)
    {
        return getEvent(getPrimeProcedureIdentifier(), name);
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @param label FR parameter label
     * @return the list of FR parameters
     */
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureInstanceIdentifier piid,
                                                                                            Label label)
    {
        synchronized (this.parameters)
        {
            return getProcedureParameters(piid).values().stream()
                    .filter(p -> (p instanceof FunctionalResourceParameterEx) && p.getLabel().equals(label))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @param label FR parameter label
     * @return the list of FR parameters
     */
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(Label label)
    {
        return getParameters(getPrimeProcedureIdentifier(), label);
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @param frn FR name
     * @return the list of FR parameters
     */
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureInstanceIdentifier piid,
                                                                                            FunctionalResourceName frn)
    {
        synchronized (this.parameters)
        {
            return getProcedureParameters(piid).values()
                    .stream().filter(p -> (p instanceof FunctionalResourceParameterEx<?, ?>
                                           && p.getName().getFunctionalResourceName().equals(frn)))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @param frn FR name
     * @return the list of FR parameters
     */
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(FunctionalResourceName frn)
    {
        return getParameters(getPrimeProcedureIdentifier(), frn);
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @param frt FR type
     * @return the list of FR instance labels
     */
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureInstanceIdentifier piid,
                                                                                            FunctionalResourceType frt)
    {
        synchronized (this.parameters)
        {
            return getProcedureParameters(piid).values().stream()
                    .filter(p -> (p instanceof FunctionalResourceParameterEx<?, ?>
                                  && p.getLabel().getFunctionalResourceType().equals(frt)))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @param frt FR type
     * @return the map of FR instance number to map of labels to FR parameters
     */
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(FunctionalResourceType frt)
    {
        return getParameters(getPrimeProcedureIdentifier(), frt);
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @return the list of FR parameters
     */
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters()
    {
        return getParameters(getPrimeProcedureIdentifier());
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @return the map of FR instance number to map of labels to FR parameters
     */
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureInstanceIdentifier piid)
    {
        synchronized (this.parameters)
        {
            return getProcedureParameters(piid).values().stream()
                    .filter(p -> (p instanceof FunctionalResourceParameterEx<?, ?>
                                  && p.getName().getProcedureInstanceIdentifier().equals(piid)))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @param frt FR procedure type
     * @return the map of FR instance number to map of labels to FR parameters
     */
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureInstanceIdentifier piid,
                                                                                            ProcedureType procTyp)
    {
        synchronized (this.parameters)
        {
            return getProcedureParameters(piid).values()
                    .stream().filter(p -> (p instanceof FunctionalResourceParameterEx<?, ?>
                                           && p.getLabel().getProcedureType().equals(procTyp)))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @param procTyp The procedure type
     * @return the list of FR parameters
     */
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureType procTyp)
    {
        return getParameters(getPrimeProcedureIdentifier(), procTyp);
    }

    public ICstsValue getParameterValue(ProcedureInstanceIdentifier piid, Name name) throws IllegalAccessException
    {
        FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter = getParameter(piid, name);
        if (parameter == null)
        {
            throw new IllegalArgumentException("Parameter " + name + " is not available in MD collection");
        }
        return ((FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>) parameter).getCstsValue();
    }

    public Map<Long, ICstsValue> getParameterValues(ProcedureInstanceIdentifier piid,
                                                    Label label) throws IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(piid, label);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Label " + label + " is not available in MD collection");
        }

        Map<Long, ICstsValue> res = new HashMap<>();
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter : parameters)
        {
            res.put(Long.valueOf(parameter.getName().getFunctionalResourceName().getInstanceNumber()),
                    parameter.getCstsValue());
        }
        return res;
    }

    public Map<Name, ICstsValue> getParameterValues(ProcedureInstanceIdentifier piid,
                                                    FunctionalResourceName frn) throws IllegalArgumentException,
                                                                                IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(piid, frn);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource name " + frn
                                               + " is not available in MD collection");
        }

        Map<Name, ICstsValue> res = new HashMap<Name, ICstsValue>(parameters.size());
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter : parameters)
        {
            res.put(parameter.getName(), parameter.getCstsValue());
        }
        return res;
    }

    public Map<Long, Map<Label, ICstsValue>> getParameterValues(ProcedureInstanceIdentifier piid,
                                                                FunctionalResourceType frt) throws IllegalArgumentException,
                                                                                            IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(piid, frt);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource type " + frt
                                               + " is not available in MD collection");
        }

        Map<Long, Map<Label, ICstsValue>> ret = new HashMap<Long, Map<Label, ICstsValue>>();
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> p : parameters)
        {
            Long instNum = Long.valueOf(p.getName().getFunctionalResourceName().getInstanceNumber());
            Map<Label, ICstsValue> instPars;
            if (ret.containsKey(instNum))
            {
                instPars = ret.get(instNum);
            }
            else
            {
                instPars = new HashMap<Label, ICstsValue>();
                ret.put(instNum, instPars);
            }
            instPars.put(p.getLabel(), p.getCstsValue());
        }
        return ret;
    }

    public Map<Name, ICstsValue> getParameterValues(ProcedureInstanceIdentifier piid) throws IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(piid);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Procedure instance identifier " + piid
                                               + " is not available in MD collection");
        }

        Map<Name, ICstsValue> ret = new HashMap<Name, ICstsValue>(parameters.size());
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> p : parameters)
        {
            ret.put(p.getName(), p.getCstsValue());
        }
        return ret;
    }

    public Map<ProcedureRole, Map<Long, Map<Label, ICstsValue>>> getParameterValues(ProcedureInstanceIdentifier piid,
                                                                                    ProcedureType procTyp) throws IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(piid, procTyp);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Procedure type " + procTyp + " is not available in MD collection");
        }

        Map<ProcedureRole, Map<Long, Map<Label, ICstsValue>>> ret = new HashMap<ProcedureRole, Map<Long, Map<Label, ICstsValue>>>();
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> p : parameters)
        {
            ProcedureRole procedureRole = piid.getRole();
            Map<Long, Map<Label, ICstsValue>> roleMap;
            if (ret.containsKey(procedureRole))
            {
                roleMap = ret.get(procedureRole);
            }
            else
            {
                roleMap = new HashMap<Long, Map<Label, ICstsValue>>();
                ret.put(procedureRole, roleMap);
            }

            Long instNum = Long.valueOf(p.getName().getFunctionalResourceName().getInstanceNumber());
            Map<Label, ICstsValue> instMap;
            if (roleMap.containsKey(instNum))
            {
                instMap = roleMap.get(instNum);
            }
            else
            {
                instMap = new HashMap<Label, ICstsValue>();
                roleMap.put(instNum, instMap);
            }
            instMap.put(p.getLabel(), p.getCstsValue());
        }
        return ret;
    }

}
