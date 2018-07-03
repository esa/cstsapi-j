package esa.egos.proxy.logging;

public enum CstsLogMessageType {

	ALARM(0),
	INFO(1),
	invalid(-1);

    private int code;
	
    /**
     * Constructor SLE_LogMessageType.
     * 
     * @param code
     * @param msg
     */
    private CstsLogMessageType(int code)
    {
        this.code = code;

    }
	
	public static CstsLogMessageType getLogMessageByCode(int readInt) {
        for (CstsLogMessageType e : values())
        {
            if (e.code == readInt)
            {
                return e;
            }
        }

        return null;
	}

	public int getCode() {
		return this.code;
	}
}
