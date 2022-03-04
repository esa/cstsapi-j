package esa.egos.csts.api.parameters.impl;

import java.time.LocalDateTime;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractParameter;

public abstract class FunctionalResourceParameter extends AbstractParameter
{

    private String classifier;

    private String authorizingEntitiy;

    private LocalDateTime creationDate;

    private boolean deprecated;

    private boolean configured;

    private String semanticDefinition;

    private double value;

    private String unit;


    public FunctionalResourceParameter(ObjectIdentifier identifier, FunctionalResourceName functionalResourceName)
    {
        super(identifier, functionalResourceName);
    }

    public String getClassifier()
    {
        return classifier;
    }

    public void setClassifier(String classifier)
    {
        this.classifier = classifier;
    }

    public String getAuthorizingEntitiy()
    {
        return authorizingEntitiy;
    }

    public void setAuthorizingEntitiy(String authorizingEntitiy)
    {
        this.authorizingEntitiy = authorizingEntitiy;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    public boolean isDeprecated()
    {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated)
    {
        this.deprecated = deprecated;
    }

    public boolean isConfigured()
    {
        return configured;
    }

    public void setConfigured(boolean configured)
    {
        this.configured = configured;
    }

    public String getSemanticDefinition()
    {
        return semanticDefinition;
    }

    public void setSemanticDefinition(String semanticDefinition)
    {
        this.semanticDefinition = semanticDefinition;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
        this.setChanged();
        this.notifyObservers();
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FunctionalResourceParameter [");
        sb.append(super.toString());
        sb.append(", classifier=");
        sb.append(this.classifier);
        sb.append(", authorizingEntitiy=");
        sb.append(this.authorizingEntitiy);
        sb.append(", creationDate=");
        sb.append(this.creationDate);
        sb.append(", deprecated=");
        sb.append(this.deprecated);
        sb.append(", configured=");
        sb.append(this.configured);
        sb.append(", semanticDefinition=");
        sb.append(this.semanticDefinition);
        sb.append(", value=");
        sb.append(this.value);
        sb.append(", unit=");
        sb.append(this.unit);
        sb.append("]");
        return sb.toString();
    }

}
