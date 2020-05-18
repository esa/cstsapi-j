package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR TmAosMcDemux events
 */
public class NotificationTmAosMcDemuxTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tmAosMcDemux;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.TmAosMcDemux.parameter.tmAosMcDemuxMcIdsParamOid, 
        		CstsComplexValue.of("tmAosMcDemuxMcIds",
	        		CstsComplexValue.of("tmFrames",
    					CstsComplexValue.of("seqOf", 
    							CstsComplexValue.of("SEQUENCE", 
    									CstsIntValue.of("tfvn",47), 
    									CstsIntValue.of("scid",50))),
						CstsComplexValue.of("seqOf", 
								CstsComplexValue.of("SEQUENCE", 
										CstsIntValue.of("tfvn",47), 
										CstsIntValue.of("scid",50))),
    					CstsComplexValue.of("seqOf", 
    							CstsComplexValue.of("SEQUENCE", 
    									CstsIntValue.of("tfvn",47), 
    									CstsIntValue.of("scid",50)))
    			)),
        		CstsComplexValue.of("tmAosMcDemuxMcIds",
        			CstsComplexValue.of("aosFrames",
						CstsComplexValue.of("seqOf", 
								CstsComplexValue.of("SEQUENCE", 
										CstsIntValue.of("tfvn",47), 
										CstsIntValue.of("scid",50))),
						CstsComplexValue.of("seqOf", 
								CstsComplexValue.of("SEQUENCE", 
										CstsIntValue.of("tfvn",47), 
										CstsIntValue.of("scid",50))),
    					CstsComplexValue.of("seqOf", 
    							CstsComplexValue.of("SEQUENCE", 
    									CstsIntValue.of("tfvn",47), 
    									CstsIntValue.of("scid",50)))
        		))));        		
        testParameters.add(new TestParameter(Fr.TmAosMcDemux.parameter.tmAosMcDemuxClcwExtractionParamOid,
        		CstsComplexValue.of("tmAosMcDemuxClcwExtraction",
        				CstsComplexValue.of("clcwExtractionTmMc",
	        				CstsIntValue.of("tfvn",47),
	        				CstsIntValue.of("scid",50))
        				),
        		CstsComplexValue.of("tmAosMcDemuxClcwExtraction",
        				CstsComplexValue.of("clcwExtractionAosMc",
	        				CstsIntValue.of("tfvn",47),
	        				CstsIntValue.of("scid",50))
        				)
        		));
        testParameters.add(new TestParameter(Fr.TmAosMcDemux.parameter.tmAosMcDemuxResourceStatParamOid, "tmAosMcDemuxResourceStat", 100, 110));
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