package esa.egos.csts.api.procedures;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.rules.TestRule;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.functionalresources.FunctionalResourceMetadata;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.CstsProviderApi;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.csts.sim.impl.MdCstsSiConfig;
import esa.egos.csts.sim.impl.prv.MdCollection;
import esa.egos.csts.sim.impl.prv.MdCstsSiProvider;
import esa.egos.csts.sim.impl.prv.MdCstsSiProviderConfig;
import esa.egos.csts.sim.impl.usr.MdCstsSiUser;
import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

/**
 * Setup for CSTS FR Antenna tests
 */
public abstract class MdCstsTestBase
{
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    protected ICstsApi providerApi;

    protected ICstsApi userApi;

    // S/C identifier
    protected ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);

    // G/S identifier
    protected ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

    // prime procedure identifier
    protected ProcedureInstanceIdentifier piid_ocr_prime = ProcedureInstanceIdentifier
            .of(ProcedureType.of(OIDs.ocoCyclicReport), ProcedureRole.PRIME, 0);

    protected ProcedureInstanceIdentifier piid_ocr_secondary_01 = ProcedureInstanceIdentifier
            .of(ProcedureType.of(OIDs.ocoCyclicReport), ProcedureRole.SECONDARY, 1);

    protected ProcedureInstanceIdentifier piid_ocr_secondary_02 = ProcedureInstanceIdentifier
            .of(ProcedureType.of(OIDs.ocoCyclicReport), ProcedureRole.SECONDARY, 2);

    // secondary inform query procedure identifier
    protected ProcedureInstanceIdentifier piid_iq_secondary = ProcedureInstanceIdentifier
            .of(ProcedureType.of(OIDs.informationQuery), ProcedureRole.SECONDARY, 1);

    // secondary notification procedure identifier
    protected ProcedureInstanceIdentifier piid_n_secondary = ProcedureInstanceIdentifier
            .of(ProcedureType.of(OIDs.notification), ProcedureRole.SECONDARY, 2);

    // all IQ procedure identifiers
    protected List<ProcedureInstanceIdentifier> iq_piids = Arrays.asList(this.piid_iq_secondary);

    // all procedure identifiers
    protected List<ProcedureInstanceIdentifier> piids = Arrays.asList(this.piid_ocr_prime,
                                                                      this.piid_ocr_secondary_01,
                                                                      this.piid_ocr_secondary_02,
                                                                      this.piid_iq_secondary,
                                                                      this.piid_n_secondary);

    protected long minimumAllowedDeliveryCycle = 50;

    // create provider SI configuration
    protected MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(this.minimumAllowedDeliveryCycle,
                                                                                     null,
                                                                                     null,
                                                                                     this.scId,
                                                                                     this.facilityId,
                                                                                     0,
                                                                                     "CSTS_USER",
                                                                                     "CSTS_PT1",
                                                                                     this.piids);

    // create provider SI configuration
    protected MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(this.scId,
                                                                 this.facilityId,
                                                                 0,
                                                                 "CSTS_PROVIDER",
                                                                 "CSTS_PT1",
                                                                 this.piids);

    // the default label list
    protected List<Label> defaultLabelList;

    // MD collection
    protected MdCollection mdCollection;

    // the provider SI
    protected MdCstsSiProvider providerSi;

    // the user SI
    protected MdCstsSiUser userSi;

    protected static final List<TestParameter> testParameters = new LinkedList<TestParameter>();

    protected static final List<TestParameter> testEvents = new LinkedList<TestParameter>();

    static class TestParameter
    {
        public final ObjectIdentifier oid;
        public final String name;
        public final ICstsValue initValue;
        public final ICstsValue updatedValue;
        
        public TestParameter(ObjectIdentifier oid, String name, int initValue, int updatedValue)
        {
            this.oid = oid;
            this.name = name;
            this.initValue = CstsIntValue.of(name, initValue);
            this.updatedValue = CstsIntValue.of(name, updatedValue);
        }
        public TestParameter(ObjectIdentifier oid, ICstsValue initValue, ICstsValue updatedValue)
        {
            this.oid = oid;
            this.name = initValue.getName();
            this.initValue = initValue;
            this.updatedValue = updatedValue;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("TestParameter [oid=");
            sb.append(this.oid);
            sb.append(", name=");
            sb.append(this.name);
            sb.append(", initValue=");
            sb.append(this.initValue);
            sb.append(", updatedValue=");
            sb.append(this.updatedValue);
            sb.append("]");
            return sb.toString();
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestBootstrap.initCs();
        //FunctionalResourceMetadata.getInstance().loadFromBinaryClasses("frm.csts.functional.resource.types");
        testParameters.clear();
        testEvents.clear();
    }

    /**
     * Initializes the provider API and the use API and starts the communication
     * server
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        System.out.println("MdCstsTestBase#setUp() begin");

        File file = new File("src/test/resources/ProviderConfig.xml");
        String providerConfigName = file.getAbsolutePath();

        file = new File("src/test/resources/UserConfig.xml");
        String userConfigName = file.getAbsolutePath();

        System.out.println("provider config: " + providerConfigName);
        System.out.println("user config: " + userConfigName);

        System.out.println("Starting CSTS user and provider API");

        this.providerApi = new CstsProviderApi("Test Service Provider API",providerConfigName);
        this.providerApi.start();

        this.userApi = new CstsUserApi("Test Service User API",userConfigName);
        this.userApi.start();

        System.out.println("CSTS user and provider API started");

        System.out.println("Loading FR metadata");

        FunctionalResourceMetadata.getInstance().loadFromBinaryClasses("frm.csts.functional.resource.types");

        System.out.println("Loaded FR metadata");

        System.out.println("Creating provider SI");

        // create provider SI
        this.providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

        System.out.println("Created provider SI");

        // create FR parameters and attach them to the provider SI
        System.out.println("Setting the initial values to MD collection");

        System.out.println("Set the initial values to MD collection");

        System.out.println("Creating user SI");

        // create user SI
        this.userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

        System.out.println("Created user SI");

        System.out.println("Creating default label list");

        this.defaultLabelList = createDefaultLabelList();

        System.out.println("MdCstsTestBase#setUp() end");
    }

    /**
     * Stops the provider and user API
     */
    @After
    public void tearDown()
    {
        System.out.println("MdCstsTestBase#tearDown() begin");

        this.providerApi.stop();
        this.userApi.stop();
        if (this.userSi.isBound())
        {
            try
            {
                System.out.println("userSi#destroy()");
                this.userSi.destroy();
            }
            catch (Exception e)
            {
                System.out.println("MdCstsTestBase#tearDown() userSi exception: " + e.toString());
            }
        }
        if (this.providerSi.isBound())
        {
            try
            {
                System.out.println("providerSi#destroy()");
                this.providerSi.destroy();
            }
            catch (Exception e)
            {
                System.out.println("MdCstsTestBase#tearDown() providerSi exception: " + e.toString());
            }
        }
        System.out.println("CSTS user and provider API stopped");

        System.out.println("MdCstsTestBase#tearDown() end");
    }

    protected abstract List<Label> createDefaultLabelList();
}
