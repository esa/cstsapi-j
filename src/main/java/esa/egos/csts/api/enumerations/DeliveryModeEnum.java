package esa.egos.csts.api.enumerations;

public enum DeliveryModeEnum {

	UNDEFINED(0),
	REAL_TIME(1),
	COMPLETE(2);
	
	private int code;
	
	private DeliveryModeEnum(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
}
