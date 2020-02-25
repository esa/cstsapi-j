package esa.egos.csts.monitored.data.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import b1.ccsds.on.change.option.cyclic.report.pdus.OnChangeOptCyclicReportStartInvocExt;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportProvider;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.LabelList;

/**
 * Implementation of the On Change Cyclic Report Procedure as per 922.1 B-1 
 * TODO: implement the actual on change behavior
 */
public class OnChangeCyclicReportProvider extends CyclicReportProvider implements IOnChangeCyclicReport {
	
	public final static ProcedureType ocoCyclicReport = ProcedureType.of(ObjectIdentifier.of(b1.ccsds.monitored.data.object.identifiers.OidValues.ocoCyclicReport.value));
	
	public final static ObjectIdentifier onChangeOptCyclicReportStartInvocExt = ObjectIdentifier.of(b1.ccsds.on.change.option.cyclic.report.pdus.OidValues.onChangeOptCyclicReportStartInvocExt.value);

	@SuppressWarnings("unused")
	private boolean onChange;
	
	@Override
	public ProcedureType getType() {
		return ocoCyclicReport;		
	}
	
	@Override
	protected void decodeStartInvocationExtExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(onChangeOptCyclicReportStartInvocExt)) {
			OnChangeOptCyclicReportStartInvocExt onChangeCyclicReportStartExt = new OnChangeOptCyclicReportStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				onChangeCyclicReportStartExt.decode(is);
				this.onChange = onChangeCyclicReportStartExt.getOnChangeOnly().value;
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: throw e;
			}			
		}
	}

	/**
	 * Not applicable for a provider
	 */
	@Override
	public CstsResult requestCyclicReport(long deliveryCycle, boolean onChange, ListOfParameters listOfParameters) {
		throw new UnsupportedOperationException("requestCyclicReport(long deliveryCycle, boolean onChange, ListOfParameters listOfParameters) not supported for OnChangeCyclicReportProvider -"
				+ " only for user!");
	}
	
	@Override
	protected void initializeConfigurationParameters() {
		LabelLists namedLists = new LabelLists(OIDs.pCRnamedLabelLists, true, true /*false #hd# how can that be used with false ??? */, this);
		LabelList list = new LabelList("test-list-1", true);
		namedLists.add(list);
		
		addConfigurationParameter(namedLists);
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pCRminimumAllowedDeliveryCycle, true, false, this));
	}
}
