package esa.egos.proxy.util;

import esa.egos.csts.api.exceptions.ApiException;

public interface ITimeSource {
	
	byte[] getCurrentTime() throws ApiException;
}
