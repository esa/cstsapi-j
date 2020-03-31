package esa.egos.csts.sim.impl.frm;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;

public class Fr 
{
    public static final FunctionalResourceType antArray = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1001));
    public static final FunctionalResourceType antenna = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000));
    public static final FunctionalResourceType ccsds401SpaceLinkCarrierRcpt = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000));
    public static final FunctionalResourceType ccsds401SpaceLinkCarrierXmit = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000));
    public static final FunctionalResourceType cfdpRcvEntity = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 19000));
    public static final FunctionalResourceType cfdpSendEntity = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 18000));
    public static final FunctionalResourceType ddorRawDataCollection = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 22000));
    public static final FunctionalResourceType ddorRawDataStore = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 28000));
    public static final FunctionalResourceType flfFrameSyncChnEncodeAndOidGen = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000));
    public static final FunctionalResourceType flfSyncAndChnlDecode = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000));
    public static final FunctionalResourceType frameDataSink = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 17000));
    public static final FunctionalResourceType fspTsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 32000));
    public static final FunctionalResourceType fwdAosEncapPktProcAndVcGen = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 10002));
    public static final FunctionalResourceType fwdAosMcMux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 10000));
    public static final FunctionalResourceType fwdAosVcMux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 10001));
    public static final FunctionalResourceType fwdCltuTsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000));
    public static final FunctionalResourceType fwdEncapMapPktProc = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9004));
    public static final FunctionalResourceType fwdFileDataStore = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 31000));
    public static final FunctionalResourceType fwdFileSvcProd = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 18001));
    public static final FunctionalResourceType fwdMapMux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9003));
    public static final FunctionalResourceType fwdTcEncapVcPktProcVcGen = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9002));
    public static final FunctionalResourceType fwdUslpEncapAndMapPktProc = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 11004));
    public static final FunctionalResourceType fwdUslpMcMux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 11000));
    public static final FunctionalResourceType fwdUslpVcGen = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 11002));
    public static final FunctionalResourceType fwdUslpVcMux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 11001));
    public static final FunctionalResourceType mdCollection = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 40001));
    public static final FunctionalResourceType mdCstsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 40000));
    public static final FunctionalResourceType nonValRmDataCollection = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 21000));
    public static final FunctionalResourceType nonValRmDataStore = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 26000));
    public static final FunctionalResourceType offlineFrameBuffer = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 24000));
    public static final FunctionalResourceType openLoopDataStore = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 29000));
    public static final FunctionalResourceType openLoopRxFormatter = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 23000));
    public static final FunctionalResourceType rafTsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000));
    public static final FunctionalResourceType rcfTsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000));
    public static final FunctionalResourceType rngAndDopplerExtraction = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001));
    public static final FunctionalResourceType rngXmit = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2001));
    public static final FunctionalResourceType rocfTsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000));
    public static final FunctionalResourceType rtnFileDataStore = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 30000));
    public static final FunctionalResourceType rtnFileSvcProd = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 19001));
    public static final FunctionalResourceType rtnPktExtAndDeencap = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13002));
    public static final FunctionalResourceType svcContrCstsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 41000));
    public static final FunctionalResourceType svcContrProd = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 41001));
    public static final FunctionalResourceType tcMcMux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9000));
    public static final FunctionalResourceType tcPlopSyncAndChnlEncode = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000));
    public static final FunctionalResourceType tcVcMux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001));
    public static final FunctionalResourceType tdCstsProvider = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 39000));
    public static final FunctionalResourceType tdmRcrdBuffer = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 25000));
    public static final FunctionalResourceType tdmSegmentGen = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 20000));
    public static final FunctionalResourceType tgftHost = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 35000));
    public static final FunctionalResourceType tmAosMcDemux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13000));
    public static final FunctionalResourceType tmAosVcDemux = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13001));
    public static final FunctionalResourceType valRmDataStore = FunctionalResourceType.of(ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 27000));

    public static class Antenna 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier antElevationAberration = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 14, 1, 1);
            public static final ObjectIdentifier antPointingMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 15, 1, 1);
            public static final ObjectIdentifier antClosedLoopConfiguration = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 16, 1, 1);
            public static final ObjectIdentifier antTrackingRxMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 17, 1, 1);
            public static final ObjectIdentifier antTrackingSignalPolarization = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 18, 1, 1);
            public static final ObjectIdentifier antTrackingRxInpLevel = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 19, 1, 1);
            public static final ObjectIdentifier antTrackingRxNominalFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 20, 1, 1);
            public static final ObjectIdentifier antTrackingRxFreqSearchRange = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 21, 1, 1);
            public static final ObjectIdentifier antCommandedElevation = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 6, 1, 1);
            public static final ObjectIdentifier antContrAzimuth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 7, 1, 1);
            public static final ObjectIdentifier antContrElevation = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 8, 1, 1);
            public static final ObjectIdentifier antContrAzimuthRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 9, 1, 1);
            public static final ObjectIdentifier antContrElevationRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 10, 1, 1);
            public static final ObjectIdentifier antAzimuthResidual = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 11, 1, 1);
            public static final ObjectIdentifier antElevationResidual = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 12, 1, 1);
            public static final ObjectIdentifier antAzimuthAberration = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 13, 1, 1);
            public static final ObjectIdentifier antAccumulatedPrecipitation = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 30, 1, 1);
            public static final ObjectIdentifier antPrecipitationRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 31, 1, 1);
            public static final ObjectIdentifier antRelativeHumidity = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 32, 1, 1);
            public static final ObjectIdentifier antAmbientTemperature = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 34, 1, 1);
            public static final ObjectIdentifier antTrackingRxPredictMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 22, 1, 1);
            public static final ObjectIdentifier antTrackingRxLoopBwdth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 23, 1, 1);
            public static final ObjectIdentifier antTrackingRxOrderOfLoop = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 24, 1, 1);
            public static final ObjectIdentifier antTrackingRxLockStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 25, 1, 1);
            public static final ObjectIdentifier antWindIntegrationTime = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 26, 1, 1);
            public static final ObjectIdentifier antMeanWindSpeed = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 27, 1, 1);
            public static final ObjectIdentifier antPeakWindSpeed = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 28, 1, 1);
            public static final ObjectIdentifier antWindDirection = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 29, 1, 1);
            public static final ObjectIdentifier antResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 1, 1, 1);
            public static final ObjectIdentifier antId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 2, 1, 1);
            public static final ObjectIdentifier antActualAzimuth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 3, 1, 1);
            public static final ObjectIdentifier antActualElevation = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 4, 1, 1);
            public static final ObjectIdentifier antCommandedAzimuth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 1, 5, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier antEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 2, 1, 1, 1);
            public static final ObjectIdentifier antEventTrackingRxLockStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 2, 2, 1, 1);
            public static final ObjectIdentifier antWindSpeedCriticality = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 1000, 2, 3, 1, 1);
        }
    }

    public static class Ccsds401SpaceLinkCarrierRcpt 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier ccsds401CarrierRcptNominalFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 9, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptSystemNoiseTemperature = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 8, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptSignalLevelResidual = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 7, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptExpectedSignalLevel = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 6, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptOrderOfLoop = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 13, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptTrackingLoopBwdth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 12, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptPredictMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 11, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptFreqSearchRange = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 10, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 1, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptPolarizationAngle = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 5, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptModulationType = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 3, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptPhysChnlName = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 2, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptNominalSymbolRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 26, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptActualSymbolRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 27, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptSymbolSynchronizerLoopBwdth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 28, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptSymbolLoopMeanPhaseError = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 29, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptActualSubcarrierFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 22, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptSubcarrierDemodLoopBwdth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 23, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptSubcarrierLoopMeanPhaseError = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 24, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptSubcarrierLevelEstimate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 25, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptActualFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 17, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptCarrierLoopMeanPhaseError = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 16, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptCarrierLoopSnr = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 15, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptLockStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 14, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptBestLockFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 20, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptDopplerStdDeviation = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 19, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptFreqOffset = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 18, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptExpectedEsOverNo = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 1, 30, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier ccsds401CarrierRcptEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 2, 1, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierRcptEventLockStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4000, 2, 2, 1, 1);
        }
    }

    public static class Ccsds401SpaceLinkCarrierXmit 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier ccsds401CarrierXmitSymbolRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 18, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitSymbolStreamModType = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 14, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitRngModIndex = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 15, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitSubcarrierFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 16, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitSubcarrierMod = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 17, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitPhysChnlName = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 2, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 3, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitEirp = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 4, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitPolarization = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 5, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 1, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitSweepProfile = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 10, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitMod = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 11, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitModInputStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 12, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitTcPriority = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 13, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitActualCarrierFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 6, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitNominalCarrierFreq = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 7, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitReferenceFreqLock = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 8, 1, 1);
            public static final ObjectIdentifier ccsds401CarrierXmitSweepProcStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 1, 9, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier ccsds401CarrierXmitEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2000, 2, 1, 1, 1);
        }
    }

    public static class FlfFrameSyncChnEncodeAndOidGen 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier fwdAosSyncFrameEncoding = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 5, 1, 1);
            public static final ObjectIdentifier fwdAosFrameRandomization = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 4, 1, 1);
            public static final ObjectIdentifier fwdAosSyncCaduLength = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 3, 1, 1);
            public static final ObjectIdentifier fwdAosSyncPhysicalChnlName = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 2, 1, 1);
            public static final ObjectIdentifier fwdAosSyncFrameLdpcCodeRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 10, 1, 1);
            public static final ObjectIdentifier fwdAosSyncFrameTurboCodeRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 9, 1, 1);
            public static final ObjectIdentifier fwdAosSyncFrameRsInterleavingDepth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 8, 1, 1);
            public static final ObjectIdentifier fwdAosSyncFrameRsErrorCorrectionCapability = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 7, 1, 1);
            public static final ObjectIdentifier fwdAosSyncFrameConvolCodeRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 1, 6, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier fwdAosSyncResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 7000, 2, 1, 1, 1);
        }
    }

    public static class FlfSyncAndChnlDecode 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier flfSyncCaduLength = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 9, 1, 1);
            public static final ObjectIdentifier flfSyncErtAnnotationLockedToReference = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 8, 1, 1);
            public static final ObjectIdentifier flfSyncNumberOfRsErrorsCorrected = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 7, 1, 1);
            public static final ObjectIdentifier flfSyncFrameErrorRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 6, 1, 1);
            public static final ObjectIdentifier flfSyncDecodeQualityIndications = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 13, 1, 1);
            public static final ObjectIdentifier flfSyncFecfPresent = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 12, 1, 1);
            public static final ObjectIdentifier flfSyncDecode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 11, 1, 1);
            public static final ObjectIdentifier flfSyncDerandomization = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 10, 1, 1);
            public static final ObjectIdentifier flfSyncResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 1, 1, 1);
            public static final ObjectIdentifier flfSyncSymbolInversion = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 5, 1, 1);
            public static final ObjectIdentifier flfSyncFrameSyncLockStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 4, 1, 1);
            public static final ObjectIdentifier flfSyncAsmConfig = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 2, 1, 1);
            public static final ObjectIdentifier flfSyncAsmCorrelationError = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 1, 3, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier flfSyncFrameSyncLockStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 2, 2, 1, 1);
            public static final ObjectIdentifier flfSyncEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 8000, 2, 1, 1, 1);
        }
    }

    public static class FrameDataSink 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier servicePackageId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 17000, 1, 1, 1, 1);
        }
    }

    public static class FwdCltuTsProvider 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier fwdCltuNotificationMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 8, 1, 1);
            public static final ObjectIdentifier fwdCltuProtocolAbortMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 9, 1, 1);
            public static final ObjectIdentifier fwdCltuRtnTimeoutPeriod = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 6, 1, 1);
            public static final ObjectIdentifier fwdCltuDeliveryMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 7, 1, 1);
            public static final ObjectIdentifier fwdCltuExpectedCltuIdentification = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 12, 1, 1);
            public static final ObjectIdentifier fwdCltuExpectedEventInvocationId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 13, 1, 1);
            public static final ObjectIdentifier fwdCltuSiState = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 10, 1, 1);
            public static final ObjectIdentifier fwdCltuReportingCycle = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 11, 1, 1);
            public static final ObjectIdentifier fwdCltuNumberOfCltusRadiated = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 16, 1, 1);
            public static final ObjectIdentifier fwdCltuNumberOfCltusReceived = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 14, 1, 1);
            public static final ObjectIdentifier fwdCltuNumberOfCltusProcessed = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 15, 1, 1);
            public static final ObjectIdentifier fwdCltuProductionStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 1, 1, 1);
            public static final ObjectIdentifier fwdCltuResponderId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 4, 1, 1);
            public static final ObjectIdentifier fwdCltuResponderPortId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 5, 1, 1);
            public static final ObjectIdentifier fwdCltuServiceInstanceId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 2, 1, 1);
            public static final ObjectIdentifier fwdCltuInitiatorId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 33000, 1, 3, 1, 1);
        }
    }

    public static class RafTsProvider 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier rafNumberOfFramesDelivered = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 15, 1, 1);
            public static final ObjectIdentifier rafNumberOfErrorFreeFramesDelivered = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 14, 1, 1);
            public static final ObjectIdentifier rafInitiatorId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 3, 1, 1);
            public static final ObjectIdentifier rafServiceInstanceIdentifier = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 2, 1, 1);
            public static final ObjectIdentifier rafResponderPortId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 5, 1, 1);
            public static final ObjectIdentifier rafResponderId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 4, 1, 1);
            public static final ObjectIdentifier rafRequestedFrameQuality = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 11, 1, 1);
            public static final ObjectIdentifier rafPermittedFrameQuality = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 10, 1, 1);
            public static final ObjectIdentifier rafReportingCycle = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 13, 1, 1);
            public static final ObjectIdentifier rafSiState = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 12, 1, 1);
            public static final ObjectIdentifier rafDeliveryMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 7, 1, 1);
            public static final ObjectIdentifier rafRtnTimeoutPeriod = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 6, 1, 1);
            public static final ObjectIdentifier rafTransferBufferSize = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 9, 1, 1);
            public static final ObjectIdentifier rafLatencyLimit = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 36000, 1, 8, 1, 1);
        }
    }

    public static class RcfTsProvider 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier rcfProductionStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 1, 1, 1);
            public static final ObjectIdentifier rcfInitiatorId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 3, 1, 1);
            public static final ObjectIdentifier rcfServiceInstanceId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 2, 1, 1);
            public static final ObjectIdentifier rcfResponderPortId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 5, 1, 1);
            public static final ObjectIdentifier rcfResponderId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 4, 1, 1);
            public static final ObjectIdentifier rcfDeliveryMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 7, 1, 1);
            public static final ObjectIdentifier rcfRtnTimeoutPeriod = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 6, 1, 1);
            public static final ObjectIdentifier rcfTransferBufferSize = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 9, 1, 1);
            public static final ObjectIdentifier rcfLatencyLimit = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 8, 1, 1);
            public static final ObjectIdentifier rcfRequestedGlobalVcid = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 11, 1, 1);
            public static final ObjectIdentifier rcfPermittedGlobalVcidSet = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 10, 1, 1);
            public static final ObjectIdentifier rcfReportingCycle = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 13, 1, 1);
            public static final ObjectIdentifier rcfSiState = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 12, 1, 1);
            public static final ObjectIdentifier rcfNumberOfFramesDelivered = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 1, 14, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier rocfEventProductionStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 37000, 2, 1, 1, 1);
        }
    }

    public static class RngAndDopplerExtraction 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier rngAndDopplerExtractionRngSignalAcquisitionProbability = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 6, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionLoopSettlingTime = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 8, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionOpenLoopTime = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 7, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionLoopLockStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 9, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionToneIntegrationTime = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 12, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionExpectedRngModIndex = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 11, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionPresteering = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 2, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 1, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionRngLoopBwdth = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 4, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionRngPowerOverNoResidual = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 3, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionObservableAndResidual = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 21, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionObservablesCount = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 22, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionAmbiguityResolved = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 15, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionSpacecraftTransponderMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 16, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionPnCodeIntegrationTime = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 13, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionCodeNumberCorrelated = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 14, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionDopplerMeasurementSamplingRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 19, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionDataCollection = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 20, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionExpectedSpacecraftPnAcqDuration = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 17, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionRngMeasurementSamplingRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 1, 18, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier rngAndDopplerExtractionEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 2, 1, 1, 1);
            public static final ObjectIdentifier rngAndDopplerExtractionStatChangeValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 4001, 2, 2, 1, 1);
        }
    }

    public static class RngXmit 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier rngXmitRngType = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2001, 1, 3, 1, 1);
            public static final ObjectIdentifier rngXmitPnChipRate = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2001, 1, 4, 1, 1);
            public static final ObjectIdentifier rngXmitResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2001, 1, 1, 1, 1);
            public static final ObjectIdentifier rngXmitMod = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2001, 1, 2, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier rngXmitEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 2001, 2, 1, 1, 1);
        }
    }

    public static class RocfTsProvider 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier rocfReportingCycle = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 19, 1, 1);
            public static final ObjectIdentifier rocfSiState = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 18, 1, 1);
            public static final ObjectIdentifier rocfNumberOfOcfsDelivered = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 20, 1, 1);
            public static final ObjectIdentifier rocfRequestedTcVcid = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 15, 1, 1);
            public static final ObjectIdentifier rocfPermittedTcVcidSet = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 14, 1, 1);
            public static final ObjectIdentifier rocfRequestedUpdateMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 17, 1, 1);
            public static final ObjectIdentifier rocfPermittedUpdateMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 16, 1, 1);
            public static final ObjectIdentifier rocfRequestedGlobalVcid = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 11, 1, 1);
            public static final ObjectIdentifier rocfPermittedGlobalVcidSet = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 10, 1, 1);
            public static final ObjectIdentifier rocfRequestedContrWordType = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 13, 1, 1);
            public static final ObjectIdentifier rocfPermittedContrWordTypeSet = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 12, 1, 1);
            public static final ObjectIdentifier rocfDeliveryMode = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 7, 1, 1);
            public static final ObjectIdentifier rocfRtnTimeoutPeriod = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 6, 1, 1);
            public static final ObjectIdentifier rocfTransferBufferSize = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 9, 1, 1);
            public static final ObjectIdentifier rocfLatencyLimit = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 8, 1, 1);
            public static final ObjectIdentifier rocfInitiatorId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 3, 1, 1);
            public static final ObjectIdentifier rocfServiceInstanceId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 2, 1, 1);
            public static final ObjectIdentifier rocfResponderPortId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 5, 1, 1);
            public static final ObjectIdentifier rocfResponderId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 4, 1, 1);
            public static final ObjectIdentifier rocfProductionStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 38000, 1, 1, 1, 1);
        }
    }

    public static class RtnPktExtAndDeencap 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier rfspApidSet = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13002, 1, 1, 1, 1);
        }
    }

    public static class TcMcMux 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier tcMcMuxContr = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9000, 1, 4, 1, 1);
            public static final ObjectIdentifier tcMcMuxPresenceOfFec = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9000, 1, 5, 1, 1);
            public static final ObjectIdentifier tcMcMuxMaxNumberOfFramesPerCltu = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9000, 1, 2, 1, 1);
            public static final ObjectIdentifier tcMcMuxMaxFrameLength = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9000, 1, 3, 1, 1);
            public static final ObjectIdentifier tcMcMuxResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9000, 1, 1, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier tcMcMuxEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9000, 2, 1, 1, 1);
        }
    }

    public static class TcPlopSyncAndChnlEncode 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier tcPlopSyncResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 1, 1, 1);
            public static final ObjectIdentifier tcPlopSyncMaxCltuRepetitions = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 5, 1, 1);
            public static final ObjectIdentifier tcPlopSyncEncodeType = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 4, 1, 1);
            public static final ObjectIdentifier tcPlopSyncMaxNumberOfFramesPerCltu = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 3, 1, 1);
            public static final ObjectIdentifier tcPlopSyncMaxCltuLength = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 2, 1, 1);
            public static final ObjectIdentifier tcPlopSyncTcLinkStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 9, 1, 1);
            public static final ObjectIdentifier tcPlopSyncMinDelayTime = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 8, 1, 1);
            public static final ObjectIdentifier tcPlopSyncAcqAndIdlePattern = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 7, 1, 1);
            public static final ObjectIdentifier tcPlopSyncPlop = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 1, 6, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier tcPlopSyncEventTcLinkStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 2, 2, 1, 1);
            public static final ObjectIdentifier tcPlopSyncEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 2, 1, 1, 1);
            public static final ObjectIdentifier tcPlopSyncDataUnitIdAndSourceValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 6000, 2, 3, 1, 1);
        }
    }

    public static class TcVcMux 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier tcVcMuxMc = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001, 1, 5, 1, 1);
            public static final ObjectIdentifier tcVcMuxContr = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001, 1, 6, 1, 1);
            public static final ObjectIdentifier tcVcMuxResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001, 1, 1, 1, 1);
            public static final ObjectIdentifier tcVcMaxFrameLength = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001, 1, 2, 1, 1);
            public static final ObjectIdentifier tcVcMuxAdFrameRepetitions = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001, 1, 3, 1, 1);
            public static final ObjectIdentifier tcVcMuxBcFrameRepetitions = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001, 1, 4, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier tcVcMuxEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 9001, 2, 1, 1, 1);
        }
    }

    public static class TmAosMcDemux 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier tmAosMcDemuxMcIds = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13000, 1, 2, 1, 1);
            public static final ObjectIdentifier tmAosMcDemuxClcwExtraction = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13000, 1, 3, 1, 1);
            public static final ObjectIdentifier tmAosMcDemuxResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13000, 1, 1, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier tmAosMcDemuxEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13000, 2, 1, 1, 1);
        }
    }

    public static class TmAosVcDemux 
    {

        public static class parameter 
        {
            public static final ObjectIdentifier tmAosVcDemuxResourceStat = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13001, 1, 1, 1, 1);
            public static final ObjectIdentifier tmAosVcDemuxVcId = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13001, 1, 2, 1, 1);
            public static final ObjectIdentifier tmAosVcDemuxClcwExtraction = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13001, 1, 3, 1, 1);
            public static final ObjectIdentifier tmAosVcDemuxGvcid = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13001, 1, 4, 1, 1);
        }

        public static class event 
        {
            public static final ObjectIdentifier tmAosVcDemuxEventResourceStatValue = ObjectIdentifier.of(OIDs.crossSupportFunctionalities, 13001, 2, 1, 1, 1);
        }
    }
}
