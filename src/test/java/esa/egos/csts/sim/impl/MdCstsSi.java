package esa.egos.csts.sim.impl;

import java.util.HashMap;
import java.util.Map;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReport;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.IInformationQuery;
import esa.egos.csts.api.procedures.notification.INotification;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceIdentifier;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.sim.impl.frm.FunctionalResourceParameterEx;
import esa.egos.csts.sim.impl.frm.values.ICstsValue;

/**
 * Base class for MD-CSTS service instance (SI)
 */
public abstract class MdCstsSi<K extends MdCstsSiConfig, I extends IInformationQuery, C extends ICyclicReport, N extends INotification>
                              implements IServiceInform
{
    /** The SI identifier */ 
    private static final ObjectIdentifier SIOID = ObjectIdentifier.of(1, 3, 112, 4, 4, 1, 2);

    /** CSTS API IF */
    protected final ICstsApi api;

    /** CSTS SI IF*/
    protected IServiceInstance serviceInstance;

    /** CSTS SI IF*/
    protected K config;


    public MdCstsSi(ICstsApi api, K config) throws ApiException
    {
        super();

        System.out.println("MdCstsSi#MdCstsSi() begin");

        this.api = api;
        this.config = config;

        IServiceInstanceIdentifier identifier = new ServiceInstanceIdentifier(config.getScId(),
                                                                              config.getFacilityId(),
                                                                              SIOID,
                                                                              config.getInstanceNumber());

        this.serviceInstance = api.createServiceInstance(identifier, this);
        System.out.println("created service instance " + identifier);

        createProcedures(config);

        // the application needs to make sure that it chooses valid values from
        // the proxy configuration
        this.serviceInstance.setPeerIdentifier(config.getPeerIdentifier());
        this.serviceInstance.setResponderPortIdentifier(config.getResponderPortIdentifier());
        this.serviceInstance.configure();

        System.out.println("MdCstsSi#MdCstsSi() end");
    }

    protected void createProcedures(K config) throws ApiException
    {
        for (ProcedureInstanceIdentifier pii : config.getProceduresIdentifiers())
        {
            if (pii.getType().getOid().equals(OIDs.cyclicReport))
            {
                C cyclicReport = createCyclicReportProcedure();
                addProcedure(cyclicReport, pii, config);
            }
            else if (pii.getType().getOid().equals(OIDs.informationQuery))
            {
                I informationQuery = createInformationQueryProcedure();
                addProcedure(informationQuery, pii, config);
            }
            else if (pii.getType().getOid().equals(OIDs.notification))
            {
                N notification = createNotificationProcedure();
                addProcedure(notification, pii, config);
            }
            else
            {
                System.err.println("ignore unsupported procedure " + pii.getType().getOid());
            }
        }
    }

    protected abstract I createInformationQueryProcedure() throws ApiException;

    protected abstract C createCyclicReportProcedure() throws ApiException;

    protected abstract N createNotificationProcedure() throws ApiException;

    protected void addProcedure(IProcedure procedure, ProcedureInstanceIdentifier pii, K config) throws ApiException
    {
        procedure.setRole(pii.getRole(), new Long(pii.getInstanceNumber()).intValue());
        this.serviceInstance.addProcedure(procedure);

        System.out.println("added procedure " + procedure.getProcedureInstanceIdentifier());
    }

    public void destroy() throws ApiException
    {
        this.api.destroyServiceInstance(this.serviceInstance);
    }

    public Map<Name, FunctionalResourceParameterEx<?>> getParameters()
    {
        Map<Name, FunctionalResourceParameterEx<?>> ret = new HashMap<Name, FunctionalResourceParameterEx<?>>();
        this.serviceInstance.gatherParameters().stream().filter(p -> (p instanceof FunctionalResourceParameterEx))
                .forEach(p -> ret.put(p.getName(), (FunctionalResourceParameterEx<?>) p));
        return ret;
    }

    @SuppressWarnings("rawtypes")
    public FunctionalResourceParameterEx<?> getParameter(Name name)
    {
        return (FunctionalResourceParameterEx) this.serviceInstance.gatherParameters().stream()
                .filter(p -> (p.getName().equals(name) && p instanceof FunctionalResourceParameterEx)).findFirst()
                .get();
    }

    public void setParameterValue(Name name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        FunctionalResourceParameter parameter = getParameter(name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
        }

        if (!(parameter instanceof FunctionalResourceParameterEx<?>))
        {
            throw new UnsupportedOperationException("Parameter " + name + " is not an instance of FunctionalResourceParameterEx<?>");
        }

        ((FunctionalResourceParameterEx<?>) parameter).setCstsValue(value);
    }

    public ICstsValue getParameterValue(Name name) throws IllegalArgumentException, IllegalAccessException
    {
        FunctionalResourceParameter parameter = getParameter(name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
        }

        if (!(parameter instanceof FunctionalResourceParameterEx<?>))
        {
            throw new UnsupportedOperationException("Parameter " + name + " is not an instance of FunctionalResourceParameterEx<?>");
        }

        return ((FunctionalResourceParameterEx<?>) parameter).getCstsValue();
    }

}
