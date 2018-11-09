package esa.egos.csts.api.parameters;

import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;

/**
 * The Configuration Parameter Interface.
 *
 * A Configuration Parameter is a Parameter which belongs to a Procedure and
 * specifies whether it is dynamically modifiable and whether it is readable.
 */
public interface IConfigurationParameter extends IParameter {

	/**
	 * Returns the Procedure Type.
	 * 
	 * @return the Procedure Type.
	 */
	ProcedureType getProcedureType();

	/**
	 * Indicates whether this is dynamically modifiable.
	 * 
	 * @return true if this is dynamically modifiable, false otherwise
	 */
	boolean isDynamicallyModifiable();

	/**
	 * Indicates whether this is readable.
	 * 
	 * @return true if this is readable, false otherwise
	 */
	boolean isReadable();

	/**
	 * Indicates whether this Configuration Parameter has been configured.
	 * 
	 * @return true is this is configured, false otherwise
	 */
	boolean isConfigured();

	/**
	 * Sets whether a Configuration Parameter is configured.
	 * 
	 * @param configured
	 *            the flag to indicate if the Configuration Parameter has been
	 *            configured
	 */
	void setConfigured(boolean configured);

	/**
	 * Returns the corresponding Procedure which is configured by this Configuration
	 * Parameter.
	 * 
	 * @return the corresponding Procedure
	 */
	IProcedure getProcedure();

}
