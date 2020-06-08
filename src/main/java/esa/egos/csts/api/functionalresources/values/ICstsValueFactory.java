package esa.egos.csts.api.functionalresources.values;

import java.math.BigInteger;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.oids.ObjectIdentifier;

public interface ICstsValueFactory
{
    ICstsBoolValue createCstsBoolValue(boolean value);

    ICstsBoolValue createCstsBoolValue(String name, ParameterQualifier qualifier, boolean value);

    ICstsEnumValue createCstsEnumValue(String name, int value, String valueName);

    ICstsEnumValue createCstsEnumValue(String name, ParameterQualifier qualifier, int value, String valueName);

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
    
    ICstsBitStringValue createCstsBitStringValue(byte[] value);

    ICstsBitStringValue createCstsBitStringValue(String name, byte[] value);

    ICstsBitStringValue createCstsBitStringValue(boolean[] bits);

    ICstsBitStringValue createCstsBitStringValue(String name, boolean[] bits);

    CstsNullValue createCstsNullValue(String name);

    CstsNullValue createCstsNullValue(String name, ParameterQualifier qualifier);

    ICstsComplexValue createCstsComplexValue(String name, ICstsValue... values);

    ICstsComplexValue createCstsComplexValue(String name, ParameterQualifier qualifier, ICstsValue... values);

    ICstsValue createEmptyValue();
}
