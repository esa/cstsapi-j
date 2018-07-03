package esa.egos.proxy.util;

import esa.egos.csts.api.util.ICredentials;

public interface ISecAttributes {

    /**
     * Sets the user name.
     * 
     * @param name
     */
    void setUserName(String name);

    /**
     * Sets the password.
     * 
     * @param pwd
     */
    void setPassword(byte[] pwd);

    /**
     * Sets Hex password.
     * 
     * @param pwd
     */
    void setHexPassword(String pwd);

    /**
     * Generate credentials.
     * 
     * @return
     */
    ICredentials generateCredentials();

    /**
     * Authenticate.
     * 
     * @param credentials
     * @param acceptableDelay
     * @return
     */
    boolean authenticate(ICredentials credentials, int acceptableDelay);

    /**
     * Copies the object.
     * 
     * @return
     */
    ISecAttributes copy();

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
	
}
