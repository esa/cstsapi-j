package esa.egos.csts.api.functionalresources.values;

import esa.egos.csts.api.oids.ObjectIdentifier;

public interface ICstsOidValue extends ICstsSimpleValue<int[]>
{
    ObjectIdentifier getOidValue();
}
