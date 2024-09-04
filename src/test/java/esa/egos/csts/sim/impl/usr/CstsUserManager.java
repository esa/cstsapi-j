package esa.egos.csts.sim.impl.usr;

import java.util.Arrays;
import java.util.List;

import esa.egos.csts.sicf.model.MDSicf;
import esa.egos.csts.sicf.model.ServiceInstances.ServiceInstance;
import esa.egos.csts.sim.impl.MdCstsSiConfig;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.sicf.SicfParameter;
import esa.egos.csts.sicf.SicfReader;
import esa.egos.csts.sicf.SicfUtil;

public class CstsUserManager {
	private static final ProcedureInstanceIdentifier PIID = ProcedureInstanceIdentifier
			.of(ProcedureType.of(OIDs.ocoCyclicReport), ProcedureRole.PRIME, 0);
	private static final List<ProcedureInstanceIdentifier> PIIDs = Arrays.asList(PIID);

	private final String apiName;
	private final String userConfigPath;
	private ICstsApi cstsApi;

	public CstsUserManager(String apiName, String userConfig) {
		this.apiName = apiName;
		this.userConfigPath = userConfig;
	}

	public void init() {
		this.cstsApi = new CstsUserApi(apiName);
		try {
			cstsApi.initialize(userConfigPath);
			cstsApi.start();
		} catch (ApiException e) {
			throw new RuntimeException("Couldn't initialize and start CSTS User API: " + e.getMessage());
		}
	}

	public MdCstsSiUser createSIUser(ServiceInstance serviceInstance, String sicfPath) {
		MDSicf sicf = SicfReader.createSicf(sicfPath);
		MdCstsSiConfig siConfig = createSiUserConfig(serviceInstance);
		List<SicfParameter> sicfParams = SicfUtil.getDefaultSicfParameters(sicf, serviceInstance);
		MdCstsSiUser siUser = null;
		try {
			siUser = new MdCstsSiUser(cstsApi, siConfig, 1, sicfParams, serviceInstance.getSIID());
			siUser.setAgencySpecyficParameters(true);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return siUser;
	}

	private MdCstsSiConfig createSiUserConfig(ServiceInstance serviceInstance) {
		ObjectIdentifier scId = ObjectIdentifier.of(serviceInstance.getSpacecraftOID());
		ObjectIdentifier facilityId = ObjectIdentifier.of(serviceInstance.getStationOID());
		int serviceInstanceNumber = Integer.valueOf(serviceInstance.getInstance());
		String peerIdentifier = serviceInstance.getResponderIdentifier();
		String portId = serviceInstance.getResponderPortId();
		return new MdCstsSiConfig(scId, facilityId, serviceInstanceNumber, peerIdentifier, portId, PIIDs);
	}
}
