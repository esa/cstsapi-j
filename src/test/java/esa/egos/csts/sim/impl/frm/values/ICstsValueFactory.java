package esa.egos.csts.sim.impl.frm.values;

import java.math.BigInteger;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.oids.ObjectIdentifier;

public interface ICstsValueFactory
{
    ICstsBoolValue createCstsBoolValue(boolean value);

    ICstsBoolValue createCstsBoolValue(String name, ParameterQualifier qualifier, boolean value);

    ICstsIntValue createCstsIntValue(int value);

    ICstsIntValue createCstsIntValue(String name, ParameterQualifier qualifier, int value);

    ICstsIntValue createCstsIntValue(BigInteger value);

    ICstsIntValue createCstsIntValue(String name, ParameterQualifier qualifier, BigInteger value);

    ICstsOctetStringValue createCstsOctetStringValue(byte[] value);

    ICstsOctetStringValue createCstsOctetStringValue(String name, ParameterQualifier qualifier, byte[] value);

    ICstsOidValue createCstsOidValue(int[] value);

    ICstsOidValue createCstsOidValue(String name, ParameterQualifier qualifier, int[] value);

    ICstsOidValue createCstsOidValue(ObjectIdentifier value);

    ICstsOidValue createCstsOidValue(String name, ParameterQualifier qualifier, ObjectIdentifier value);

    ICstsRealValue createCstsRealValue(double value);

    ICstsRealValue createCstsRealValue(String name, ParameterQualifier qualifier, double value);

    ICstsStringValue createCstsStringValue(byte[] value);

    ICstsStringValue createCstsStringValue(String name, ParameterQualifier qualifier, byte[] value);

    ICstsStringValue createCstsStringValue(String value);

    ICstsStringValue createCstsStringValue(String name, ParameterQualifier qualifier, String value);

    ICstsComplexValue createCstsComplexValue(String name, ICstsValue... values);

    ICstsComplexValue createCstsComplexValue(String name, ParameterQualifier qualifier, ICstsValue... values);

    ICstsValue createEmptyValue();
}
