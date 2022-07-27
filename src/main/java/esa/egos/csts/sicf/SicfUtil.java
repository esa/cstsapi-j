package esa.egos.csts.sicf;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import esa.egos.csts.sicf.model.MDSicf;
import esa.egos.csts.sicf.model.SIParameterListType.ListName;
import esa.egos.csts.sicf.model.SIParameterListType.ListName.Parameter;
import esa.egos.csts.sicf.model.ServiceInstances.ServiceInstance;

public class SicfUtil {
	public static ServiceInstance getServiceInstanceBySiid(MDSicf sicf,
			String siid) {
		return sicf.getServiceInstances().getServiceInstance().stream()
				.filter(si -> si.getSIID().equals(siid)).findFirst()
				.orElse(null);
	}

	public static List<SicfParameter> getDefaultSicfParameters(MDSicf sicf,
			ServiceInstance serviceInstance) {
		ArrayList<SicfParameter> sicfParameters = new ArrayList<SicfParameter>();
		ListName defaultListName = getDefaultListName(serviceInstance);
		for (Parameter listNameParam : defaultListName.getParameter()) {
			SicfParameter sicfParam = createSicfParameter(sicf, listNameParam);
			sicfParameters.add(sicfParam);
		}
		return sicfParameters;
	}

	private static SicfParameter createSicfParameter(MDSicf sicf,
			Parameter listNameParam) {
		String stcParamName = createSTCParamName(listNameParam);
		for (esa.egos.csts.sicf.model.ParameterDefinitions.Parameter paramDef : sicf
				.getParameterDefinitions().getParameter()) {
			String stcNameParamDef = createSTCParamName(paramDef);
			if (stcParamName.equals(stcNameParamDef))
				return createSicfParameter(paramDef);
		}
		throw new RuntimeException(
				"Listname parameter not found in parameter definitions.");
	}

	public static String createSTCParamName(Parameter param) {
		String funcResType = param.getFunctionalResourceType();
		String paramId = param.getParameterOrEventId();
		String instanceNumber = String.valueOf(param
				.getFunctionalResourceInstance());
		if (instanceNumber.equals("0"))
			instanceNumber = "";
		String unprefixedFuncResType = StringUtils.removeStart(funcResType, "/");
		String subsystem = StringUtils.substringBefore(unprefixedFuncResType, "/");
		String subsystemUnit = StringUtils.substringAfter(unprefixedFuncResType,subsystem);
		return createSTCParamName(subsystem,instanceNumber,subsystemUnit,paramId);
	}

	private static SicfParameter createSicfParameter(
			esa.egos.csts.sicf.model.ParameterDefinitions.Parameter paramDef) {
		String mcsName = paramDef.getName();
		String oidString = paramDef.getOID();
		int funcResInstanceNumber = paramDef.getFunctionalResourceInstance();
		String description = paramDef.getDescription();
		String type = paramDef.getParameterType();
		SicfParameter sicfParam = new SicfParameter(mcsName, oidString,
				funcResInstanceNumber, description, type);
		if (paramDef.getEnumReference() != null)
			sicfParam.setEnumReference(paramDef.getEnumReference());
		return sicfParam;
	}

	public static ListName getDefaultListName(ServiceInstance serviceInstance) {
		List<ListName> listNames = serviceInstance.getSIParameterList()
				.getListName();
		for (ListName listName : listNames)
			if (listName.getName() == null)
				return listName;
		return null;
	}

	public static String createSTCParamName(
			esa.egos.csts.sicf.model.ParameterDefinitions.Parameter param) {
		String funcResType = param.getFunctionalResourceType();
		String paramId = param.getParamOrEventId();
		String instanceNumber = String.valueOf(param
				.getFunctionalResourceInstance());
		if (instanceNumber.equals("0"))
			instanceNumber = "";
		String unprefixedFuncResType = StringUtils.removeStart(funcResType, "/");
		String subsystem = StringUtils.substringBefore(unprefixedFuncResType, "/");
		String subsystemUnit = StringUtils.substringAfter(unprefixedFuncResType,subsystem);
		return createSTCParamName(subsystem,instanceNumber,subsystemUnit,paramId);
	}
	
	private static String createSTCParamName(String subsystem, String instanceNumber, String subsystemUnit, String paramID ) {
		StringBuilder build = new StringBuilder();
		if(subsystem!=null && !subsystem.isEmpty()) {
			build.append("/");
			build.append(subsystem);
		}
		if(instanceNumber!=null && !instanceNumber.isEmpty()) {
			build.append(instanceNumber);
		}
		if(subsystemUnit!=null && !subsystemUnit.isEmpty()) {
			build.append("/");
			build.append(subsystemUnit);
		}
		if(paramID!=null && !paramID.isEmpty()) {
			build.append("/");
			build.append(paramID);
		}	
		return StringUtils.replace(build.toString(), "//", "/");
	}
}
