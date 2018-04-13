/**
 * @(#) ESLE_DefaultLogger.java
 */

package esa.egos.csts.api.proxy;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.logging.IReporter;

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
