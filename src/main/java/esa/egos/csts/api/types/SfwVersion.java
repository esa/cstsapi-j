package esa.egos.csts.api.types;

public enum SfwVersion {
	NOT_DEF(0),B1(1),B2 (2);

	private final int versionNumber;
	
	SfwVersion(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	public int toInt() {
		return versionNumber;
	}
	
	public static SfwVersion fromInt(int versionNumber) {
		switch(versionNumber) {
		case 1: return B1;
		case 2: return B2;
		default: return NOT_DEF;
		}
	}
}
