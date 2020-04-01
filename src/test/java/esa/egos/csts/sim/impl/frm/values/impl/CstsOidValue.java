package esa.egos.csts.sim.impl.frm.values.impl;

import java.util.Arrays;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.sim.impl.frm.values.ICstsOidValue;

public class CstsOidValue extends CstsSimpleValue<int[]> implements ICstsOidValue
{
    public static CstsOidValue of(ObjectIdentifier oid)
    {
        return new CstsOidValue(oid);
    }

    public static CstsOidValue of(String name, ObjectIdentifier oid)
    {
        return new CstsOidValue(name, oid);
    }

    public static CstsOidValue of(int[] oidArray)
    {
        return new CstsOidValue(oidArray);
    }

    public static CstsOidValue of(String name, int[] oidArray)
    {
        return new CstsOidValue(name, oidArray);
    }

    private CstsOidValue(ObjectIdentifier oid)
    {
        super(ParameterQualifier.VALID);
        this.value = oid.toArray();
    }

    private CstsOidValue(String name, ObjectIdentifier oid)
    {
        super(name, ParameterQualifier.VALID);
        this.value = oid.toArray();
    }

    private CstsOidValue(int[] oidArray)
    {
        super(ParameterQualifier.VALID);
        this.value = Arrays.copyOf(oidArray, oidArray.length);
    }

    private CstsOidValue(String name, int[] oidArray)
    {
        super(name, ParameterQualifier.VALID);
        this.value = oidArray;
    }

    private CstsOidValue(ObjectIdentifier oid, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = oid.toArray();
    }

    private CstsOidValue(String name, ObjectIdentifier oid, ParameterQualifier qualifier)
    {
        super(name, qualifier);
        this.value = oid.toArray();
    }

    public ObjectIdentifier getOidValue()
    {
        return ObjectIdentifier.of(this.value);
    }
}
