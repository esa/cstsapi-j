package esa.egos.csts.monitored.data.procedures;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import b1.ccsds.on.change.option.cyclic.report.pdus.OnChangeOptCyclicReportStartInvocExt;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportProvider;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;

/**
 * Implementation of the On Change Cyclic Report Procedure as per 922.1 B-1
 */
public class OnChangeCyclicReportProvider extends CyclicReportProvider implements IOnChangeCyclicReport
{
    /** The logger */
    private static final Logger LOG = Logger.getLogger(OnChangeCyclicReportProvider.class.getName());

    /** The procedure type */
    public final static ProcedureType ocoCyclicReport = ProcedureType.of(OIDs.ocoCyclicReport);

    /**
     * The on change option cyclic report start invocation extension object
     * identifier
     */
    public final static ObjectIdentifier onChangeOptCyclicReportStartInvocExt = ObjectIdentifier
            .of(OIDs.onChangeOptCyclicReportStartInvocExt);

    /** The on change flag */
    private boolean onChange = false;

    /** The first updated dispatched flag */
    private boolean firstUpdateDispatched = false;

    /** The list of parameters observed by this procedure */
    private List<IParameter> observedParameters = new ArrayList<IParameter>();

    /** The list of changed and observed parameters by this procedure */
    private List<IParameter> changedParameters = new ArrayList<IParameter>();

    /**
     * Get the procedure type
     * 
     * @return ProcedureType
     */
    @Override
    public ProcedureType getType()
    {
        return ocoCyclicReport;
    }

    /**
     * Start the cyclic report and reset the first update dispatched flag
     */
    @Override
    public synchronized void startCyclicReport()
    {
        this.firstUpdateDispatched = false;
        super.startCyclicReport();
    }

    /**
     * Decode the StartInvocationExt extension
     * 
     * @param extension The StartInvocationExt extension
     */
    @Override
    protected void decodeStartInvocationExtExtension(Extension extension)
    {
        if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(onChangeOptCyclicReportStartInvocExt))
        {
            OnChangeOptCyclicReportStartInvocExt onChangeCyclicReportStartExt = new OnChangeOptCyclicReportStartInvocExt();
            try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData()))
            {
                onChangeCyclicReportStartExt.decode(is);
                this.onChange = onChangeCyclicReportStartExt.getOnChangeOnly().value;
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, "Failed to decode StartInvocationExtExtension", e);
            }
        }
    }

    /**
     * Not applicable for a provider
     */
    @Override
    public CstsResult requestCyclicReport(long deliveryCycle, boolean onChange, ListOfParameters listOfParameters)
    {
        throw new UnsupportedOperationException("requestCyclicReport(long deliveryCycle, boolean onChange, ListOfParameters listOfParameters)"
                + " not supported for OnChangeCyclicReportProvider - only for user!");
    }

    /**
     * Processes the List of Parameters extension in the START invocation,
     * returns the result and writes the List of Parameters Diagnostics if the
     * result is negative. In case the onChange flag is true then it also
     * subscribes for the parameters updates
     * 
     * @return true if the List of Parameters is valid, false otherwise
     */
    protected synchronized boolean processListOfParameters()
    {
        boolean ret = super.processListOfParameters();
        if (ret && this.onChange)
        {
            ListOfParameters listOfParameters = getListOfParameters();
            LabelLists labelLists = (LabelLists) getConfigurationParameter(OIDs.pCRnamedLabelLists);
            List<IParameter> parameters = getServiceInstance().gatherParameters();

            switch (listOfParameters.getType())
            {
            case EMPTY:
                for (Label label : labelLists.queryDefaultList().getLabels())
                {
                    parameters.stream().filter(p -> p.getLabel().equals(label)).forEach(p -> p.addObserver(this));
                }
                break;

            case FUNCTIONAL_RESOURCE_NAME:
                parameters.stream().filter(p -> listOfParameters.getFunctionalResourceName()
                        .equals(p.getName().getFunctionalResourceName())).forEach(p -> p.addObserver(this));
                break;

            case FUNCTIONAL_RESOURCE_TYPE:
                parameters.stream().filter(p -> listOfParameters.getFunctionalResourceType()
                        .equals(p.getLabel().getFunctionalResourceType())).forEach(p -> p.addObserver(this));
                break;

            case LABELS_SET:
                for (Label label : listOfParameters.getParameterLabels())
                {
                    parameters.stream().filter(p -> p.getLabel().equals(label)).forEach(p -> p.addObserver(this));
                }
                break;

            case LIST_NAME:
                LabelList namedList = labelLists.queryList(listOfParameters.getListName());
                for (Label label : namedList.getLabels())
                {
                    parameters.stream().filter(p -> p.getLabel().equals(label)).forEach(p -> p.addObserver(this));
                }
                break;

            case NAMES_SET:
                for (Name name : listOfParameters.getParameterNames())
                {
                    parameters.stream().filter(p -> p.getName().equals(name)).forEach(p -> p.addObserver(this));
                }
                break;

            case PROCEDURE_INSTANCE_IDENTIFIER:
                parameters.stream()
                        .filter(p -> listOfParameters.getProcedureInstanceIdentifier()
                                .equals(p.getName().getProcedureInstanceIdentifier()))
                        .forEach(p -> p.addObserver(this));
                break;

            case PROCEDURE_TYPE:
                parameters.stream()
                        .filter(p -> listOfParameters.getProcedureType().equals(p.getLabel().getProcedureType()))
                        .forEach(p -> p.addObserver(this));
                break;
            }
        }
        return ret;
    }

    /**
     * Keep the changed changed parameter for the next on change cyclic report
     * 
     * @param parameter The changed parameter
     */
    protected void processParameterChange(IParameter parameter)
    {
        synchronized (this.changedParameters)
        {
            LOG.fine(() -> "Changed parameter: " + parameter.toString());
            this.changedParameters.add(parameter);
        }
    }

    /**
     * Generate qualified parameters for the TRANSFER-DATA operation
     */
    protected synchronized void generateQualifiedParameters()
    {
        if (this.onChange && this.firstUpdateDispatched)
        {
            // generate only the changed parameters since the last report
            synchronized (this.changedParameters)
            {
                this.qualifiedParameters.clear();
                this.changedParameters.stream().forEach(p -> this.qualifiedParameters.add(p.toQualifiedParameter()));
                this.changedParameters.clear();
            }
        }
        else
        {
            // generate all parameters
            super.generateQualifiedParameters();
        }
    }

    /**
     * Stop the cyclic report and un-subscribe from the observed parameters in case the on change cyclic
     * report has been started
     */
    @Override
    public synchronized void stopCyclicReport()
    {
        super.stopCyclicReport();
        if (this.onChange)
        {
            synchronized (this.observedParameters)
            {
                this.observedParameters.stream().forEach(p -> p.deleteObserver(this));
                this.observedParameters.clear();
            }
        }
    }

    /**
     * Create and invoke the TRANSFER-DATA operation only 
     * - in case onChange is false
     * - in case onChange is true there is at least one changed parameter
     * - in case onChange is true, none changed parameter but the first update has not been dispatched
     */
    @Override
    protected void createAndTransferData()
    {
        synchronized (this.changedParameters)
        {
            if (!this.onChange || !this.changedParameters.isEmpty() || !this.firstUpdateDispatched)
            {
                super.createAndTransferData();

                if (!this.firstUpdateDispatched)
                {
                    this.firstUpdateDispatched = true;
                }
            }
        }
    }
}
