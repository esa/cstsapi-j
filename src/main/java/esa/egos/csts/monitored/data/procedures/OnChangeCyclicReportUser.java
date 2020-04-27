package esa.egos.csts.monitored.data.procedures;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerBoolean;

import b1.ccsds.on.change.option.cyclic.report.pdus.OnChangeOptCyclicReportStartInvocExt;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportUser;
import esa.egos.csts.api.procedures.impl.ProcedureType;

public class OnChangeCyclicReportUser extends CyclicReportUser implements IOnChangeCyclicReport
{
    /** The logger */
    private static final Logger LOG = Logger.getLogger(OnChangeCyclicReportProvider.class.getName());

    /** The procedure type */
    public final static ProcedureType ocoCyclicReport = ProcedureType.of(OIDs.ocoCyclicReport);

    /** The initial capacity of a stream buffer */
    private static final int INIT_STREAM_BUFF_SIZE = 128;

    /** The on change flag */
    private boolean onChange;


    @Override
    public ProcedureType getType()
    {
        return ocoCyclicReport;
    }

    /**
     * Encodes the start invocation extension of the on change cyclic report
     * procedure
     * 
     * @return the Extension object representing the extension of the derived
     *         procedure
     */
    protected Extension encodeStartInvocationExtExtension()
    {
        OnChangeOptCyclicReportStartInvocExt invocationExtension = new OnChangeOptCyclicReportStartInvocExt();
        invocationExtension.setOnChangeOnly(new BerBoolean(this.onChange));
        invocationExtension.setOnChangeOptCyclicReportStartInvocExtExtension(Extension.notUsed().encode());

        // encode with a resizable output stream and an initial capacity of 128
        // bytes
        try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(INIT_STREAM_BUFF_SIZE, true))
        {
            invocationExtension.encode(os);
            invocationExtension.code = os.getArray();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "Failed to encode StartInvocationExtExtension", e);
        }

        return Extension.of(EmbeddedData.of(OIDs.onChangeOptCyclicReportStartInvocExt, invocationExtension.code));
    }

    @Override
    public CstsResult requestCyclicReport(long deliveryCycle, boolean onChange, ListOfParameters listOfParameters)
    {
        this.onChange = onChange;
        return super.requestCyclicReport(deliveryCycle, listOfParameters);
    }
}
