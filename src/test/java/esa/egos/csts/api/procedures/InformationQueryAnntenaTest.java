package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ConfigurationParameterNotModifiableException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;
import esa.egos.csts.api.TestUtils;

import org.junit.Ignore;

/**
 * Test CSTS API information query procedure w/ Antenna resources
 */
public class InformationQueryAnntenaTest extends MdCstsAntennaTestBase
{
    /**
     * Test information query procedure and its GET operation w/ default list =>
     * ListOfParameter /w type EMPTY
     */
    @Test
    public void testQueryInformationWithDefaultLabelList()
    {
        try
        {
            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // the secondary procedure w/ instance number 1 has not defined the
            // default list
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, ListOfParameters.empty()),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains that the Default list not defined
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       this.userSi.getDiagnostic().contains("Default list not defined"));

            Exception exc_01 = null;
            try
            {
                System.out.println("try to set the default label list to bound provider SI");
                this.providerSi.setDefaultLabelList(this.piid_iq_secondary, this.defaultLabelList);
            }
            catch (ConfigurationParameterNotModifiableException e)
            {
                exc_01 = e;
            }
            assertNotNull("setDefaultLabelList() did not throw ConfigurationParameterNotModifiableException on bound SI",
                          exc_01);

            // the default list parameter is not dynamically modifiable so
            // unbound is needed
            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid_iq_secondary, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // try to once again w/ the defined default list
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, ListOfParameters.empty()),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> receivedQualifiedParameters = this.userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", receivedQualifiedParameters.isEmpty());

            // we should get FR parameters according to the configured default
            // label list
            System.out.println("received parameters: " + receivedQualifiedParameters.size());
            assertEquals("different number of received parameters from requested parameters",
                         this.defaultLabelList.size(),
                         receivedQualifiedParameters.size());

            // we should get FR parameters w/ FR's type
            for (QualifiedParameter receivedQualifiedParameter : receivedQualifiedParameters)
            {
                System.out.println("received qualified parameter: " + receivedQualifiedParameter);
                FunctionalResourceType frType = receivedQualifiedParameter.getName().getFunctionalResourceName()
                        .getType();
                assertTrue("different FR type, expected " + Fr.antenna + " but received " + frType,
                           frType.equals(Fr.antenna));
            }

            for (Label label : this.defaultLabelList)
            {
                QualifiedParameter expectedQualifiedParameter = this.mdCollection.getFirstQualifiedParameter(label);

                Optional<QualifiedParameter> result = receivedQualifiedParameters.stream()
                        .filter(receivedQualifiedParameter -> receivedQualifiedParameter
                                .equals(expectedQualifiedParameter))
                        .findAny();
                assertTrue("missing qualified parameter " + label, result.isPresent());
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

    @Ignore
    /**
     * Test information query procedure and its GET operation w/
     * FUNCTIONAL_RESOURCE_NAME
     */
    @Test
    public void testQueryInformationWithFunctionalResourceName()
    {
        try
        {
            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(
                                   userSi.queryInformation(0,
                                                           ListOfParameters
                                                                   .of(FunctionalResourceName.of(Fr.antenna, 0))),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> receivedQualifiedParameters = this.userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", receivedQualifiedParameters.isEmpty());

            // we should get FR parameters according to the configured default
            // label list
            System.out.println("received parameters: " + receivedQualifiedParameters.size());
            assertEquals("different number of received parameters from requested parameters",
                         this.defaultLabelList.size(),
                         receivedQualifiedParameters.size());

            // we should get FR parameters w/ FR's type
            for (QualifiedParameter receivedQualifiedParameter : receivedQualifiedParameters)
            {
                System.out.println("received qualified parameter: " + receivedQualifiedParameter);
                FunctionalResourceType frType = receivedQualifiedParameter.getName().getFunctionalResourceName()
                        .getType();
                assertTrue("different FR type, expected " + Fr.antenna + " but received " + frType,
                           frType.equals(Fr.antenna));
            }

            for (FunctionalResourceParameter parameter : this.mdCollection.getParameters())
            {
                QualifiedParameter expectedQualifiedParameter = parameter.toQualifiedParameter();

                Optional<QualifiedParameter> result = receivedQualifiedParameters.stream()
                        .filter(receivedQualifiedParameter -> receivedQualifiedParameter
                                .equals(expectedQualifiedParameter))
                        .findAny();
                assertTrue("missing qualified parameter " + parameter.getName(), result.isPresent());
            }

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

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
