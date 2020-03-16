package esa.egos.csts.sim.impl.prv;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportProvider;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.InformationQueryProvider;
import esa.egos.csts.api.procedures.notification.NotificationProvider;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.sim.impl.MdCstsSi;
import esa.egos.csts.sim.impl.MdCstsSiConfig;

/**
 * MD-CSTS Provider service instance (SI) implementation for testing
 */
public class MdCstsSiProvider extends MdCstsSi<InformationQueryProvider, CyclicReportProvider, NotificationProvider>
{
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
                                MdCstsSiConfig config) throws ApiException
    {
        super.addProcedure(procedure, pii, config);

        if (procedure instanceof CyclicReportProvider) {
            ((CyclicReportProvider) procedure).getMinimumAllowedDeliveryCycle()
                    .initializeValue(((MdCstsSiProviderConfig) config).getMinimumAllowedDeliveryCycle());
    		updateDefaultLabelList(((CyclicReportProvider) procedure).getLabelLists(),
    				((MdCstsSiProviderConfig) config).getDefaultLabelList());
    		((CyclicReportProvider) procedure).getLabelLists().setConfigured(true);
        }
        else if (procedure instanceof InformationQueryProvider) {
    		updateDefaultLabelList(((InformationQueryProvider) procedure).getLabelLists(),
    				((MdCstsSiProviderConfig) config).getDefaultLabelList());
    		((InformationQueryProvider) procedure).getLabelLists().setConfigured(true);
        }
    }

    public void setDefaultLabelList(ProcedureInstanceIdentifier piid, List<Label> defaultLabeList) throws Exception {
    	IProcedure procedure = this.serviceInstance.getProcedure(piid);
    	if (procedure instanceof CyclicReportProvider) {
    		updateDefaultLabelList(((CyclicReportProvider) procedure).getLabelLists(), defaultLabeList);
    		((CyclicReportProvider) procedure).getLabelLists().setConfigured(true);
    		
    	}
    	else if (procedure instanceof InformationQueryProvider) {
    		updateDefaultLabelList(((InformationQueryProvider) procedure).getLabelLists(), defaultLabeList);
    		((InformationQueryProvider) procedure).getLabelLists().setConfigured(true);
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
    protected CyclicReportProvider createCyclicReportProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(CyclicReportProvider.class);
    }

    @Override
    protected NotificationProvider createNotificationProcedure() throws ApiException
    {
        return this.serviceInstance.createProcedure(NotificationProvider.class);
    }

    public void setMdCollection(MdCollection mdCollection)
    {
        mdCollection.getParameters().stream().forEach(this.serviceInstance::addExternalParameter);
        mdCollection.getEvents().stream().forEach(this.serviceInstance::addExternalEvent);
    }

}