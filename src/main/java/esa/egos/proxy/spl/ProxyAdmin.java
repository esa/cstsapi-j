/**
 * @(#) EE_APIPX_Proxy.java
 */

package esa.egos.proxy.spl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;
import esa.egos.proxy.ILocator;
import esa.egos.proxy.IProxy;
import esa.egos.proxy.enums.AssocState;
import esa.egos.proxy.enums.BindRole;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.impl.EE_APIPX_BinderPxy;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.util.ISecAttributes;
import esa.egos.proxy.util.impl.Reference;
import esa.egos.proxy.xml.LogicalPort;
import esa.egos.proxy.xml.ProviderConfig;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.ProxyRoleEnum;
import esa.egos.proxy.xml.ConfigServiceType;
import esa.egos.proxy.xml.UserConfig;

/**
 * The class EE_APIPX_Proxy implements the interfaces exported by the component
 * class 'API Proxy' and provides its functionality defined in reference
 * [SLE-API]. It is responsible for : - startup, configuration and closedown of
 * the proxy component. - creation, initialisation and release of initiating
 * association objects. - delegation of port registration/de-registration for
 * responding applications using IEE_Binder. - generation of log records. -
 * generation of trace records that are not related to a particular association.
 * After a successful configuration, which is done by a call to
 * ISLE_ProxyAdmin::Configure, the processing is started via the ISLE_Concurrent
 * interface. After processing is started, the Proxy is ready for
 * registration/de-registration, and for creation/deletion of association.
 */
public class ProxyAdmin implements IProxy
{
    private static final Logger LOG = Logger.getLogger(ProxyAdmin.class.getName());

    private IApi api;

    /**
     * Pointer to the IReporter interface.
     */
    private IReporter reporter;


    /**
     * Pointer to the ISLE_Locator interface.
     */
    private ILocator locator;

    /**
     * Pointer to the IEE_Binder interface.
     */
    private IBinder binder;

    /**
     * The protocol Id identifying the communication technology used by the
     * proxy component, i.e. "ISP1".
     */
    private String protocolId;

    /**
     * Indicates if the proxy has been started.
     */
    private boolean started;

    /**
     * Indicates if the configuration of the proxy has already been done.
     */
    private boolean configOk;

    public ConcurrentLinkedQueue<Association> concurrentAssociationsList;
    
    public ProxyConfig config;

    public ISecAttributes iSecAttr;

    private final ReentrantLock objMutex;

//    /**
//     * The only instance of this class
//     */
//    private static ProxyAdmin pProxyInstance = null;
//

//    /**
//     * This method is called once to create the EE_APIPX_Proxy instance
//     */
//    public static synchronized void initialiseInstance()
//    {
//        if (pProxyInstance == null)
//        {
//            pProxyInstance = new ProxyAdmin();
//        }
//    }

    /**
     * This method is called every time the EE_APIPX_Proxy instance is needed
     * 
     * @return
     */
//    public static ProxyAdmin getInstance()
//    {
//        if (pProxyInstance == null)
//        {
//            throw new IllegalStateException("The initialise method has never been called and the instance never created");
//        }
//
//        return pProxyInstance;
//    }

    public ProxyAdmin()
    {
        this.reporter = null;
        this.locator = null;
        this.binder = null;
        this.started = false;
        this.configOk = false;
        this.concurrentAssociationsList = new ConcurrentLinkedQueue<>();
        this.iSecAttr = null;
        this.objMutex = new ReentrantLock();
        this.protocolId = "ISP1";
        this.configOk = false;
        this.api = null;
    }

    /**
     * Registers the association in the association list.
     */
    public void registerAssoc(Association pAssoc)
    {
        // insert the association in the list
    	this.concurrentAssociationsList.add(pAssoc);
    }

    /**
     * De-registers the associations which have the release attribute set from
     * the association list.
     */
    public void deregisterAssoc()
    {
        // check all the associations
        // if one association is release, the proxy can release it
        this.objMutex.lock();
        
        Iterator<Association> qi = this.concurrentAssociationsList.iterator();
        while (qi.hasNext())
        {
            Association pAssoc = qi.next();
            if (pAssoc != null && pAssoc.getIsReleased())
            {
            	this.concurrentAssociationsList.remove(pAssoc);
            }
        }
    
        this.objMutex.unlock();
    }

    /**
     * See specification of ISLE_ProxyAdmin.
     */
    @Override
    public void configure(String configFilePath,
    						ILocator plocator,
				            IApi api,
				            IReporter preporter) throws ApiException
    {
        this.objMutex.lock();
        Result res = Result.E_FAIL;

        if (configFilePath == null || plocator == null || api == null
            || preporter == null)
        {
            this.objMutex.unlock();
            if (preporter != null)
            {
                // report an alarm
                String mess = "Configuration error: Invalid argument";
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("Invalid argument");
                }
                preporter.logRecord(null, null, Component.CP_proxy, null, CstsLogMessageType.ALARM, mess);
            }

            throw new ApiException(Result.E_INVALIDARG.toString());
        }

        this.reporter = preporter;
        this.locator = plocator;
        this.api = api;

        File configFile = new File(configFilePath);
        if (!configFile.exists() || configFile.isDirectory())
        {
            this.objMutex.unlock();
            if (this.reporter != null)
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("Cannot access the Proxy configuration file");
                }
                // report an alarm
                String mess = "Configuration error: Cannot access the Proxy configuration file";
                this.reporter.logRecord(null, null, Component.CP_proxy, null, CstsLogMessageType.ALARM, mess);
            }

            throw new ApiException(Result.SLE_E_NOFILE.toString());
        }

        this.objMutex.unlock();

        // instantiate and check the database
        // report any problem

        // throw away the old database
        if (this.config != null)
        {
            this.config = null;
        }
        
        
		if(this.api.getRole() == AppRole.USER){
			
			UserConfig userConfig;
			try {
				userConfig = UserConfig.load(new FileInputStream(new File(configFilePath)));
				this.config = new ProxyConfig(userConfig, this.api.getFrameworkConfig());
			} catch (FileNotFoundException e) {
				throw new ApiException("ApiException: File not found for configFilePath " + configFilePath);
			}
			
		}else {
			
			ProviderConfig providerConfig;
			try {
				providerConfig = ProviderConfig.load(new FileInputStream(new File(configFilePath)));
				this.config = new ProxyConfig(providerConfig,this.api.getFrameworkConfig());
			} catch (FileNotFoundException e) {
				throw new ApiException("ApiException: File not found for configFilePath " + configFilePath);
			}
			
		}
		
        if (this.config != null)
        {

            // check if the responder role is supported
            if (this.config.getRole() == ProxyRoleEnum.RESPONDER)
            {
                // create a binder proxy
                if (this.binder == null)
                {
                    EE_APIPX_BinderPxy pBinderPxy = new EE_APIPX_BinderPxy(this,
                                                                           this.reporter,
                                                                           this.config,
                                                                           this.api);
                    this.binder = (IBinder) pBinderPxy;
                    if (this.binder == null)
                    {
                        throw new ApiException(Result.E_FAIL.toString());
                    }
                }
            }

            // set the security attributes
            if (this.iSecAttr == null)
            {
                ISecAttributes pIsleSecAtt = this.api.createSecAttributes();
                if (pIsleSecAtt != null)
                {
                    this.iSecAttr = pIsleSecAtt;
                    // set the user name and password
                    String username = this.config.getLocalId();
                    byte[] password = this.config.getLocalPw();
                    this.iSecAttr.setUserName(username);
                    this.iSecAttr.setPassword(password);
                }
            }

            this.configOk = true;

        }else {

            if (this.reporter != null)
            {
                // report alarm
                String mess = "Configuration error: Cannot open the database";
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("Cannot open the database");
                }
                this.reporter
                        .logRecord(null, null, Component.CP_proxy, null, CstsLogMessageType.ALARM, mess);
            }

            res = Result.SLE_E_CONFIG;

            if (res != Result.S_OK)
            {
                this.config = null;
                throw new ApiException(res.toString());
            }
        }
    }

    /**
     * See specification of ISLE_ProxyAdmin.
     */
    @Override
    public void shutDown() throws ApiException
    {
        deregisterAssoc();
        this.objMutex.lock();

        // check if all the associations are terminated
        for (Association passoc : this.concurrentAssociationsList)        	
        {
            if (passoc.getAssocState() != AssocState.sleAST_unbound)
            {
                this.objMutex.unlock();
                throw new ApiException(Result.SLE_E_STATE.toString());
            }
        }

        this.reporter = null;

        this.locator = null;
        
        this.api = null;

        this.binder = null;

        this.configOk = false;

        this.objMutex.unlock();
    }

    /**
     * See specification of ISLE_ProxyAdmin.
     */
    @Override
    public int registerPort(IServiceInstanceIdentifier sii, String responderPort) throws ApiException
    {
        this.objMutex.lock();

        // check if the responder role is supported
        if (this.config.getRole() == ProxyRoleEnum.INITIATOR)
        {
            this.objMutex.unlock();
            throw new ApiException(Result.E_NOTIMPL.toString());
        }
        // check if the responder port is part of the address mapping
        // information
        ArrayList<LogicalPort> pResponderPortList = this.config.getLogicalPortList();
        LogicalPort pResponderPort = null;
        
        for(LogicalPort lPort : pResponderPortList){
        	if(lPort.getName().equals(responderPort))
        		pResponderPort = lPort;
        }
        if (pResponderPort == null)
        {
            this.objMutex.unlock();
            throw new ApiException(Result.SLE_E_UNKNOWN.toString());
        }
        // check if the responder port identifier is a local port
        if (pResponderPort.getIsForeign())
        {
            this.objMutex.unlock();
            throw new ApiException(Result.SLE_E_INVALIDID.toString());
        }

        Reference<Integer> regId = new Reference<Integer>();

        if (this.binder != null)
        {
        	IBinder tmpBinder = this.binder;
        	this.objMutex.unlock();
            Result res = tmpBinder.registerPort(sii, responderPort, regId); // CSTSAPI-61
            this.objMutex.lock();
            if (res != Result.S_OK)
            {
                this.objMutex.unlock();
                throw new ApiException(res.toString());
            }
        }

        this.objMutex.unlock();

        if (regId.getReference() == null)
        {
            return -1;
        }
        else
        {
            return regId.getReference();
        }
    }

    /**
     * See specification of ISLE_ProxyAdmin.
     */
    @Override
    public void deregisterPort(int regId) throws ApiException
    {
        this.objMutex.lock();

        // check if the responder role is supported
        if (this.config.getRole() == ProxyRoleEnum.INITIATOR)
        {
            this.objMutex.unlock();
            throw new ApiException(Result.E_NOTIMPL.toString());
        }

        if (regId < 0)
        {
            this.objMutex.unlock();
            throw new ApiException(Result.E_INVALIDARG.toString());
        }

        if (this.binder != null)
        {
        	IBinder tmpBinder = this.binder;
        	this.objMutex.unlock();
        	
        	// don't leave the object under lock. Otherwise the object cen't be used from outside while the message is sent
            Result res = tmpBinder.deregisterPort(regId); // CSTSAPI-61 Interlock of received peer abort and initiating abort
            if (res != Result.S_OK)
            {                
                throw new ApiException(res.name());
            }
        }
        else
        {
        	this.objMutex.unlock();
        }
    }

    /**
     * See specification of ISLE_ProxyAdmin.
     */
    @Override
    public String getProtocolId()
    {
        return this.protocolId;
    }

    /**
     * See specification of ISLE_AssocFactory.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Object> T createAssociation(Class<T> iid,
                                                    ServiceType srvType,
                                                    IServiceInstance pclientIf) throws ApiException
    {
        this.objMutex.lock();

        if (!this.started)
        {
            this.objMutex.unlock();
            throw new ApiException(Result.SLE_E_STATE.toString());
        }

        // check if the service type is a supported service type in the database
        ProxyConfig pProxyConfig = this.config;
        AppRole sleAppRole;
        if(pProxyConfig.getRole() == ProxyRoleEnum.INITIATOR)
        	sleAppRole = AppRole.USER;
        else
        	sleAppRole = AppRole.PROVIDER;
        
        if (sleAppRole == AppRole.PROVIDER)
        {
        	ArrayList<ConfigServiceType> pSrvTypeList = this.config.getServiceTypeList();
        	ConfigServiceType pSrvType = null;
        	for(ConfigServiceType serverType: pSrvTypeList){
        		if(serverType.getServiceId().equals(Arrays.toString(srvType.getOid().toArray())))
        			pSrvType = serverType;
        	}
            if (pSrvType == null)
            {
                this.objMutex.unlock();
                throw new ApiException(Result.E_NOTIMPL.toString());
            }
        }

        Association pAssoc = new InitiatingAssoc(  	this.reporter,
        											pclientIf.getApi(),
        											this);
        // try to get the interface
        if (!iid.isAssignableFrom(pAssoc.getClass()))
        {
            this.objMutex.unlock();
            throw new ApiException(Result.E_NOINTERFACE.toString());
        }

        // insert the association in the list
        registerAssoc(pAssoc);
        // set the inform interface
        pAssoc.setSrvProxyInform(pclientIf);

        this.objMutex.unlock();

        return (T) pAssoc;
    }

    /**
     * See specification of ISLE_AssocFactory.
     */
    @Override
    public void destroyAssociation(Object passoc) throws ApiException
    {
    	Object pUnknown1 = null;
    	Object pUnknown2 = null;
        Result res = Result.SLE_E_UNKNOWN;

        this.objMutex.lock();

        // take the iunknown interface from the given parameter
        pUnknown1 = passoc;
        if (pUnknown1 != null)
        {
            for (Association passocList : this.concurrentAssociationsList) 
            {
                // take the iunknown interface from the assoc of the list
                pUnknown2 = passocList;
                if (pUnknown2 != null)
                {
                    if (pUnknown1.equals(pUnknown2))
                    {
                        // the association has been founded in the list
                        // check if it is a initiating association
                        if (passocList.getRole() != BindRole.BR_initiator)
                        {
                            res = Result.SLE_E_TYPE;
                        }
                        else
                        {
                            // check if the state is bound
                            if (passocList.getAssocState() != AssocState.sleAST_unbound)
                            {
                                res = Result.SLE_E_STATE;
                            }
                            else
                            {
                                passocList.releaseChannel();
                                // erase the association from the list
                                this.concurrentAssociationsList.remove(passoc);
                                res = Result.S_OK;
                            }
                        }

                        break;
                    }
                }
            }
        }

        this.objMutex.unlock();

        if (res != Result.S_OK)
        {
            throw new ApiException(res.toString());
        }
    }

    /**
     * Gets the locator.
     */
    public ILocator getLocator()
    {
        return this.locator;
    }

    /**
     * Gets the ConfigOk attribute of the object.
     */
    public boolean getConfigOk()
    {
        return this.configOk;
    }

    public ISecAttributes getSecurityAttribures()
    {
        return this.iSecAttr;
    }

    public IReporter getReporter()
    {
        return this.reporter;
    }

    public void setReporter(IReporter reporter)
    {
        this.reporter = reporter;
    }

    public IBinder getBinder()
    {
        return this.binder;
    }

    public void setBinder(IBinder binder)
    {
        this.binder = binder;
    }

    public boolean isStarted()
    {
        return this.started;
    }

    public void setStarted(boolean started)
    {
        this.started = started;
    }

    public void setLocator(ILocator locator)
    {
        this.locator = locator;
    }

    public void setProtocolId(String protocolId)
    {
        this.protocolId = protocolId;
    }

    public void setConfigOk(boolean configOk)
    {
        this.configOk = configOk;
    }

	@Override
	public void startConcurrent() throws ApiException {
        this.objMutex.lock();

        if (this.started)
        {
            this.objMutex.unlock();
            throw new ApiException("Proxy " + this.getProtocolId() + " already started");
        }

        if (!this.configOk)
        {
            this.objMutex.unlock();
            throw new ApiException("Proxy " + this.getProtocolId() + " not configured");
        }

        this.started = true;

        this.objMutex.unlock();
	}

	@Override
	public void terminateConcurrent() throws ApiException {
        this.objMutex.lock();

        if (!this.started)
        {
            this.objMutex.unlock();
            throw new ApiException("Proxy " + this.getProtocolId() + " not started, can not stop it.");
        }

        this.started = false;

        this.objMutex.unlock();
	}
}
