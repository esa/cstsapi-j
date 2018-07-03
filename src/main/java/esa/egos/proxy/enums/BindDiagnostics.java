package esa.egos.proxy.enums;

public enum BindDiagnostics {

    BD_accessDenied(0, "access denied"),
    BD_serviceTypeNotSupported(1, "service type not supported"),
    BD_versionNotSupported(2, "version not supported"),
    BD_noSuchServiceInstance(3, "no such service instance"),
    BD_alreadyBound(4, "already bound"),
    BD_siNotAccessibleToThisInitiator(5, "service instance not accessible to this initiator"),
    BD_inconsistentServiceType(6, "inconsistent service type"),
    BD_invalidTime(7, "invalid time"),
    BD_outOfService(8, "out of service"),
    BD_otherReason(127, "other reason"),
    BD_invalid(-1, "invalid");

    private int code;

    private String msg;


    /**
     * Constructor SLE_BindDiagnostic.
     * 
     * @param code
     * @param msg
     */
    private BindDiagnostics(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets the code.
     * 
     * @return
     */
    public int getCode()
    {
        return this.code;
    }

    @Override
    public String toString()
    {
        return this.msg;
    }

    /**
     * Gets bind diagnostic by code.
     * 
     * @param code
     * @return null if there is no bind diagnostic with the given code.
     */
    public static BindDiagnostics getBindDiagnosticByCode(int code)
    {
        for (BindDiagnostics e : values())
        {
            if (e.code == code)
            {
                return e;
            }
        }

        return null;
    }
	
}
