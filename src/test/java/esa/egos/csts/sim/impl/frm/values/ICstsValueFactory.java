package esa.egos.csts.sim.impl.frm.values;

import java.math.BigInteger;

import esa.egos.csts.api.oids.ObjectIdentifier;

public interface ICstsValueFactory
{
    ICstsBoolValue createCstsBoolValue(boolean value);

    ICstsBoolValue createCstsBoolValue(String name, boolean value);

    ICstsIntValue createCstsIntValue(int value);

    ICstsIntValue createCstsIntValue(String name, int value);

    ICstsIntValue createCstsIntValue(BigInteger value);

    ICstsIntValue createCstsIntValue(String name, BigInteger value);

    ICstsOctetStringValue createCstsOctetStringValue(byte[] value);

    ICstsOctetStringValue createCstsOctetStringValue(String name, byte[] value);

    ICstsOidValue createCstsOidValue(int[] value);

    ICstsOidValue createCstsOidValue(String name, int[] value);

    ICstsOidValue createCstsOidValue(ObjectIdentifier value);

    ICstsOidValue createCstsOidValue(String name, ObjectIdentifier value);

    ICstsRealValue createCstsRealValue(double value);

    ICstsRealValue createCstsRealValue(String name, double value);

    ICstsStringValue createCstsStringValue(byte[] value);

    ICstsStringValue createCstsStringValue(String name, byte[] value);

    ICstsStringValue createCstsStringValue(String value);

    ICstsStringValue createCstsStringValue(String name, String value);

    ICstsComplexValue createCstsComplexValue(String name, ICstsValue... values);

    ICstsValue createEmptyValue();
}
