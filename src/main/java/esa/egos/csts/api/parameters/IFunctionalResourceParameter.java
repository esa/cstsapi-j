package esa.egos.csts.api.parameters;

import java.time.LocalDateTime;

import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Name;

@Deprecated
public interface IFunctionalResourceParameter extends IParameter {

	Name getName();
	
	void setUnit(String unit);

	String getUnit();

	void setValue(double value);

	double getValue();

	void setSemanticDefinition(String semanticDefinition);

	String getSemanticDefinition();

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

	IFunctionalResource getFunctionalResource();
	
	QualifiedParameter toQualifiedParameter();

}
