package esa.egos.csts.test.mdslite.procedures;

import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerBoolean;

import b1.ccsds.on.change.option.cyclic.report.pdus.OnChangeOptCyclicReportStartInvocExt;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportUser;
import esa.egos.csts.api.procedures.impl.ProcedureType;

public class OnChangeCyclicReportUser extends CyclicReportUser implements IOnChangeCyclicReport {

	private boolean onChange;
	
	private final ObjectIdentifier onChangeExtOid = ObjectIdentifier.of(b1.ccsds.on.change.option.cyclic.report.pdus.OidValues.onChangeOptCyclicReportStartInvocExt.value);

	@Override
	public ProcedureType getType() {
		return OnChangeCyclicReportProvider.ocoCyclicReport; // the same for use and provider	 	
	}	
	
	/**
	 * Encodes the start invocation extension of the on change cyclic report procedure 
	 * @return the Extension object representing the extension of the derived
	 *         procedure
	 */
	protected Extension encodeStartInvocationExtExtension() {
		OnChangeOptCyclicReportStartInvocExt invocationExtension = new OnChangeOptCyclicReportStartInvocExt();
		invocationExtension.setOnChangeOnly(new BerBoolean(this.onChange));
		invocationExtension.setOnChangeOptCyclicReportStartInvocExtExtension(Extension.notUsed().encode());
		
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return Extension.of(EmbeddedData.of(onChangeExtOid, invocationExtension.code));
	}
	
	@Override
	public CstsResult requestCyclicReport(long deliveryCycle, boolean onChange, ListOfParameters listOfParameters) {
		this.onChange = onChange;
		return super.requestCyclicReport(deliveryCycle, listOfParameters);
	}
	
}
