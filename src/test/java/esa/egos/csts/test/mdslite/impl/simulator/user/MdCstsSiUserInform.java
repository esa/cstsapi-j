package esa.egos.csts.test.mdslite.impl.simulator.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
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
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportUser;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.InformationQueryUser;
import esa.egos.csts.api.procedures.notification.NotificationUser;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.test.mdslite.impl.simulator.MdCstsSi;
import esa.egos.csts.test.mdslite.impl.simulator.MdCstsSiConfig;
import esa.egos.csts.test.mdslite.impl.simulator.Utils;

/**
 * MD-CSTS User service inform
 */
public abstract class MdCstsSiUserInform extends MdCstsSi<InformationQueryUser, CyclicReportUser, NotificationUser>
{
    /** the size of buffer for printing an operation parameters */
    protected static final int PRINT_BUFF_SIZE = 1024;

    /** lock protecting this service instance */
    protected final Lock retLock;

    /** condition variable for signaling an operation return or abort */
    protected final Condition retCond;

    /** the last invoked operation result */
    protected OperationResult operationResult;

    /** the diagnostic of the last failed operation */
    protected AtomicReference<String> dignostic;

    /**
     * parameters received on the GET operation return from a query inform
     * procedure
     */
    private List<List<QualifiedParameter>> queriedParameters;

    /**
     * parameters received on the TRANSFER-DATA operation invocation from a
     * cyclic report procedure
     */
    private List<List<QualifiedParameter>> cyclicParameters;

    /**
     * events received on the NOTIFY operation invocation from a
     * cyclic report procedure
     */
    private List<Name> notifiedEvents;


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

        System.out.println(operation.print(PRINT_BUFF_SIZE));

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

        System.out.println(operation.print(PRINT_BUFF_SIZE));

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
            System.out.println("this.operationResult: " + this.operationResult);
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
            onReturn(start, () -> { return start.getStartDiagnostic().getType().name();});
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
                synchronized (this.queriedParameters)
                {
                    this.queriedParameters.add(new ArrayList<QualifiedParameter>(get.getQualifiedParameters()));
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
     * Process a transferData operation invocation from the provider
     * 
     * @param transferData The invoked transferData operation
     */
    private void onTransferData(ITransferData transferData)
    {
        System.out.println("MdCstsSiUserInform#onTransferData() begin");

        ProcedureInstanceIdentifier piid = transferData.getProcedureInstanceIdentifier();
        System.out.println(piid);

        if (transferData.getEmbeddedData().getData() != null)
        {
            System.out.println(Utils.toHex(transferData.getEmbeddedData().getData()));
        }

        List<QualifiedParameter> params = ((CyclicReportUser) this.serviceInstance.getProcedure(piid))
                .getQualifiedParameters();

        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (QualifiedParameter p : params)
        {
            sb.append(p.getName());

            for (QualifiedValues vals : p.getQualifiedValues())
            {
                for (ParameterValue pv : vals.getParameterValues())
                {
                    if (!first)
                    {
                        sb.append(",");
                    }
                    else
                    {
                        sb.append(" ");
                    }
                    sb.append(pv.getType() + " : " + pv.getIntegerParameterValues());
                }
            }
        }
        System.out.println(sb.toString());

        synchronized (this.cyclicParameters)
        {
            synchronized (this.cyclicParameters)
            {
                this.cyclicParameters.add(new ArrayList<QualifiedParameter>(params));
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

        ProcedureInstanceIdentifier piid = notify.getProcedureInstanceIdentifier();
        System.out.println(piid);

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

}
