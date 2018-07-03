package esa.egos.proxy.util;

import esa.egos.csts.api.util.ICredentials;

public interface IUtil {

	ISecAttributes createSecAttributes();

	ICredentials createCredentials();

	ITime createTime();
	
}
