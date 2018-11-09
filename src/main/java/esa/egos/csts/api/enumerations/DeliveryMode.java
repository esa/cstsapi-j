package esa.egos.csts.api.enumerations;

import java.util.Arrays;

import esa.egos.csts.api.exceptions.CodeNotFoundException;

public enum DeliveryMode {

	UNDEFINED(0),
	REAL_TIME(1),
	COMPLETE(2);
	
	private long code;
	
	private DeliveryMode(long code) {
		this.code = code;
	}
	
	public long getCode() {
		return code;
	}
	
	public static DeliveryMode getDeliveryModeByCode(long code) {
		return Arrays.stream(values())
				.filter(d -> d.code == code)
				.findFirst()
				.orElseThrow(CodeNotFoundException::new);
	}
	
}
