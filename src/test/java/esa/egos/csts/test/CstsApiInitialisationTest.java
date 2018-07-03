package esa.egos.csts.test;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.roles.UnbufferedDataDeliveryUser;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.proxy.del.ITranslator;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.util.TimeFactory;
import esa.egos.proxy.util.impl.TimeSource;

public class CstsApiInitialisationTest {
	
	int version = 1;
	String sii =  "spacecraft=1,3,112,4,7,0.facility=1,3,112,4,6,0.type=1,3,112,4,4,1,2.serviceinstance=0";
	AppRole role = AppRole.USER;
	IProcedure procedure;
	IReporter reporter;
	TimeSource timeSource;
	
	public void testCstsApiInitialisation() {
		
		// initialise time and reporter
		timeSource = TimeFactory.createTimeSource();
		reporter = new TestReporter(); 
		
		// Get the CSTS API entry point
		CstsApi apiInstance = new CstsApi("MyUserInstance1", role);
		try {
			apiInstance.initialise("/tmp/UserConfig.xml", timeSource, reporter);

			IServiceInform callback = new IServiceInform() { 
				
				@Override
				public void resumeDataTransfer() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void protocolAbort(byte[] diagnostic) throws ApiException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void informOpReturn(IConfirmedOperation confOperation, long seqCount)
						throws ApiException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void informOpInvoke(IOperation operation, long seqCount)
						throws ApiException {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void informOpAck(IAcknowledgedOperation operation, long seqCount)
						throws ApiException {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void provisionPeriodEnds() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void pduTransmitted(IOperation poperation) throws ApiException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public ITranslator getTranslator() {
					// TODO Auto-generated method stub
					return null;
				}
			};
			
			// Create a service instance (user)
			IServiceInstance si = apiInstance.createServiceInstance(sii, callback);
			
			try {
				// Create procedure X as prime
				IProcedure primeProcedure = si.createProcedure(UnbufferedDataDeliveryUser.class, version);
				// setRole also initialises the ProcedureInstanceIdentifier
				primeProcedure.setRole(ProcedureRole.PRIME, 0);
	
				// Add procedure X
				si.addProcedure(primeProcedure);
				
				// add any other procedure
				IProcedure secondaryProcedure = si.createProcedure(UnbufferedDataDeliveryUser.class, version);
				secondaryProcedure.setRole(ProcedureRole.SECONDARY, 1);
				si.addProcedure(secondaryProcedure);
				// Finalise service instance creation
				si.configure();
	
				// send bind (always association control)
	
				IOperation  bindOp = si.createOperation(IBind.class);
				bindOp.setProcedureInstanceIdentifier(si.getAssociationControlProcedure().getProcedureInstanceIdentifier()); //make setters and getters
				
				si.initiateOpInvoke(bindOp,1);
				//primeProcedure.createOperation(clazz); // like this or on serviceInstance
				
	
				// data transfer
				IOperation transferDataOp = si.createOperation(ITransferData.class);
				// removed from create so has to be set!
				transferDataOp.setProcedureInstanceIdentifier(si.getPrimeProcedure().getProcedureInstanceIdentifier()); //make setters and getters
				
				si.initiateOpInvoke(transferDataOp,1);
				
				// TODO check: can one add parameters to si?
				
				// destroy si
				apiInstance.destroyServiceInstance(si);
				
			} catch (ApiException e) {
				e.printStackTrace();
			}
			
		} catch (ApiException e1) {
			e1.printStackTrace();
		} 		
	}
}
