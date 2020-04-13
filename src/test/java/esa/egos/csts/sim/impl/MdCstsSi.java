package esa.egos.csts.sim.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReport;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.IInformationQuery;
import esa.egos.csts.api.procedures.notification.INotification;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

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

    // called by Provider (from set/getParameterValue, User calls MdCstsSiUserInform#getParameter())
    public FunctionalResourceParameterEx<?> getParameter(Name name)
    {
        return (FunctionalResourceParameterEx<?>) this.serviceInstance.gatherParameters().stream()
                .filter(p -> (p.getName().equals(name) && p instanceof FunctionalResourceParameterEx))
                .findFirst().get();
    }

    public List<FunctionalResourceParameterEx<?>> getParameters(Label label)
    {
        List<FunctionalResourceParameterEx<?>> ret = new ArrayList<FunctionalResourceParameterEx<?>>();
        this.serviceInstance.gatherParameters().stream()
                .filter(p -> (p.getLabel().equals(label) && p instanceof FunctionalResourceParameterEx))
                .forEach(p -> ret.add((FunctionalResourceParameterEx<?>)p));
        return ret;
    }
    
    public List<FunctionalResourceParameterEx<?>> getParameters(FunctionalResourceName frn)
    {
        List<FunctionalResourceParameterEx<?>> ret = new ArrayList<FunctionalResourceParameterEx<?>>();
        this.serviceInstance.gatherParameters().stream()
                .filter(p -> (p instanceof FunctionalResourceParameterEx<?>
                                && p.getName().getFunctionalResourceName().equals(frn)))
                .forEach(p -> ret.add((FunctionalResourceParameterEx<?>)p));
        return ret;
    }

    public Map<Integer, Map<Label, FunctionalResourceParameterEx<?>>> getParameters(FunctionalResourceType frt)
    {
        // return list of maps, each map contains parameters for specific instance number
        Map<Integer, Map<Label, FunctionalResourceParameterEx<?>>> ret =
                new HashMap<Integer, Map<Label, FunctionalResourceParameterEx<?>>>();

        this.serviceInstance.gatherParameters().stream()
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

        return ret;
    }

    public void setParameterValue(Name name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        FunctionalResourceParameter parameter = getParameter(name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
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
        return ((FunctionalResourceParameterEx<?>) parameter).getCstsValue();
    }
    
    public void setParameterValue(Label label, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        List<FunctionalResourceParameterEx<?>> parameters = getParameters(label);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Label " + label + " is not available in MD collection");
        }
        for (FunctionalResourceParameterEx<?> parameter : parameters) {
            parameter.setCstsValue(value);
        }
    }

    public List<ICstsValue> getParameterValue(Label label) throws IllegalArgumentException, IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?>> parameters = getParameters(label);
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

    public void setParameterValue(FunctionalResourceName name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        List<FunctionalResourceParameterEx<?>> parameters = getParameters(name);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource name " + name + " is not available in MD collection");
        }
        for (FunctionalResourceParameterEx<?> parameter : parameters) {
            parameter.setCstsValue(value);
        }
    }

    public Map<Name, ICstsValue> getParameterValues(FunctionalResourceName frn) throws IllegalArgumentException, IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?>> parameters = getParameters(frn);
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
    
    public Map<Integer, Map<Label, ICstsValue>> getParameterValues(FunctionalResourceType frt) throws IllegalArgumentException, IllegalAccessException
    {
        Map<Integer, Map<Label, FunctionalResourceParameterEx<?>>> parameters = getParameters(frt);
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
