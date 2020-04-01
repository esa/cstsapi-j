package esa.egos.csts.sim.impl.frm.values;

import esa.egos.csts.api.oids.ObjectIdentifier;

public interface ICstsOidValue extends ICstsSimpleValue<int[]>
{
    ObjectIdentifier getOidValue();
}
