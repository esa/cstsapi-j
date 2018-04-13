package esa.egos.csts.api.util.impl;

import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.ISecAttributes;
import esa.egos.csts.api.util.ITime;
import esa.egos.csts.api.util.IUtil;

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
