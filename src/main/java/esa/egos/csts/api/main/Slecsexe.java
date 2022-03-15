package esa.egos.csts.api.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.proxy.impl.MasterLink;
import esa.egos.proxy.xml.FrameworkConfig;
import esa.egos.proxy.xml.ProviderConfig;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.ProxyRoleEnum;

public class Slecsexe {
	private static boolean verbose = false;

	private static final Logger LOG = Logger.getLogger(Slecsexe.class.getName());

	public static Result comServer(String configFilePath, AppRole role, boolean wait) throws ApiException {
		Result res = Result.S_FALSE;

		ProxyConfig proxyConfig = null;
		ProviderConfig providerConfig = null;

		InputStream configFileStream;
		try {
			configFileStream = new FileInputStream(new File(configFilePath));
		} catch (FileNotFoundException e) {
			throw new ApiException("File not found: " + configFilePath);
		}

		providerConfig = ProviderConfig.load(configFileStream);
		if (providerConfig != null && providerConfig.getRole() == ProxyRoleEnum.RESPONDER) {
			FrameworkConfig frameworkConfig = new FrameworkConfig();
			providerConfig.getServiceTypeList()
				.forEach(configService -> configService.getServiceVersion()
					.forEach(serviceVersion -> 	frameworkConfig.addServiceVersion(
							SfwVersion.fromInt(serviceVersion.sfwVersion), 
							ObjectIdentifier.of(configService.getServiceId(),","),
							serviceVersion.value)));
			proxyConfig = new ProxyConfig(providerConfig,frameworkConfig);
		} else {
			throw new ApiException("The role " + providerConfig.getRole().name() +
					" specified in the configuration file does not match the role " + ProxyRoleEnum.RESPONDER.name()
					+ " used to construct the csts api instance.");
		}

		res = MasterLink.initialise(null, null, proxyConfig, verbose);
		if (res != Result.S_OK) {
			System.err.println("ESA SLE Communication Server : Initialise failed. return " + res.toString());
			return Result.E_FAIL;
		}

		if(wait == true) {		
			// Register to the ShutdownHook for SIGTERM
			Runtime rST = Runtime.getRuntime();
			rST.addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (verbose) {
						System.out.println("ESA SLE Communication Server : SIGTERM received");
						System.out.println("Shutdown of the ESA SLE Communication Server Process");
					}
	
					MasterLink.shutdown();
				}
			});
	
			// infinite wait
			try {
				new Semaphore(0).acquire();
			} catch (InterruptedException e) {
				LOG.log(Level.FINE, "InterruptedException ", e);
			}
		}

		return res;
	}

	public static void main(String[] args) {

		Logger rootLogger = LogManager.getLogManager().getLogger("");
		rootLogger.setLevel(Level.ALL);
		for (Handler h : rootLogger.getHandlers()) {
			h.setLevel(Level.ALL);
		}

		AppRole role = null;
		String configFilePath = "";
		verbose = false;
		boolean argcheck = true;

		for (int count = 0; count < args.length; count++) {
			if (args[count].equals("-d")) {
				configFilePath = args[++count];
			} else if (args[count].equals("-v")) {
				verbose = true;
			} else {
				argcheck = false;
			}
		}

		if (configFilePath.isEmpty() || !argcheck) {
			System.err.println("ESA SLE Communication Server usage : slecs [-v] -d <proxy database file name>");
			return;
		}

		try {
			comServer(configFilePath, role, true);
		} catch (ApiException e) {
			e.printStackTrace();
		}

		return;
	}
}
