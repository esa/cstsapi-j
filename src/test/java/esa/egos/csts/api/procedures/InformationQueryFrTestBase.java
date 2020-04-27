package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.TestUtils;
import esa.egos.csts.api.enumerations.CstsResult;

/**
 * Test CSTS API information query procedure w/ Fr parameters
 */
public abstract class InformationQueryFrTestBase extends MdCstsTestBase
{

    protected abstract List<Label> createDefaultLabelList();
    
    protected abstract FunctionalResourceType getFunctionalResource();
    
    // non existent parameter - parameter is not added into MD collection
    private ObjectIdentifier antIdId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1, 1, 1, 1 });
    private FunctionalResourceType antIdType = FunctionalResourceType.of(this.antIdId);
    private FunctionalResourceName antIdName = FunctionalResourceName.of(this.antIdType, 1);
    private Name nonExistentParameterName = Name.of(this.antIdId, this.antIdName);

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        MdCstsTestBase.setUpClass();
    }

    /**
     * Test information query procedure and its GET operation w/ NAME_SET
     */
    @Test
    public void testQueryInformationWithNameSet()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of names for Fr 0 and 1 and set parameters in provider
            System.out.println("Create names for Fr 0 and 1 and set parameters in provider");
            List<Name> names0 = new ArrayList<Name>(testParameters.size());
            List<Name> names1 = new ArrayList<Name>(testParameters.size());
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
                this.providerSi.setParameterValue(name0, testParameter.initValue);
                names0.add(name0);

                Name name1 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                this.providerSi.setParameterValue(name1, testParameter.initValue);
                names1.add(name1);
            }

            // check values at provider
            System.out.println("Check values at provider");
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                assertTrue("provider(Fr=0): found incorrect value of " + testParameter.name,
                           this.providerSi.getParameterValue(names0.get(index)).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + testParameter.name,
                           this.providerSi.getParameterValue(names1.get(index)).equals(testParameter.initValue));
                index++;
            }
            
            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            List<Name> namesList = new ArrayList<Name>(2*testParameters.size());
            namesList.addAll(names0);
            namesList.addAll(names1);
            ListOfParameters listOfParameters = ListOfParameters.of(namesList.toArray(new Name[] {}));

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");

            // check values at user
            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                assertTrue("user(Fr=0): found incorrect value of " + testParameter.name,
                           this.userSi.getParameterValue(this.piid_iq_secondary, names0.get(index)).equals(testParameter.initValue));
                assertTrue("user(Fr=1): found incorrect value of " + testParameter.name,
                           this.userSi.getParameterValue(this.piid_iq_secondary, names1.get(index)).equals(testParameter.initValue));
                index++;
            }

            // update values at provider
            System.out.println("Update values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(names0.get(index), testParameter.updatedValue);
                this.providerSi.setParameterValue(names1.get(index), testParameter.updatedValue);
                index++;
            }

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");

            // check updated values at user
            System.out.println("Check updated values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                assertTrue("user(Fr=0): found incorrect updated value of " + testParameter.name,
                           this.userSi.getParameterValue(this.piid_iq_secondary, names0.get(index)).equals(testParameter.updatedValue));
                assertTrue("user(Fr=1): found incorrect updated value of " + testParameter.name,
                           this.userSi.getParameterValue(this.piid_iq_secondary, names1.get(index)).equals(testParameter.updatedValue));
                index++;
            }

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test information query procedure and its GET operation w/ LABEL_SET
     */
    @Test
    public void testQueryInformationWithLabelSet()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of Labels for both Fr and set parameters at provider
            System.out.println("Create labels for both Fr and set parameters in provider");
            List<Label> labels = createDefaultLabelList();
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels.get(index++), testParameter.initValue);
            }

            // check values at provider
            System.out.println("Check values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> providerValue = this.providerSi.getParameterValue(labels.get(index++));
                assertTrue("provider: didn't get 2 values for " + testParameter.name, providerValue.size() == 2);
                assertTrue("provider(Fr=0): found incorrect value of " + testParameter.name,
                           providerValue.get(0).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + testParameter.name,
                           providerValue.get(1).equals(testParameter.initValue));
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            ListOfParameters listOfParameters = ListOfParameters.of(labels.toArray(new Label[] {}));

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");


            // check values at user
            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> userValue = this.userSi.getParameterValue(this.piid_iq_secondary, labels.get(index++));
                assertTrue("user: didn't get 2 values for " + testParameter.name, userValue.size() == 2);
                assertTrue("user(Fr=0): found incorrect value of " + testParameter.name,
                           userValue.get(0).equals(testParameter.initValue));
                assertTrue("user(Fr=1): found incorrect value of " + testParameter.name,
                           userValue.get(1).equals(testParameter.initValue));
            }

            // change values at provider
            System.out.println("Change values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels.get(index++), testParameter.updatedValue);
            }

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");

            // check values at user
            System.out.println("Check modified values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> userValue = this.userSi.getParameterValue(this.piid_iq_secondary, labels.get(index++));
                assertTrue("user: didn't get 2 values for " + testParameter.name, userValue.size() == 2);
                assertTrue("user(Fr=0): fount incorrect value of " + testParameter.name,
                           userValue.get(0).equals(testParameter.updatedValue));
                assertTrue("user(Fr=1): found incorrect value of " + testParameter.name,
                           userValue.get(1).equals(testParameter.updatedValue));
            }

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test information query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     */
    @Test
    public void testQueryInformationWithFunctionalResourceName()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create functional resource names for Fr 0 and 1 and set parameters at provider
            System.out.println("Create functional resource names for Fr 0 and 1 and set parameters in provider");
            FunctionalResourceName frn0 = FunctionalResourceName.of(getFunctionalResource(), 0);
            FunctionalResourceName frn1 = FunctionalResourceName.of(getFunctionalResource(), 1);
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Label.of(testParameter.oid, getFunctionalResource()), testParameter.initValue);
            }
            
            // create lists of names of parameters set at provider
            List<Name> names0 = new ArrayList<Name>(testParameters.size());
            List<Name> names1 = new ArrayList<Name>(testParameters.size());
            for (TestParameter testParameter : testParameters)
            {
                names0.add(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0)));
                names1.add(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1)));
            }

            // check values at provider for both Fr
            System.out.println("Check values at provider for both Fr");
            Map<Name, ICstsValue> providerValues0 = this.providerSi.getParameterValues(frn0);
            Map<Name, ICstsValue> providerValues1 = this.providerSi.getParameterValues(frn1);
            Iterator<Name> it0 = names0.iterator();
            Iterator<Name> it1 = names1.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = it0.next();
                assertTrue("provider(Fr=0): found incorrect value of " + name0,
                           providerValues0.get(name0).equals(testParameter.initValue));
                Name name1 = it1.next();
                assertTrue("provider(Fr=1): found incorrect value of " + name1,
                           providerValues1.get(name1).equals(testParameter.initValue));
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for Fr 0");
            ListOfParameters listOfParameters0 = ListOfParameters.of(frn0);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters0),
                    "QUERY-INFORMATION");

            // check values at user for Fr 0
            System.out.println("Check values at user for Fr 0");
            Map<Name, ICstsValue> userValues = this.userSi.getParameterValues(this.piid_iq_secondary, frn0);
            it0 = names0.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it0.next();
                assertTrue("user(Fr=0): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.initValue));
            }

            System.out.println("Query parameters for Fr 1");
            ListOfParameters listOfParameters1 = ListOfParameters.of(frn1);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters1),
                    "QUERY-INFORMATION");

            // check values at user for Fr 1
            System.out.println("Check values at user for Fr 1");
            userValues = this.userSi.getParameterValues(this.piid_iq_secondary, frn1);
            it1 = names1.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it1.next();
                assertTrue("user(Fr=1): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.initValue));
            }

            // change values at provider
            System.out.println("Change values at provider");
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0)),
                                                  testParameter.updatedValue);
                this.providerSi.setParameterValue(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1)),
                                                  testParameter.updatedValue);
            }

            System.out.println("Query updated parameters for Fr 0");
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters0),
                    "QUERY-INFORMATION");

            // check values at user for Fr 0
            System.out.println("Check values at user for Fr 0");
            userValues = this.userSi.getParameterValues(this.piid_iq_secondary, frn0);
            it0 = names0.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it0.next();
                assertTrue("user(Fr=0): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.updatedValue));
            }

            System.out.println("Query updated parameters for Fr 1");
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters1),
                    "QUERY-INFORMATION");

            // check values at user for Fr 1
            System.out.println("Check values at user for Fr 1");
            userValues = this.userSi.getParameterValues(this.piid_iq_secondary, frn1);
            it1 = names1.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it1.next();
                assertTrue("user(Fr=0): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.updatedValue));
            }

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test information query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testQueryInformationWithFunctionalResourceType()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // set parameters at provider and create lists of Labels of parameters set at provider
            System.out.println("Set values of parameters in provider for both Fr");
            List<Label> labels = createDefaultLabelList();
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels.get(index++), testParameter.initValue);
            }

            // check values at provider for both Fr
            System.out.println("Check values at provider for both Fr");
            Map<Integer, Map<Label, ICstsValue>> providerValues = this.providerSi.getParameterValues(getFunctionalResource());
            assertTrue("provider: didn't get 2 sets of parameters for " + getFunctionalResource(), providerValues.size() == 2);
            Map<Label, ICstsValue> values0 = providerValues.get(Integer.valueOf(0));
            Map<Label, ICstsValue> values1 = providerValues.get(Integer.valueOf(1));
            Iterator<Label> it = labels.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Label label = it.next();
                assertTrue("provider(Fr=0): value not found: " + label, values0.containsKey(label));
                assertTrue("provider(Fr=1): value not found: " + label, values1.containsKey(label));
                assertTrue("provider(Fr=0): found incorrect value of " + label,
                           values0.get(label).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + label,
                           values1.get(label).equals(testParameter.initValue));
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for both Fr");
            ListOfParameters listOfParameters = ListOfParameters.of(getFunctionalResource());

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");

            // check values at user for both Fr
            System.out.println("Check values at user for both Fr");
            Map<Integer, Map<Label, ICstsValue>> userValues =
                    this.userSi.getParameterValues(this.piid_iq_secondary, getFunctionalResource());
            assertTrue("user: didn't get 2 sets of parameters for " + getFunctionalResource(), userValues.size() == 2);
            values0 = userValues.get(Integer.valueOf(0));
            values1 = userValues.get(Integer.valueOf(1));
            it = labels.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Label label = it.next();
                assertTrue("user(Fr=0): value not found: " + label, values0.containsKey(label));
                assertTrue("user(Fr=1): value not found: " + label, values1.containsKey(label));
                assertTrue("user(Fr=0): found incorrect value of " + label,
                           values0.get(label).equals(testParameter.initValue));
                assertTrue("user(Fr=1): found incorrect value of " + label,
                           values1.get(label).equals(testParameter.initValue));
            }

            // change values at provider
            System.out.println("Change values at provider");
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Label.of(testParameter.oid, getFunctionalResource()), testParameter.updatedValue);
            }

            System.out.println("Query updated parameters for Fr 0");
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");

            // check updated values at user for both Fr
            System.out.println("Check updated values at user for both Fr");
            userValues = this.userSi.getParameterValues(this.piid_iq_secondary, getFunctionalResource());
            assertTrue("user: didn't get 2 sets of parameters for " + getFunctionalResource(), userValues.size() == 2);
            values0 = userValues.get(Integer.valueOf(0));
            values1 = userValues.get(Integer.valueOf(1));
            it = labels.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Label label = it.next();
                assertTrue("user(Fr=0): value not found: " + label, values0.containsKey(label));
                assertTrue("user(Fr=1): value not found: " + label, values1.containsKey(label));
                assertTrue("user(Fr=0): found incorrect value of " + label,
                           values0.get(label).equals(testParameter.updatedValue));
                assertTrue("user(Fr=1): found incorrect value of " + label,
                           values1.get(label).equals(testParameter.updatedValue));
            }

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test information query procedure and its GET operation w/ PROCEDURE_TYPE
     */
    @Test
    public void testQueryInformationWithProcedureType()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of Labels for both Fr and set parameters at provider
            System.out.println("Create labels for both Fr and set parameters in provider");
            List<Label> labels = createDefaultLabelList();

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid_iq_secondary, labels);

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            ProcedureType procedureType = this.piid_iq_secondary.getType();
            ListOfParameters listOfParameters = ListOfParameters.of(procedureType);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters = userSi.getLastQueriedParameters();
            Optional<QualifiedParameter> result = queriedParameters.stream()
                            .filter(qualifiedParameter -> qualifiedParameter.getName().getProcedureInstanceIdentifier().getType()
                                    .equals(procedureType) && qualifiedParameter.getName().toString().contains("pIQnamedLabelLists"))
                            .findAny();
            assertTrue("missing pIQnamedLabelLists parameter of " + piid_iq_secondary, result.isPresent());

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test information query procedure and its GET operation w/ PROCEDURE_INSTANCE_IDENTIFIER
     */
    @Test
    public void testQueryInformationWithProcedureInstanceIdentifier()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of Labels for both Fr and set parameters at provider
            System.out.println("Create labels for both Fr and set parameters in provider");
            List<Label> labels = createDefaultLabelList();

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid_iq_secondary, labels);

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            ProcedureType procedureType = this.piid_iq_secondary.getType();
            ListOfParameters listOfParameters = ListOfParameters.of(procedureType);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters = userSi.getLastQueriedParameters();
            Optional<QualifiedParameter> result = queriedParameters.stream()
                            .filter(qualifiedParameter -> qualifiedParameter.getName().getProcedureInstanceIdentifier().getType()
                                    .equals(procedureType) && qualifiedParameter.getName().toString().contains("pIQnamedLabelLists"))
                            .findAny();
            assertTrue("missing pIQnamedLabelLists parameter of " + piid_iq_secondary, result.isPresent());

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test information query procedure and its GET operation w/ EMPTY
     */
    @Test
    public void testQueryInformationWithEmpty()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, ListOfParameters.empty()),
                    "QUERY-INFORMATION",
                    CstsResult.FAILURE);

            // verify that diagnostic contains that the Default list not defined
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("Default list not defined"));

            System.out.println("DIAG: "+userSi.getDiagnostic());

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
