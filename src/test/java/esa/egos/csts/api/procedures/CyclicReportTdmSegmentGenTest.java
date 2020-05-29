package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsBoolValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOidValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API cyclic report procedure w/ TdmSegmentGen parameters
 */
public class CyclicReportTdmSegmentGenTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tdmSegmentGen;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();      
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenXmitFreqRatePathTableParamOid,
        		CstsComplexValue.of("tdmSegGenXmitFreqRatePathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59)))),
        		CstsComplexValue.of("tdmSegGenXmitFreqRatePathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))))
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenAntAnglesPathTableParamOid,
        		CstsComplexValue.of("tdmSegGenAntAnglesPathTable",
        				CstsStringValue.of("trackingDataPathId", "chname1"),
						CstsStringValue.of("apertureName", "chname2"),
						CstsStringValue.of("spaceUserNodeName", "chname3"),
						CstsIntValue.of("reportingPeriod", 14),
						CstsComplexValue.of("frName",
								CstsOidValue.of("frType", new int[] {2,3,4}),
								CstsIntValue.of("frin", 75))
        				),
        		CstsComplexValue.of("tdmSegGenAntAnglesPathTable",
        				CstsStringValue.of("trackingDataPathId", "chname1"),
						CstsStringValue.of("apertureName", "chname2"),
						CstsStringValue.of("spaceUserNodeName", "chname3"),
						CstsIntValue.of("reportingPeriod", 14),
						CstsComplexValue.of("frName",
								CstsOidValue.of("frType", new int[] {2,3,4}),
								CstsIntValue.of("frin", 75))
        				)
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenResourceStatParamOid,
                CstsStringValue.of("tdmSegGenResourceStat", "chname1"),
                CstsStringValue.of("tdmSegGenResourceStat", "chname2")
                		));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenPrOverNoPathTableParamOid,
                CstsComplexValue.of("tdmSegGenPrOverNoPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59)))),
        		CstsComplexValue.of("tdmSegGenPrOverNoPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))))
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenDoppInstPathTableParamOid,
                CstsComplexValue.of("tdmSegGenDoppInstPathTable",
            		CstsComplexValue.of("seqOf",
        				CstsComplexValue.of("SEQUENCE",
        					CstsComplexValue.of("pathTableCommonElements",
    							CstsStringValue.of("trackingDataPathId", "chname1"),
								CstsStringValue.of("apertureName", "chname2"),
								CstsStringValue.of("spaceUserNodeName", "chname3"),
								CstsIntValue.of("reportingPeriod", 14),
								CstsComplexValue.of("frName",
										CstsOidValue.of("frType", new int[] {2,3,4}),
										CstsIntValue.of("frin", 75))
							),
        					CstsStringValue.of("threeWayRcvAperture", "chname"),
        					CstsComplexValue.of("rcvFreqBand",
        						CstsComplexValue.of("seqOf",
        							CstsComplexValue.of("SEQUENCE",
        								CstsStringValue.of("name", "chname"),
        								CstsBoolValue.of("defaultList", true),
        								CstsComplexValue.of("labels",
        									CstsComplexValue.of("seqOf",
        										CstsComplexValue.of("LabelV1",
	        										CstsComplexValue.of("frOrProcedureType",
	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
	        										),
	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        										)
        									),
        									CstsComplexValue.of("seqOf",
    											CstsComplexValue.of("LabelV1",
	        										CstsComplexValue.of("frOrProcedureType",
	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
	        										),
	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
            									)
            								),
        									CstsComplexValue.of("seqOf",
        										CstsComplexValue.of("LabelV1",
    	        									CstsComplexValue.of("frOrProcedureType",
    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        									),
    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                								)
                							)
        								)
        							)
        						),
        						CstsComplexValue.of("seqOf",
            							CstsComplexValue.of("SEQUENCE",
            								CstsStringValue.of("name", "chname"),
            								CstsBoolValue.of("defaultList", true),
            								CstsComplexValue.of("labels",
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
            										)
            									),
            									CstsComplexValue.of("seqOf",
        											CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                									)
                								),
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
        	        									CstsComplexValue.of("frOrProcedureType",
        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        									),
        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    								)
                    							)
            								)
            							)
            						),
        						CstsComplexValue.of("seqOf",
            							CstsComplexValue.of("SEQUENCE",
            								CstsStringValue.of("name", "chname"),
            								CstsBoolValue.of("defaultList", true),
            								CstsComplexValue.of("labels",
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
            										)
            									),
            									CstsComplexValue.of("seqOf",
        											CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                									)
                								),
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
        	        									CstsComplexValue.of("frOrProcedureType",
        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        									),
        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    								)
                    							)
            								)
            							)
            						)
        					),
        					CstsComplexValue.of("xmitFreqBand",
        							CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						),
                						CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						),
                						CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						)
                					)
        				)
            		),
            		CstsComplexValue.of("seqOf",
            				CstsComplexValue.of("SEQUENCE",
            					CstsComplexValue.of("pathTableCommonElements",
        							CstsStringValue.of("trackingDataPathId", "chname1"),
    								CstsStringValue.of("apertureName", "chname2"),
    								CstsStringValue.of("spaceUserNodeName", "chname3"),
    								CstsIntValue.of("reportingPeriod", 14),
    								CstsComplexValue.of("frName",
    										CstsOidValue.of("frType", new int[] {2,3,4}),
    										CstsIntValue.of("frin", 75))
    							),
            					CstsStringValue.of("threeWayRcvAperture", "chname"),
            					CstsComplexValue.of("rcvFreqBand",
            						CstsComplexValue.of("seqOf",
            							CstsComplexValue.of("SEQUENCE",
            								CstsStringValue.of("name", "chname"),
            								CstsBoolValue.of("defaultList", true),
            								CstsComplexValue.of("labels",
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
            										)
            									),
            									CstsComplexValue.of("seqOf",
        											CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                									)
                								),
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
        	        									CstsComplexValue.of("frOrProcedureType",
        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        									),
        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    								)
                    							)
            								)
            							)
            						),
            						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						),
            						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						)
            					),
            					CstsComplexValue.of("xmitFreqBand",
            							CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						),
                    						CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
                    						CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)
                    					)
            				)
                		),
            		CstsComplexValue.of("seqOf",
            				CstsComplexValue.of("SEQUENCE",
            					CstsComplexValue.of("pathTableCommonElements",
        							CstsStringValue.of("trackingDataPathId", "chname1"),
    								CstsStringValue.of("apertureName", "chname2"),
    								CstsStringValue.of("spaceUserNodeName", "chname3"),
    								CstsIntValue.of("reportingPeriod", 14),
    								CstsComplexValue.of("frName",
    										CstsOidValue.of("frType", new int[] {2,3,4}),
    										CstsIntValue.of("frin", 75))
    							),
            					CstsStringValue.of("threeWayRcvAperture", "chname"),
            					CstsComplexValue.of("rcvFreqBand",
            						CstsComplexValue.of("seqOf",
            							CstsComplexValue.of("SEQUENCE",
            								CstsStringValue.of("name", "chname"),
            								CstsBoolValue.of("defaultList", true),
            								CstsComplexValue.of("labels",
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
            										)
            									),
            									CstsComplexValue.of("seqOf",
        											CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                									)
                								),
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
        	        									CstsComplexValue.of("frOrProcedureType",
        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        									),
        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    								)
                    							)
            								)
            							)
            						),
            						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						),
            						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						)
            					),
            					CstsComplexValue.of("xmitFreqBand",
            							CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						),
                    						CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
                    						CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)
                    					)
            				)
                		)
                ),
                CstsComplexValue.of("tdmSegGenDoppInstPathTable",
                		CstsComplexValue.of("seqOf",
            				CstsComplexValue.of("SEQUENCE",
            					CstsComplexValue.of("pathTableCommonElements",
        							CstsStringValue.of("trackingDataPathId", "chname1"),
    								CstsStringValue.of("apertureName", "chname2"),
    								CstsStringValue.of("spaceUserNodeName", "chname3"),
    								CstsIntValue.of("reportingPeriod", 14),
    								CstsComplexValue.of("frName",
    										CstsOidValue.of("frType", new int[] {2,3,4}),
    										CstsIntValue.of("frin", 75))
    							),
            					CstsStringValue.of("threeWayRcvAperture", "chname"),
            					CstsComplexValue.of("rcvFreqBand",
            						CstsComplexValue.of("seqOf",
            							CstsComplexValue.of("SEQUENCE",
            								CstsStringValue.of("name", "chname"),
            								CstsBoolValue.of("defaultList", true),
            								CstsComplexValue.of("labels",
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
            										)
            									),
            									CstsComplexValue.of("seqOf",
        											CstsComplexValue.of("LabelV1",
    	        										CstsComplexValue.of("frOrProcedureType",
    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
    	        										),
    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                									)
                								),
            									CstsComplexValue.of("seqOf",
            										CstsComplexValue.of("LabelV1",
        	        									CstsComplexValue.of("frOrProcedureType",
        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        									),
        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    								)
                    							)
            								)
            							)
            						),
            						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						),
            						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						)
            					),
            					CstsComplexValue.of("xmitFreqBand",
            							CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						),
                    						CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
                    						CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)
                    					)
            				)
                		),
                		CstsComplexValue.of("seqOf",
                				CstsComplexValue.of("SEQUENCE",
                					CstsComplexValue.of("pathTableCommonElements",
            							CstsStringValue.of("trackingDataPathId", "chname1"),
        								CstsStringValue.of("apertureName", "chname2"),
        								CstsStringValue.of("spaceUserNodeName", "chname3"),
        								CstsIntValue.of("reportingPeriod", 14),
        								CstsComplexValue.of("frName",
        										CstsOidValue.of("frType", new int[] {2,3,4}),
        										CstsIntValue.of("frin", 75))
        							),
                					CstsStringValue.of("threeWayRcvAperture", "chname"),
                					CstsComplexValue.of("rcvFreqBand",
                						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						),
                						CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						),
                						CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						)
                					),
                					CstsComplexValue.of("xmitFreqBand",
                							CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
                        						CstsComplexValue.of("seqOf",
                            							CstsComplexValue.of("SEQUENCE",
                            								CstsStringValue.of("name", "chname"),
                            								CstsBoolValue.of("defaultList", true),
                            								CstsComplexValue.of("labels",
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            										)
                            									),
                            									CstsComplexValue.of("seqOf",
                        											CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                									)
                                								),
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                        	        									CstsComplexValue.of("frOrProcedureType",
                        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                        	        									),
                        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                    								)
                                    							)
                            								)
                            							)
                            						),
                        						CstsComplexValue.of("seqOf",
                            							CstsComplexValue.of("SEQUENCE",
                            								CstsStringValue.of("name", "chname"),
                            								CstsBoolValue.of("defaultList", true),
                            								CstsComplexValue.of("labels",
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            										)
                            									),
                            									CstsComplexValue.of("seqOf",
                        											CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                									)
                                								),
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                        	        									CstsComplexValue.of("frOrProcedureType",
                        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                        	        									),
                        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                    								)
                                    							)
                            								)
                            							)
                            						)
                        					)
                				)
                    		),
                		CstsComplexValue.of("seqOf",
                				CstsComplexValue.of("SEQUENCE",
                					CstsComplexValue.of("pathTableCommonElements",
            							CstsStringValue.of("trackingDataPathId", "chname1"),
        								CstsStringValue.of("apertureName", "chname2"),
        								CstsStringValue.of("spaceUserNodeName", "chname3"),
        								CstsIntValue.of("reportingPeriod", 14),
        								CstsComplexValue.of("frName",
        										CstsOidValue.of("frType", new int[] {2,3,4}),
        										CstsIntValue.of("frin", 75))
        							),
                					CstsStringValue.of("threeWayRcvAperture", "chname"),
                					CstsComplexValue.of("rcvFreqBand",
                						CstsComplexValue.of("seqOf",
                							CstsComplexValue.of("SEQUENCE",
                								CstsStringValue.of("name", "chname"),
                								CstsBoolValue.of("defaultList", true),
                								CstsComplexValue.of("labels",
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                										)
                									),
                									CstsComplexValue.of("seqOf",
            											CstsComplexValue.of("LabelV1",
        	        										CstsComplexValue.of("frOrProcedureType",
        	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        	        										),
        	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    									)
                    								),
                									CstsComplexValue.of("seqOf",
                										CstsComplexValue.of("LabelV1",
            	        									CstsComplexValue.of("frOrProcedureType",
            	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        									),
            	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        								)
                        							)
                								)
                							)
                						),
                						CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						),
                						CstsComplexValue.of("seqOf",
                    							CstsComplexValue.of("SEQUENCE",
                    								CstsStringValue.of("name", "chname"),
                    								CstsBoolValue.of("defaultList", true),
                    								CstsComplexValue.of("labels",
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                    										)
                    									),
                    									CstsComplexValue.of("seqOf",
                											CstsComplexValue.of("LabelV1",
            	        										CstsComplexValue.of("frOrProcedureType",
            	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
            	        										),
            	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        									)
                        								),
                    									CstsComplexValue.of("seqOf",
                    										CstsComplexValue.of("LabelV1",
                	        									CstsComplexValue.of("frOrProcedureType",
                	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        									),
                	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            								)
                            							)
                    								)
                    							)
                    						)
                					),
                					CstsComplexValue.of("xmitFreqBand",
                							CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
                        						CstsComplexValue.of("seqOf",
                            							CstsComplexValue.of("SEQUENCE",
                            								CstsStringValue.of("name", "chname"),
                            								CstsBoolValue.of("defaultList", true),
                            								CstsComplexValue.of("labels",
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            										)
                            									),
                            									CstsComplexValue.of("seqOf",
                        											CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                									)
                                								),
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                        	        									CstsComplexValue.of("frOrProcedureType",
                        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                        	        									),
                        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                    								)
                                    							)
                            								)
                            							)
                            						),
                        						CstsComplexValue.of("seqOf",
                            							CstsComplexValue.of("SEQUENCE",
                            								CstsStringValue.of("name", "chname"),
                            								CstsBoolValue.of("defaultList", true),
                            								CstsComplexValue.of("labels",
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            										)
                            									),
                            									CstsComplexValue.of("seqOf",
                        											CstsComplexValue.of("LabelV1",
                    	        										CstsComplexValue.of("frOrProcedureType",
                    	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        										),
                    	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                									)
                                								),
                            									CstsComplexValue.of("seqOf",
                            										CstsComplexValue.of("LabelV1",
                        	        									CstsComplexValue.of("frOrProcedureType",
                        	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                        	        									),
                        	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                    								)
                                    							)
                            								)
                            							)
                            						)
                        					)
                				)
                    		)
                    )
          ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenCarrierPowerPathTableParamOid,
        		CstsComplexValue.of("tdmSegGenCarrierPowerPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59)))),
        		CstsComplexValue.of("tdmSegGenCarrierPowerPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))))
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenPcOverN0PathTableParamOid,
        		CstsComplexValue.of("tdmSegGenPcOverN0PathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59)))),
        		CstsComplexValue.of("tdmSegGenPcOverN0PathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))))
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSeqGenRcvFreqPathTableParamOid,
        		CstsComplexValue.of("tdmSeqGenRcvFreqPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59)))),
        		CstsComplexValue.of("tdmSeqGenRcvFreqPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("rcvFreqBand", 59))))
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenXmitFreqPathTableParamOid,
        		CstsComplexValue.of("tdmSegGenXmitFreqPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59)))),
        		CstsComplexValue.of("tdmSegGenXmitFreqPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsIntValue.of("xmitFreqBand", 59))))
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenDoppIntegPathTableParamOid,
        		CstsComplexValue.of("tdmSegGenDoppIntegPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsIntValue.of("rcvFreqBand", 59),
	        						CstsIntValue.of("xmitFreqBand", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsIntValue.of("rcvFreqBand", 59),
	        						CstsIntValue.of("xmitFreqBand", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsIntValue.of("rcvFreqBand", 59),
	        						CstsIntValue.of("xmitFreqBand", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59)))),
        		CstsComplexValue.of("tdmSegGenDoppIntegPathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsIntValue.of("rcvFreqBand", 59),
	        						CstsIntValue.of("xmitFreqBand", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsIntValue.of("rcvFreqBand", 59),
	        						CstsIntValue.of("xmitFreqBand", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsIntValue.of("rcvFreqBand", 59),
	        						CstsIntValue.of("xmitFreqBand", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))))
                ));
        testParameters.add(new TestParameter(Fr.TdmSegmentGen.parameter.tdmSegGenRangePathTableParamOid,
        		CstsComplexValue.of("tdmSegGenRangePathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsComplexValue.of("rcvFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsComplexValue.of("xmitFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsIntValue.of("rangeUnits", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsComplexValue.of("rcvFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsComplexValue.of("xmitFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsIntValue.of("rangeUnits", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsComplexValue.of("rcvFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsComplexValue.of("xmitFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsIntValue.of("rangeUnits", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59)))),
        		CstsComplexValue.of("tdmSegGenRangePathTable",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsComplexValue.of("rcvFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsComplexValue.of("xmitFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsIntValue.of("rangeUnits", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsComplexValue.of("rcvFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsComplexValue.of("xmitFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsIntValue.of("rangeUnits", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
	        						CstsComplexValue.of("pathTableCommonElements", 
	        								CstsStringValue.of("trackingDataPathId", "chname1"),
	        								CstsStringValue.of("apertureName", "chname2"),
	        								CstsStringValue.of("spaceUserNodeName", "chname3"),
	        								CstsIntValue.of("reportingPeriod", 14),
	        								CstsComplexValue.of("frName",
	        										CstsOidValue.of("frType", new int[] {2,3,4}),
	        										CstsIntValue.of("frin", 75))
	        								),
	        						CstsStringValue.of("threeWayRcvAperture","chname4"),
	        						CstsComplexValue.of("rcvFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsComplexValue.of("xmitFreqBand",
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						),
	        								CstsComplexValue.of("seqOf",
                        							CstsComplexValue.of("SEQUENCE",
                        								CstsStringValue.of("name", "chname"),
                        								CstsBoolValue.of("defaultList", true),
                        								CstsComplexValue.of("labels",
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                        										)
                        									),
                        									CstsComplexValue.of("seqOf",
                    											CstsComplexValue.of("LabelV1",
                	        										CstsComplexValue.of("frOrProcedureType",
                	        											CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                	        										),
                	        										CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                            									)
                            								),
                        									CstsComplexValue.of("seqOf",
                        										CstsComplexValue.of("LabelV1",
                    	        									CstsComplexValue.of("frOrProcedureType",
                    	        										CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
                    	        									),
                    	        									CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
                                								)
                                							)
                        								)
                        							)
                        						)),
	        						CstsIntValue.of("rangeUnits", 59),
	        						CstsIntValue.of("integrationInterval", 59),
	        						CstsIntValue.of("integrationRef", 59))))
                ));
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
    
}
