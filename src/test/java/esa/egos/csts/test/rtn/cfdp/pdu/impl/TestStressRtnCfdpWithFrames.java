package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;
import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.CSTS_LOG;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.app.si.rtn.cfdp.pdu.ICfpdPduReceiver;
import esa.egos.csts.app.si.rtn.cfdp.pdu.IRtnCfdpPduProduction;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduDeliveryProcedureConfig;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiProvider;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiUser;
import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame;
import esa.egos.csts.rtn.cfdp.ccsds.frames.FrameConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.PacketConfigData;
import esa.egos.csts.rtn.cfdp.extraction.CfdpPduExtractAndTransfer;
import esa.egos.csts.rtn.cfdp.reduction.CfdpPduPackAndTransfer;


public class TestStressRtnCfdpWithFrames {
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;
    private ICstsApi userApi;
    
    Lock testConditionLock = new ReentrantLock();
    Condition testCondition = testConditionLock.newCondition();

	private long numFramesReceived;
	
	private long numBytesReceived;

	private long numFramesSent;
	
	
	
	private static class StopWatcher {
		public Instant startTransmit;
		public Instant stopTransmit;
		public Instant startReceive;
		public Instant stopReceive;
	}
	
	private StopWatcher swatcher;

    @BeforeClass
    public static void setUpClass() throws ApiException
    {
        TestBootstrap.initCs();
    }

	/**
	 * Initializes the provider API and the use API and starts the communication server
	 * @throws ApiException
	 */
	@Before
	public void init() throws ApiException {
		File file = new File("src/test/resources/ProviderConfig2.xml");
		String providerConfigName = file.getAbsolutePath();

		file = new File("src/test/resources/UserConfig2.xml");
		String userConfigName = file.getAbsolutePath();

		file = new File("src/test/resources/log.properties");
		System.setProperty("java.util.logging.config.file", file.getAbsolutePath());
		
		System.out.println("provider config: "  + providerConfigName);
		System.out.println("user config: "  + userConfigName);
		
		providerApi = new CstsApi("Test Service Provider API", AppRole.PROVIDER);
		providerApi.initialize(providerConfigName);
		providerApi.start();

		userApi = new CstsApi("Test Service User API", AppRole.USER);
		userApi.initialize(userConfigName);
		userApi.start();
		
		System.out.println("CSTS user and provider API started");
		
		 swatcher = new StopWatcher();
	}

	/**
	 * Stops the provider and user API
	 */
	@After
	public void finalize() {
		providerApi.stop();
		userApi.stop();
		System.out.println("CSTS user and provider API stopped");		
	}

	//-agentlib:hprof=cpu=times -XX:+UseG1GC
	//-XX:+UseG1GC -DlistRcvDataCapacity=25000
	
	@Test
	@Ignore
	public void testOneFrameMaxSize() {
		implementRtnCfdpPduDataTransfer(1, 16384);
	}
	
	@Test
	@Ignore
	public void testTwoFramesMaxSize() {
		implementRtnCfdpPduDataTransfer(2, 16384);
	}
	
	@Test
	@Ignore
	public void test1kFramesMaxSize() {
		implementRtnCfdpPduDataTransfer(1000, 16384);
	}
	
	@Test
	@Ignore
	public void test2kFramesMaxSize() {
		implementRtnCfdpPduDataTransfer(2000, 16384);
	}
	
	@Test
	public void test10kFramesMaxSize() {
		implementRtnCfdpPduDataTransfer(10000, 16384);
	}
	
	
	public void implementRtnCfdpPduDataTransfer(int nPdus, int pduSize) {
		
		
			RtnCfdpPduDeliveryProcedureConfig rtnCfdpProcedureConfig = new RtnCfdpPduDeliveryProcedureConfig();
			rtnCfdpProcedureConfig.setLatencyLimit(1);
			rtnCfdpProcedureConfig.setReturnBufferSize(5);
		
			System.out.println("Test return Cfdp Pdu Service Data Transfer");			
			
			try {
				
				FrameGenerator frameGenerator = new FrameGenerator(FrameGenerator.generateTestFrameConfigData(),
						FrameGenerator.generatePacketconfigData());
				List<CcsdsTmFrame> frames = frameGenerator.prepareDataFrames(nPdus, pduSize);
				
				int tPdus = 3;
				
				
				SiConfig providerConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
												ObjectIdentifier.of(1,3,112,4,6,0),
												0, 
												"CSTS-USER", 
												"CSTS_PT1");

				SiConfig userConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
						ObjectIdentifier.of(1,3,112,4,6,0),
						0, 
						"CSTS-PROVIDER",
						"CSTS_PT1");
				
				numFramesReceived = 0;
				numBytesReceived = 0;
				RtnCfdpPduSiProvider providerSi = new RtnCfdpPduSiProvider(providerApi, providerConfig, rtnCfdpProcedureConfig, new IRtnCfdpPduProduction() {
					
					@Override
					public void stopCfdpPduDelivery() {
						System.out.println("Stop CFDP PDU delivery requested to production");
						swatcher.stopTransmit = Instant.now();
						
					}
					
					@Override
					public void startCfdpPduDelivery(Time startGenerationTime, Time stopGenerationTime) {
						swatcher.startTransmit = Instant.now();
						if(startGenerationTime != null && stopGenerationTime != null) {
							System.out.println("Start CFDP PDU delivery requested to production. Start time: " + startGenerationTime.toLocalDateTime() 
								+ " stop time: " + stopGenerationTime.toLocalDateTime());
						} else {
							System.out.println("Start CFDP PDU delivery requested to production. No times provided");
						}
					}
				});
				RtnCfdpPduSiUser userSi = new RtnCfdpPduSiUser(userApi, userConfig, new ICfpdPduReceiver() {
					
					@Override
					public void cfdpPdu(byte[] cfdpPdu) {
						
						if(numFramesReceived==0) {
							swatcher.startReceive = Instant.now();
						}
						numFramesReceived++;
						CSTS_LOG.CSTS_API_LOGGER.info("Received " + numFramesReceived + " CFDP PDU of length " + cfdpPdu.length);
						
						numBytesReceived += cfdpPdu.length;
						
						testConditionLock.lock();
						//if(numFramesReceived == nPdus+3) {
						if(numBytesReceived == (nPdus*18 + 64)) {
							System.out.println("Received the expected " + numFramesReceived 
									+ " frames for a total of " + numBytesReceived + " bytes" );
							swatcher.stopReceive = Instant.now();
							testCondition.signal();
						}
						testConditionLock.unlock();
					}

					@Override
					public void abort(PeerAbortDiagnostics diag) {
						System.out.println("Return CFDP PDU User aborted. Diagnostic: " + diag);
						
					}
				});
				
//								Scanner scanner = new Scanner(System.in);
//							    scanner.nextLine();

				numFramesSent = 0;
				
				
				doDataTransfer(userSi, providerSi, frames, false);	

				testConditionLock.lock();
				//while(numFramesReceived < nPdus+3) {
				while(numBytesReceived < (nPdus*18 + 64)) {
					testCondition.await(10, TimeUnit.SECONDS);
				}
				testConditionLock.unlock();
				
				if(Objects.isNull(swatcher.startTransmit)) {
					CSTS_LOG.CSTS_API_LOGGER.info("Start is null");
				}
				
				if(Objects.isNull( swatcher.stopReceive)) {
					CSTS_LOG.CSTS_API_LOGGER.info("Stop is null");
				}
				
				Duration interval = Duration.between(swatcher.startTransmit, swatcher.stopReceive);
				
				double timeSpan = interval.getSeconds() + interval.getNano()/(1000.0*1000*1000);
				
				//bits /s
				double rate = (nPdus*pduSize*8.0)/(timeSpan); 
				
				providerSi.destroy();
				userSi.destroy();

				CSTS_LOG.CSTS_API_LOGGER.info("Transmission Time: " + timeSpan);	
				CSTS_LOG.CSTS_API_LOGGER.info("Data size: " + nPdus*pduSize/(1000*1000) + " MByte, rate: "+ rate/1000_000  +  " Mbit/s");
				CSTS_LOG.CSTS_API_LOGGER.info("Data units: " + nPdus + " rate: " + nPdus/timeSpan + " Units/s");
				
				String recordPerformance = System.getProperty("recordPerformance");
				
				if(Objects.nonNull(recordPerformance)) {
					FileWriter fw = new FileWriter("FramePerformanceLog.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					bw.write(timestamp + ", " + nPdus +
							", " + pduSize +
							", " + timeSpan +
							", " + rate/1000_000_000 +
							", " + nPdus/timeSpan);
					bw.newLine();
					bw.close();
				}

							
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	private void doDataTransfer(RtnCfdpPduSiUser userSi, RtnCfdpPduSiProvider providerSi,List<CcsdsTmFrame> frames, boolean withTime) throws InterruptedException {
				
		CSTS_LOG.CSTS_API_LOGGER.info("BIND...");
		verifyResult(userSi.bind(), "BIND");
		
		if(withTime) {
			Time start = Time.of(LocalDateTime.now());
			Time stop = Time.of(LocalDateTime.now().plusMinutes(10));

			userSi.requestDataDelivery(start, stop);
		} else {
			userSi.requestDataDelivery();
		}
		
		
		Assert.assertTrue(userSi.getDeliveryProc().isActive() == true);
		Assert.assertTrue(userSi.getDeliveryProc().isActivationPending() == false);
		Assert.assertTrue(userSi.getDeliveryProc().isDeactivationPending() == false);
		
		Assert.assertTrue(providerSi.getDeliveryProc().isActive() == true);
		Assert.assertTrue(providerSi.getDeliveryProc().isActivationPending() == false);
		Assert.assertTrue(providerSi.getDeliveryProc().isDeactivationPending() == false);
		
		CfdpPduPackAndTransfer packAndTransfer = new CfdpPduPackAndTransfer(providerSi,1000,500,true);
		PacketConfigData packetConfigData = FrameGenerator.generatePacketconfigData();
		FrameConfigData frameConfigData = FrameGenerator.generateTestFrameConfigData();
		CfdpPduExtractAndTransfer extractAndTransfer = 
				new CfdpPduExtractAndTransfer(packAndTransfer,frameConfigData,packetConfigData);
		
		extractAndTransfer.startTransfer();
		
		while(packAndTransfer.isTransferring()==false) {
			Thread.currentThread().sleep(100);
		}
		
		for(CcsdsTmFrame ccsdsFrame:frames) {
			extractAndTransfer.transferData(ccsdsFrame.GetTransferFrame());
			numFramesSent++;
		}
				
		extractAndTransfer.stopTransfer();
		CSTS_LOG.CSTS_API_LOGGER.info("Waiting for transfer completion...");
		
		while(packAndTransfer.isTransferring()) {
			Thread.currentThread().sleep(100);
		}
		
		CSTS_LOG.CSTS_API_LOGGER.info("...done");
		
		verifyResult(userSi.endDataDelivery(), "END DATA Delivery");
		
		CSTS_LOG.CSTS_API_LOGGER.info("UNBIND...");
		verifyResult(userSi.unbind(), "UNBIND");		
	}

	
	private void verifyResult(CstsResult res, String what) {
		Assert.assertEquals(CstsResult.SUCCESS, res);
		if(res != CstsResult.SUCCESS) {
			CSTS_LOG.CSTS_API_LOGGER.severe("Failed to " + what + " " + res);
		} else {
			CSTS_LOG.CSTS_API_LOGGER.info(what + " OK " + res);
		}		
	}	
}
