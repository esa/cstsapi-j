package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.exception.ApiException;

/**
 * The interface is used to control processing of a component providing the
 * behavior "Concurrent Flows of Control" as defined in chapter 4. Processing of
 * the component is started with the method StartConcurent The function checks
 * the configuration and returns as soon as processing within the component has
 * been started.
 * 
 * @version: 1.0, October 2015
 */
public interface IConcurrent
{
    /**
     * Starts the processing of the component
     * 
     * @throws SleApiException
     */
    void startConcurrent() throws ApiException;

    /**
     * Terminates the processing of the component
     * 
     * @throws SleApiException
     */
    void terminateConcurrent() throws ApiException;
}
