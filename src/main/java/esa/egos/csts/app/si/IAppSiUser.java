package esa.egos.csts.app.si;

import esa.egos.csts.api.enumerations.CstsResult;

public interface IAppSiUser {

	public CstsResult bind();
	
	public CstsResult unbind();
}
