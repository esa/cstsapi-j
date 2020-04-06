package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.sim.impl.frm.Fr;
import esa.egos.csts.api.TestUtils;

/**
 * Test CSTS API information query procedure w/ Antenna parameters
 */
public class InformationQueryAnntenaTest extends MdCstsAntennaTestBase
{
    // TODO implement equals for all derived classes from ICstsValue for comparison of the provider and user values

    /**
     * Test information query procedure and its GET operation w/ NAME_SET
     */
    @Test
    public void testQueryInformationWithNameSet()
    {
        try
        {
            // set FRs of Antenna 0 and Antenna 1
            this.providerSi.setFunctionalResources(Fr.antenna, Fr.antenna);

            Name antActualAzimuthName = Name.of(Fr.Antenna.parameter.antActualAzimuth,
                                                FunctionalResourceName.of(Fr.antenna, 0));

            Name antActualElevationName = Name.of(Fr.Antenna.parameter.antActualElevation,
                                                        FunctionalResourceName.of(Fr.antenna, 1));

            this.providerSi.setParameterValue(antActualAzimuthName, CstsIntValue.of(100));
            this.providerSi.setParameterValue(antActualElevationName, CstsIntValue.of(30));

            System.out.println(this.providerSi.getParameterValue(antActualAzimuthName));
            System.out.println(this.providerSi.getParameterValue(antActualElevationName));


            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

//            ListOfParameters listOfParameters = ListOfParameters.of(FunctionalResourceName.of(Fr.antenna, 0));
            ListOfParameters listOfParameters = ListOfParameters.of(antActualAzimuthName, antActualElevationName);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");


            ICstsValue antActualAzimuthValue = this.userSi.getParameterValue(antActualAzimuthName);
            ICstsValue antActualElevationValue = this.userSi.getParameterValue(antActualElevationName);

            System.out.println(antActualAzimuthValue);
            System.out.println(antActualElevationValue);


            this.providerSi.setParameterValue(antActualAzimuthName, CstsIntValue.of(200));
            this.providerSi.setParameterValue(antActualElevationName, CstsIntValue.of(50));

            antActualAzimuthValue = this.userSi.getParameterValue(antActualAzimuthName);
            antActualElevationValue = this.userSi.getParameterValue(antActualElevationName);

            System.out.println(antActualAzimuthValue);
            System.out.println(antActualElevationValue);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");


            antActualAzimuthValue = this.userSi.getParameterValue(antActualAzimuthName);
            antActualElevationValue = this.userSi.getParameterValue(antActualElevationName);

            System.out.println(antActualAzimuthValue);
            System.out.println(antActualElevationValue);


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
