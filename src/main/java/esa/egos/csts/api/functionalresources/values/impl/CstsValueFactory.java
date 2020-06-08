package esa.egos.csts.api.functionalresources.values.impl;

import java.math.BigInteger;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsBitStringValue;
import esa.egos.csts.api.functionalresources.values.ICstsBoolValue;
import esa.egos.csts.api.functionalresources.values.ICstsComplexValue;
import esa.egos.csts.api.functionalresources.values.ICstsEnumValue;
import esa.egos.csts.api.functionalresources.values.ICstsIntValue;
import esa.egos.csts.api.functionalresources.values.ICstsOctetStringValue;
import esa.egos.csts.api.functionalresources.values.ICstsOidValue;
import esa.egos.csts.api.functionalresources.values.ICstsRealValue;
import esa.egos.csts.api.functionalresources.values.ICstsStringValue;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.ICstsValueFactory;
import esa.egos.csts.api.oids.ObjectIdentifier;

public class CstsValueFactory implements ICstsValueFactory
{
    private static CstsValueFactory instance = null;

    private static Object instanceLock = new Object();

    private CstsValueFactory()
    {
    }

    public static CstsValueFactory getInstance()
    {
        if (instance == null)
        {
            synchronized (instanceLock)
            {
                instance = new CstsValueFactory();
            }
        }
        return instance;
    }

    @Override
    public ICstsBoolValue createCstsBoolValue(boolean value)
    {
        return CstsBoolValue.of(value);
    }

    @Override
    public ICstsBoolValue createCstsBoolValue(String name, ParameterQualifier qualifier, boolean value)
    {
        return CstsBoolValue.of(name, value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(int value)
    {
        return CstsIntValue.of(value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(String name, ParameterQualifier qualifier, int value)
    {
        return CstsIntValue.of(name, value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(BigInteger value)
    {
        return CstsIntValue.of(value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(String name, ParameterQualifier qualifier, BigInteger value)
    {
        return CstsIntValue.of(name, value);
    }

    @Override
    public ICstsOctetStringValue createCstsOctetStringValue(byte[] value)
    {
        return CstsOctetStringValue.of(value);
    }

    @Override
    public ICstsOctetStringValue createCstsOctetStringValue(String name, ParameterQualifier qualifier, byte[] value)
    {
        return CstsOctetStringValue.of(name, value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(int[] value)
    {
        return CstsOidValue.of(value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(String name, ParameterQualifier qualifier, int[] value)
    {
        return CstsOidValue.of(name, value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(ObjectIdentifier value)
    {
        return CstsOidValue.of(value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(String name, ParameterQualifier qualifier, ObjectIdentifier value)
    {
        return CstsOidValue.of(name, value);
    }

    @Override
    public ICstsRealValue createCstsRealValue(double value)
    {
        return CstsRealValue.of(value);
    }

    @Override
    public ICstsRealValue createCstsRealValue(String name, ParameterQualifier qualifier, double value)
    {
        return CstsRealValue.of(name, value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(byte[] value)
    {
        return CstsStringValue.of(value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(String name, ParameterQualifier qualifier, byte[] value)
    {
        return CstsStringValue.of(name, value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(String value)
    {
        return CstsStringValue.of(value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(String name, ParameterQualifier qualifier, String value)
    {
        return CstsStringValue.of(name, value);
    }

    @Override
    public ICstsBitStringValue createCstsBitStringValue(byte[] value)
    {
        return CstsBitStringValue.of(value);
    }

    @Override
    public ICstsBitStringValue createCstsBitStringValue(String name, byte[] value)
    {
        return CstsBitStringValue.of(name, value);
    }

    @Override
    public ICstsBitStringValue createCstsBitStringValue(boolean[] bits)
    {
        return CstsBitStringValue.of(bits);
    }

    @Override
    public ICstsBitStringValue createCstsBitStringValue(String name, boolean[] bits)
    {
        return CstsBitStringValue.of(name, bits);
    }

    @Override
    public ICstsComplexValue createCstsComplexValue(String name, ICstsValue... values)
    {
        return CstsComplexValue.of(name, values);
    }

    @Override
    public ICstsComplexValue createCstsComplexValue(String name, ParameterQualifier qualifier, ICstsValue... values)
    {
        return CstsComplexValue.of(name, qualifier, values);
    }
    
    @Override
    public CstsNullValue createCstsNullValue(String name)
    {
        return CstsNullValue.of(name);
    }

    @Override
    public CstsNullValue createCstsNullValue(String name, ParameterQualifier qualifier)
    {
        return CstsNullValue.of(name, qualifier);
    }

    @Override
    public ICstsValue createEmptyValue()
    {
        return CstsValue.empty();
    }

    @Override
    public ICstsEnumValue createCstsEnumValue(String name, int value, String valueName)
    {
        return CstsEnumValue.of(name, value, valueName);
    }

    @Override
    public ICstsEnumValue createCstsEnumValue(String name, ParameterQualifier qualifier, int value, String valueName)
    {
        return CstsEnumValue.of(name, value, valueName, qualifier);
    }
}
