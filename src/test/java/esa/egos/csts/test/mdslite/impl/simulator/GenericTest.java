package esa.egos.csts.test.mdslite.impl.simulator;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

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
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.test.mdslite.impl.simulator.provider.MdCollection;
import esa.egos.csts.test.mdslite.impl.simulator.provider.MdCstsSiProvider;
import esa.egos.csts.test.mdslite.impl.simulator.user.MdCstsSiUser;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Test the CSTS API at the example of the monitored data service
 */
public class GenericTest
{

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

    @Test
    public void testAssociation()
    {
        System.out.println("Test association procedure and its bind and unbind operations");

        try
        {
            // S/C identifier
            ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);
            // G/S identifier
            ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                                              ProcedureRole.PRIME,
                                                                              0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(50,
                                                                                   new LabelList("", true),
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

    @Test
    public void testCyclicReport()
    {
        System.out.println("Test the Cyclic report procedure");

        try
        {
            // S/C identifier
            ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);
            // G/S identifier
            ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                                              ProcedureRole.PRIME,
                                                                              0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(50,
                                                                                   new LabelList("", true),
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

    @Test
    public void testNotification()
    {
        System.out.println("Test the Notification procedure");

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
                                                                                   new LabelList("", true),
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

}
