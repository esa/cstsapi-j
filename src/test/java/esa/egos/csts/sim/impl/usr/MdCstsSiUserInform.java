package esa.egos.csts.sim.impl.usr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt;
import b1.ccsds.csts.notification.pdus.NotificationStartDiagnosticExt;
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.diagnostics.StartDiagnosticType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.events.FunctionalResourceEventEx;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceMetadata;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
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
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.procedures.informationquery.InformationQueryUser;
import esa.egos.csts.api.procedures.notification.NotificationUser;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportUser;
import esa.egos.csts.sim.impl.MdCstsSi;
import esa.egos.csts.sim.impl.MdCstsSiConfig;

/**
 * MD-CSTS User service inform
 */
public abstract class MdCstsSiUserInform extends MdCstsSi<MdCstsSiConfig, InformationQueryUser, OnChangeCyclicReportUser, NotificationUser>
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
     * events received on the NOTIFY operation invocation from a
     * cyclic report procedure
     */
    private List<Name> notifiedEvents;

    /** FR parameters received from procedures */
    private Map<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceParameterEx<?>>> parameters;

    /** FR parameters received from procedures */
    private Map<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceEventEx<?>>> events;

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
    public MdCstsSiUserInform(ICstsApi api, MdCstsSiConfig config) throws ApiException
    {
        super(api, config);

        System.out.println("MdCstsSiUserInform#MdCstsSiUser() begin");

        this.retLock = new ReentrantLock();
        this.retCond = retLock.newCondition();

        this.queriedParameters = new ArrayList<List<QualifiedParameter>>();
        this.cyclicParameters = new ArrayList<List<QualifiedParameter>>();
        this.notifiedEvents = new ArrayList<Name>();

        this.parameters = new HashMap<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceParameterEx<?>>>();
        this.events = new HashMap<ProcedureInstanceIdentifier, Map<Name, FunctionalResourceEventEx<?>>>();
        this.parameterUpdateCounters = new HashMap<ProcedureInstanceIdentifier, Integer>();
        this.eventUpdateCounters = new HashMap<ProcedureInstanceIdentifier, Integer>();
        for (ProcedureInstanceIdentifier piid : config.getProceduresIdentifiers())
        {
            if (piid.getType().equals(ProcedureType.of(OIDs.ocoCyclicReport)) 
                    || piid.getType().equals(ProcedureType.of(OIDs.informationQuery)))
            {
                this.parameters.put(piid, new HashMap<Name, FunctionalResourceParameterEx<?>>());
            }
            else if (piid.getType().equals(ProcedureType.of(OIDs.notification)))
            {
                this.events.put(piid, new HashMap<Name, FunctionalResourceEventEx<?>>());
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
        System.out.println("MdCstsSiUserInform#informOpInvocation() begin");

        switch (operation.getType())
        {
        case TRANSFER_DATA:
            onTransferData((ITransferData) operation);
            break;

        case NOTIFY:
            onNotify((INotify) operation);
            break;

        default:
            System.err.print("unexpected operation " + operation.getClass().getSimpleName() + " invoked");
            break;
        }
        System.out.println("MdCstsSiUserInform#informOpInvocation() end");
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
            onReturn((IBind) operation, ()-> { return ((IBind) operation).getBindDiagnostic().name();});
            break;

        case UNBIND:
            onReturn((IUnbind) operation,()-> { return ((IUnbind) operation).getDiagnostic().getType().name();});
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
            this.retCond.signal();
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#protocolAbort() end");
    }

    /**
     * Process operation's result and diagnostic and signal return to the invoker
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
            this.retCond.signal();
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#onReturn() end");
    }

    /**
     * Process a start operation return
     * 
     * @param start The returned start operation
     */
    private void onStartReturn(IStart start)
    {
        System.out.println("MdCstsSiUserInform#onStartReturn() begin");

        this.retLock.lock();

        try
        {
            if (start.getResult() == OperationResult.NEGATIVE) {
                onReturn(start, () -> {
                    StartDiagnostic sd = start.getStartDiagnostic();
                    if (sd.getType() == StartDiagnosticType.EXTENDED)
                    {
                        EmbeddedData ed = sd.getDiagnosticExtension();
                        if (ed.getOid().equals(OIDs.crStartDiagExt)) {
                            CyclicReportStartDiagnosticExt cyclicReportStartDiagnosticExt = new CyclicReportStartDiagnosticExt();
                            try (ByteArrayInputStream is = new ByteArrayInputStream(ed.getData())) {
                                cyclicReportStartDiagnosticExt.decode(is);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt)
                                    .getListOfParametersDiagnostics().toString();
                        } else if (ed.getOid().equals(OIDs.nStartDiagExt)) {
                            NotificationStartDiagnosticExt notificationStartDiagnosticExt = new NotificationStartDiagnosticExt();
                            try (ByteArrayInputStream is = new ByteArrayInputStream(ed.getData())) {
                                notificationStartDiagnosticExt.decode(is);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return notificationStartDiagnosticExt.getCommon().toString();
                        }
                    }
                    return start.getStartDiagnostic().getType().name();
                });
            } else {
                onReturn(start, () -> { return start.getStartDiagnostic().getType().name();});
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#onStartReturn() end");
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
            onReturn(stop, ()->{ return stop.getDiagnostic().getType().name();});
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUserInform#onStopReturn() end");
    }

    /**
     * Process a get operation return
     * 
     * @param get The returned get operation
     */
    private void onGetReturn(IGet get)
    {
        System.out.println("MdCstsSiUserInform#onGetReturn() begin");

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
                        synchronized (this.parameters)
                        {
                            Map<Name, FunctionalResourceParameterEx<?>> procedureParameters =
                                    this.parameters.get(get.getProcedureInstanceIdentifier());
                            this.queriedParameters.add(get.getQualifiedParameters());
                            for (QualifiedParameter qualifiedParameter : get.getQualifiedParameters())
                            {
                                if (!qualifiedParameter.getQualifiedValues().isEmpty()
                                    && !qualifiedParameter.getQualifiedValues().get(0).getParameterValues().isEmpty()
                                    && qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                                            .getType() == ParameterType.EXTENDED)
                                {
                                    FunctionalResourceParameterEx<?> parameter = procedureParameters.get(qualifiedParameter.getName());
                                    if (parameter == null)
                                    {
                                        parameter = FunctionalResourceMetadata.getInstance()
                                                .createParameter(qualifiedParameter.getName());
                                        procedureParameters.put(qualifiedParameter.getName(), parameter);
                                    }
                                    parameter.setValue(qualifiedParameter);
                                }
                            }
                        }

                        if (this.queriedParameters.size() > KEEP_MAX_LAST_DATA_UPDATES)
                        {
                            // remove oldest queried parameters
                            this.queriedParameters.remove(0);
                        }
                    }
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

        System.out.println("MdCstsSiUserInform#onGetReturn() end");
    }

    /**
     * Increment a procedure invocation counter
     * @param piid The procedure identifier
     * @param counter The map with counters for each configured procedure
     */
    private static void incrementCounter(ProcedureInstanceIdentifier piid,
                                         Map<ProcedureInstanceIdentifier, Integer> counter, int increment)
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
     * @param piid The procedure identifier
     */
    public void resetParameterUpdateCount(ProcedureInstanceIdentifier piid)
    {
        resetCounter(piid, this.parameterUpdateCounters);
    }

    /**
     * Reset the parameter update count
     * @param piid The procedure identifier
     * @param increment The number of new parameters
     */
    protected void incrementParameterUpdateCount(ProcedureInstanceIdentifier piid, int increment)
    {
        incrementCounter(piid, this.parameterUpdateCounters, increment);
    }

    /**
     * Reset the event update counter
     * @param piid The procedure identifier
     */
    public void resetEventUpdateCount(ProcedureInstanceIdentifier piid)
    {
        resetCounter(piid, this.eventUpdateCounters);
    }

    /**
     * Reset the event update counter
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
     * 
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
     * 
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
        System.out.println("MdCstsSiUserInform#onTransferData() begin");

        ProcedureInstanceIdentifier piid = transferData.getProcedureInstanceIdentifier();

        List<QualifiedParameter> params = ((OnChangeCyclicReportUser) this.serviceInstance.getProcedure(piid))
                .getQualifiedParameters();

        synchronized (this.cyclicParameters)
        {
            this.cyclicParameters.add(params);
            try
            {
                synchronized (this.parameters)
                {
                    incrementCounter(piid, this.parameterUpdateCounters, params.size());
                    Map<Name, FunctionalResourceParameterEx<?>> procedureParameters = 
                            this.parameters.get(transferData.getProcedureInstanceIdentifier());
                    for (QualifiedParameter qualifiedParameter : params)
                    {
                        if (!qualifiedParameter.getQualifiedValues().isEmpty()
                                && !qualifiedParameter.getQualifiedValues().get(0).getParameterValues().isEmpty()
                                && qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0).getType() == ParameterType.EXTENDED)
                        {
                            FunctionalResourceParameterEx<?> parameter = procedureParameters.get(qualifiedParameter.getName());
                            if (parameter == null)
                            {
                                parameter = FunctionalResourceMetadata.getInstance()
                                        .createParameter(qualifiedParameter.getName());
                                procedureParameters.put(qualifiedParameter.getName(), parameter);
                            }
                            parameter.setValue(qualifiedParameter);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (this.cyclicParameters.size() > KEEP_MAX_LAST_DATA_UPDATES)
            {
                // remove oldest reported parameters
                this.cyclicParameters.remove(0);
            }
        }

        System.out.println("MdCstsSiUserInform#onTransferData() end");
    }

    /**
     * Process a notify operation invocation from the provider
     * 
     * @param notify The invoked notify operation
     */
    private void onNotify(INotify notify)
    {
        System.out.println("MdCstsSiUserInform#onNotify() begin");

        Name eventName = notify.getEventName();
        if (eventName != null)
        {
            System.out.println(eventName);

            synchronized (this.notifiedEvents)
            {
                this.notifiedEvents.add(Name.of(eventName.getOid(), eventName.getFunctionalResourceName()));
            }
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
            ret.addAll(listOflist.get(n));
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
     * 
     * @return The map of the procedure's FR parameters
     */
    protected Map<Name, FunctionalResourceParameterEx<?>> getProcedureParameters(ProcedureInstanceIdentifier piid)
    {
        Map<Name, FunctionalResourceParameterEx<?>> ret = this.parameters.get(piid);
        if (ret == null)
        {
            throw new IllegalArgumentException(piid + " is not supported by " + this.serviceInstance.getServiceInstanceIdentifier());
        }

        return ret;
    }

    // TODO clone the returned instances of FunctionalResourceParameterEx<?> from methods below
    /**
     * Get parameter from a procedure
     * 
     * @param piid The procedure identifier
     * @param name FR parameter name
     * 
     * @return the list of FR parameters
     */
    public FunctionalResourceParameterEx<?> getParameter(ProcedureInstanceIdentifier piid, Name name)
    {
        FunctionalResourceParameterEx<?> ret;
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
     * Get parameter from the prime procedure
     * 
     * @param name FR parameter name
     * 
     * @return FR parameter
     */
    @Override
    public FunctionalResourceParameterEx<?> getParameter(Name name)
    {
        return getParameter(getPrimeProcedureIdentifier(), name);
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @param label FR parameter label
     * 
     * @return the list of FR parameters
     */
    public List<FunctionalResourceParameterEx<?>> getParameters(ProcedureInstanceIdentifier piid, Label label)
    {
        List<FunctionalResourceParameterEx<?>> ret = new ArrayList<FunctionalResourceParameterEx<?>>();
        synchronized(this.parameters)
        {
            getProcedureParameters(piid).values().stream()
                    .filter(p -> (p instanceof FunctionalResourceParameterEx) && p.getLabel().equals(label))
                    .forEach(p -> ret.add(p));
        }
        return ret;
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @param label FR parameter label
     * 
     * @return the list of FR parameters
     */
    @Override
    public List<FunctionalResourceParameterEx<?>> getParameters(Label label)
    {
        return getParameters(getPrimeProcedureIdentifier(), label);
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @param frn FR name
     * 
     * @return the list of FR parameters
     */
    public List<FunctionalResourceParameterEx<?>> getParameters(ProcedureInstanceIdentifier piid, FunctionalResourceName frn)
    {
        List<FunctionalResourceParameterEx<?>> ret = new ArrayList<FunctionalResourceParameterEx<?>>();
        synchronized(this.parameters)
        {
            getProcedureParameters(piid).values().stream()
                    .filter(p -> (p instanceof FunctionalResourceParameterEx && p.getName().getFunctionalResourceName().equals(frn)))
                    .forEach(p -> ret.add(p));
        }
        return ret;
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @param frn FR name
     * 
     * @return the list of FR parameters
     */
    @Override
    public List<FunctionalResourceParameterEx<?>> getParameters(FunctionalResourceName frn)
    {
        return getParameters(getPrimeProcedureIdentifier(), frn);
    }

    /**
     * Get parameters from a procedure
     * 
     * @param piid The procedure identifier
     * @param frt FR type
     * 
     * @return the map of FR instance number to map of labels to FR parameters
     */
    public Map<Integer, Map<Label, FunctionalResourceParameterEx<?>>> getParameters(ProcedureInstanceIdentifier piid, FunctionalResourceType frt)
    {
        Map<Integer, Map<Label, FunctionalResourceParameterEx<?>>> ret =
                new HashMap<Integer, Map<Label, FunctionalResourceParameterEx<?>>>();
        synchronized(this.parameters)
        {
            getProcedureParameters(piid).values().stream()
                    .filter(p -> (p instanceof FunctionalResourceParameterEx<?>
                                    && p.getName().getFunctionalResourceName().getType().equals(frt)))
                    // if instance number of this parameter is seen first -> add new map to ret
                    .peek(p -> {
                        if (!ret.containsKey(Integer.valueOf(p.getName().getFunctionalResourceName().getInstanceNumber())))
                        {
                            ret.put(Integer.valueOf(p.getName().getFunctionalResourceName().getInstanceNumber()),
                                    new HashMap<Label, FunctionalResourceParameterEx<?>>());
                        }})
                    // store parameter in proper map in ret list (index from instNums mapping)
                    .forEach(p -> ret.get(Integer.valueOf(p.getName().getFunctionalResourceName().getInstanceNumber()))
                                     .put(p.getLabel(), (FunctionalResourceParameterEx<?>)p));
        }

        return ret;
    }

    /**
     * Get parameters from the prime procedure
     * 
     * @param frt FR type
     * 
     * @return the map of FR instance number to map of labels to FR parameters
     */
    @Override
    public Map<Integer, Map<Label, FunctionalResourceParameterEx<?>>> getParameters(FunctionalResourceType frt)
    {
        return getParameters(getPrimeProcedureIdentifier(), frt);
    }

    /**
     * Get the parameter value from a procedure
     * 
     * @param piid The procedure identifier
     * @param name FR parameter name
     * 
     * @return FR parameter
     */
    public ICstsValue getParameterValue(ProcedureInstanceIdentifier piid, Name name) throws IllegalArgumentException, IllegalAccessException
    {
        FunctionalResourceParameter parameter = getParameter(piid, name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
        }
        return ((FunctionalResourceParameterEx<?>) parameter).getCstsValue();
    }

    /**
     * Get the parameters values from a procedure
     * 
     * @param piid The procedure identifier
     * @param label The parameter labelprocedure identifier
     * 
     * @return the map of FR instance number to map of labels to FR parameters
     */
    public List<ICstsValue> getParameterValue(ProcedureInstanceIdentifier piid, Label label) throws IllegalArgumentException, IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?>> parameters = getParameters(piid, label);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Label " + label + " is not available in MD collection");
        }

        List<ICstsValue> res = new ArrayList<>(parameters.size());
        for (FunctionalResourceParameterEx<?> parameter : parameters) {
            res.add(parameter.getCstsValue());
        }
        return res;
    }

    public Map<Name, ICstsValue> getParameterValues(ProcedureInstanceIdentifier piid, FunctionalResourceName frn) throws IllegalArgumentException, IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?>> parameters = getParameters(piid, frn);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource name " + frn + " is not available in MD collection");
        }

        Map<Name, ICstsValue> res = new HashMap<Name, ICstsValue>(parameters.size());
        for (FunctionalResourceParameterEx<?> parameter : parameters) {
            res.put(parameter.getName(), parameter.getCstsValue());
        }
        return res;
    }

    public Map<Integer, Map<Label, ICstsValue>> getParameterValues(ProcedureInstanceIdentifier piid, FunctionalResourceType frt) throws IllegalArgumentException, IllegalAccessException
    {
        Map<Integer, Map<Label, FunctionalResourceParameterEx<?>>> parameters = getParameters(piid, frt);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource type " + frt + " is not available in MD collection");
        }
        
        Map<Integer, Map<Label, ICstsValue>> ret = new HashMap<Integer, Map<Label, ICstsValue>>(parameters.size());
        for (Integer instNum : parameters.keySet())
        {
            Map<Label, ICstsValue> instParams = new HashMap<Label, ICstsValue>(parameters.get(instNum).size());
            for (Map.Entry<Label, FunctionalResourceParameterEx<?>> parameter : parameters.get(instNum).entrySet()) {
                instParams.put(parameter.getKey(), parameter.getValue().getCstsValue());
            }
            ret.put(instNum, instParams);
        }
        return ret;
    }
}
