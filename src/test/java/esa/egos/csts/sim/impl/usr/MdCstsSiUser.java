package esa.egos.csts.sim.impl.usr;

import java.util.List;
import java.util.concurrent.TimeUnit;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
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
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportUser;
import esa.egos.csts.sim.impl.MdCstsSiConfig;

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

    // getter only for test purpose to simulate a peer abort
    public IServiceInstance getServiceInstance() 
    {
    	return serviceInstance;
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
    protected OnChangeCyclicReportUser createOnChangeCyclicReportProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(OnChangeCyclicReportUser.class);
    }

    /**
     * Create NotificationUser procedure
     * 
     * @return NotificationUser
     */
    @Override
    protected NotificationUser createNotificationProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(NotificationUser.class);
    }

    /**
     * Initiates a BIND to the remote peer of this SI and blocks until the BIND
     * return is received or a return timeout occurs.
     * 
     * @return The result of the BIND operation of the association procedure
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
                                           "service instance is still not bound");
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
     * @return The result of the UNIND operation of the association procedure
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
                                               "service instance is still bound");
                }
            }
            else
            {
                // there is still active at least one procedure
                ret = CstsResult.FAILURE;
                setDiagnostic("ACTIVE");
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
     * Invokes PEER-ABORT on the remote peer of this SI.
     * 
     * @return The result of the PEER-ABORT operation of the association procedure
     * @throws InterruptedException
     */
    public CstsResult peerAbort() throws InterruptedException
    {
        System.out.println("MdCstsSiUser#peerAbort() begin");

        CstsResult ret = this.associationProcedure.abort(PeerAbortDiagnostics.OPERATIONAL_REQUIREMENT);

        System.out.println("MdCstsSiUser#peerAbort() end");

        return ret;
    }

    /**
     * Start an on change cyclic report procedure
     * 
     * @param piid The cyclic procedure procedure instance identifier
     * @param listOfParameters The list of requested functional resource
     *            parameters to be cyclically reported
     * @param deliveryCycle The delivery cycle in milliseconds
     * @param onChange The on change flag, if true only changed parameters are in the report,
     *  if false all parameters
     * @return The result of the START operation of the cyclic procedure procedure
     * @throws Exception
     */
    public CstsResult startCyclicReport(ProcedureInstanceIdentifier piid,
                                        ListOfParameters listOfParameters,
                                        long deliveryCycle,
                                        boolean onChange) throws Exception
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
                OnChangeCyclicReportUser onChangeCyclicReport = (OnChangeCyclicReportUser) this.serviceInstance.getProcedure(piid);
                if (!onChangeCyclicReport.isActive())
                {
                    // reset the operation result and diagnostic
                    resetOperationResult();
                    resetCounter(piid, this.parameterUpdateCounters);
                    // initiate the start operation
                    ret = onChangeCyclicReport.requestCyclicReport(deliveryCycle, onChange, listOfParameters);
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the start
                        // operation return
                        ret = waitForReturnOrAbort(onChangeCyclicReport,
                                                   "on change cyclic report procedure (" + piid.getInstanceNumber()
                                                   + ") is still not active");
                    }
                }
                else
                {
                    // the procedure is already active and cannot be started
                    setDiagnostic("ACTIVE");
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
     * Start an on change cyclic report procedure
     * 
     * @param piid The cyclic procedure procedure instance identifier
     * @param listOfParameters The list of requested functional resource
     *            parameters to be cyclically reported
     * @param deliveryCycle The delivery cycle in milliseconds
     * @return The result of the START operation of the cyclic procedure procedure
     * @throws Exception
     */
    public CstsResult startCyclicReport(ProcedureInstanceIdentifier piid,
                                        ListOfParameters listOfParameters,
                                        long deliveryCycle) throws Exception
    {
        System.out.println("MdCstsSiUser#startCyclicReport() begin");

        CstsResult ret = startCyclicReport(piid, listOfParameters, deliveryCycle, false);

        System.out.println("MdCstsSiUser#startCyclicReport() end");

        return ret;
    }

    /**
     * Start an on change cyclic report procedure
     * 
     * @param instanceNumber The cyclic procedure procedure instance number
     * @param listOfParameters The list of requested functional resource
     *            parameters to be cyclically reported
     * @param deliveryCycle The delivery cycle in milliseconds
     * @param onChange The on change flag, if true only changed parameters are in the report,
     *  if false all parameters
     * @return The result of the START operation of the cyclic procedure procedure
     * @throws Exception
     */
    public CstsResult startCyclicReport(long instanceNumber,
                                        ListOfParameters listOfParameters,
                                        long deliveryCycle,
                                        boolean onChange) throws Exception
    {
        System.out.println("MdCstsSiUser#startCyclicReport() begin");

        OnChangeCyclicReportUser onChangeCyclicReport = getProcedure(OnChangeCyclicReportUser.class, instanceNumber);
        CstsResult ret = startCyclicReport(onChangeCyclicReport.getProcedureInstanceIdentifier(),
                                           listOfParameters,
                                           deliveryCycle,
                                           onChange);

        System.out.println("MdCstsSiUser#startCyclicReport() end");

        return ret;
    }

    /**
     * Start an on change cyclic report procedure
     * 
     * @param instanceNumber The cyclic procedure procedure instance number
     * @param listOfParameters The list of requested functional resource
     *            parameters to be cyclically reported
     * @param deliveryCycle The delivery cycle in milliseconds
     * @return The result of the START operation of the cyclic procedure procedure
     * @throws Exception
     */
    public CstsResult startCyclicReport(long instanceNumber,
                                        ListOfParameters listOfParameters,
                                        long deliveryCycle) throws Exception
    {
        System.out.println("MdCstsSiUser#startCyclicReport() begin");

        OnChangeCyclicReportUser onChangeCyclicReport = getProcedure(OnChangeCyclicReportUser.class, instanceNumber);
        CstsResult ret = startCyclicReport(onChangeCyclicReport.getProcedureInstanceIdentifier(), listOfParameters, deliveryCycle);

        System.out.println("MdCstsSiUser#startCyclicReport() end");

        return ret;
    }

    /**
     * Stop an on change cyclic report procedure
     * 
     * @param piid The cyclic procedure instance identifier
     * @return The result of the STOP operation of the cyclic procedure procedure
     * @throws Exception
     */
    public CstsResult stopCyclicReport(ProcedureInstanceIdentifier piid) throws Exception
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
                CyclicReportUser cyclicReport = (CyclicReportUser) this.serviceInstance.getProcedure(piid);
                if (cyclicReport.isActive())
                {
                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the stop operation
                    ret = cyclicReport.endCyclicReport();
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the stop
                        // operation return
                        ret = waitForReturnOrAbort(cyclicReport,
                                                   "cyclic report procedure (" + piid.getInstanceNumber()
                                                   + ") is still active");
                    }
                }
                else
                {
                    // the procedure is inactive and cannot be stopped
                    setDiagnostic("INACTIVE");
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
     * Stop an on change cyclic report procedure
     * 
     * @param instanceNumber The cyclic procedure instance number
     * @return The result of the STOP operation of the cyclic procedure procedure
     * @throws Exception
     */
    public CstsResult stopCyclicReport(long instanceNumber) throws Exception
    {
        System.out.println("MdCstsSiUser#stopCyclicReport() begin");

        CyclicReportUser cyclicReport = getProcedure(OnChangeCyclicReportUser.class, instanceNumber);
        CstsResult ret = stopCyclicReport(cyclicReport.getProcedureInstanceIdentifier());

        System.out.println("MdCstsSiUser#stopCyclicReport() end");

        return ret;
    }

    /**
     * Start a notification procedure
     * 
     * @param piid The notification procedure instance identifier
     * @param listOfEvents The list of requested functional resource events to be notified

     * @return The result of the START of the notification procedure
     * @throws Exception
     */
    public CstsResult startNotification(ProcedureInstanceIdentifier piid,
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
                NotificationUser notification = (NotificationUser) this.serviceInstance.getProcedure(piid);
                if (!notification.isActive())
                {
                    // reset the operation result and diagnostic
                    resetOperationResult();
                    resetCounter(piid, this.eventUpdateCounters);
                    // initiate the start operation
                    ret = notification.requestNotification(listOfEvents);
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the start
                        // operation return
                        ret = waitForReturnOrAbort(notification,
                                                   "notification procedure (" + piid.getInstanceNumber()
                                                   + ") is still not active");
                    }
                }
                else
                {
                    // the procedure is already active and cannot be started
                    setDiagnostic("ACTIVE");
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
     * Start a notification procedure
     * 
     * @param instanceNumber The notification procedure instance number
     * @param listOfEvents The list of requested functional resource events to be notified

     * @return The result of the START of the notification procedure
     * @throws Exception
     */
    public CstsResult startNotification(long instanceNumber,
                                        ListOfParameters listOfEvents) throws Exception
    {
        System.out.println("MdCstsSiUser#startNotification() begin");

        NotificationUser notification = getProcedure(NotificationUser.class, instanceNumber);
        CstsResult ret = startNotification(notification.getProcedureInstanceIdentifier(), listOfEvents);

        System.out.println("MdCstsSiUser#startNotification() end");

        return ret;
    }

    /**
     * Stop a notification procedure
     * 
     * @param piid The notification procedure instance identifier
     * @return The result of the STOP operation of the notification procedure 
     * @throws Exception
     */
    public CstsResult stopNotification(ProcedureInstanceIdentifier piid) throws Exception
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
                NotificationUser notification = (NotificationUser) this.serviceInstance.getProcedure(piid);
                if (notification.isActive())
                {
                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the stop operation
                    ret = notification.endNotification();
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the stop
                        // operation return
                        ret = waitForReturnOrAbort(notification,
                                                   "notification procedure (" + piid.getInstanceNumber()
                                                   + ") is still active");
                    }
                }
                else
                {
                    // the procedure is inactive and cannot be stopped
                    setDiagnostic("INACTIVE");
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
     * Stop a notification procedure
     * 
     * @param instanceNumber The notification procedure instance number
     * @return The result of the STOP operation of the notification procedure 
     * @throws Exception
     */
    public CstsResult stopNotification(long instanceNumber) throws Exception
    {
        System.out.println("MdCstsSiUser#stopNotification() begin");

        NotificationUser notification = getProcedure(NotificationUser.class, instanceNumber);
        CstsResult ret = stopNotification(notification.getProcedureInstanceIdentifier());

        System.out.println("MdCstsSiUser#stopNotification() end");

        return ret;
    }

    /**
     * Query parameters
     * 
     * @param piid The procedure instance identifier
     * @param listOfParameters The list of requested functional resource parameters to be retrieved
     *
     * @return The result of the GET operation of the query information procedure
     * @throws Exception
     */
    public CstsResult queryInformation(ProcedureInstanceIdentifier piid,
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
                InformationQueryUser informationQuery = (InformationQueryUser) this.serviceInstance.getProcedure(piid);

                    // reset the operation result and diagnostic
                    resetOperationResult();
                    // initiate the operation invocation
                    ret = informationQuery.queryInformation(listOfParameters);
                    if (ret == CstsResult.SUCCESS)
                    {
                        // invocation initiation succeeded, wait for the start
                        // operation return
                        ret = waitForReturnOrAbort(informationQuery,
                                                   "information query procedure (" + piid.getInstanceNumber()
                                                   + ") still not received new parameters");
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

    /**
     * Query parameters
     * 
     * @param instanceNumber The procedure instance number
     * @param listOfParameters The list of requested functional resource parameters to be retrieved
     *
     * @return The result of the GET operation of the query information procedure
     * @throws Exception
     */
    public CstsResult queryInformation(long instanceNumber,
                                       ListOfParameters listOfParameters) throws Exception
    {
        System.out.println("MdCstsSiUser#queryInformation() begin");

        InformationQueryUser informationQuery = getProcedure(InformationQueryUser.class, instanceNumber);
        CstsResult ret = queryInformation(informationQuery.getProcedureInstanceIdentifier(), listOfParameters);

        System.out.println("MdCstsSiUser#queryInformation() end");

        return ret;
    }

    protected CstsResult waitForReturnOrAbort(IProcedure proc,
                                              String waitMsg) throws InterruptedException
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

        if (clazz.equals(OnChangeCyclicReportUser.class))
        {
            ret = ProcedureType.of(OIDs.ocoCyclicReport);
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

    private boolean isAnyStartableProcedureActive() {
        boolean ret = false;

        List<IProcedure> procedures = ((IServiceInstanceInternal) this.serviceInstance).getProcedures();
        for (IProcedure procedure : procedures) {
            if (procedure instanceof OnChangeCyclicReportUser) {
                if (((OnChangeCyclicReportUser) procedure).isActive()) {
                    ret = true;
                    break;
                }
            } else if (procedure instanceof NotificationUser) {
                if (((NotificationUser) procedure).isActive()) {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

}
