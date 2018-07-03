package esa.egos.csts.api.parameters;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.Label;

@Deprecated
public interface IParameter {

	Label getLabel();
	
	ObjectIdentifier getIdentifier();
	
}
