package esa.egos.csts.sim.impl.frm.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsStringValue;

public class CstsStringValue extends CstsSimpleValue<byte[]> implements ICstsStringValue
{
    public static CstsStringValue of(String value)
    {
        return new CstsStringValue(value);
    }

    public static CstsStringValue of(byte[] value)
    {
        return new CstsStringValue(value);
    }

    public static CstsStringValue of(String name, byte[] value)
    {
        return new CstsStringValue(name, value);
    }

    public static CstsStringValue of(String name, String value)
    {
        return new CstsStringValue(name, value);
    }

    private CstsStringValue(String value)
    {
        super(ParameterQualifier.VALID);
        this.value = value.getBytes();
    }

    private CstsStringValue(byte[] value)
    {
        super(ParameterQualifier.VALID);
        this.value = value.clone();
    }

    private CstsStringValue(String name, byte[] value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsStringValue(String name, String value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value.getBytes();
    }

    private CstsStringValue(String value, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = value.getBytes();
    }

    public String getStringValue()
    {
        return new String(this.value);
    }
}
