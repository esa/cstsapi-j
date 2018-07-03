package esa.egos.proxy.enums;

public enum AlarmLevel
{

    sleAL_accessViolation(0, "access violation"),
    sleAL_authFailure(1, "authentication failure"),
    sleAL_commsFailure(2, "communications failure"),
    sleAL_localAbort(3, "local abort"),
    sleAL_remoteAbort(4, "remote abort"),
    sleAL_invalid(-1, "invalid");

    private int code;

    private String msg;


    /**
     * Constructor SLE_Alarm
     * 
     * @param code
     * @param msg
     */
    private AlarmLevel(int code, String msg)
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

    public static AlarmLevel getAlarmByCode(int code)
    {
        for (AlarmLevel e : values())
        {
            if (e.code == code)
            {
                return e;
            }
        }

        return null;
    }
}
