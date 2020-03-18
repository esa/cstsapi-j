package esa.egos.csts.sim.impl.frm;

import java.util.HashMap;
import java.util.Map;

public class FunctionalResourceBitLabels {

	public static final Map<String, String> FrRemap;

	public static final Map<Integer, String> FrMap;

	static {

		// common parameter/evemt prefixes identifying FRs, needs to be re-mapped to correct FR name
		// NOT USED
		FrRemap = new HashMap<>();
		FrRemap.put("ant", "Antenna");
		FrRemap.put("fwd401CarrierXmit", "Fwd401SpaceLinkCarrierXmit");
		FrRemap.put("fwdLinkRng", "FwdLinkRng");
		FrRemap.put("rtn401", "Rtn401SpaceLinkCarrierRcpt");
		FrRemap.put("rtnRngAndDopplerExtraction", "RtnRngAndDopplerExtraction");
		FrRemap.put("fwdTcPlopSync", "FwdTcPlopSyncAndChnlEncode");
		FrRemap.put("", "FwdFlfFrameSyncChnEncodeAndOidGen");
		FrRemap.put("rtnTmSync", "RtnTmSyncAndChnlDecode");
		FrRemap.put("fwdTcMcMux", "FwdTcMcMux");
		FrRemap.put("fwdTcVcM", "FwdTcVcMux");
		FrRemap.put("", "FwdTcEncapVcPktProcVcGen");
		FrRemap.put("", "FwdMapMux");
		FrRemap.put("", "FwdEncapMapPktProc");
		FrRemap.put("", "FwdAosMcMux");
		FrRemap.put("", "FwdAosVcMux");
		FrRemap.put("", "FwdAosEncapPktProcAndVcGen");
		FrRemap.put("", "FwdUslpMcMux");
		FrRemap.put("", "FwdUslpVcMux");
		FrRemap.put("", "FwdUslpVcGen");
		FrRemap.put("", "FwdUslpEncapAndMapPktProc");
		FrRemap.put("rtnTmMcDemux", "RtnTmMcDemux");
		FrRemap.put("rtnTmVcDemux", "RtnTmVcDemux");
		FrRemap.put("", "RtnPktExtAndDeencap");
		FrRemap.put("", "FrameDataSink");
		FrRemap.put("", "CfdpSendEntity");
		FrRemap.put("", "FwdFileSvcProd");
		FrRemap.put("", "CfdpRcvEntity");
		FrRemap.put("", "RtnFileSvcProd");
		FrRemap.put("", "TdmSegmentGen");
		FrRemap.put("", "NonValRmDataCollection");
		FrRemap.put("", "DdorRawDataCollection");
		FrRemap.put("", "OpenLoopRxFormatter");
		FrRemap.put("", "OfflineFrameBuffer");
		FrRemap.put("", "TdmRcrdBuffer");
		FrRemap.put("", "NonValRmDataStore");
		FrRemap.put("", "ValRmDataStore");
		FrRemap.put("", "DdorRawDataStore");
		FrRemap.put("", "OpenLoopDataStore");
		FrRemap.put("", "RtnFileDataStore");
		FrRemap.put("", "FwdFileDataStore");
		FrRemap.put("", "FspTsProvider");
		FrRemap.put("fwdCltu", "FwdCltuTsProvider");
		FrRemap.put("", "FwdFrameCstsProvider");
		FrRemap.put("", "TgftHost");
		FrRemap.put("raf", "RafTsProvider");
		FrRemap.put("rcf", "RcfTsProvider");
		FrRemap.put("rocf", "RocfTsProvider");
		FrRemap.put("", "TdCstsProvider");
		FrRemap.put("", "MdCstsProvider");
		FrRemap.put("", "MdCollection");
		FrRemap.put("", "SvcContrCstsProvider");
		FrRemap.put("", "SvcContrProd");
		 
		// clear undefined FR
		FrRemap.remove("");

		// -1 unknown FT bit number, no generated parameters, parameters are described in functional_resources_css.html
		//-10 unknown FT bit number, no generated parameters, parameters are NOT described in functional_resources_css.html
		// USED
		FrMap = new HashMap<>();
		FrMap.put( 1000, "Antenna");
		FrMap.put( 2000, "Fwd401SpaceLinkCarrierXmit");
		FrMap.put( 2001, "FwdLinkRng");
		FrMap.put( 4000, "Rtn401SpaceLinkCarrierRcpt");
		FrMap.put( 4001, "RtnRngAndDopplerExtraction");
		FrMap.put( 6000, "FwdTcPlopSyncAndChnlEncode");
		FrMap.put( 7000, "FwdFlfFrameSyncChnEncodeAndOidGen");
		FrMap.put( 8000, "RtnTmSyncAndChnlDecode");
		FrMap.put( 9000, "FwdTcMcMux");
		FrMap.put( 9001, "FwdTcVcMux");
		FrMap.put(   -1, "FwdTcEncapVcPktProcVcGen");
		FrMap.put(  -10, "FwdMapMux");
		FrMap.put(   -1, "FwdEncapMapPktProc");
		FrMap.put(   -1, "FwdAosMcMux");
		FrMap.put(   -1, "FwdAosVcMux");
		FrMap.put(   -1, "FwdAosEncapPktProcAndVcGen");
		FrMap.put(  -10, "FwdUslpMcMux");
		FrMap.put(  -10, "FwdUslpVcMux");
		FrMap.put(  -10, "FwdUslpVcGen");
		FrMap.put(  -10, "FwdUslpEncapAndMapPktProc");
		FrMap.put(13000, "RtnTmMcDemux");
		FrMap.put(13001, "RtnTmVcDemux");
		FrMap.put(13002, "RtnPktExtAndDeencap");
		FrMap.put(17000, "FrameDataSink");
		FrMap.put(   -1, "CfdpSendEntity");
		FrMap.put(  -10, "FwdFileSvcProd");
		FrMap.put(   -1, "CfdpRcvEntity");
		FrMap.put(  -10, "RtnFileSvcProd");
		FrMap.put(  -10, "TdmSegmentGen");
		FrMap.put(  -10, "NonValRmDataCollection");
		FrMap.put(  -10, "DdorRawDataCollection");
		FrMap.put(   -1, "OpenLoopRxFormatter");
		FrMap.put(  -10, "OfflineFrameBuffer");
		FrMap.put(  -10, "TdmRcrdBuffer");
		FrMap.put(  -10, "NonValRmDataStore");
		FrMap.put(  -10, "ValRmDataStore");
		FrMap.put(  -10, "DdorRawDataStore");
		FrMap.put(  -10, "OpenLoopDataStore");
		FrMap.put(  -10, "RtnFileDataStore");
		FrMap.put(  -10, "FwdFileDataStore");
		FrMap.put(32000, "FspTsProvider");
		FrMap.put(33000, "FwdCltuTsProvider");
		FrMap.put(  -10, "FwdFrameCstsProvider");
		FrMap.put(  -10, "TgftHost");
		FrMap.put(36000, "RafTsProvider");
		FrMap.put(37000, "RcfTsProvider");
		FrMap.put(38000, "RocfTsProvider");
		FrMap.put(   -1, "TdCstsProvider");
		FrMap.put(  -10, "MdCstsProvider");
		FrMap.put(   -1, "MdCollection");
		FrMap.put(   -1, "SvcContrCstsProvider");
		FrMap.put(   -1, "SvcContrProd");

		// clear undefined FR
		FrMap.remove(-1);
		FrMap.remove(-10);
	}
}
