package esa.egos.csts.api.functionalresources.values.impl;

import com.beanit.jasn1.ber.types.BerNull;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsNullValue;

public class CstsNullValue extends CstsSimpleValue<BerNull> implements ICstsNullValue
{

    protected CstsNullValue(String name)
    {
        super(name);
    }

    protected CstsNullValue(String name, ParameterQualifier qualifier)
    {
        super(name, qualifier);
    }

    public static CstsNullValue of(String name)
    {
        return new CstsNullValue(name);
    }

    public static CstsNullValue of(String name, ParameterQualifier qualifier)
    {
        return new CstsNullValue(name, qualifier);
    }

    @Override
    public String toString()
    {
        return "CstsNullValue [name=" + getName() + ", quality=" + getQuality() + "]";
    }

}
