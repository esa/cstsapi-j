package esa.egos.csts.test.mdslite.impl;

import java.io.IOException;
import java.util.List;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.test.mdslite.procedures.IOnChangeCyclicReport;
import frm.csts.functional.resource.types.AntActualAzimuth;
import frm.csts.functional.resource.types.OidValues;

/**
 * MD Provider implementation for testing 
 */
public class MdSiProvider extends MdSi {
	
	public MdSiProvider(ICstsApi api, SiConfig config, List<ListOfParameters> parameterLists) throws ApiException {
		super(api, config, parameterLists, true);
		
		for(int idx= 0; idx<parameterLists.size(); idx++) {
			setAntAzimut(4711, idx);
		}
		
	}
	
	@Override
	public void informOpInvocation(IOperation operation) {
		System.out.println("MD Provider received operation " + operation);
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		System.out.println("MD Provider received ack " + operation);
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		System.out.println("MD Provider received operation return " + operation);
	}

	@Override
	public void protocolAbort() {
		System.out.println("MD Provider received protocol abort");
	}
	
	public void setAntAzimut(long value, int crProcedureInstanceNo) {
		
		/**
		 * Below is an example how to encode a single parameter value for a Functional resource Instance
		 */		
		IOnChangeCyclicReport cr = getCyclicReportProcedure(crProcedureInstanceNo);
		
		ObjectIdentifier anrFrOid = ObjectIdentifier.of(1, 3, 112, 4, 4, 2, 1, 1000);
		ObjectIdentifier antAzimuthOid = ObjectIdentifier.of(OidValues.antAccumulatedPrecipitationType.value);
		FunctionalResourceType frt = FunctionalResourceType.of(anrFrOid);
		FunctionalResourceName frn = FunctionalResourceName.of(frt, 0 /*FR instance*/);
		Name antennaAzimuthParamName = Name.of(antAzimuthOid, frn /*TODO: generate the FR OIDs*/);
		
		QualifiedParameter qualifiedParameter = new QualifiedParameter(antennaAzimuthParamName); 
		QualifiedValues qv = new QualifiedValues(ParameterQualifier.VALID);
		
		AntActualAzimuth azimuth = new AntActualAzimuth(value);
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(12, true)) {
			azimuth.encode(os);
			azimuth.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		EmbeddedData valueExtension = EmbeddedData.of(antAzimuthOid, azimuth.code);
		System.out.println("Encoded AntActualAzimuth embedded: " + valueExtension);
		
		ParameterValue pv = new ParameterValue(valueExtension);
		qv.getParameterValues().add(pv);			
		qualifiedParameter.getQualifiedValues().add(qv);
		
		cr.getQualifiedParameters().clear(); // clear prev.values
		cr.getQualifiedParameters().add(qualifiedParameter);
	}
}
