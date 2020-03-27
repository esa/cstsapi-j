package esa.egos.csts.sim.impl.frm.values;

import esa.egos.csts.api.enumerations.ParameterQualifier;

public interface ICstsValue
{
    String getName();

    ParameterQualifier getQuality();
}
