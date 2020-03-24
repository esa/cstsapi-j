package esa.egos.csts.api.procedures;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.sim.impl.frm.Fr;
import esa.egos.csts.sim.impl.frm.FunctionalResourceParameterEx;
import esa.egos.csts.sim.impl.prv.MdCollection;
import frm.csts.functional.resource.types.AntAccumulatedPrecipitation;
import frm.csts.functional.resource.types.AntActualAzimuth;
import frm.csts.functional.resource.types.AntActualElevation;

/**
 * Setup for CSTS FR Antenna tests
 */
public class MdCstsAntennaTestBase extends MdCstsTestBase
{
    @Override
    protected MdCollection createCollection() throws Exception
    {
        return MdCollection.createCollection(Fr.antenna);
    }

    @Override
    protected void initValues()
    {
        @SuppressWarnings("unchecked")
        FunctionalResourceParameterEx<AntAccumulatedPrecipitation> antAccumulatedPrecipitation = (FunctionalResourceParameterEx<AntAccumulatedPrecipitation>) this.mdCollection
                .getParameter(Name.of(Fr.Antenna.parameter.antAccumulatedPrecipitation,
                                      FunctionalResourceName.of(Fr.antenna, 0)));
        antAccumulatedPrecipitation.getBerValue().value = BigInteger.valueOf(1);

        @SuppressWarnings("unchecked")
        FunctionalResourceParameterEx<AntActualAzimuth> antActualAzimuth = (FunctionalResourceParameterEx<AntActualAzimuth>) this.mdCollection
                .getParameter(Name.of(Fr.Antenna.parameter.antActualAzimuth, FunctionalResourceName.of(Fr.antenna, 0)));
        antActualAzimuth.getBerValue().value = BigInteger.valueOf(2);

        @SuppressWarnings("unchecked")
        FunctionalResourceParameterEx<AntActualElevation> antActualElevation = (FunctionalResourceParameterEx<AntActualElevation>) this.mdCollection
                .getParameter(Name.of(Fr.Antenna.parameter.antActualElevation,
                                      FunctionalResourceName.of(Fr.antenna, 0)));
        antActualElevation.getBerValue().value = BigInteger.valueOf(3);

        // TODO set the remaining initial values
    }

    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>();
        ret.add(Label.of(Fr.Antenna.parameter.antAccumulatedPrecipitation, Fr.antenna));
        ret.add(Label.of(Fr.Antenna.parameter.antActualAzimuth, Fr.antenna));
        ret.add(Label.of(Fr.Antenna.parameter.antActualElevation, Fr.antenna));
        return ret;
    }

}
