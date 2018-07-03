package esa.egos.proxy.util.impl;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.time.CstsTime;
import esa.egos.proxy.time.CstsTimePrec;
import esa.egos.proxy.util.ITimeSource;

public class TimeSource implements ITimeSource{

	@Override
	public byte[] getCurrentTime() throws ApiException {
		
        int ci_encodeBLen = 8;
        // Get the current time from EE_Time
        byte[] time_cds = new byte[ci_encodeBLen];
        CstsTime time = new CstsTime(CstsTimePrec.eeTIME_PrecMILLISEC);
        time.getCDSlevel1(time_cds);
        return time_cds;
	}

}
