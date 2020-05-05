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
 * Test CSTS API information query procedure w/ Ccsds401SpaceLinkCarrierXmit parameters
 */
// Fwd401SpaceLinkCarrierXmit
public class InformationQueryCcsds401SpaceLinkCarrierXmitTest extends InformationQueryFrTestBase
{	
	@BeforeClass
    public static void setupClass() throws Exception
    {	
		InformationQueryFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitSymbolRateParamOid, "ccsds401CarrierXmitSymbolRate", 10, 30));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitModInpStatParamOid,
        		CstsComplexValue.of("ccsds401CarrierXmitModInpStat",
        				CstsIntValue.of("tcInp",47),
        				CstsIntValue.of("rngInp",50)),
        		CstsComplexValue.of("ccsds401CarrierXmitModInpStat",
        				CstsIntValue.of("tcInp",47),
        				CstsIntValue.of("rngInp",50))
        		));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitTcPriorityParamOid, "ccsds401CarrierXmitTcPriority", 30000, 50000));
//        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitSweepProfileParamOid,
//		CstsComplexValue.of("ccsds401CarrierXmitSweepProfile", CstsIntValue.of("startfreq", 22),
//				CstsComplexValue.of("sweepLegs",
//						CstsComplexValue.of("seqOf",
//							CstsComplexValue.of("SEQUENCE",
//        						CstsIntValue.of("dwellTime", 1),
//        						CstsIntValue.of("sweepTime", 2),
//        						CstsIntValue.of("endFreq", 3)
//					))
//					)
//		),
//		CstsComplexValue.of("ccsds401CarrierXmitSweepProfile", CstsIntValue.of("startfreq", 22),
//				CstsComplexValue.of("sweepLegs",
//						CstsComplexValue.of("seqOf",
//        					CstsComplexValue.of("SEQUENCE",
//        						CstsIntValue.of("dwellTime", 1),
//        						CstsIntValue.of("sweepTime", 2),
//        						CstsIntValue.of("endFreq", 3)
//    					))
//						)
//		)
//		));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitModParamOid, "ccsds401CarrierXmitMod", 30000, 50000));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitSubcarrierFreqParamOid, "ccsds401CarrierXmitSubcarrierFreq", 30000, 50000));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitSubcarrierModParamOid, "ccsds401CarrierXmitSubcarrierMod", 30000, 50000));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitRngModIndexParamOid, 
        		CstsComplexValue.of("ccsds401CarrierXmitRngModIndex",CstsIntValue.of("duringAmbiguityResolution",47),CstsIntValue.of("afterAmbiguityResolution",50)),
        		CstsComplexValue.of("ccsds401CarrierXmitRngModIndex",CstsIntValue.of("duringAmbiguityResolution",47),CstsIntValue.of("afterAmbiguityResolution",50))
        		));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitStatParamOid, "ccsds401CarrierXmitStat", 200, -200));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitPhysChnlNameParamOid,
                CstsStringValue.of("ccsds401CarrierXmitPhysChnlName", "chname1"),
                CstsStringValue.of("ccsds401CarrierXmitPhysChnlName", "chname2")
        		));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitPolarizationParamOid, "ccsds401CarrierXmitPolarization", 8, 7));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitEirpParamOid, "ccsds401CarrierXmitEirp", 30000, 50000));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitNominalCarrierFreqParamOid,
        		CstsComplexValue.of("ccsds401CarrierXmitNominalCarrierFreq",CstsIntValue.of("nominalXmitFreq",47),CstsIntValue.of("xmitLinkRamping",50)),
        		CstsComplexValue.of("ccsds401CarrierXmitNominalCarrierFreq",CstsIntValue.of("nominalXmitFreq",47),CstsIntValue.of("xmitLinkRamping",50))
        		));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitActualCarrierFreqParamOid, "ccsds401CarrierXmitActualCarrierFreq", 30000, 50000));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitSweepProcStatParamOid, "ccsds401CarrierXmitSweepProcStat", 90, 900));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitReferenceFreqLockParamOid, "ccsds401CarrierXmitReferenceFreqLock", 80, 90));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierXmit.parameter.ccsds401CarrierXmitResourceStatParamOid, "ccsds401CarrierXmitResourceStat", 30000, 50000));
    }

    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>(testParameters.size());
        for (TestParameter testParameter : testParameters)
        {
        	System.out.println("smth " + Fr.ccsds401SpaceLinkCarrierXmit);
            ret.add(Label.of(testParameter.oid, Fr.ccsds401SpaceLinkCarrierXmit));
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

	@Override
	protected FunctionalResourceType getFunctionalResource() {
		return Fr.ccsds401SpaceLinkCarrierXmit;
	}
}
