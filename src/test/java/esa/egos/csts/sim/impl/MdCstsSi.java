package esa.egos.csts.sim.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.impl.FunctionalResourceEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReport;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
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
            if (pii.getType().getOid().equals(OIDs.ocoCyclicReport))
            {
                C cyclicReport = createOnChangeCyclicReportProcedure();
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

    protected abstract C createOnChangeCyclicReportProcedure() throws ApiException;

    protected abstract N createNotificationProcedure() throws ApiException;

    protected void addProcedure(IProcedure procedure, ProcedureInstanceIdentifier pii, K config) throws ApiException
    {
        procedure.setRole(pii.getRole(), new Long(pii.getInstanceNumber()).intValue());
        this.serviceInstance.addProcedure(procedure);

        System.out.println("added procedure " + procedure.getProcedureInstanceIdentifier());
    }

    protected ProcedureInstanceIdentifier getPrimeProcedureIdentifier()
    {
        return this.serviceInstance.getPrimeProcedure().getProcedureInstanceIdentifier();
    }

    public void destroy() throws ApiException
    {
        this.api.destroyServiceInstance(this.serviceInstance);
    }

    public abstract FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> getParameter(Name name);
    public abstract FunctionalResourceEvent<?, FunctionalResourceValue<?>> getEvent(Name name);
    public abstract List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(Label label);
    public abstract List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(FunctionalResourceName frn);
    public abstract List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(FunctionalResourceType frt);
    public abstract List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureInstanceIdentifier piid);
    public abstract List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureType procTyp);

    public ICstsValue getParameterValue(Name name) throws IllegalAccessException
    {
        FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter = getParameter(name);
        if (parameter == null)
        {
            throw new IllegalArgumentException("Parameter " + name + " is not available in MD collection");
        }
        return parameter.getCstsValue();
    }

    public ICstsValue getEventValue(Name name) throws IllegalAccessException
    {
        FunctionalResourceEvent<?, FunctionalResourceValue<?>> event = getEvent(name);
        if (event == null)
        {
            throw new IllegalArgumentException("Event " + name + " is not available in MD collection");
        }
        return event.getCstsValue();
    }

    public Map<Long, ICstsValue> getParameterValues(Label label) throws IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(label);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Label " + label + " is not available in MD collection");
        }

        Map<Long, ICstsValue> res = new HashMap<Long, ICstsValue>();
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> p : parameters)
        {
            res.put(Long.valueOf(p.getName().getFunctionalResourceName().getInstanceNumber()), p.getCstsValue());
        }
        return res;
    }

    public Map<Name, ICstsValue> getParameterValues(FunctionalResourceName frn) throws IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(frn);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource name " + frn + " is not available in MD collection");
        }

        Map<Name, ICstsValue> res = new HashMap<Name, ICstsValue>(parameters.size());
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> p : parameters) {
            res.put(p.getName(), p.getCstsValue());
        }
        return res;
    }

    public Map<Long, Map<Label, ICstsValue>> getParameterValues(FunctionalResourceType frt) throws IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(frt);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource type " + frt + " is not available in MD collection");
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
            throw new IllegalArgumentException("Procedure instance identifier " + piid + " is not available in MD collection");
        }
        
        Map<Name, ICstsValue> ret = new HashMap<Name, ICstsValue>(parameters.size());
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> p : parameters) {
            ret.put(p.getName(), p.getCstsValue());
        }
        return ret;
    }

    public Map<ProcedureRole, Map<Long, Map<Label, ICstsValue>>> getParameterValues(ProcedureType procTyp) throws IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(procTyp);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Procedure type " + procTyp + " is not available in MD collection");
        }

        Map<ProcedureRole, Map<Long, Map<Label, ICstsValue>>> ret = new HashMap<ProcedureRole, Map<Long, Map<Label, ICstsValue>>>();
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> p : parameters)
        {
            ProcedureInstanceIdentifier piid = p.getName().getProcedureInstanceIdentifier();

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

            Long instNum = Long.valueOf(piid.getInstanceNumber());
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

    public boolean isBound() {
        return this.serviceInstance.isBound();
    }

}
