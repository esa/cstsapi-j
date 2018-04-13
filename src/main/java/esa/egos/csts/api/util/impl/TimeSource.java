package esa.egos.csts.api.util.impl;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.time.CstsTime;
import esa.egos.csts.api.time.CstsTimePrec;
import esa.egos.csts.api.util.ITimeSource;

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
