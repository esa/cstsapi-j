package esa.egos.csts.sim.impl.frm.values.impl;

import java.math.BigInteger;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.sim.impl.frm.values.ICstsBoolValue;
import esa.egos.csts.sim.impl.frm.values.ICstsComplexValue;
import esa.egos.csts.sim.impl.frm.values.ICstsIntValue;
import esa.egos.csts.sim.impl.frm.values.ICstsOctetStringValue;
import esa.egos.csts.sim.impl.frm.values.ICstsOidValue;
import esa.egos.csts.sim.impl.frm.values.ICstsRealValue;
import esa.egos.csts.sim.impl.frm.values.ICstsStringValue;
import esa.egos.csts.sim.impl.frm.values.ICstsValue;
import esa.egos.csts.sim.impl.frm.values.ICstsValueFactory;

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
    public ICstsBoolValue createCstsBoolValue(String name, boolean value)
    {
        return CstsBoolValue.of(name, value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(int value)
    {
        return CstsIntValue.of(value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(String name, int value)
    {
        return CstsIntValue.of(name, value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(BigInteger value)
    {
        return CstsIntValue.of(value);
    }

    @Override
    public ICstsIntValue createCstsIntValue(String name, BigInteger value)
    {
        return CstsIntValue.of(name, value);
    }

    @Override
    public ICstsOctetStringValue createCstsOctetStringValue(byte[] value)
    {
        return CstsOctetStringValue.of(value);
    }

    @Override
    public ICstsOctetStringValue createCstsOctetStringValue(String name, byte[] value)
    {
        return CstsOctetStringValue.of(name, value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(int[] value)
    {
        return CstsOidValue.of(value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(String name, int[] value)
    {
        return CstsOidValue.of(name, value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(ObjectIdentifier value)
    {
        return CstsOidValue.of(value);
    }

    @Override
    public ICstsOidValue createCstsOidValue(String name, ObjectIdentifier value)
    {
        return CstsOidValue.of(name, value);
    }

    @Override
    public ICstsRealValue createCstsRealValue(double value)
    {
        return CstsRealValue.of(value);
    }

    @Override
    public ICstsRealValue createCstsRealValue(String name, double value)
    {
        return CstsRealValue.of(name, value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(byte[] value)
    {
        return CstsStringValue.of(value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(String name, byte[] value)
    {
        return CstsStringValue.of(name, value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(String value)
    {
        return CstsStringValue.of(value);
    }

    @Override
    public ICstsStringValue createCstsStringValue(String name, String value)
    {
        return CstsStringValue.of(name, value);
    }

    @Override
    public ICstsComplexValue createCstsComplexValue(String name, ICstsValue... values)
    {
        return CstsComplexValue.of(name, values);
    }

    @Override
    public ICstsValue createEmptyValue()
    {
        return CstsValue.empty();
    }
}
