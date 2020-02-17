package esa.egos.csts.test.mdslite.impl.simulator;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.test.mdslite.impl.simulator.provider.MdCollection;
import esa.egos.csts.test.mdslite.impl.simulator.provider.MdCstsSiProvider;
import esa.egos.csts.test.mdslite.impl.simulator.user.MdCstsSiUser;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * Test the CSTS API at the example of the monitored data service
 */
public class InformationQueryTest
{

    private ICstsApi providerApi;

    private ICstsApi userApi;

    // S/C identifier
    private ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);

    // G/S identifier
    private ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

    // prime inform query procedure identifier
    private ProcedureInstanceIdentifier piid_prime = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.informationQuery),
                                                                              ProcedureRole.PRIME,
                                                                              0);
    // secondary inform query procedure identifier
    private ProcedureInstanceIdentifier piid_secondary = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.informationQuery),
                                                                              ProcedureRole.SECONDARY,
                                                                              1);

    // all procedure identifiers
    private List<ProcedureInstanceIdentifier> piids = Arrays.asList(this.piid_prime, this.piid_secondary);

    private long minimumAllowedDeliveryCycle = 50;

    static LabelList defaultLabelList = new LabelList("", true);
    {
        ObjectIdentifier antActualAzimuthId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1, 1, 3, 1 });
        FunctionalResourceType antActualAzimuthType = FunctionalResourceType.of(antActualAzimuthId);

        defaultLabelList.getLabels().add(Label.of(antActualAzimuthId, antActualAzimuthType));
    }

    // create provider SI configuration
    private MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(this.minimumAllowedDeliveryCycle,
                                                                                   defaultLabelList,
                                                                                   this.scId,
                                                                                   this.facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   this.piids);

    // create provider SI configuration
    private MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(this.scId,
                                                               this.facilityId,
                                                               0,
                                                               "CSTS_PROVIDER",
                                                               "CSTS_PT1",
                                                               this.piids);

    // non existent parameter - parameter is not added into MD collection
    private ObjectIdentifier antIdId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1, 1, 1, 1 });

    private FunctionalResourceType antIdType = FunctionalResourceType.of(this.antIdId);

    private FunctionalResourceName antIdName = FunctionalResourceName.of(this.antIdType, 1);

//    private FunctionalResourceIntegerParameter antActualAzimuth = new FunctionalResourceIntegerParameter(this.antIdId,
//                                                                                                 this.antIdName);
    private Name nonExistentParameterName = Name.of(this.antIdId, this.antIdName);

//    private Label nonExistentParameterLabel = Label.of(this.antIdId, this.antIdType);

    
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
    public void testQueryEmpty()
    {
        System.out.println("Test Information Query procedure and its GET operation w/ default list => ListOfParameter /w type EMPTY");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            // the secondary procedure w/ instance number 1 has not defined the default list
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(1, ListOfParameters.empty()),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains that the Default list not defined
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("Default list not defined"));

            
            // TODO try to once again w/ defined default list
//            // the primary procedure w/ instance number 0 has defined the default list
//            System.out.println("QUERY-INFORMATION...");
//            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.empty()),
//                                   "QUERY-INFORMATION");
//
//            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
//            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());
//
//            // verify that the user SI received all queried qualified parameters
//            Optional<QualifiedParameter> result_01 = queriedParameters_01
//                    .stream().filter(qualifiedParameter -> qualifiedParameter.getName().getFunctionalResourceName()
//                            .getType().equals(defaultLabelList.getLabels().get(0).getFunctionalResourceType()))
//                    .findAny();
//            assertTrue(result_01.isPresent());
//
//            QualifiedParameter qualifiedParameter_01 = result_01.get();
//            System.out.println("got: " + qualifiedParameter_01);

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
    public void testQueryFunctionalResourceName()
    {
        System.out.println("Test Information Query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Name name = mdCollection.getParameterName();
            FunctionalResourceName functionalResourceName = name.getFunctionalResourceName();

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(functionalResourceName)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue(result_01.isPresent());

            QualifiedParameter qualifiedParameter_01 = result_01.get();
            System.out.println("got: " + qualifiedParameter_01);

            if (qualifiedParameter_01.getQualifiedValues().get(0).getParameterValues().get(0)
                    .getType() == ParameterType.INTEGER)
            {
                long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                        .getParameterValues().get(0).getIntegerParameterValues().get(0);
                long userValue = qualifiedParameter_01.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getIntegerParameterValues().get(0);

                System.out.println("providerValue = " + providerValue);
                System.out.println("userValue = " + userValue);
                assertEquals("parameter " + name + " value is different", providerValue, userValue);
            }

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 5);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_02 = queriedParameters_02.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue(result_02.isPresent());

            QualifiedParameter qualifiedParameter_02 = result_02.get();
            System.out.println("got: " + qualifiedParameter_02);

            if (qualifiedParameter_02.getQualifiedValues().get(0).getParameterValues().get(0)
                    .getType() == ParameterType.INTEGER)
            {
                long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                        .getParameterValues().get(0).getIntegerParameterValues().get(0);
                long userValue = qualifiedParameter_02.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getIntegerParameterValues().get(0);

                System.out.println("providerValue = " + providerValue);
                System.out.println("userValue = " + userValue);
                assertEquals("parameter " + name + " value is different", providerValue, userValue);
            }

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
    public void testQueryFunctionalResourceNameNonExistent()
    {
        System.out
                .println("Test Information Query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME non-existent");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(
                                   userSi.queryInformation(0,
                                                           ListOfParameters.of(this.nonExistentParameterName
                                                                   .getFunctionalResourceName())),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("1, 3, 112, 4, 4, 2, 1, 1, 1, 1, 1"));

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
    public void testQueryFunctionalResourceType()
    {
        System.out.println("Test Information Query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Name name = mdCollection.getParameterName();
            FunctionalResourceType functionalResourceType = name.getFunctionalResourceName().getType();

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(functionalResourceType)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue(result_01.isPresent());

            QualifiedParameter qualifiedParameter_01 = result_01.get();
            System.out.println("got: " + qualifiedParameter_01);

            if (qualifiedParameter_01.getQualifiedValues().get(0).getParameterValues().get(0)
                    .getType() == ParameterType.INTEGER)
            {
                long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                        .getParameterValues().get(0).getIntegerParameterValues().get(0);
                long userValue = qualifiedParameter_01.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getIntegerParameterValues().get(0);

                System.out.println("providerValue = " + providerValue);
                System.out.println("userValue = " + userValue);
                assertEquals("parameter " + name + " value is different", providerValue, userValue);
            }

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 10);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_02 = queriedParameters_02.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue(result_02.isPresent());

            QualifiedParameter qualifiedParameter_02 = result_02.get();
            System.out.println("got: " + qualifiedParameter_02);

            if (qualifiedParameter_02.getQualifiedValues().get(0).getParameterValues().get(0)
                    .getType() == ParameterType.INTEGER)
            {
                long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                        .getParameterValues().get(0).getIntegerParameterValues().get(0);
                long userValue = qualifiedParameter_02.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getIntegerParameterValues().get(0);

                System.out.println("providerValue = " + providerValue);
                System.out.println("userValue = " + userValue);
                assertEquals("parameter " + name + " value is different", providerValue, userValue);
            }

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
    public void testQueryFunctionalResourceTypeNonExistent()
    {
        System.out
                .println("Test Information Query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE non-existent");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(
                                   userSi.queryInformation(0,
                                                           ListOfParameters.of(this.nonExistentParameterName
                                                                   .getFunctionalResourceName())),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            System.out.println();
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("1, 3, 112, 4, 4, 2, 1, 1, 1, 1, 1"));

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
    public void testQueryNameSet()
    {
        System.out.println("Test Information Query procedure and its GET operation w/ NAME_SET");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                Optional<QualifiedParameter> result = queriedParameters_01.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
                assertTrue(result.isPresent());

                QualifiedParameter qualifiedParameter = result.get();
                System.out.println("got: " + qualifiedParameter);

                if (qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getType() == ParameterType.INTEGER)
                {
                    long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                            .getParameterValues().get(0).getIntegerParameterValues().get(0);
                    long userValue = qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                            .getIntegerParameterValues().get(0);

                    System.out.println("providerValue = " + providerValue);
                    System.out.println("userValue = " + userValue);
                    assertEquals("parameter " + name + " value is different", providerValue, userValue);
                }
            }

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 20);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                Optional<QualifiedParameter> result = queriedParameters_02.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
                assertTrue(result.isPresent());

                QualifiedParameter qualifiedParameter = result.get();
                System.out.println("got: " + qualifiedParameter);

                if (qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getType() == ParameterType.INTEGER)
                {
                    long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                            .getParameterValues().get(0).getIntegerParameterValues().get(0);
                    long userValue = qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                            .getIntegerParameterValues().get(0);

                    System.out.println("providerValue = " + providerValue);
                    System.out.println("userValue = " + userValue);
                    assertEquals("parameter " + name + " value is different", providerValue, userValue);
                }
            }

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
    public void testQueryNameSetNonExistentParameter()
    {
        System.out.println("Test Information Query procedure and its GET operation");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Name parameterName = mdCollection.getParameterNameSet().getParameterNames().get(0);

            // create list of parameters NAME_SET /w a non existent parameter
            ListOfParameters listOfParameters = ListOfParameters.of(parameterName, this.nonExistentParameterName);
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, listOfParameters),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("1, 3, 112, 4, 4, 2, 1, 1, 1, 1, 1"));

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
    public void testQueryLableSet()
    {
        System.out.println("Test Information Query procedure and its GET operation");

        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterLabelSet()),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Label label : mdCollection.getParameterLabelSet().getParameterLabels())
            {
                Optional<QualifiedParameter> result = queriedParameters_01.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().getFunctionalResourceName().getType()
                                .equals(label.getFunctionalResourceType()))
                        .findAny();
                assertTrue(result.isPresent());

                QualifiedParameter qualifiedParameter = result.get();
                System.out.println("got: " + qualifiedParameter);
            }

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

    @Ignore
    @Test
    public void testMultipleInformationQuerySecondary()
    {
        System.out.println("Test the Cyclic report procedure");

        try
        {
            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid_01 = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                                                 ProcedureRole.PRIME,
                                                                                 0);
            // inform query procedure identifier
            ProcedureInstanceIdentifier piid_02 = ProcedureInstanceIdentifier
                    .of(ProcedureType.of(OIDs.informationQuery), ProcedureRole.SECONDARY, 0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Arrays.asList(piid_01, piid_02);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(this.minimumAllowedDeliveryCycle,
                                                                                   defaultLabelList,
                                                                                   this.scId,
                                                                                   this.facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   piids);

            // create provider SI configuration
            MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(scId, facilityId, 0, "CSTS_PROVIDER", "CSTS_PT1", piids);

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
            TestUtils.verifyResult(
                                   userSi.startCyclicReport(piid_01.getInstanceNumber(),
                                                            mdCollection.getParameterNameSet(),
                                                            3000),
                                   "START-CYCLIC-REPORT");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                Optional<QualifiedParameter> result = queriedParameters_01.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
                assertTrue(result.isPresent());

                QualifiedParameter qualifiedParameter = result.get();
                System.out.println("got: " + qualifiedParameter);

                if (qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getType() == ParameterType.INTEGER)
                {
                    long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                            .getParameterValues().get(0).getIntegerParameterValues().get(0);
                    long userValue = qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                            .getIntegerParameterValues().get(0);

                    System.out.println("providerValue = " + providerValue);
                    System.out.println("userValue = " + userValue);
                    assertEquals("parameter " + name + " value is different", providerValue, userValue);
                }
            }

            // wait for several cyclic reports
            Thread.sleep(4000);

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 10);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                Optional<QualifiedParameter> result = queriedParameters_02.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
                assertTrue(result.isPresent());

                QualifiedParameter qualifiedParameter = result.get();
                System.out.println("got: " + qualifiedParameter);

                if (qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                        .getType() == ParameterType.INTEGER)
                {
                    long providerValue = mdCollection.getQualifiedParameter(name).getQualifiedValues().get(0)
                            .getParameterValues().get(0).getIntegerParameterValues().get(0);
                    long userValue = qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0)
                            .getIntegerParameterValues().get(0);

                    System.out.println("providerValue = " + providerValue);
                    System.out.println("userValue = " + userValue);
                    assertEquals("parameter " + name + " value is different", providerValue, userValue);
                }
            }

            assertTrue("no cyclic report received", userSi.getCyclicReportCount() > 0);

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(0), "STOP-CYCLIC-REPORT");

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
