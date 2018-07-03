/**
 * @(#) ESLE_DefaultLogger.java
 */

package esa.egos.proxy;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.logging.IReporter;

/**
 * The interface is provided to the client for reception of report and tracing
 * messages that cannot be uniquely assigned to a service instance. The
 * interface inherits from ISLE_TraceControl and takes a ISLE_Repoter interface
 * to which all messages are written.
 */
public interface IDefaultLogger
{

    void setReporter(IReporter preporter);

    void connect(String ipcAddress) throws ApiException;

    void disconnect() throws ApiException;

}
