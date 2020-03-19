package esa.egos.csts.rtn.cfdp.procedures;

import java.util.Arrays;
import esa.egos.csts.api.exceptions.CodeNotFoundException;

public enum CfdpOperationMode {
	FULL(0),
	REDUCED(1);
	
	private long code;
	
	private CfdpOperationMode(long code) {
		this.code = code;
	}
	
	public long getCode() {
		return code;
	}
	
	public static CfdpOperationMode getCfdpOperationModByCode(long code) {
		return Arrays.stream(values())
				.filter(d -> d.code == code)
				.findFirst()
				.orElseThrow(CodeNotFoundException::new);
	}
}
