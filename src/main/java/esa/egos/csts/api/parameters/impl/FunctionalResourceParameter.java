package esa.egos.csts.api.parameters.impl;

import java.time.LocalDateTime;

import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractParameter;
import esa.egos.csts.api.types.Name;

public class FunctionalResourceParameter extends AbstractParameter {

	private final Name name;
	private final IFunctionalResource functionalResource;
	private String classifier;
	private String authorizingEntitiy;
	private LocalDateTime creationDate;
	private boolean deprecated;
	private boolean configured;
	private String semanticDefinition;
	private double value;
	private String unit;

	public FunctionalResourceParameter(ObjectIdentifier identifier, IFunctionalResource functionalResource) {
		super(identifier, functionalResource.getType());
		this.functionalResource = functionalResource;
		name = new Name(identifier, functionalResource.getName());
	}
	
	public Name getName() {
		return name;
	}
	
	public IFunctionalResource getFunctionalResource() {
		return functionalResource;
	}
	
	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public String getAuthorizingEntitiy() {
		return authorizingEntitiy;
	}

	public void setAuthorizingEntitiy(String authorizingEntitiy) {
		this.authorizingEntitiy = authorizingEntitiy;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	public String getSemanticDefinition() {
		return semanticDefinition;
	}

	public void setSemanticDefinition(String semanticDefinition) {
		this.semanticDefinition = semanticDefinition;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public QualifiedParameter toQualifiedParameter() {
		QualifiedParameter qualifiedParameter = new QualifiedParameter();
		qualifiedParameter.setParameterName(getName());
		// TODO fill values
		return qualifiedParameter;
	}

}
