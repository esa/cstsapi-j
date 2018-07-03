package esa.egos.proxy.util.impl;

import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.impl.Credentials;
import esa.egos.proxy.util.ISecAttributes;
import esa.egos.proxy.util.ITime;
import esa.egos.proxy.util.IUtil;

public class Util implements IUtil {

	@Override
	public ISecAttributes createSecAttributes() {
		SecAttributes a = new SecAttributes((IUtil)this);
		return a;
	}

	@Override
	public ICredentials createCredentials() {
		Credentials c = new Credentials();
		return c;
	}

	@Override
	public ITime createTime() {
		ApiTime t = new ApiTime();
		return t;
	}

}
