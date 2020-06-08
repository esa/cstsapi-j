package esa.egos.csts.api.functionalresources.values;

import java.math.BigInteger;

public interface ICstsEnumValue extends ICstsSimpleValue<BigInteger>
{
    long getEnumValueOrdinal();

    String getEnumValueName();
}
