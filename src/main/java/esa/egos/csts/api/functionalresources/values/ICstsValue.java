package esa.egos.csts.api.functionalresources.values;

import esa.egos.csts.api.enumerations.ParameterQualifier;

public interface ICstsValue
{
    String getName();

    ParameterQualifier getQuality();
}
