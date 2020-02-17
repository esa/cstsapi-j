package esa.egos.csts.test.mdslite.impl.simulator.parameters;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;

public class FunctionalResourceIntegerParameter extends FunctionalResourceParameter
{
    public FunctionalResourceIntegerParameter(ObjectIdentifier identifier,
                                              FunctionalResourceName functionalResourceName)
    {
        super(identifier, functionalResourceName);
        
    }

    public long getLongValue() 
    {
        return new Double(getValue()).longValue();
    }

    @Override
    public QualifiedParameter toQualifiedParameter()
    {
        QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());

        ParameterValue parameterValue = new ParameterValue(ParameterType.INTEGER);
        parameterValue.getIntegerParameterValues().add(new Double(getValue()).longValue());

        QualifiedValues qualifiedValues = new QualifiedValues(ParameterQualifier.VALID);
        qualifiedValues.getParameterValues().add(parameterValue);

        qualifiedParameter.getQualifiedValues().add(qualifiedValues);

        return qualifiedParameter;
    }
}
