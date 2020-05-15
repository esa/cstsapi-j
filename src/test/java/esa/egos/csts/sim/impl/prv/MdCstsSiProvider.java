package esa.egos.csts.sim.impl.prv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.events.impl.FunctionalResourceEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportProvider;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.InformationQueryProvider;
import esa.egos.csts.api.procedures.notification.NotificationProvider;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportProvider;
import esa.egos.csts.sim.impl.MdCstsSi;

/**
 * MD-CSTS Provider service instance (SI) implementation for testing
 */
public class MdCstsSiProvider extends MdCstsSi<MdCstsSiProviderConfig, InformationQueryProvider, OnChangeCyclicReportProvider, NotificationProvider>
{

    MdCollection mdCollection;


    public MdCstsSiProvider(ICstsApi api,
                            MdCstsSiProviderConfig config) throws ApiException
    {
        super(api, config);

        System.out.println("MdCstsSiProvider#MdCstsSiProvider() begin");
        System.out.println("MdCstsSiProvider#MdCstsSiProvider() end");
    }

    @Override
    protected void addProcedure(IProcedure procedure,
                                ProcedureInstanceIdentifier pii,
                                MdCstsSiProviderConfig config) throws ApiException
    {
        super.addProcedure(procedure, pii, config);

        if (procedure instanceof OnChangeCyclicReportProvider) {
        	configureProcedure(config, (OnChangeCyclicReportProvider) procedure);
        }
        else if (procedure instanceof InformationQueryProvider) {
        	configureProcedure(config, (InformationQueryProvider) procedure);
        }
        else if (procedure instanceof NotificationProvider) {
        	configureProcedure(config, (NotificationProvider) procedure);
        }
    }

    private void configureProcedure(MdCstsSiProviderConfig config, InformationQueryProvider informationQuery) {
	    informationQuery.getLabelLists().initializeValue(config.getDefaultParameterLabelList());
    }

    private void configureProcedure(MdCstsSiProviderConfig config, CyclicReportProvider cyclicReport) {
	    cyclicReport.getMinimumAllowedDeliveryCycle().initializeValue(config.getMinimumAllowedDeliveryCycle());
	    cyclicReport.getLabelLists().initializeValue(config.getDefaultParameterLabelList());
    }

    private void configureProcedure(MdCstsSiProviderConfig config, NotificationProvider notification) {
	    notification.getLabelLists().initializeValue(config.getDefaultEventLabelList());
    }

    public void setDefaultLabelList(ProcedureInstanceIdentifier piid, List<Label> defaultLabeList) throws Exception {
    	IProcedure procedure = this.serviceInstance.getProcedure(piid);
    	if (procedure instanceof CyclicReportProvider) {
    		updateDefaultLabelList(((CyclicReportProvider) procedure).getLabelLists(), defaultLabeList);
    		
    	}
    	else if (procedure instanceof InformationQueryProvider) {
    		updateDefaultLabelList(((InformationQueryProvider) procedure).getLabelLists(), defaultLabeList);
    	}
    	else {
    		throw new Exception("procedure " + piid + " does not support the default label list feature");
    	}
    }

    private void updateDefaultLabelList(LabelLists labelLists, List<Label> defaultLabeList) {
		if (defaultLabeList != null) {
			LabelList oldDelaultLabelList = labelLists.queryDefaultList();
			if (oldDelaultLabelList != null) {
				labelLists.remove(oldDelaultLabelList);
			}
			LabelList newDefaultLabelList = new LabelList("default", true);
			newDefaultLabelList.getLabels().addAll(new ArrayList<Label>(defaultLabeList));
			labelLists.add(newDefaultLabelList);
		}
    }

    public void informOpInvocation(IOperation operation)
    {
        System.out.println("MdCstsSiProvider#informOpInvocation() begin");

        System.out.println("Operation invocation:  " + operation);

        System.out.println("MdCstsSiProvider#informOpInvocation() end");
    }

    @Override
    public void informOpAcknowledgement(IAcknowledgedOperation operation)
    {
        System.out.println("MdCstsSiProvider#informOpAcknowledgement() begin");

        System.out.println("Operation acknowledgement: " + operation);

        System.out.println("MdCstsSiProvider#informOpAcknowledgement() end");
    }

    @Override
    public void informOpReturn(IConfirmedOperation operation)
    {
        System.out.println("MdCstsSiProvider#informOpReturn() begin");

        System.out.println("Operation return:  " + operation);

        System.out.println("MdCstsSiProvider#informOpReturn() end");
    }

    @Override
    public void protocolAbort()
    {
        System.out.println("MdCstsSiProvider#protocolAbort() begin");

        System.out.println("CSTS provider service instance received protocol abort");

        System.out.println("MdCstsSiProvider#informOpAcknowledgement() end");
    }

    @Override
    protected InformationQueryProvider createInformationQueryProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(InformationQueryProvider.class);
    }

    @Override
    protected OnChangeCyclicReportProvider createOnChangeCyclicReportProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(OnChangeCyclicReportProvider.class);
    }

    @Override
    protected NotificationProvider createNotificationProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(NotificationProvider.class);
    }

    private void clearFunctionaResources()
    {
        if (this.serviceInstance.isBound())
        {
            throw new UnsupportedOperationException("Cannot change functional resource types in the bound state");
        }

        // remove all external parameters
        List<IParameter> parameters = this.serviceInstance.gatherParameters();
        parameters.forEach(p -> this.serviceInstance.removeExternalParameter(p));

        // remove all external events
        List<IEvent> events = this.serviceInstance.gatherEvents();
        events.forEach(e -> this.serviceInstance.removeExternalEvent(e));
    }

    public void setMdCollection(MdCollection mdCollection)
    {
        clearFunctionaResources();
        mdCollection.getParameters().stream().forEach(this.serviceInstance::addExternalParameter);
        mdCollection.getEvents().stream().forEach(this.serviceInstance::addExternalEvent);
    }

    public void setFunctionalResources(FunctionalResourceType... functionalResourceTypes) throws Exception
    {
        setMdCollection(MdCollection.createCollection(functionalResourceTypes));
    }

    public void setParameterValue(Name name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        FunctionalResourceParameterEx<?> parameter = getParameter(name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
        }
        parameter.setCstsValue(value);
    }

    public void setEventValue(Name name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        FunctionalResourceEvent<?> event = getEvent(name);
        if (event == null)
        {
            throw new NullPointerException("Event " + name + " is not available in MD collection");
        }
        event.setCstsValue(value);
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

    public void setParameterValue(FunctionalResourceName frn, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        List<FunctionalResourceParameterEx<?>> parameters = getParameters(frn);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Functional resource name " + frn + " is not available in MD collection");
        }

        for (FunctionalResourceParameterEx<?> parameter : parameters)
        {
            parameter.setCstsValue(value);
        }
    }

    public FunctionalResourceParameterEx<?> getParameter(Name name)
    {
        Optional<IParameter> op = this.serviceInstance.gatherParameters().stream()
                .filter(p -> (p.getName().equals(name) && p instanceof FunctionalResourceParameterEx)).findFirst();

        if (!op.isPresent())
        {
            throw new IllegalArgumentException("Parameter " + name + " is not availeble in "
                                               + this.serviceInstance.getServiceInstanceIdentifier());
        }

        return (FunctionalResourceParameterEx<?>) op.get();
    }

    public FunctionalResourceEvent<?> getEvent(Name name)
    {
        Optional<IEvent> op = this.serviceInstance.gatherEvents().stream()
                .filter(e -> (e.getName().equals(name) && e instanceof FunctionalResourceEvent)).findFirst();

        if (!op.isPresent())
        {
            throw new IllegalArgumentException("Event " + name + " is not availeble in "
                                               + this.serviceInstance.getServiceInstanceIdentifier());
        }

        return (FunctionalResourceEvent<?>) op.get();
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
}
