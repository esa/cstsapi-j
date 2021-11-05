package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.oids.ObjectIdentifier;

/**
 * This interface represents a Service Instance Identifier.
 */
public interface IServiceInstanceIdentifier {

	/**
	 * Returns the Spacecraft Identifier.
	 * 
	 * @return the Spacecraft Identifier
	 */
	ObjectIdentifier getSpacecraftIdentifier();

	/**
	 * Returns the Facility Identifier.
	 * 
	 * @return the Facility Identifier
	 */
	ObjectIdentifier getFacilityIdentifier();

	/**
	 * Returns the CSTS Type Identifier.
	 * 
	 * @return the CSTS Type Identifier
	 */
	ObjectIdentifier getCstsTypeIdentifier();

	/**
	 * Returns the Service Instance Number.
	 * 
	 * @return the Service Instance Number
	 */
	int getServiceInstanceNumber();

	b1.ccsds.csts.service.instance.id.ServiceInstanceIdentifier encode(
			b1.ccsds.csts.service.instance.id.ServiceInstanceIdentifier sid);
	
	b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier encode(
			b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier sid);

	/**
	 * Checks whether the initial format is used.
	 * 
	 * @deprecated This method should not be needed outside the context of the
	 *             underlying communication service. To be replaced in upcoming
	 *             versions.
	 * @return true if the initial format is used, false otherwise
	 */
	@Deprecated
	boolean getInitialFormatUsed();

	/**
	 * Returns a string representation of the object.
	 * 
	 * @deprecated This method should not be needed outside the context of the
	 *             underlying communication service. To be removed in upcoming
	 *             versions. Use {@link Object#toString()} instead.
	 * @return a string representation of the object
	 */
	@Deprecated
	String toAscii();

}
