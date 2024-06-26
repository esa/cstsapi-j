package esa.egos.csts.sim.impl.prv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.events.impl.FunctionalResourceEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
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
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.procedures.informationquery.InformationQueryProvider;
import esa.egos.csts.api.procedures.notification.NotificationProvider;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.states.service.ServiceStatus;
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
    
    /** lock protecting this service instance */
    private final Lock stateLock;

    /** condition variable for signaling an operation return or abort */
    private final Condition stateCond;

    private final Logger LOG = Logger.getLogger(getClass().getName());
    
    public MdCstsSiProvider(ICstsApi api,
                            MdCstsSiProviderConfig config) throws ApiException
    {
        super(api, config,0);
        this.stateLock = new ReentrantLock();
        this.stateCond = stateLock.newCondition();

        LOG.info("MdCstsSiProvider#MdCstsSiProvider() begin");
        LOG.info("MdCstsSiProvider#MdCstsSiProvider() end");
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
    	IProcedure procedure = this.getApiSi().getProcedure(piid);
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
        LOG.info("MdCstsSiProvider#informOpInvocation() begin");
        this.stateLock.lock();

        LOG.info("Operation invocation (state: " + getApiSi().getStatus() + "):  " + operation);
        this.stateCond.signalAll();
        
        this.stateLock.unlock();
        LOG.info("MdCstsSiProvider#informOpInvocation() end");
        
    }

    @Override
    public void informOpAcknowledgement(IAcknowledgedOperation operation)
    {
        LOG.info("MdCstsSiProvider#informOpAcknowledgement() begin");

        LOG.info("Operation acknowledgement: " + operation);

        LOG.info("MdCstsSiProvider#informOpAcknowledgement() end");
    }

    @Override
    public void informOpReturn(IConfirmedOperation operation)
    {
        LOG.info("MdCstsSiProvider#informOpReturn() begin");

        LOG.info("Operation return:  " + operation);

        LOG.info("MdCstsSiProvider#informOpReturn() end");
    }

    @Override
    public void protocolAbort()
    {
        LOG.info("MdCstsSiProvider#protocolAbort() begin");
        this.stateLock.lock();

        LOG.info("CSTS provider service instance received protocol abort");
        this.stateCond.signalAll();

        this.stateLock.unlock();
        LOG.info("MdCstsSiProvider#informOpAcknowledgement() end");
    }

    @Override
    protected InformationQueryProvider createInformationQueryProcedure() throws ApiException
    {
        return this.getApiSi().createProcedure(InformationQueryProvider.class);
    }

    @Override
    protected OnChangeCyclicReportProvider createOnChangeCyclicReportProcedure() throws ApiException
    {
        return this.getApiSi().createProcedure(OnChangeCyclicReportProvider.class);
    }

    @Override
    protected NotificationProvider createNotificationProcedure() throws ApiException
    {
        return this.getApiSi().createProcedure(NotificationProvider.class);
    }

    private void clearFunctionaResources()
    {
        if (this.getApiSi().isBound())
        {
            throw new UnsupportedOperationException("Cannot change functional resource types in the bound state");
        }

        // remove all external parameters
        List<IParameter> parameters = this.getApiSi().gatherParameters();
        parameters.forEach(p -> this.getApiSi().removeExternalParameter(p));

        // remove all external events
        List<IEvent> events = this.getApiSi().gatherEvents();
        events.forEach(e -> this.getApiSi().removeExternalEvent(e));
    }

    public void setMdCollection(MdCollection mdCollection)
    {
        clearFunctionaResources();
        mdCollection.getParameters().stream().forEach(this.getApiSi()::addExternalParameter);
        mdCollection.getEvents().stream().forEach(this.getApiSi()::addExternalEvent);
    }

    public void setFunctionalResources(FunctionalResourceType... functionalResourceTypes) throws Exception
    {
        setMdCollection(MdCollection.createCollection(functionalResourceTypes));
    }

    public void setParameterValue(Name name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter = getParameter(name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
        }
        parameter.setCstsValue(value);
    }

    public void setEventValue(Name name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        FunctionalResourceEvent<?, FunctionalResourceValue<?>> event = getEvent(name);
        if (event == null)
        {
            throw new NullPointerException("Event " + name + " is not available in MD collection");
        }
        event.setCstsValue(value);
    }

    public void setParameterValue(Label label, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> parameters = getParameters(label);
        if (parameters.isEmpty())
        {
            throw new IllegalArgumentException("Label " + label + " is not available in MD collection");
        }
        for (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> parameter : parameters) {
            parameter.setCstsValue(value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> getParameter(Name name)
    {
        Optional<IParameter> op = this.getApiSi().gatherParameters().stream()
                .filter(p -> (p instanceof FunctionalResourceParameterEx && p.getName().equals(name))).findFirst();

        if (!op.isPresent())
        {
            throw new IllegalArgumentException("Parameter " + name + " is not available in "
                                               + this.getApiSi().getServiceInstanceIdentifier());
        }

        return (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>) op.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public FunctionalResourceEvent<?, FunctionalResourceValue<?>> getEvent(Name name)
    {
        Optional<IEvent> op = this.getApiSi().gatherEvents().stream()
                .filter(e -> (e instanceof FunctionalResourceEvent && e.getName().equals(name))).findFirst();

        if (!op.isPresent())
        {
            throw new IllegalArgumentException("Event " + name + " is not availeble in "
                                               + this.getApiSi().getServiceInstanceIdentifier());
        }

        return (FunctionalResourceEvent<?, FunctionalResourceValue<?>>) op.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(Label label)
    {
        return this.getApiSi().gatherParameters().stream()
                .filter(p -> (p instanceof FunctionalResourceParameterEx && p.getLabel().equals(label)))
                .map(p -> (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>)p)
                .collect(Collectors.toList());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(FunctionalResourceName frn)
    {
        return this.getApiSi().gatherParameters().stream()
                .filter(p -> (p instanceof FunctionalResourceParameterEx && p.getName().getFunctionalResourceName().equals(frn)))
                .map(p -> (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>)p)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(FunctionalResourceType frt)
    {
        return this.getApiSi().gatherParameters().stream()
                .filter(p -> (p instanceof FunctionalResourceParameterEx && p.getLabel().getFunctionalResourceType().equals(frt)))
                .map(p -> (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>)p)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureInstanceIdentifier piid)
    {
        return this.getApiSi().gatherParameters().stream()
                .filter(p -> (p instanceof FunctionalResourceParameterEx && p.getName().getProcedureInstanceIdentifier().equals(piid)))
                .map(p -> (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>)p)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>> getParameters(ProcedureType procTyp)
    {
        return this.getApiSi().gatherParameters().stream()
                .filter(p -> (p instanceof FunctionalResourceParameterEx && p.getLabel().getProcedureType().equals(procTyp)))
                .map(p -> (FunctionalResourceParameterEx<?, FunctionalResourceValue<?>>)p)
                .collect(Collectors.toList());
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
    		LOG.info("Provider SI has desired state, no wait for " + desiredState + " " + getApiSi().getServiceInstanceIdentifier());
    		return desiredState;
    	}
    	
    	this.stateLock.lock();
    	try {
    		signaled = this.stateCond.await(msTimeout, TimeUnit.MILLISECONDS);
    	}
    	catch(InterruptedException ie) {
    		// ignore 
    	} finally {
    		this.stateLock.unlock();
    	}
    	
    	if(desiredState == getApiSi().getStatus()) {
    		LOG.info("Provider SI has desired state " + desiredState + ", state change signaled: " + signaled + " " + getApiSi().getServiceInstanceIdentifier());
    	} else {
    		LOG.severe("Provider SI does not have desired state " + desiredState + ", state change signaled: " + signaled + " " + getApiSi().getServiceInstanceIdentifier());
    	}
    	
    	return getApiSi().getStatus();
    }    

}
