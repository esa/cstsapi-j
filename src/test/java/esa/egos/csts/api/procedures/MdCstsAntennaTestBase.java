package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Setup for CSTS FR Antenna tests
 */
public class MdCstsAntennaTestBase extends MdCstsTestBase
{

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
