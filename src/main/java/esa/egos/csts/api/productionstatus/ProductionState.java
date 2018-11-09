package esa.egos.csts.api.productionstatus;

import java.util.Arrays;

import esa.egos.csts.api.exceptions.CodeNotFoundException;

public enum ProductionState {
	
	CONFIGURED(0),
	OPERATIONAL(1),
	INTERRUPTED(2),
	HALTED(3);
	
	private long code;
	
	private ProductionState(long code) {
		this.code = code;
	}
	
	public long getCode() {
		return code;
	}
	
	public static ProductionState getProductionStateByCode(long code) {
		return Arrays.stream(values())
				.filter(d -> d.code == code)
				.findFirst()
				.orElseThrow(CodeNotFoundException::new);
	}
	
}