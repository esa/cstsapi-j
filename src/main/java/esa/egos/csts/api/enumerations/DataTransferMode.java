package esa.egos.csts.api.enumerations;

import java.util.Arrays;

import esa.egos.csts.api.exceptions.CodeNotFoundException;

public enum DataTransferMode {

	UNDEFINED(0),
	TIMELY(1),
	COMPLETE(2);
	
	private long code;
	
	private DataTransferMode(long code) {
		this.code = code;
	}
	
	public long getCode() {
		return code;
	}
	
	public static DataTransferMode getDataTransferModeByCode(long code) {
		return Arrays.stream(values())
				.filter(d -> d.code == code)
				.findFirst()
				.orElseThrow(CodeNotFoundException::new);
	}
	
}
