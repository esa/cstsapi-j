package esa.egos.csts.api.functionalresources.values;

import java.util.List;

public interface ICstsComplexValue extends ICstsValue
{
    List<ICstsValue> getValues();

    boolean isSizeConstrainedType();
}
