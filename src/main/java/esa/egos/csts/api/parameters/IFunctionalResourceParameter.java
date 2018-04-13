package esa.egos.csts.api.parameters;

import java.time.LocalDateTime;

import esa.egos.csts.api.main.ObjectIdentifier;

public interface IFunctionalResourceParameter extends IParameter {

	void setUnit(String unit);

	String getUnit();

	void setValue(double value);

	double getValue();

	void setSemanticDefinition(String semanticDefinition);

	String getSemanticDefinition();

	void setIdentifier(ObjectIdentifier identifier);

	ObjectIdentifier getIdentifier();

	void setConfigured(boolean configured);

	boolean isConfigured();

	void setDeprecated(boolean deprecated);

	boolean isDeprecated();

	void setCreationDate(LocalDateTime creationDate);

	LocalDateTime getCreationDate();

	void setAuthorizingEntitiy(String authorizingEntitiy);

	String getAuthorizingEntitiy();

	void setClassifier(String classifier);

	String getClassifier();

}
