package esa.egos.csts.api.util;

import ccsds.csts.common.types.Credentials;

public interface ICredentials {

    /**
     * Gets random number.
     * 
     * @return
     */
    long getRandomNumber();

    /**
     * Gets protected.
     * 
     * @return
     */
    byte[] getProtected();

    /**
     * Gets the time ref.
     * 
     * @return
     */
    ITime getTimeRef();

    /**
     * Sets random number.
     * 
     * @param number
     */
    void setRandomNumber(long number);

    /**
     * Sets protected.
     * 
     * @param hashCode
     */
    void setProtected(byte[] hashCode);

    /**
     * Sets time ref.
     * 
     * @param time
     */
    void setTimeRef(ITime time);

    /**
     * Copies the instance object.
     * 
     * @return
     */
    ICredentials copy();

    /**
     * Dumps
     * 
     * @return
     */
    String dump();

    /**
     * @param o
     * @return
     */
    @Override
    boolean equals(Object o);

    /**
     * @return
     */
    @Override
    int hashCode();

	Credentials asCredentials();
	
}
