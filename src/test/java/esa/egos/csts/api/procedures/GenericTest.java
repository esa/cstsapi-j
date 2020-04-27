package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.sim.impl.MdCstsSiConfig;
import esa.egos.csts.sim.impl.prv.MdCollection;
import esa.egos.csts.sim.impl.prv.MdCstsSiProvider;
import esa.egos.csts.sim.impl.prv.MdCstsSiProviderConfig;
import esa.egos.csts.sim.impl.usr.MdCstsSiUser;
import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;
import esa.egos.csts.api.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

/**
 * Test the CSTS API at the example of the monitored data service
 */
public class GenericTest
{
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;

    private ICstsApi userApi;

    @BeforeClass
    public static void setUpClass() throws ApiException
    {
        TestBootstrap.initCs();
    }

    /**
     * Initializes the provider API and the use API and starts the communication
     * server
     * 
     * @throws ApiException
     */
    @Before
    public void setUp() throws ApiException
    {
	File file = new File("src/test/resources/ProviderConfig.xml");
        String providerConfigName = file.getAbsolutePath();

        file = new File("src/test/resources/UserConfig.xml");
        String userConfigName = file.getAbsolutePath();

        System.out.println("provider config: " + providerConfigName);
        System.out.println("user config: " + userConfigName);

        this.providerApi = new CstsApi("Test Service Provider API", AppRole.PROVIDER);
        this.providerApi.initialize(providerConfigName);
        this.providerApi.start();

        this.userApi = new CstsApi("Test Service User API", AppRole.USER);
        this.userApi.initialize(userConfigName);
        this.userApi.start();

        System.out.println("CSTS user and provider API started");
    }

    /**
     * Stops the provider and user API
     */
    @After
    public void tearDown()
    {
        this.providerApi.stop();
        this.userApi.stop();
        System.out.println("CSTS user and provider API stopped");
    }

    /**
     * Test association procedure and its bind and unbind operations
     */
    @Test
    public void testAssociation()
    {
        try
        {
            // S/C identifier
            ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);
            // G/S identifier
            ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.ocoCyclicReport),
                                                                              ProcedureRole.PRIME,
                                                                              0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(50,
                                                                                   null,
                                                                                   null,
                                                                                   scId,
                                                                                   facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   piids);

            // create provider SI configuration
            MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(scId,
                                                               facilityId,
                                                               0,
                                                               "CSTS_PROVIDER",
                                                               "CSTS_PT1",
                                                               piids);

            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("again BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND", CstsResult.ALREADY_BOUND);

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            System.out.println("again UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND", CstsResult.ALREADY_UNBOUND);

            System.out.println("again BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test the Cyclic report procedure
     */
    @Test
    public void testCyclicReport()
    {
        try
        {
            // S/C identifier
            ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);
            // G/S identifier
            ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.ocoCyclicReport),
                                                                              ProcedureRole.PRIME,
                                                                              0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(50,
                                                                                   null,
                                                                                   null,
                                                                                   scId,
                                                                                   facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   piids);

            // create provider SI configuration
            MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(scId,
                                                               facilityId,
                                                               0,
                                                               "CSTS_PROVIDER",
                                                               "CSTS_PT1",
                                                               piids);

            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, mdSiUserConfig, 1);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(), mdCollection.getParameterNameSet(), 3000), "START-CYCLIC-REPORT");

            // wait for several cyclic reports
            Thread.sleep(7000);

            assertTrue("no cyclic report received", userSi.getCyclicReportCount() > 0);

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test the Notification procedure
     */
    @Test
    public void testNotification()
    {
        try
        {
            // S/C identifier
            ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);
            // G/S identifier
            ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.notification),
                                                                              ProcedureRole.PRIME,
                                                                              0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(50,
                                                                                   null,
                                                                                   null,
                                                                                   scId,
                                                                                   facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   piids);

            // create provider SI configuration
            MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(scId,
                                                               facilityId,
                                                               0,
                                                               "CSTS_PROVIDER",
                                                               "CSTS_PT1",
                                                               piids);

            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, mdSiUserConfig, 1);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(), mdCollection.getEventNames()), "START-NOTIFICATION");

            mdCollection.fireAllEvents();

            // wait for notification
            Thread.sleep(500);
            assertTrue("no event notified", userSi.getNotifiedEventCount() > 0);

            System.out.println("STOP-NOTIFICATION...");
            TestUtils.verifyResult(userSi.stopNotification(0), "STOP-NOTIFICATION");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test the peer abort operation
     */
    @Test
    public void testPeerAbort()
    {
        try
        {
            // S/C identifier
            ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);
            // G/S identifier
            ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.ocoCyclicReport),
                                                                              ProcedureRole.PRIME,
                                                                              0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(50,
                                                                                   null,
                                                                                   null,
                                                                                   scId,
                                                                                   facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   piids);

            // create provider SI configuration
            MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(scId,
                                                               facilityId,
                                                               0,
                                                               "CSTS_PROVIDER",
                                                               "CSTS_PT1",
                                                               piids);

            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, mdSiUserConfig, 1);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(), mdCollection.getParameterNameSet(), 3000), "START-CYCLIC-REPORT");

            // wait for one cyclic report
            Thread.sleep(4000);

            System.out.println("PEER-ABORT...");
            TestUtils.verifyResult(userSi.peerAbort(), "PEER-ABORT");
            Thread.sleep(1000);

            System.out.println("again PEER-ABORT...");
            TestUtils.verifyResult(userSi.peerAbort(), "PEER-ABORT", CstsResult.FAILURE);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(), mdCollection.getParameterNameSet(), 3000), "START-CYCLIC-REPORT");

            // wait for one cyclic report
            Thread.sleep(4000);

            System.out.println("PEER-ABORT...");
            TestUtils.verifyResult(userSi.peerAbort(), "PEER-ABORT");

            providerSi.destroy();
            userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
