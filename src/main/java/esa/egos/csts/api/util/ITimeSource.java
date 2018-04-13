package esa.egos.csts.api.util;

import esa.egos.csts.api.exception.ApiException;

public interface ITimeSource {
	
	byte[] getCurrentTime() throws ApiException;
}
