package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.enums.AppRole;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.ConfigException;
import esa.egos.csts.api.proxy.ServiceType;
import esa.egos.csts.api.util.ITime;

public interface IServiceConfiguration {
	
    /**
     * Verifies that the configuration is complete and consistent.
     * 
     * @throws ConfigException
     */
	void configure() throws ConfigException, ApiException;
	
    /**
     * Sets the service instance id.
     * 
     * @param id
     */
    void setServiceInstanceIdentifier(IServiceInstanceIdentifier id) throws ConfigException;

    /**
     * Sets peer identifier.
     * 
     * @param id
     */
    void setPeerIdentifier(String id) throws ConfigException;

    /**
     * Sets the provision period.
     * 
     * @param start
     * @param stop
     */
    void setProvisionPeriod(ITime start, ITime stop) throws ConfigException;

    /**
     * Sets responder port identifier.
     * 
     * @param portId
     */
    void setResponderPortIdentifier(String portId) throws ConfigException;

    /**
     * Set return timeout.
     * 
     * @param timeout
     */
    void setReturnTimeout(int timeout) throws ConfigException;

    /**
     * Gets the service type.
     * 
     * @return
     */
    ServiceType getServiceType();

    void setServiceType(ServiceType serviceType);
    
    /**
     * Gets the version.
     * 
     * @return
     */
    int getVersion();

    void setVersion(int version);
    
    /**
     * Gets the role.
     * 
     * @return
     */
    AppRole getRole();

    /**
     * Gets service instance identifier.
     * 
     * @return
     */
    IServiceInstanceIdentifier getServiceInstanceIdentifier();

    /**
     * Gets peer identifier.
     * 
     * @return
     */
    String getPeerIdentifier();

    /**
     * Gets the start provision time.
     * 
     * @return
     */
    ITime getProvisionPeriodStart();

    /**
     * Gets the stop provision period.
     * 
     * @return
     */
    ITime getProvisionPeriodStop();

    /**
     * Gets bind initiative.
     * 
     * @return
     */
    AppRole getBindInitiative() throws ApiException;

    /**
     * Gets responder port identifier.
     * 
     * @return
     */
    String getResponderPortIdentifier();

    /**
     * Gets return timeout.
     * 
     * @return
     */
    int getReturnTimeout();

	void setProvisionPeriodStart(ITime start) throws ConfigException;
	void setProvisionPeriodStop(ITime stop) throws ConfigException;
    
}
