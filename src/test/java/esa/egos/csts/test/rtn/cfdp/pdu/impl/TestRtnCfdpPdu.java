package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
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
import esa.egos.csts.api.main.CstsProviderApi;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.CSTS_LOG;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.app.si.rtn.cfdp.pdu.ICfpdPduReceiver;
import esa.egos.csts.app.si.rtn.cfdp.pdu.IRtnCfdpPduProduction;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduDeliveryProcedureConfig;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiProvider;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiUser;

public class TestRtnCfdpPdu {
     @Rule
     public TestRule testWatcher = new CstsTestWatcher();

     private ICstsApi providerApi;
     private ICstsApi userApi;

     Lock testConditionLock = new ReentrantLock();
     Condition testCondition = testConditionLock.newCondition();

     private long fullPdusReceived;

     private long fullPdusSent;

     private static final int SERVICE_VERSION_SFW_B1 = 1;

     private static final int SERVICE_VERSION_SFW_B2 = 2;

     @BeforeClass
     public static void setUpClass() throws ApiException {
          TestBootstrap.initCs();
     }

     /**
      * Initializes the provider API and the use API and starts the communication
      * server
      * 
      * @throws ApiException
      */
     @Before
     public void init() throws ApiException {
          File file = new File("src/test/resources/ProviderConfig1.xml");
          String providerConfigName = file.getAbsolutePath();

          file = new File("src/test/resources/UserConfig1.xml");
          String userConfigName = file.getAbsolutePath();

          file = new File("src/test/resources/log.properties");
          System.setProperty("java.util.logging.config.file", file.getAbsolutePath());

          System.out.println("provider config: " + providerConfigName);
          System.out.println("user config: " + userConfigName);

          providerApi = new CstsProviderApi("Test Service Provider API");
          providerApi.initialize(providerConfigName);
          providerApi.start();

          userApi = new CstsUserApi("Test Service User API");
          userApi.initialize(userConfigName);
          userApi.start();

          System.out.println("CSTS user and provider API started");
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

     @Test
     @Ignore
     public void testRtnCfdpPduDataTransfer2() {
          testRtnCfdpPduDataTransfer(SERVICE_VERSION_SFW_B1);
          testRtnCfdpPduDataTransfer(SERVICE_VERSION_SFW_B1);
     }
     
     @Test
     public void testRtnCfdpPduDataTransferB1() {
          testRtnCfdpPduDataTransfer(SERVICE_VERSION_SFW_B1);
     }

     @Test
     public void testRtnCfdpPduDataTransferB2() {
          testRtnCfdpPduDataTransfer(SERVICE_VERSION_SFW_B2);
     }

     public void testRtnCfdpPduDataTransfer(int sfwVersion) {
          RtnCfdpPduDeliveryProcedureConfig rtnCfdpProcedureConfig = new RtnCfdpPduDeliveryProcedureConfig();
          rtnCfdpProcedureConfig.setLatencyLimit(1);
          rtnCfdpProcedureConfig.setReturnBufferSize(2);

          System.out.println("Test return Cfdp Pdu Service Data Transfer");

          try {
               SiConfig providerConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                         ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-USER", "CSTS_PT1");

               SiConfig userConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                         ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-PROVIDER", "CSTS_PT1");

               fullPdusReceived = 0;
               RtnCfdpPduSiProvider providerSi = new RtnCfdpPduSiProvider(providerApi, providerConfig,
                         rtnCfdpProcedureConfig, new IRtnCfdpPduProduction() {

                              @Override
                              public void stopCfdpPduDelivery() {
                                   System.out.println("Stop CFDP PDU delivery requested to production");

                              }

                              @Override
                              public void startCfdpPduDelivery(Time startGenerationTime, Time stopGenerationTime) {
                                   if (startGenerationTime != null && stopGenerationTime != null) {
                                        System.out.println(
                                                  "Start CFDP PDU delivery requested to production. Start time: "
                                                            + startGenerationTime.toLocalDateTime() + " stop time: "
                                                            + stopGenerationTime.toLocalDateTime());
                                   } else {
                                        System.out.println(
                                                  "Start CFDP PDU delivery requested to production. No times provided");
                                   }

                              }
                         });
               RtnCfdpPduSiUser userSi = new RtnCfdpPduSiUser(userApi, userConfig, new ICfpdPduReceiver() {

                    @Override
                    public void cfdpPdu(byte[] cfdpPdu) {
                         // CSTS_LOG.CSTS_API_LOGGER.fine("Received CFDP PDU of length " +
                         // cfdpPdu.length);
                         fullPdusReceived++;

                         testConditionLock.lock();
                         if (fullPdusReceived == fullPdusSent) {
                              System.out.println("Received the expected " + fullPdusReceived + " frames");
                              testCondition.signal();
                         }
                         testConditionLock.unlock();
                    }

                    @Override
                    public void abort(PeerAbortDiagnostics diag) {
                         System.out.println("Return CFDP PDU User aborted. Diagnostic: " + diag);

                    }

                    @Override
                    public void cfdpPdu(List<byte[]> cfdpPdus) {
                         // TODO Auto-generated method stub

                    }
               }, sfwVersion);

               fullPdusSent = 0;
               for (int idx = 0; idx < 2; idx++) {
                    doDataTransfer(userSi, providerSi, 5, (idx % 2 == 0));
               }

               testConditionLock.lock();
               while (fullPdusReceived != fullPdusSent) {
                    testCondition.await(100, TimeUnit.SECONDS);
               }
               testConditionLock.unlock();

               providerSi.destroy();
               userSi.destroy();

          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     private void doDataTransfer(RtnCfdpPduSiUser userSi, RtnCfdpPduSiProvider providerSi, long numFrames,
               boolean withTime) throws InterruptedException {
          byte[] cfdpPduData = new byte[1024];

          CSTS_LOG.CSTS_API_LOGGER.info("BIND...");
          verifyResult(userSi.bind(), "BIND");

          if (withTime) {
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

          for (int idx = 0; idx < numFrames; idx++) {
               providerSi.transferData(cfdpPduData);
               fullPdusSent++;
          }

          verifyResult(userSi.endDataDelivery(), "END DATA Delivery");

          CSTS_LOG.CSTS_API_LOGGER.info("UNBIND...");
          verifyResult(userSi.unbind(), "UNBIND");
     }

     @Test
     @Ignore
     public void testRtnCfdpPduDataNoInitialConnect() {
          RtnCfdpPduDeliveryProcedureConfig rtnCfdpProcedureConfig = new RtnCfdpPduDeliveryProcedureConfig();
          rtnCfdpProcedureConfig.setLatencyLimit(1);
          rtnCfdpProcedureConfig.setReturnBufferSize(2);
          long numFrames = 10;

          System.out.println("Test return Cfdp Pdu Service Data Transfer");

          try {
               SiConfig providerConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                         ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-USER", "CSTS_PT1");

               SiConfig userConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                         ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-PROVIDER", "CSTS_PT1");

               fullPdusReceived = 0;

               RtnCfdpPduSiUser userSi = new RtnCfdpPduSiUser(userApi, userConfig, new ICfpdPduReceiver() {

                    @Override
                    public void cfdpPdu(byte[] cfdpPdu) {
                         // CSTS_LOG.CSTS_API_LOGGER.fine("Received CFDP PDU of length " +
                         // cfdpPdu.length);
                         fullPdusReceived++;

                         testConditionLock.lock();
                         if (fullPdusReceived == fullPdusSent) {
                              System.out.println("Received the expected " + fullPdusReceived + " frames");
                              testCondition.signal();
                         }
                         testConditionLock.unlock();
                    }

                    @Override
                    public void abort(PeerAbortDiagnostics diag) {
                         System.out.println("Return CFDP PDU User aborted. Diagnostic: " + diag);

                    }

                    @Override
                    public void cfdpPdu(List<byte[]> cfdpPdus) {
                         // TODO Auto-generated method stub

                    }
               }, SERVICE_VERSION_SFW_B1);

               fullPdusSent = 0;

               byte[] cfdpPduData = new byte[1024];

               CSTS_LOG.CSTS_API_LOGGER.info("BIND...");
               userSi.bind(); // should fail - no active provider!

               RtnCfdpPduSiProvider providerSi = new RtnCfdpPduSiProvider(providerApi, providerConfig,
                         rtnCfdpProcedureConfig, new IRtnCfdpPduProduction() {

                              @Override
                              public void stopCfdpPduDelivery() {
                                   System.out.println("Stop CFDP PDU delivery requested to production");

                              }

                              @Override
                              public void startCfdpPduDelivery(Time startGenerationTime, Time stopGenerationTime) {
                                   if (startGenerationTime != null && stopGenerationTime != null) {
                                        System.out.println(
                                                  "Start CFDP PDU delivery requested to production. Start time: "
                                                            + startGenerationTime.toLocalDateTime() + " stop time: "
                                                            + stopGenerationTime.toLocalDateTime());
                                   } else {
                                        System.out.println(
                                                  "Start CFDP PDU delivery requested to production. No times provided");
                                   }

                              }
                         });

               verifyResult(userSi.bind(), "BIND...");

               userSi.requestDataDelivery();

               Assert.assertTrue(userSi.getDeliveryProc().isActive() == true);
               Assert.assertTrue(userSi.getDeliveryProc().isActivationPending() == false);
               Assert.assertTrue(userSi.getDeliveryProc().isDeactivationPending() == false);

               Assert.assertTrue(providerSi.getDeliveryProc().isActive() == true);
               Assert.assertTrue(providerSi.getDeliveryProc().isActivationPending() == false);
               Assert.assertTrue(providerSi.getDeliveryProc().isDeactivationPending() == false);

               for (int idx = 0; idx < numFrames; idx++) {
                    providerSi.transferData(cfdpPduData);
                    fullPdusSent++;
               }

               verifyResult(userSi.endDataDelivery(), "END DATA Delivery");

               CSTS_LOG.CSTS_API_LOGGER.info("UNBIND...");
               verifyResult(userSi.unbind(), "UNBIND");

               testConditionLock.lock();
               while (fullPdusReceived != fullPdusSent) {
                    testCondition.await(100, TimeUnit.SECONDS);
               }
               testConditionLock.unlock();

               providerSi.destroy();
               userSi.destroy();

          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     @Test
     @Ignore
     public void testHeartBeat() {
          RtnCfdpPduDeliveryProcedureConfig rtnCfdpProcedureConfig = new RtnCfdpPduDeliveryProcedureConfig();
          rtnCfdpProcedureConfig.setLatencyLimit(1);
          rtnCfdpProcedureConfig.setReturnBufferSize(2);
          long waitSeconds = 10;

          System.out.println("Test return Cfdp Pdu Service Data Transfer");

          try {
               SiConfig providerConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                         ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-USER", "CSTS_PT1");

               SiConfig userConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                         ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-PROVIDER", "CSTS_PT1");

               fullPdusReceived = 0;
               RtnCfdpPduSiProvider providerSi = new RtnCfdpPduSiProvider(providerApi, providerConfig,
                         rtnCfdpProcedureConfig);
               RtnCfdpPduSiUser userSi = new RtnCfdpPduSiUser(userApi, userConfig, new ICfpdPduReceiver() {

                    @Override
                    public void cfdpPdu(byte[] cfdpPdu) {
                    }

                    @Override
                    public void abort(PeerAbortDiagnostics diag) {
                         System.out.println("Return CFDP PDU User aborted. Diagnostic: " + diag);
                         testConditionLock.lock();
                         testCondition.signal();
                         testConditionLock.unlock();

                    }

                    @Override
                    public void cfdpPdu(List<byte[]> cfdpPdus) {
                         // TODO Auto-generated method stub

                    }
               }, SERVICE_VERSION_SFW_B1);

               userSi.bind();
               assertTrue(userSi.getApiSi().getStatus() == ServiceStatus.BOUND);

               testConditionLock.lock();
               System.out.print("Wait for " + waitSeconds + " seconds...");
               boolean signalledAbort = testCondition.await(waitSeconds, TimeUnit.SECONDS);
               System.out.println(" after wait, signalled abort is " + signalledAbort);
               testConditionLock.unlock();

               userSi.unbind();
               assertTrue(userSi.getApiSi().getStatus() == ServiceStatus.UNBOUND);

               Assert.assertTrue(signalledAbort == false);

               providerSi.destroy();
               userSi.destroy();

          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     private void verifyResult(CstsResult res, String what) {
          Assert.assertEquals(CstsResult.SUCCESS, res);
          if (res != CstsResult.SUCCESS) {
               CSTS_LOG.CSTS_API_LOGGER.severe("Failed to " + what + " " + res);
          } else {
               CSTS_LOG.CSTS_API_LOGGER.info(what + " OK " + res);
          }
     }
}
