package esa.egos.csts.test.mdslite.impl;

import java.io.IOException;
import java.util.List;
import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import esa.egos.csts.api.enumerations.OperationType;
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
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;
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
	public synchronized void informOpInvocation(IOperation operation) {
		System.out.println("MD Provider received operation " + operation);
		if(operation.getType() == OperationType.PEER_ABORT) {
			this.notify();
		}
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
	
	/**
	 * This is an example method illustrating how to set a Functional Resource Parameter into
	 * the list of qualified values of the Cyclic Report Procedure.
	 * 
	 * Again: This shall just illustrate the principle for parameter setting. 
	 * A real approach should be generic and/or rely on code generation from a functional resource (instance) model.
	 * 
	 * @param value
	 * @param crProcedureInstanceNo
	 */
	public void setAntAzimut(long value, int crProcedureInstanceNo) {	
		IOnChangeCyclicReport cr = getCyclicReportProcedure(crProcedureInstanceNo);
		
		ObjectIdentifier antFrOid = ObjectIdentifier.of(OidValues.antennaType.value);
		ObjectIdentifier antAzimuthOid = ObjectIdentifier.of(OidValues.antAccumulatedPrecipitationType.value);
		FunctionalResourceType antFrType = FunctionalResourceType.of(antFrOid);
		FunctionalResourceName antFrInstance = FunctionalResourceName.of(antFrType, 0 /*FR instance number*/);
		Name antennaAzimuthParamName = Name.of(antAzimuthOid, antFrInstance);
		
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
	
	/**
	 * If the SI is bound, wait for an abort
	 * @param timeoutMillies
	 */
	public synchronized void waitForAbort(long timeoutMillies) {
		try {
			while(this.getApiServiceInstance().getStatus() == ServiceStatus.BOUND) {
				long start = System.currentTimeMillis();
				this.wait(timeoutMillies);
				System.out.println("Provider SI, wait for abort ("+ timeoutMillies + " ms) returned with " 
						+ (System.currentTimeMillis() - start) + " ms SI status: " + this.getApiServiceInstance().getStatus());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
