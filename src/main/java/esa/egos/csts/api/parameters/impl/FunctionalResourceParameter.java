package esa.egos.csts.api.parameters.impl;

import java.time.LocalDateTime;

import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.parameters.IFunctionalResourceParameter;
import esa.egos.csts.api.types.IName;

public class FunctionalResourceParameter implements IFunctionalResourceParameter {

	private String classifier;
	private String authorizingEntitiy;
	private LocalDateTime creationDate;
	private boolean deprecated;
	private boolean configured;
	private ObjectIdentifier identifier;
	private String semanticDefinition;
	private double value;
	private String unit;

	@Override
	public String getClassifier() {
		return classifier;
	}

	@Override
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	@Override
	public String getAuthorizingEntitiy() {
		return authorizingEntitiy;
	}

	@Override
	public void setAuthorizingEntitiy(String authorizingEntitiy) {
		this.authorizingEntitiy = authorizingEntitiy;
	}

	@Override
	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	@Override
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public boolean isDeprecated() {
		return deprecated;
	}

	@Override
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	@Override
	public boolean isConfigured() {
		return configured;
	}

	@Override
	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	@Override
	public ObjectIdentifier getIdentifier() {
		return identifier;
	}

	@Override
	public void setIdentifier(ObjectIdentifier identifier) {
		this.identifier = identifier;
	}

	@Override
	public String getSemanticDefinition() {
		return semanticDefinition;
	}

	@Override
	public void setSemanticDefinition(String semanticDefinition) {
		this.semanticDefinition = semanticDefinition;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String getUnit() {
		return unit;
	}

	@Override
	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public QualifiedParameter toQualifiedParameter(IName name) {
		QualifiedParameter qualifiedParameter = new QualifiedParameter();
		qualifiedParameter.setParameterName(name);
		// TODO fill values
		return qualifiedParameter;
	}

}
