package esa.egos.csts.test.mdslite.impl.simulator.user;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.associationcontrol.AssociationControlUser;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportUser;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.procedures.informationquery.InformationQueryUser;
import esa.egos.csts.api.procedures.notification.NotificationUser;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.test.mdslite.impl.simulator.MdCstsSiConfig;

/**
 * MD-CSTS User service instance
 */
public class MdCstsSiUser extends MdCstsSiUserInform
{
    /** the association procedure */
    private AssociationControlUser associationProcedure;


    /**
     * Constructs an MD CSTS User SI
     * 
     * @param api The CSTS user API to work on
     * @param config The SI configuration to use
     * @param version The CSTS version to use
     * @throws ApiException
     */
    public MdCstsSiUser(ICstsApi api, MdCstsSiConfig config, int version) throws ApiException
    {
        super(api, config);

        System.out.println("MdCstsSiUser#MdCstsSiUser() begin");

        this.serviceInstance.setVersion(version);
        this.associationProcedure = (AssociationControlUser) this.serviceInstance.getAssociationControlProcedure();

        System.out.println("MdCstsSiUser#MdCstsSiUser() end");
    }

    /**
     * Create InformationQueryUser procedure
     * 
     * @return InformationQueryUser
     */
    @Override
    protected InformationQueryUser createInformationQueryProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(InformationQueryUser.class);
    }

    /**
     * Create CyclicReportUser procedure
     * 
     * @return CyclicReportUser
     */
    @Override
    protected CyclicReportUser createCyclicReportProcedure() throws ApiException
    {
        CyclicReportUser ret = this.serviceInstance.createProcedure(CyclicReportUser.class);
        this.cyclicReportProcedures.put(ret, ProcedureState.INACTIVE);
        return ret;
    }

    /**
     * Create NotificationUser procedure
     * 
     * @return NotificationUser
     */
    @Override
    protected NotificationUser createNotificationProcedure() throws ApiException
    {
        NotificationUser ret = this.serviceInstance.createProcedure(NotificationUser.class);
        this.notificationProcedures.put(ret, ProcedureState.INACTIVE);
        return ret;
    }

    /**
     * Initiates a BIND to the remote peer of this SI and blocks until the BIND
     * return is received or a return timeout occurs.
     * 
     * @return The result of the BIND
     * @throws InterruptedException
     */
    public CstsResult bind() throws InterruptedException
    {
        System.out.println("MdCstsSiUser#bind() begin");

        CstsResult ret = CstsResult.FAILURE;
        this.retLock.lock();

        try
        {
            // reset the operation result and diagnostic
            resetOperationResult();

            // initiate invocation
            ret = this.associationProcedure.bind();
            if (ret == CstsResult.SUCCESS)
            {
                // invocation initiation succeeded
                // wait for the bind operation return
                ret = waitForReturnOrAbort(this.associationProcedure,
                                           "service instance is still not bound",
                                           null);
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUser#bind() end");
        return ret;
    }

    /**
     * Initiates an UNBIND to the remote peer of this SI and blocks until the
     * UNBIND return is received or a return timeout occurs.
     * 
     * @return The result of the BUNIND
     * @throws InterruptedException
     */
    public CstsResult unbind() throws InterruptedException
    {
        System.out.println("MdCstsSiUser#unbind() begin");

        CstsResult ret = CstsResult.FAILURE;
        this.retLock.lock();

        try
        {
            if (!isAnyStartableProcedureActive())
            {
                // reset the operation result and diagnostic
                resetOperationResult();

                // initiate invocation
                ret = this.associationProcedure.unbind();
                if (ret == CstsResult.SUCCESS)
                {
                    // invocation initiation succeeded
                    // wait for the unbind operation return
                    ret = waitForReturnOrAbort(this.associationProcedure,
                                               "service instance is still bound",
                                               null);
                }
            }
            else
            {
                // there is still active at least one procedure
                ret = CstsResult.FAILURE;
                setDiagnostic(ProcedureState.ACTIVE);
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUser#unbind() end");
        return ret;
    }

    /**
     * Start a cyclic report procedure
     * 
     * @param instanceNumber The cyclic procedure procedure instance number
     * @param listOfParameters The list of requested functional resource
     *            parameters to be cyclically reported
     * @param deliveryCycle The delivery cycle in milliseconds
     * @return The result of the start
     * @throws Exception
     */
    public CstsResult startCyclicReport(long instanceNumber,
                                        ListOfParameters listOfParameters,
                                        long deliveryCycle) throws Exception
    {
        System.out.println("MdCstsSiUser#startCyclicReport() begin");

        CstsResult ret = CstsResult.FAILURE;
        this.retLock.lock();

        try
        {
            // check whether this service instance is bound
            if (this.serviceInstance.getStatus() == ServiceStatus.BOUND)
            {
                // get the cyclic report procedure from the instance number
                CyclicReportUser cyclicReport = getProcedure(CyclicReportUser.class, instanceNumber);
                if (this.cyclicReportProcedures.get(cyclicReport) == ProcedureState.INACTIVE)
                {
                    // indicate transition from the INACTIVE state to the ACTIVE
                    // state
                    updateProcedureState(cyclicReport.getProcedureInstanceIdentifier(), ProcedureState.START_PENDING);

                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the start operation
                    ret = cyclicReport.requestCyclicReport(deliveryCycle, listOfParameters);
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the start
                        // operation return
                        ret = waitForReturnOrAbort(cyclicReport,
                                                   "cyclic report procedure (" + instanceNumber
                                                   + ") is still not active",
                                                   ProcedureState.INACTIVE);
                    }
                }
                else
                {
                    // the procedure is already active and cannot be started
                    setDiagnostic(ProcedureState.ACTIVE);
                }
            }
            else
            {
                // the service instance is unbound so the cyclic report
                // procedure cannot be started
                setDiagnostic(ServiceStatus.UNBOUND);
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUser#startCyclicReport() end");

        return ret;
    }

    /**
     * Stop a cyclic report procedure
     * 
     * @param instanceNumber The cyclic procedure procedure instance number
     * @return The result of the start
     * @throws Exception
     */
    public CstsResult stopCyclicReport(long instanceNumber) throws Exception
    {
        System.out.println("MdCstsSiUser#stopCyclicReport() begin");

        CstsResult ret = CstsResult.FAILURE;
        this.retLock.lock();

        try
        {
            // check whether this service instance is bound
            if (this.serviceInstance.getStatus() == ServiceStatus.BOUND)
            {
                // get the cyclic report procedure from the instance number
                CyclicReportUser cyclicReport = getProcedure(CyclicReportUser.class, instanceNumber);
                if (this.cyclicReportProcedures.get(cyclicReport) == ProcedureState.ACTIVE)
                {
                    // indicate transition from the ACTIVE state to the INACTIVE
                    // state
                    updateProcedureState(cyclicReport.getProcedureInstanceIdentifier(), ProcedureState.STOP_PENDING);

                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the stop operation
                    ret = cyclicReport.endCyclicReport();
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the stop
                        // operation return
                        ret = waitForReturnOrAbort(cyclicReport,
                                                   "cyclic report procedure (" + instanceNumber
                                                   + ") is still active",
                                                   ProcedureState.ACTIVE);
                    }
                }
                else
                {
                    // the procedure is inactive and cannot be stopped
                    setDiagnostic(ProcedureState.INACTIVE);
                }
            }
            else
            {
                // the service instance is unbound so the cyclic report
                // procedure cannot be started
                setDiagnostic(ServiceStatus.UNBOUND);
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUser#stopCyclicReport() end");

        return ret;
    }


    /**
     * Start a notification procedure
     * 
     * @param instanceNumber The cyclic procedure procedure instance number
     * @param listOfEvents The list of requested functional resource events to be notified

     * @return The result of the start
     * @throws Exception
     */
    public CstsResult startNotification(long instanceNumber,
                                        ListOfParameters listOfEvents) throws Exception
    {
        System.out.println("MdCstsSiUser#startNotification() begin");

        CstsResult ret = CstsResult.FAILURE;
        this.retLock.lock();

        try
        {
            // check whether this service instance is bound
            if (this.serviceInstance.getStatus() == ServiceStatus.BOUND)
            {
                // get the cyclic report procedure from the instance number
                NotificationUser notification = getProcedure(NotificationUser.class, instanceNumber);
                if (this.notificationProcedures.get(notification) == ProcedureState.INACTIVE)
                {
                    // indicate transition from the INACTIVE state to the ACTIVE
                    // state
                    updateProcedureState(notification.getProcedureInstanceIdentifier(), ProcedureState.START_PENDING);

                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the start operation
                    ret = notification.requestNotification(listOfEvents);
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the start
                        // operation return
                        ret = waitForReturnOrAbort(notification,
                                                   "notification procedure (" + instanceNumber
                                                   + ") is still not active",
                                                   ProcedureState.INACTIVE);
                    }
                }
                else
                {
                    // the procedure is already active and cannot be started
                    setDiagnostic(ProcedureState.ACTIVE);
                }
            }
            else
            {
                // the service instance is unbound so the cyclic report
                // procedure cannot be started
                setDiagnostic(ServiceStatus.UNBOUND);
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUser#startNotification() end");

        return ret;
    }

    /**
     * Stop a notification procedure
     * 
     * @param instanceNumber The cyclic procedure procedure instance number
     * @return The result of the start
     * @throws Exception
     */
    public CstsResult stopNotification(long instanceNumber) throws Exception
    {
        System.out.println("MdCstsSiUser#stopNotification() begin");

        CstsResult ret = CstsResult.FAILURE;
        this.retLock.lock();

        try
        {
            // check whether this service instance is bound
            if (this.serviceInstance.getStatus() == ServiceStatus.BOUND)
            {
                // get the notification procedure from the instance number
                NotificationUser notification = getProcedure(NotificationUser.class, instanceNumber);
                if (this.notificationProcedures.get(notification) == ProcedureState.ACTIVE)
                {
                    // indicate transition from the ACTIVE state to the INACTIVE
                    // state
                    updateProcedureState(notification.getProcedureInstanceIdentifier(), ProcedureState.STOP_PENDING);

                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the stop operation
                    ret = notification.endNotification();
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the stop
                        // operation return
                        ret = waitForReturnOrAbort(notification,
                                                   "notification procedure (" + instanceNumber
                                                   + ") is still active",
                                                   ProcedureState.ACTIVE);
                    }
                }
                else
                {
                    // the procedure is inactive and cannot be stopped
                    setDiagnostic(ProcedureState.INACTIVE);
                }
            }
            else
            {
                // the service instance is unbound so the cyclic report
                // procedure cannot be started
                setDiagnostic(ServiceStatus.UNBOUND);
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUser#stopNotification() end");

        return ret;
    }

    /**
     * Query parameters
     * 
     * @param instanceNumber The cyclic procedure procedure instance number
     * @param queryInformation The list of requested functional resource parameter to be retrieved

     * @return The result of the start
     * @throws Exception
     */
    public CstsResult queryInformation(long instanceNumber,
                                       ListOfParameters listOfParameters) throws Exception
    {
        System.out.println("MdCstsSiUser#queryInformation() begin");

        CstsResult ret = CstsResult.FAILURE;
        this.retLock.lock();

        try
        {
            // check whether this service instance is bound
            if (this.serviceInstance.getStatus() == ServiceStatus.BOUND)
            {
                // get the information query procedure from the instance number
                InformationQueryUser informationQuery = getProcedure(InformationQueryUser.class, instanceNumber);

                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the operation invocation
                    ret = informationQuery.queryInformation(listOfParameters);
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the start
                        // operation return
                        ret = waitForReturnOrAbort(informationQuery,
                                                   "information query procedure (" + instanceNumber
                                                   + ") still not received new parameters",
                                                   null);
                    }
            }
            else
            {
                // the service instance is unbound so the cyclic report
                // procedure cannot be started
                setDiagnostic(ServiceStatus.UNBOUND);
            }
        }
        finally
        {
            this.retLock.unlock();
        }

        System.out.println("MdCstsSiUser#queryInformation() end");

        return ret;
    }

    protected CstsResult waitForReturnOrAbort(IProcedure proc,
                                              String waitMsg,
                                              ProcedureState initialProcState) throws InterruptedException
    {
        while (this.operationResult == OperationResult.INVALID)
        {
            System.out.println(waitMsg);

            boolean signalled = this.retCond.await(this.serviceInstance.getReturnTimeout(), TimeUnit.SECONDS);
            if (signalled == false)
            {
                // return timeout, the operation return has not been received
                break;
            }
        }

        CstsResult ret = convert(this.operationResult);
        if (ret == CstsResult.FAILURE)
        {
            System.out.println("DIAGNOSTIC: " + getDiagnostic());
            if (proc instanceof CyclicReportUser || proc instanceof NotificationUser)
            {
                // NEGATIVE operation result or return timeout, set
                // procedure's state to the state prior to the last invocation
                updateProcedureState(proc.getProcedureInstanceIdentifier(), initialProcState);
            }
        }

        return ret;
    }

    /**
     * Get procedure type from a Java class
     * 
     * @param clazz The procedure Java class
     * @return the procedure type
     * @throws Exception if the Java class is not a supported procedure class
     */
    private static <P extends IProcedure> ProcedureType getProcedureTypeFromClass(Class<P> clazz) throws Exception
    {
        ProcedureType ret;

        if (clazz.equals(CyclicReportUser.class))
        {
            ret = ProcedureType.of(OIDs.cyclicReport);
        }
        else if (clazz.equals(InformationQueryUser.class))
        {
            ret = ProcedureType.of(OIDs.informationQuery);
        }
        else if (clazz.equals(NotificationUser.class))
        {
            ret = ProcedureType.of(OIDs.notification);
        }
        else
        {
            throw new Exception("Unsupported procedure class " + clazz.getSimpleName());
        }

        return ret;
    }

    /**
     * Get a procedure instance from procedure class, role and instance number
     * 
     * @param clazz The Java class
     * @param procedureRole The procedure role
     * @param instanceNumber The procedure instance number
     * 
     * @return The procedure instance or null if not created(configured)
     */
    private <P extends IProcedure> P getProcedure(Class<P> clazz, ProcedureRole procedureRole, long instanceNumber)
    {
        P ret = null;
        try
        {
            ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier
                    .of(getProcedureTypeFromClass(clazz), procedureRole, new Long(instanceNumber).intValue());

            ret = clazz.cast(this.serviceInstance.getProcedure(piid));
        }
        catch (Exception e)
        {
            // there is not configured such procedure
        }

        return ret;
    }

    /**
     * Get a procedure instance from procedure class and instance number
     * 
     * @param clazz The Java class
     * @param instanceNumber The procedure instance number
     * 
     * @return The procedure instance
     * 
     * @throws Exception If the procedure does not exist (not configured)
     */   
    private <P extends IProcedure> P getProcedure(Class<P> clazz, long instanceNumber) throws Exception
    {
        System.out.println("MdCstsSiUser#getProcedure() begin");

        // first try to find procedure w/ the PRIME role
        P ret = getProcedure(clazz, ProcedureRole.PRIME, instanceNumber);
        if (ret == null)
        {
            // second try to find procedure w/ the SECONDARY role
            ret = getProcedure(clazz, ProcedureRole.SECONDARY, instanceNumber);
        }

        if (ret == null)
        {
            throw new Exception("procedure " + clazz.getSimpleName() + " /w instance number " + instanceNumber
                                + " is not configured");
        }

        System.out.println("MdCstsSiUser#getProcedure() end");
        return ret;
    }

    private boolean isAnyStartableProcedureActive()
    {
        boolean ret = false;

        if (Collections.frequency(this.cyclicReportProcedures.values(), ProcedureState.ACTIVE) == 0)
        {
            if (Collections.frequency(this.notificationProcedures.values(), ProcedureState.ACTIVE) > 0)
            {
                // there is at least one active notification procedure
                ret = true;
            }
        }
        else
        {
            // there is at least one active cyclic report procedure
            ret = true;
        }
        return ret;
    }
}
