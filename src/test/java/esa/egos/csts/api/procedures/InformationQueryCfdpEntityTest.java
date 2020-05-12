package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ CfdpEntity parameters
 */
public class InformationQueryCfdpEntityTest extends InformationQueryFrTestBase
{
    
    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.cfdpEntity;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.CfdpEntity.parameter.cfdpRemoteEntityConfigurationParamOid, 
        		CstsComplexValue.of("cfdpRemoteEntityConfiguration",
        				CstsComplexValue.of("seqOf",
        					CstsComplexValue.of("SEQUENCE",
        						CstsIntValue.of("cfdpRemoteEntityId", 12),
        						CstsStringValue.of("cfdpRemoteUtAddress", "chname"),
        						CstsIntValue.of("cfdpRemoteOneWayLightTime", 13),
        						CstsIntValue.of("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay", 14),
        						CstsComplexValue.of("cfdpRemoteAsynchronousNakInterval",
        								CstsIntValue.of("timerValue", 15)),
        						CstsComplexValue.of("cfdpRemoteAsynchronousKeepAliveInterval",
        								CstsIntValue.of("timerValue", 16)),
        						CstsComplexValue.of("cfdpRemoteAsynchronousReportInterval",
        								CstsIntValue.of("timerValue", 17)),
        						CstsIntValue.of("cfdpRemoteImmediateNakModeEnabled", 18),
        						CstsComplexValue.of("cfdpRemotePromptTransmissionInterval",
        								CstsIntValue.of("timerValue", 19)),
        						CstsIntValue.of("cfdpRemoteDefaultTransmissionMode", 20),
        						CstsIntValue.of("cfdpRemoteDispositionOfIncompleteReceivedFile", 21),
        						CstsIntValue.of("cfdpRemoteCrcRequiredOnTransmission", 22),
        						CstsIntValue.of("cfdpRemoteMaximumFileSegmentLength", 23),
        						CstsComplexValue.of("cfdpRemoteKeepAliveDiscrepancyLimit",
        								CstsIntValue.of("keepAliveDiscrepancyLimit", 29)),
        						CstsIntValue.of("cfdpRemotePositiveAckTimerExpirationLimit", 24),
        						CstsIntValue.of("cfdpRemoteNakTimerExpirationLimit", 25),
        						CstsIntValue.of("cfdpRemoteTransactionInactivityLimit", 26),
        						CstsIntValue.of("cfdpRemoteTransmissionWindow", 27),
        						CstsIntValue.of("cfdpRemoteReceptionWindow", 28)
        						)),
        				CstsComplexValue.of("seqOf",
            					CstsComplexValue.of("SEQUENCE",
            						CstsIntValue.of("cfdpRemoteEntityId", 12),
            						CstsStringValue.of("cfdpRemoteUtAddress", "chname"),
            						CstsIntValue.of("cfdpRemoteOneWayLightTime", 13),
            						CstsIntValue.of("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay", 14),
            						CstsComplexValue.of("cfdpRemoteAsynchronousNakInterval",
            								CstsIntValue.of("timerValue", 15)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousKeepAliveInterval",
            								CstsIntValue.of("timerValue", 16)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousReportInterval",
            								CstsIntValue.of("timerValue", 17)),
            						CstsIntValue.of("cfdpRemoteImmediateNakModeEnabled", 18),
            						CstsComplexValue.of("cfdpRemotePromptTransmissionInterval",
            								CstsIntValue.of("timerValue", 19)),
            						CstsIntValue.of("cfdpRemoteDefaultTransmissionMode", 20),
            						CstsIntValue.of("cfdpRemoteDispositionOfIncompleteReceivedFile", 21),
            						CstsIntValue.of("cfdpRemoteCrcRequiredOnTransmission", 22),
            						CstsIntValue.of("cfdpRemoteMaximumFileSegmentLength", 23),
            						CstsComplexValue.of("cfdpRemoteKeepAliveDiscrepancyLimit",
            								CstsIntValue.of("keepAliveDiscrepancyLimit", 29)),
            						CstsIntValue.of("cfdpRemotePositiveAckTimerExpirationLimit", 24),
            						CstsIntValue.of("cfdpRemoteNakTimerExpirationLimit", 25),
            						CstsIntValue.of("cfdpRemoteTransactionInactivityLimit", 26),
            						CstsIntValue.of("cfdpRemoteTransmissionWindow", 27),
            						CstsIntValue.of("cfdpRemoteReceptionWindow", 28)
            						)),
        				CstsComplexValue.of("seqOf",
            					CstsComplexValue.of("SEQUENCE",
            						CstsIntValue.of("cfdpRemoteEntityId", 12),
            						CstsStringValue.of("cfdpRemoteUtAddress", "chname"),
            						CstsIntValue.of("cfdpRemoteOneWayLightTime", 13),
            						CstsIntValue.of("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay", 14),
            						CstsComplexValue.of("cfdpRemoteAsynchronousNakInterval",
            								CstsIntValue.of("timerValue", 15)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousKeepAliveInterval",
            								CstsIntValue.of("timerValue", 16)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousReportInterval",
            								CstsIntValue.of("timerValue", 17)),
            						CstsIntValue.of("cfdpRemoteImmediateNakModeEnabled", 18),
            						CstsComplexValue.of("cfdpRemotePromptTransmissionInterval",
            								CstsIntValue.of("timerValue", 19)),
            						CstsIntValue.of("cfdpRemoteDefaultTransmissionMode", 20),
            						CstsIntValue.of("cfdpRemoteDispositionOfIncompleteReceivedFile", 21),
            						CstsIntValue.of("cfdpRemoteCrcRequiredOnTransmission", 22),
            						CstsIntValue.of("cfdpRemoteMaximumFileSegmentLength", 23),
            						CstsComplexValue.of("cfdpRemoteKeepAliveDiscrepancyLimit",
            								CstsIntValue.of("keepAliveDiscrepancyLimit", 29)),
            						CstsIntValue.of("cfdpRemotePositiveAckTimerExpirationLimit", 24),
            						CstsIntValue.of("cfdpRemoteNakTimerExpirationLimit", 25),
            						CstsIntValue.of("cfdpRemoteTransactionInactivityLimit", 26),
            						CstsIntValue.of("cfdpRemoteTransmissionWindow", 27),
            						CstsIntValue.of("cfdpRemoteReceptionWindow", 28)
            						))
        		),
        		CstsComplexValue.of("cfdpRemoteEntityConfiguration",
        				CstsComplexValue.of("seqOf",
        					CstsComplexValue.of("SEQUENCE",
        						CstsIntValue.of("cfdpRemoteEntityId", 12),
        						CstsStringValue.of("cfdpRemoteUtAddress", "chname"),
        						CstsIntValue.of("cfdpRemoteOneWayLightTime", 13),
        						CstsIntValue.of("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay", 14),
        						CstsComplexValue.of("cfdpRemoteAsynchronousNakInterval",
        								CstsIntValue.of("timerValue", 15)),
        						CstsComplexValue.of("cfdpRemoteAsynchronousKeepAliveInterval",
        								CstsIntValue.of("timerValue", 16)),
        						CstsComplexValue.of("cfdpRemoteAsynchronousReportInterval",
        								CstsIntValue.of("timerValue", 17)),
        						CstsIntValue.of("cfdpRemoteImmediateNakModeEnabled", 18),
        						CstsComplexValue.of("cfdpRemotePromptTransmissionInterval",
        								CstsIntValue.of("timerValue", 19)),
        						CstsIntValue.of("cfdpRemoteDefaultTransmissionMode", 20),
        						CstsIntValue.of("cfdpRemoteDispositionOfIncompleteReceivedFile", 21),
        						CstsIntValue.of("cfdpRemoteCrcRequiredOnTransmission", 22),
        						CstsIntValue.of("cfdpRemoteMaximumFileSegmentLength", 23),
        						CstsComplexValue.of("cfdpRemoteKeepAliveDiscrepancyLimit",
        								CstsIntValue.of("keepAliveDiscrepancyLimit", 29)),
        						CstsIntValue.of("cfdpRemotePositiveAckTimerExpirationLimit", 24),
        						CstsIntValue.of("cfdpRemoteNakTimerExpirationLimit", 25),
        						CstsIntValue.of("cfdpRemoteTransactionInactivityLimit", 26),
        						CstsIntValue.of("cfdpRemoteTransmissionWindow", 27),
        						CstsIntValue.of("cfdpRemoteReceptionWindow", 28)
        						)),
        				CstsComplexValue.of("seqOf",
            					CstsComplexValue.of("SEQUENCE",
            						CstsIntValue.of("cfdpRemoteEntityId", 12),
            						CstsStringValue.of("cfdpRemoteUtAddress", "chname"),
            						CstsIntValue.of("cfdpRemoteOneWayLightTime", 13),
            						CstsIntValue.of("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay", 14),
            						CstsComplexValue.of("cfdpRemoteAsynchronousNakInterval",
            								CstsIntValue.of("timerValue", 15)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousKeepAliveInterval",
            								CstsIntValue.of("timerValue", 16)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousReportInterval",
            								CstsIntValue.of("timerValue", 17)),
            						CstsIntValue.of("cfdpRemoteImmediateNakModeEnabled", 18),
            						CstsComplexValue.of("cfdpRemotePromptTransmissionInterval",
            								CstsIntValue.of("timerValue", 19)),
            						CstsIntValue.of("cfdpRemoteDefaultTransmissionMode", 20),
            						CstsIntValue.of("cfdpRemoteDispositionOfIncompleteReceivedFile", 21),
            						CstsIntValue.of("cfdpRemoteCrcRequiredOnTransmission", 22),
            						CstsIntValue.of("cfdpRemoteMaximumFileSegmentLength", 23),
            						CstsComplexValue.of("cfdpRemoteKeepAliveDiscrepancyLimit",
            								CstsIntValue.of("keepAliveDiscrepancyLimit", 29)),
            						CstsIntValue.of("cfdpRemotePositiveAckTimerExpirationLimit", 24),
            						CstsIntValue.of("cfdpRemoteNakTimerExpirationLimit", 25),
            						CstsIntValue.of("cfdpRemoteTransactionInactivityLimit", 26),
            						CstsIntValue.of("cfdpRemoteTransmissionWindow", 27),
            						CstsIntValue.of("cfdpRemoteReceptionWindow", 28)
            						)),
        				CstsComplexValue.of("seqOf",
            					CstsComplexValue.of("SEQUENCE",
            						CstsIntValue.of("cfdpRemoteEntityId", 12),
            						CstsStringValue.of("cfdpRemoteUtAddress", "chname"),
            						CstsIntValue.of("cfdpRemoteOneWayLightTime", 13),
            						CstsIntValue.of("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay", 14),
            						CstsComplexValue.of("cfdpRemoteAsynchronousNakInterval",
            								CstsIntValue.of("timerValue", 15)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousKeepAliveInterval",
            								CstsIntValue.of("timerValue", 16)),
            						CstsComplexValue.of("cfdpRemoteAsynchronousReportInterval",
            								CstsIntValue.of("timerValue", 17)),
            						CstsIntValue.of("cfdpRemoteImmediateNakModeEnabled", 18),
            						CstsComplexValue.of("cfdpRemotePromptTransmissionInterval",
            								CstsIntValue.of("timerValue", 19)),
            						CstsIntValue.of("cfdpRemoteDefaultTransmissionMode", 20),
            						CstsIntValue.of("cfdpRemoteDispositionOfIncompleteReceivedFile", 21),
            						CstsIntValue.of("cfdpRemoteCrcRequiredOnTransmission", 22),
            						CstsIntValue.of("cfdpRemoteMaximumFileSegmentLength", 23),
            						CstsComplexValue.of("cfdpRemoteKeepAliveDiscrepancyLimit",
            								CstsIntValue.of("keepAliveDiscrepancyLimit", 29)),
            						CstsIntValue.of("cfdpRemotePositiveAckTimerExpirationLimit", 24),
            						CstsIntValue.of("cfdpRemoteNakTimerExpirationLimit", 25),
            						CstsIntValue.of("cfdpRemoteTransactionInactivityLimit", 26),
            						CstsIntValue.of("cfdpRemoteTransmissionWindow", 27),
            						CstsIntValue.of("cfdpRemoteReceptionWindow", 28)
            						))
        		)));
        testParameters.add(new TestParameter(Fr.CfdpEntity.parameter.cfdpLocalEntityIdParamOid, "cfdpLocalEntityId", 25, 50));
        testParameters.add(new TestParameter(Fr.CfdpEntity.parameter.cfdpLocalEntityConfigurationParamOid, 
        		CstsComplexValue.of("cfdpLocalEntityConfiguration",
	        		CstsIntValue.of("cfdpLocalEofSentIndicationRequired", 10),
        			CstsIntValue.of("cfdpLocalRecvIndicationRequired", 10),
        			CstsIntValue.of("cfdpLocalFileSegmentRecvdIndicationRequired", 10),
        			CstsIntValue.of("cfdpLocalTransactionFinishedIndicationRequired", 10),
        			CstsIntValue.of("cfdpLocalSuspendedIndicationRequired", 10),
        			CstsIntValue.of("cfdpLocalResumedIndicationRequired", 10),
        			CstsIntValue.of("cfdpLocalPositiveAckLimitReachedFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalKeepAliveLimitFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalInvalidTransmissionModeFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalFilestoreRejectionFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalFileChecksumFailureFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalFileSizeErrorFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalNakLimitReachedFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalInactivityDetectedFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalInvalidFileStructureFaultHandler", 10),
        			CstsIntValue.of("cfdpLocalCheckLimitReachedFaultHandler", 10),
	        		CstsComplexValue.of("cfdpLocalRoutingInformation", 
	        				CstsIntValue.of("seqOf",12),
	        				CstsIntValue.of("seqOf",13),
	        				CstsIntValue.of("seqOf",14)
	        				)),
        		CstsComplexValue.of("cfdpLocalEntityConfiguration",
    	        		CstsIntValue.of("cfdpLocalEofSentIndicationRequired", 10),
            			CstsIntValue.of("cfdpLocalRecvIndicationRequired", 10),
            			CstsIntValue.of("cfdpLocalFileSegmentRecvdIndicationRequired", 10),
            			CstsIntValue.of("cfdpLocalTransactionFinishedIndicationRequired", 10),
            			CstsIntValue.of("cfdpLocalSuspendedIndicationRequired", 10),
            			CstsIntValue.of("cfdpLocalResumedIndicationRequired", 10),
            			CstsIntValue.of("cfdpLocalPositiveAckLimitReachedFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalKeepAliveLimitFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalInvalidTransmissionModeFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalFilestoreRejectionFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalFileChecksumFailureFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalFileSizeErrorFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalNakLimitReachedFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalInactivityDetectedFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalInvalidFileStructureFaultHandler", 10),
            			CstsIntValue.of("cfdpLocalCheckLimitReachedFaultHandler", 10),
            			CstsComplexValue.of("cfdpLocalRoutingInformation", 
    	        				CstsIntValue.of("seqOf",12),
    	        				CstsIntValue.of("seqOf",13),
    	        				CstsIntValue.of("seqOf",14)
    	        				))
        		));
        testParameters.add(new TestParameter(Fr.CfdpEntity.parameter.cfdpLocalEntityResourceStatParamOid, "cfdpLocalEntityResourceStat", 25, 50));
    }

    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>(testParameters.size());
        for (TestParameter testParameter : testParameters)
        {
            ret.add(Label.of(testParameter.oid, getFunctionalResource()));
        }
        return ret;
    }
    
    @Test
    public void testQueryInformationWithNameSet()
    {
        super.testQueryInformationWithNameSet();
    }

    @Test
    public void testQueryInformationWithLabelSet()
    {
        super.testQueryInformationWithLabelSet();
    }

    @Test
    public void testQueryInformationWithFunctionalResourceName()
    {
        super.testQueryInformationWithFunctionalResourceName();
    }
    
    @Test
    public void testQueryInformationWithFunctionalResourceType()
    {
        super.testQueryInformationWithFunctionalResourceType();
    }
    
    @Test
    public void testQueryInformationWithProcedureType()
    {
        super.testQueryInformationWithProcedureType();
    }

    @Test
    public void testQueryInformationWithProcedureInstanceIdentifier()
    {
        super.testQueryInformationWithProcedureInstanceIdentifier();
    }

    @Test
    public void testQueryInformationWithEmpty()
    {
        super.testQueryInformationWithEmpty();
    }
    
}
