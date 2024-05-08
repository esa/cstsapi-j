package esa.egos.csts.api.parameters;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;

/**
 * This class represents a Configuration Parameter.
 * 
 * This class is a realization of the {@link IConfigurationParameter} Interface.
 */
public abstract class AbstractConfigurationParameter extends AbstractParameter implements IConfigurationParameter {

	private final IProcedure procedure;
	private final boolean readable;
	private final boolean dynamicallyModifiable;
	private boolean configured;

	/**
	 * Instantiates a new Configuration Parameter.
	 * 
	 * @param identifier            the Object Identifier
	 * @param readable              whether if this instance is readable
	 * @param dynamicallyModifiable whether if this instance is dynamically
	 *                              modifiable
	 * @param procedure             the Procedure being configured
	 */
	public AbstractConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, procedure.getProcedureInstanceIdentifier());
		this.procedure = procedure;
		this.readable = readable;
		this.dynamicallyModifiable = dynamicallyModifiable;
		configured = false;
	}

	@Override
	public IProcedure getProcedure() {
		return procedure;
	}

	@Override
	public boolean isReadable() {
		return readable;
	}

	/**
	 * Indicates whether the procedure being configured by this parameter is bound.
	 * 
	 * @return true if the procedure being configured by this parameter is bound,
	 *         false otherwise
	 */
	protected boolean procedureIsBound() {
		return procedure.getServiceInstance().isBound();
	}

	@Override
	public boolean isDynamicallyModifiable() {
		return dynamicallyModifiable;
	}

	@Override
	public boolean isConfigured() {
		return configured;
	}

	@Override
	public synchronized void setConfigured(boolean configured) {
		this.configured = configured;
	}

	@Override
	public ProcedureType getProcedureType() {
		return procedure.getType();
	}
	
	@Override
	public String toString() {
	    return "AbstractConfigurationParameter \n\t[" + super.toString()
	        + "\n\treadable=" + this.readable
	        + ", dynamicallyModifiable=" + this.dynamicallyModifiable
	        + ", configured=" + this.configured
	        + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AbstractConfigurationParameter)) {
			return false;
		}
	    if (!super.equals(o)) {
	        return false;
	    }
		AbstractConfigurationParameter acp = (AbstractConfigurationParameter)o;
		if (this.procedure != acp.getProcedure()) {
		    return false;
		}
		if (this.readable != acp.isReadable()) {
		    return false;
		}
		if (this.dynamicallyModifiable != acp.isDynamicallyModifiable()) {
		    return false;
		}
		if (this.configured != acp.isConfigured()) {
		    return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
	    int hash = super.hashCode();
	    hash = 31*hash + (this.readable ? 1 : 0);
	    hash = 31*hash + (this.dynamicallyModifiable ? 1 : 0);
	    hash = 31*hash + (this.configured ? 1 : 0);
	    return hash;
	}

}
