package esa.egos.csts.api.enums;

public enum DataTransferModeEnum {

	UNDEFINED(0),
	TIMELY(1),
	COMPLETE(2);
	
	private int code;
	
	private DataTransferModeEnum(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
}
