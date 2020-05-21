package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOidValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR CfdpEntity events
 */
public class NotificationCfdpEntityTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.cfdpEntity;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpMetadataIndicationValueOid, 
        		CstsComplexValue.of("cfdpMetadataIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("sourceEntityId", 61),
        				CstsStringValue.of("sourceFileName", "chname1"),
        				CstsStringValue.of("destinationFileName", "chname2"),
        				CstsStringValue.of("messagesToUser", "chname3")),
        		CstsComplexValue.of("cfdpMetadataIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("sourceEntityId", 61),
        				CstsStringValue.of("sourceFileName", "chname1"),
        				CstsStringValue.of("destinationFileName", "chname2"),
        				CstsStringValue.of("messagesToUser", "chname3"))
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpFinishedTransactionValueOid, 
        		CstsComplexValue.of("cfdpFinishedTransaction",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsComplexValue.of("fileStoreResponses",
	        				CstsIntValue.of("actionCode", 184),
	        				CstsIntValue.of("statusCode", 247),
	        				CstsStringValue.of("firstFileName", "chname1"),
	        				CstsStringValue.of("secondFileName", "chname2"),
	        				CstsStringValue.of("fileStoreMessage", "chname3")),
        				CstsStringValue.of("statusReport", "chname4"),
        				CstsIntValue.of("conditionCode", 36),
        				CstsIntValue.of("fileStatus", 17),
        				CstsIntValue.of("deliveryCode", 52)),
        		CstsComplexValue.of("cfdpFinishedTransaction",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsComplexValue.of("fileStoreResponses",
	        				CstsIntValue.of("actionCode", 184),
	        				CstsIntValue.of("statusCode", 247),
	        				CstsStringValue.of("firstFileName", "chname1"),
	        				CstsStringValue.of("secondFileName", "chname2"),
	        				CstsStringValue.of("fileStoreMessage", "chname3")),
        				CstsStringValue.of("statusReport", "chname4"),
        				CstsIntValue.of("conditionCode", 36),
        				CstsIntValue.of("fileStatus", 17),
        				CstsIntValue.of("deliveryCode", 52))
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpReportIndicationValueOid, 
        		CstsComplexValue.of("cfdpReportIndication", 
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsStringValue.of("statusReport", "chname")),
        		CstsComplexValue.of("cfdpReportIndication", 
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsStringValue.of("statusReport", "chname"))
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpFileSegmentIndicationValueValueOid, 
        		CstsComplexValue.of("cfdpFileSegmentIndicationValue",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("offset", 49),
        				CstsIntValue.of("segmentLength", 57)),
        		CstsComplexValue.of("cfdpFileSegmentIndicationValue",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("offset", 49),
        				CstsIntValue.of("segmentLength", 57))
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpEofSentTransactionIdValueOid, 
        		CstsOctetStringValue.of("cfdpEofSentTransactionId", new byte[] {2,3,4}),
        		CstsOctetStringValue.of("cfdpEofSentTransactionId", new byte[] {2,3,4})
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpTransactionTransactionIdValueOid,
        		CstsOctetStringValue.of("cfdpTransactionTransactionId", new byte[] {2,3,4}),
        		CstsOctetStringValue.of("cfdpTransactionTransactionId", new byte[] {2,3,4})
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpEofRcvdTransactionIdValueOid,
        		CstsOctetStringValue.of("cfdpEofRcvdTransactionId", new byte[] {2,3,4}),
        		CstsOctetStringValue.of("cfdpEofRcvdTransactionId", new byte[] {2,3,4})
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpConsignedTransactionIdValueOid,
        		CstsOctetStringValue.of("cfdpConsignedTransactionId", new byte[] {2,3,4}),
        		CstsOctetStringValue.of("cfdpConsignedTransactionId", new byte[] {2,3,4})
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpFaultIndicationValueOid, 
        		CstsComplexValue.of("cfdpFaultIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("conditionCode", 48),
        				CstsIntValue.of("progress", 71)),
        		CstsComplexValue.of("cfdpFaultIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("conditionCode", 48),
        				CstsIntValue.of("progress", 71))
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpResumedIndicationValueOid, 
        		CstsComplexValue.of("cfdpResumedIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("progress", 174)),
        		CstsComplexValue.of("cfdpResumedIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("progress", 174))
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpAbandonedIndicationValueOid,
        		CstsComplexValue.of("cfdpAbandonedIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("conditionCode", 48),
        				CstsIntValue.of("progress", 71)),
        		CstsComplexValue.of("cfdpAbandonedIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("conditionCode", 48),
        				CstsIntValue.of("progress", 71))
        		));
        testEvents.add(new TestParameter(Fr.CfdpEntity.event.cfdpSuspendedIndicationValueOid,
        		CstsComplexValue.of("cfdpSuspendedIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("conditionCode", 48)),
        		CstsComplexValue.of("cfdpSuspendedIndication",
        				CstsOctetStringValue.of("transactionId", new byte[] {2,3,4}),
        				CstsIntValue.of("conditionCode", 48))
        		));      
    }

    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>(testEvents.size());
        for (TestParameter testParameter : testEvents)
        {
            ret.add(Label.of(testParameter.oid, getFunctionalResource()));
        }
        return ret;
    }

}